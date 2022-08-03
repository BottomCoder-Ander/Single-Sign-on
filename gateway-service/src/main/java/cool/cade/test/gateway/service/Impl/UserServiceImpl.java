package cool.cade.test.gateway.service.Impl;

import cool.cade.test.gateway.dao.UserDaoRepository;
import cool.cade.test.gateway.entity.UserEntity;
import cool.cade.test.gateway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author :Ander
 * @date : 2022/8/2
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDaoRepository userDaoRepository;

    @Override
    public Mono<UserEntity> findUserByName(String name){
        return userDaoRepository.findByName(name);
    }


}
