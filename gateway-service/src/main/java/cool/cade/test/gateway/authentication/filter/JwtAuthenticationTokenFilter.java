package cool.cade.test.gateway.authentication.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.cade.common.utils.JacksonUtil;
import cool.cade.common.utils.ResponseResult;
import cool.cade.test.gateway.component.JwtProcessor;
import cool.cade.test.gateway.component.RedisCache;
import cool.cade.test.gateway.authentication.entity.AuthUserDetails;
import cool.cade.test.gateway.authentication.exception.JwtRefreshTokenErrorException;
import cool.cade.test.gateway.authentication.exception.JwtTokenExpiredException;
import cool.cade.test.gateway.authentication.exception.JwtTokenInvalidException;
import cool.cade.test.gateway.authentication.exception.ServerInternalException;
import cool.cade.test.gateway.entity.JwtTokenEntity;
import cool.cade.test.gateway.utils.ResponseUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * JwtToken认证Filter
 *
 * 可以将refreshToken部分再拆分出去，这样两者就独立了，你只用token也行
 */
@Slf4j
@Component
public class JwtAuthenticationTokenFilter implements WebFilter {

    private RedisCache redisCache;
    private JwtProcessor jwtProcessor;
    private String refreshUrl = "/auth/token/refresh";
    private ServerWebExchangeMatcher refreshTokenUrlMatcher = ServerWebExchangeMatchers.pathMatchers(refreshUrl);

    private JacksonUtil jacksonUtil;

    public JwtAuthenticationTokenFilter(JwtProcessor jwtProcessor, RedisCache redisCache, JacksonUtil jacksonUtil) {
        this.jwtProcessor = jwtProcessor;
        this.redisCache = redisCache;
        this.jacksonUtil = jacksonUtil;
    }

    public void setRefreshUrl(String refreshUrl) {
        this.refreshUrl = refreshUrl;
        refreshTokenUrlMatcher = ServerWebExchangeMatchers.pathMatchers(refreshUrl);
    }

    /**
     * reative 实现的接口和 servletb版本的不同
     *
     * @param exchange the current server exchange
     * @param chain    provides a way to delegate to the next filter
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return  refreshTokenUrlMatcher
                .matches(exchange)
                .filter(matchResult -> matchResult.isMatch())
                .flatMap(matchResult -> handleRefreshToken(exchange))
                .switchIfEmpty(Mono.defer(()->handleToken(exchange, chain)));
    }

    private Mono<Void> handleToken(ServerWebExchange exchange, WebFilterChain chain) {

        log.debug("handing token!");
        String token = jwtProcessor.extractTokenFromRequest(exchange.getRequest());
        if(!StringUtils.hasLength(token)) {
            log.debug("token not found!");
            return chain.filter(exchange);
        }
        //解析token
        AuthUserDetails userDetails = null;
        try {
            String subject = jwtProcessor.parseJWT(token).getSubject();
            userDetails = jacksonUtil.jsonStrToObject(subject, AuthUserDetails.class);
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            log.warn("Token Expired Exception Occur!");
            return Mono.error(new JwtTokenExpiredException());
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("Token Invalid Exception Occur");
            return Mono.error(new JwtTokenInvalidException());
        }

        //创建authentication存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // 将authentication设置进context
        // subscribeContext已经弃用，可以用contextWrite或者deferContextual
        return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));

    }

    Mono<Void> handleRefreshToken(ServerWebExchange exchange) {
        log.debug("handling refresh token");
        String refreshToken =jwtProcessor.extractRefreshTokenFromRequest(exchange.getRequest());
        if(Objects.isNull(refreshToken)){
            log.warn("refresh token not found!");
            return Mono.error(new JwtRefreshTokenErrorException());
        }

        String userId = null;
        try {
            // 可以使用不同的密钥，更安全一点
            userId = jwtProcessor.parseJWT(refreshToken).getSubject();
        }catch (ExpiredJwtException e){
            return Mono.error(new JwtRefreshTokenErrorException());
        }

        String redisKey = jwtProcessor.genRefreshTokenCacheKey(refreshToken);
        // 获取并删除
        AuthUserDetails userDetails = redisCache.getCacheObjectAndDelete(redisKey);

        // 保证一个refresh token只能使用一次
        // 过期了或者已经用过，抛出异常
        if (Objects.isNull(userDetails)) {
            return Mono.error(new JwtRefreshTokenErrorException());
        }

        JwtTokenEntity jwtTokenEntity = null;
        try {
            // 重新生成
            jwtTokenEntity = jwtProcessor.createJwtTokenAndRefreshToken(jacksonUtil.toJsonString(userDetails), userId);
        }catch (JsonProcessingException e) {
            log.warn("json processing error!");
            return Mono.error(new ServerInternalException());
        }
        // 重新生成key，放入token
        redisKey = jwtProcessor.genRefreshTokenCacheKey(jwtTokenEntity.getRefreshToken());
        redisCache.setCacheObject(redisKey, userDetails,jwtProcessor.getRefreshTTL(), TimeUnit.SECONDS);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        return ResponseUtil.writeResponseResult(exchange.getResponse(), HttpStatus.OK,
                ResponseResult.ok(jwtTokenEntity)).contextWrite(
                ReactiveSecurityContextHolder.withAuthentication(authentication));

    }
}