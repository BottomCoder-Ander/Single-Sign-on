package cool.cade.test.gateway.authentication.userdetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import cool.cade.test.gateway.perms.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * 自定义的UserDetails
 * @author :Ander
 * @date : 2022/6/30
 */
// Jackson会把isXXX, getXXX结果也序列化
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthUserDetails implements UserDetails {
    private UserEntity user;

    private List<GrantedAuthority> authorities;

    // 反序列化，需要提供一个无参构造器
    public AuthUserDetails(){}
    public AuthUserDetails(UserEntity user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(authorities == null) {
            authorities = AuthorityUtils.createAuthorityList(user.getPerms());
            authorities.addAll(AuthorityUtils.createAuthorityList(user.getRoles()));
        }
        return authorities;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 凭证过期判断
     * @return
     */

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    @JsonIgnore
    public String[] getRoles() {
        return user.getRoles();
    }

    public UserEntity getUser(){
        return user;
    }

}
