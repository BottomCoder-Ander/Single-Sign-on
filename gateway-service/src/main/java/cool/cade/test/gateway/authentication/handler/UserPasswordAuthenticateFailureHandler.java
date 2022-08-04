package cool.cade.test.gateway.authentication.handler;

import cool.cade.common.constant.BizCodeEnum;
import cool.cade.common.utils.ResponseResult;
import cool.cade.common.utils.ReactiveResponseUtil;
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
 * 账户密码验证失败处理器
 * @author :Ander
 * @date : 2022/6/30
 */

@Slf4j
@Component
public class UserPasswordAuthenticateFailureHandler implements ServerAuthenticationFailureHandler {

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        log.trace("username password authentication fail!");
        ServerWebExchange exchange = webFilterExchange.getExchange();
        ServerHttpResponse response = exchange.getResponse();//设置headers
        HttpHeaders headers = response.getHeaders();
        System.out.println(headers);
        headers.add("Cache-Control","no-store,no-cache,must-revalidate,max-age-8");

        log.trace("on authentication failure!");
        
        return ReactiveResponseUtil.writeResponseResult(
                response ,
                HttpStatus.FORBIDDEN,
                ResponseResult.error(BizCodeEnum.AUTHORITY_FAIL));
    }

}
