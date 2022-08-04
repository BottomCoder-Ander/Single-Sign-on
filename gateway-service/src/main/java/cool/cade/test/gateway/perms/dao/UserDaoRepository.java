package cool.cade.test.gateway.perms.dao;

import cool.cade.test.gateway.perms.entity.UserEntity;
import reactor.core.publisher.Mono;

/**
 * @author :Ander
 * @date : 2022/8/3
 */
public interface UserDaoRepository {
    Mono<UserEntity> findByName(String userName);
}
