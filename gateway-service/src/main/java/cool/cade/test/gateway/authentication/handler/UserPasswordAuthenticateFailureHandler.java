package cool.cade.test.gateway.authentication.handler;

import cool.cade.common.constant.StatusCodeEnum;
import cool.cade.common.utils.ResponseResult;
import cool.cade.test.gateway.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author :Ander
 * @date : 2022/6/30
 */

@Slf4j
@Component
public class UserPasswordAuthenticateFailureHandler implements ServerAuthenticationFailureHandler {

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        log.debug("username password authentication fail!");
        ServerWebExchange exchange = webFilterExchange.getExchange();
        ServerHttpResponse response = exchange.getResponse();//设置headers
        HttpHeaders headers = response.getHeaders();
        System.out.println(headers);
        headers.add("Cache-Control","no-store,no-cache,must-revalidate,max-age-8");

        log.debug("on authentication failure!");
        
        return ResponseUtil.writeResponseResult(
                response ,
                HttpStatus.FORBIDDEN,
                ResponseResult.error(StatusCodeEnum.AUTHORITY_FAIL));
    }

}
