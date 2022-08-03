package cool.cade.test.gateway.authentication.filter;

import cool.cade.test.gateway.authentication.exception.handler.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

/**
 *  Jwt异常处理器
 *  @author :Ander
 *  @date : 2022/7/14
 */

@Slf4j
public class ExceptionHandlerFilter implements WebFilter {

    private ExceptionHandler exceptionHandler;

    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange,@NonNull WebFilterChain chain) {

        return chain.filter(exchange).onErrorResume(exceptionHandler.getExceptionClass(), ex->
            exceptionHandler.handle(exchange, ex).then()
        );
    }

    public void setExceptionHandler(ExceptionHandler handler){
        this.exceptionHandler = handler;
    }




}
