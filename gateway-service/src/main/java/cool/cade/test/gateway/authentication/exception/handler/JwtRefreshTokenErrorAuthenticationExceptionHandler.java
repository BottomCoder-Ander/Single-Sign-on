package cool.cade.test.gateway.authentication.exception.handler;

import cool.cade.common.constant.BizCodeEnum;
import cool.cade.common.utils.ResponseResult;
import cool.cade.test.gateway.authentication.exception.JwtRefreshTokenErrorException;
import cool.cade.common.utils.ReactiveResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * JwtRefreshToken异常处理器
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
        log.trace("JwtRefreshTokenExceptionHandler Handling!");
        return ReactiveResponseUtil.writeResponseResult(exchange.getResponse(), HttpStatus.FORBIDDEN,
                ResponseResult.error(BizCodeEnum.REFRESH_TOKEN_INVALID));
    }
}
