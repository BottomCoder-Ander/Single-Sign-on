package cool.cade.test.gateway.authentication.exception.handler;

import cool.cade.test.gateway.authentication.exception.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * 代理异常处理器，用于处理同一个大类（比如{@link AuthenticationException}）的异常的异常处理类
 * @author :Ander
 * @date : 2022/7/14
 */
@Slf4j
public class DelegatingAuthenticationExceptionHandler extends AbstractAuthenticationExceptionHandler {

    /**
     * 被代理的Handlers
     */
    private List<ExceptionHandler> delegateExceptionHandlers;

    public DelegatingAuthenticationExceptionHandler(Class<? extends Throwable> exceptionClazz) {
        super(exceptionClazz);

    }

    public void setDelegateExceptionHandlers(ExceptionHandler ...handlers){
        delegateExceptionHandlers =  Arrays.asList(handlers);
    }

    /**
     * 异常处理
     * @param exchange ： 用于响应一些信息之类的处理
     * @param ex ： 待处理的异常
     * @return
     */
    @Override
    public Mono<?> handle(ServerWebExchange exchange, Throwable ex) {
        log.trace("delegator handling  exception!");
        for (ExceptionHandler delegateExceptionHandler : delegateExceptionHandlers) {
            if( delegateExceptionHandler.getExceptionClass().isAssignableFrom(ex.getClass())) {
                return delegateExceptionHandler.handle(exchange, ex);
            }
        }
        return Mono.empty();
    }
}
