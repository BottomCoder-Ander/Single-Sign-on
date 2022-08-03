package cool.cade.test.gateway.authentication.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * @author :Ander
 * @date : 2022/7/14
 */
@Slf4j
public class DelegatingAuthenticationExceptionHandler extends AbstractAuthenticationExceptionHandler {

    private List<ExceptionHandler> delegateExceptionHandlers;

    public DelegatingAuthenticationExceptionHandler(Class<? extends Throwable> exceptionClazz) {
        super(exceptionClazz);

    }

    public void setDelegateExceptionHandlers(ExceptionHandler ...handlers){
        delegateExceptionHandlers =  Arrays.asList(handlers);
    }

    @Override
    public Mono<?> handle(ServerWebExchange exchange, Throwable ex) {
        log.debug("delegator handling  exception!");
        for (ExceptionHandler delegateExceptionHandler : delegateExceptionHandlers) {
            if( delegateExceptionHandler.getExceptionClass().isAssignableFrom(ex.getClass())) {
                return delegateExceptionHandler.handle(exchange, ex);
            }
        }
        return Mono.empty();
    }
}
