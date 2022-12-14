package cool.cade.test.gateway.authentication.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.cade.common.utils.JacksonUtil;
import cool.cade.common.utils.ReactiveResponseUtil;
import cool.cade.common.utils.ResponseResult;
import cool.cade.common.component.RedisCache;
import cool.cade.test.gateway.authentication.userdetails.AuthUserDetails;
import cool.cade.test.gateway.authentication.exception.ServerInternalException;
import cool.cade.test.gateway.jwt.component.JwtProcessor;
import cool.cade.test.gateway.jwt.entity.JwtTokenEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

/**
 * 账户密码验证成功处理器
 * @author :Ander
 * @date : 2022/6/30
 *
 * Webflux应该实现ServerAuthenticationSuccessHandler，而servlet实现AuthenticationSuccessHandler
 * 默认实现是WebFilterChainServerAuthenticationSuccessHandler
 * 继承WebFilterChainServerAuthenticationSuccessHandler是继续调用filter和实现ServerAuthenticationSuccessHandler没有什么区别
 * 只不过它们通过名字来区分具体的使用含义。
 * WebFilterChainServerAuthenticationSuccessHandler是继续调用filter chain后面的filter
 * 而这里不需要，所以只需ServerAuthenticationSuccessHandler
 */
@Slf4j
@Component
public class UserPasswordAuthenticateSuccessHandler implements ServerAuthenticationSuccessHandler {

    private final JwtProcessor jwtProcessor;
    private final RedisCache redisCache;

    private final JacksonUtil jacksonUtil;

    public UserPasswordAuthenticateSuccessHandler(JwtProcessor jwtProcessor, RedisCache redisCache, JacksonUtil jacksonUtil) {
        this.jwtProcessor = jwtProcessor;
        this.redisCache = redisCache;
        this.jacksonUtil = jacksonUtil;
    }

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange,
                                              Authentication authentication) {
        log.trace("username password authentication success!");

        ServerWebExchange exchange = webFilterExchange.getExchange();
        ServerHttpResponse response = exchange.getResponse();//设置headers
        HttpHeaders headers = response.getHeaders();
        headers.add("Cache-Control","no-store,no-cache,must-revalidate,max-age-8");

        log.trace("on authentication success!");
        // 从authentication中重新拿到userDetails
        AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();
        String userId = userDetails.getUser().getId().toString();

        /**
         * 可以把用户信息放在token中，把token放到refreshToken中，用来判断refreshToken是否用过
         * 但是这样可能需要比较多的解析时间。因此这里把信息存到redis中，token中只有userId
         */

        byte[] dataBytes = {};
        try {
            log.trace("generating jwt token");
            // 生成token，直接把userDetails放进去
            AuthUserDetails principal = (AuthUserDetails) authentication.getPrincipal();
            JwtTokenEntity jwtTokenEntity = jwtProcessor.createJwtTokenAndRefreshToken(jacksonUtil.toJsonString(principal), userId);

            String redisKey = jwtProcessor.genRefreshTokenCacheKey(jwtTokenEntity.getRefreshToken());
            redisCache.setCacheObject(redisKey, userDetails, jwtProcessor.getRefreshTTL(), TimeUnit.SECONDS);

            return ReactiveResponseUtil.writeResponseResult(response, HttpStatus.ACCEPTED, ResponseResult.ok(jwtTokenEntity));
        } catch (JsonProcessingException e) {
            return Mono.error(new ServerInternalException());
        }
    }
}
