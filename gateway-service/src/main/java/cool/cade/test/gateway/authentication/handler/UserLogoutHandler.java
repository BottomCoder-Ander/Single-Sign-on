package cool.cade.test.gateway.authentication.handler;

import cool.cade.test.gateway.jwt.component.JwtProcessor;
import cool.cade.common.component.RedisCache;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 登出处理器
 * @author :Ander
 * @date : 2022/7/15
 */
@Component
public class UserLogoutHandler implements ServerLogoutHandler {

    private RedisCache redisCache;
    private JwtProcessor jwtProcessor;

    public UserLogoutHandler(RedisCache redisCache, JwtProcessor jwtProcessor) {
        this.redisCache = redisCache;
    }



    @Override
    public Mono<Void> logout(WebFilterExchange exchange, Authentication authentication) {

        String refreshToken = jwtProcessor.extractRefreshTokenFromRequest(exchange.getExchange().getRequest());
        // LOGOUT 需要带上jwtRefreshKey
        String jwtRedisKey = jwtProcessor.genRefreshTokenCacheKey(refreshToken);
        redisCache.deleteObject(jwtRedisKey);

        return Mono.empty();
    }
}
