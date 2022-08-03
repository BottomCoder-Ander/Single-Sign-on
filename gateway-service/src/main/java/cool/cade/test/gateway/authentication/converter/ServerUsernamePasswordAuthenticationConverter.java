//package cool.cade.test.gateway.converter;
//
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.util.function.Function;
//
///**
// * @author :Ander
// * @date : 2022/7/15
// */
//public class ServerUsernamePasswordAuthenticationConverter implements Function<ServerWebExchange, Mono<Authentication>> {
//    @Override
//    public Mono<Authentication> apply(ServerWebExchange exchange) {
//
//
//
//        return Mono.just(UsernamePasswordAuthenticationToken.unauthenticated(parts[0], parts[1]));
//    }
//}
