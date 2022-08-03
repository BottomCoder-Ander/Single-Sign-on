package cool.cade.test.gateway.authentication.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import cool.cade.common.utils.ResponseResult;
import cool.cade.test.gateway.authentication.exception.ServerInternalException;
import cool.cade.test.gateway.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author :Ander
 * @date : 2022/6/30
 */
@Slf4j
@Component
public class UserLogoutSuccessHandler implements ServerLogoutSuccessHandler {

    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
        log.debug("logout success");
        return ResponseUtil.writeResponseResult(exchange.getExchange().getResponse(), HttpStatus.ACCEPTED, ResponseResult.ok());
    }
}
