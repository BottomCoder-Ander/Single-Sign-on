package cool.cade.test.gateway.authentication.handler;

import cool.cade.common.constant.BizCodeEnum;
import cool.cade.common.utils.ResponseResult;
import cool.cade.common.utils.ReactiveResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 授权失败调用的AuthenticationEntryPoint
 * @author :Ander
 * @date : 2022/6/30
 */
@Slf4j
@Component
public class SimpleRejectServerAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        log.trace("未认证！");

        // 授权失败
        return ReactiveResponseUtil.writeResponseResult(exchange.getResponse(),
                HttpStatus.FORBIDDEN,
                ResponseResult.error(BizCodeEnum.UNAUTHENTICATED));
    }
}
