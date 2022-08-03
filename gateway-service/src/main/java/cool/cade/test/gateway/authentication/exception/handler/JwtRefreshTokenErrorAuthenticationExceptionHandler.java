package cool.cade.test.gateway.authentication.exception.handler;

import cool.cade.common.constant.StatusCodeEnum;
import cool.cade.common.utils.ResponseResult;
import cool.cade.test.gateway.authentication.exception.JwtRefreshTokenErrorException;
import cool.cade.test.gateway.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author :Ander
 * @date : 2022/7/14
 */
@Slf4j
public class JwtRefreshTokenErrorAuthenticationExceptionHandler extends AbstractAuthenticationExceptionHandler {

    public JwtRefreshTokenErrorAuthenticationExceptionHandler(){
        super(JwtRefreshTokenErrorException.class);
    }

    @Override
    public Mono<?> handle(ServerWebExchange exchange, Throwable ex) {
        log.debug("JwtRefreshTokenExceptionHandler Handling!");
        return ResponseUtil.writeResponseResult(exchange.getResponse(), HttpStatus.FORBIDDEN,
                ResponseResult.error(StatusCodeEnum.REFRESH_TOKEN_INVALID));
    }
}
