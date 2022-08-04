package cool.cade.test.gateway.authentication.handler;

import cool.cade.common.utils.ResponseResult;
import cool.cade.common.utils.ReactiveResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 登出成功回调
 * @author :Ander
 * @date : 2022/6/30
 */
@Slf4j
@Component
public class UserLogoutSuccessHandler implements ServerLogoutSuccessHandler {

    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
        log.debug("logout success");
        return ReactiveResponseUtil.writeResponseResult(exchange.getExchange().getResponse(), HttpStatus.ACCEPTED, ResponseResult.ok());
    }
}
