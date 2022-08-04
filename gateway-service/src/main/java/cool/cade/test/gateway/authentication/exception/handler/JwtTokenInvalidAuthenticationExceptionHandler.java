package cool.cade.test.gateway.authentication.exception.handler;

import cool.cade.common.constant.StatusCodeEnum;
import cool.cade.common.utils.ResponseResult;
import cool.cade.test.gateway.authentication.exception.JwtTokenInvalidException;
import cool.cade.common.utils.ReactiveResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author :Ander
 * @date : 2022/7/14
 */
@Slf4j
public class JwtTokenInvalidAuthenticationExceptionHandler extends AbstractAuthenticationExceptionHandler {
    public JwtTokenInvalidAuthenticationExceptionHandler() {
        super(JwtTokenInvalidException.class);
    }

    @Override
    public Mono<?> handle(ServerWebExchange exchange, Throwable ex) {
        log.debug("JwtTokenInvalidException Handling!");
        ServerHttpResponse response = exchange.getResponse();
        return ReactiveResponseUtil.writeResponseResult(response, HttpStatus.FORBIDDEN, ResponseResult.error(StatusCodeEnum.TOKEN_INVALID));
    }
}
