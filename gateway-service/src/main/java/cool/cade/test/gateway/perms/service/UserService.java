package cool.cade.test.gateway.perms.service;

import cool.cade.test.gateway.perms.entity.UserEntity;
import reactor.core.publisher.Mono;

/**
 * @author :Ander
 * @date : 2022/8/3
 */
public interface UserService {
    Mono<UserEntity> findUserByName(String name);
}
