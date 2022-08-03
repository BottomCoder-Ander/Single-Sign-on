package cool.cade.test.gateway.authentication.service.impl;

import cool.cade.test.gateway.authentication.entity.AuthUserDetails;
import cool.cade.test.gateway.entity.UserEntity;
import cool.cade.test.gateway.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 *
 */
@Slf4j
@Service
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {

//    @Autowired
//    private UserMapper userMapper;
//    @Autowired
//    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        log.debug("finding user by name!");
        
        return userService.findUserByName(username)
                .map(user -> (UserDetails)new AuthUserDetails(user))
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("用户名或密码错误!!!")));
    }
}