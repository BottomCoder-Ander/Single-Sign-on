package cool.cade.test.gateway.perms.service;

import org.apache.commons.lang3.tuple.Pair;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author :Ander
 * @date : 2022/8/3
 */
public interface MenuService {
    Mono<List<Pair<String, String>>> findAllMenusToPair();

    Mono<List<Pair<String, String>>> findAllMenusToPair(boolean force);
}
