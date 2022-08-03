package cool.cade.test.gateway.dao;

import cool.cade.test.gateway.entity.UserEntity;
import reactor.core.publisher.Mono;

/**
 * @author :Ander
 * @date : 2022/8/3
 */
public interface UserDaoRepository {
    Mono<UserEntity> findByName(String userName);
}
