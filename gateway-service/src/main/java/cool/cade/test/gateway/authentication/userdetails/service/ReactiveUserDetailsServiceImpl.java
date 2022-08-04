package cool.cade.test.gateway.authentication.userdetails.service;

import cool.cade.test.gateway.authentication.userdetails.AuthUserDetails;
import cool.cade.test.gateway.perms.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * 用于获取待验证用户的真实信息的Service
 * 自定义的UserDetails
 * @author :Ander
 * @date : 2022/6/30
 */
@Slf4j
@Service
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {

    private UserService userService;

    public ReactiveUserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        log.trace("finding user by name mono!");
        
        return userService.findUserByName(username)
                .map(user -> (UserDetails)new AuthUserDetails(user))
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("用户名或密码错误!!!")));
    }
}