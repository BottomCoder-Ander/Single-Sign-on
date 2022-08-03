package cool.cade.test.gateway.authentication.handler;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import cool.cade.common.constant.StatusCodeEnum;
import cool.cade.common.utils.ResponseResult;
import cool.cade.test.gateway.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
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
        ServerHttpResponse response = exchange.getResponse();


        log.debug("未认证！");

        // 授权失败
        return ResponseUtil.writeResponseResult(response,
                HttpStatus.FORBIDDEN,
                ResponseResult.error(StatusCodeEnum.UNAUTHENTICATED));
    }
}
