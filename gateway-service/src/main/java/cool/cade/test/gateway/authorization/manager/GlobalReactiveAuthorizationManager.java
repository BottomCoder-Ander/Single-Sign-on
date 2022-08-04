package cool.cade.test.gateway.authorization.manager;

import cool.cade.test.gateway.perms.service.Impl.MenuServiceImpl;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.*;

/**
 * @author :Ander
 * @date : 2022/7/28
 */
@Slf4j
@Component
public class GlobalReactiveAuthorizationManager <T> implements ReactiveAuthorizationManager<T> {

    private MenuServiceImpl menuService;

    public GlobalReactiveAuthorizationManager(MenuServiceImpl menuService){
        this.menuService = menuService;
    }

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, T object) {
//        // 需要先保证认证通过，然后才有权限认证
        return authentication.filter(Authentication::isAuthenticated)
                .flatMap(auth -> checkAuthority(auth.getAuthorities(), object))
                .defaultIfEmpty(new AuthorizationDecision(false));
    }

    private String getRequiredPerm(AuthorizationContext context, List<Pair<String,String>> permsRequired) {
        // 先获取需要的权限
        ServerWebExchange exchange = context.getExchange();
        String path = exchange.getRequest().getURI().getPath();

        // 去掉最后一个/
        if(path.length() != 1 && path.endsWith("/")) {
            path = path.substring(0, path.length() - 2);
        }

        // 得到需要的权限，第一个就是匹配最长的
        String requiredPerm = null;
        for(Pair<String,String> perms: permsRequired) {
            if(path.startsWith(perms.getLeft())){
                requiredPerm = perms.getRight();
                break;
            }
        }
        return requiredPerm;
    }

    private Mono<AuthorizationDecision>  checkAuthority(Collection<? extends GrantedAuthority> authorities, Object object) {
        Assert.isTrue(AuthorizationContext.class.isAssignableFrom(object.getClass()));

        return menuService.findAllMenusToPair().map(
                perms -> {
                    String requiredPerm = getRequiredPerm((AuthorizationContext) object, perms);
                    // 如果没有配置，则说明不需要权限即可访问
                    if(requiredPerm == null) return new AuthorizationDecision(true);

                    // 检查权限

                    return new AuthorizationDecision(authorities.stream()
                            .anyMatch(authority -> requiredPerm.equals(authority.getAuthority())));
                }
        );
    }

}
