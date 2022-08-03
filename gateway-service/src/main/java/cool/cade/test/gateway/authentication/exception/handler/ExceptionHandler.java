package cool.cade.test.gateway.authentication.exception.handler;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author :Ander
 * @date : 2022/7/14
 */
public interface ExceptionHandler {

    Class<? extends Throwable> getExceptionClass();

    Mono<?> handle(ServerWebExchange exchange, Throwable ex);

}
