package cool.cade.test.gateway.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.Set;

/**
 * @author :Ander
 * @date : 2022/8/2
 */

@Data
public class UserEntity {
    private static final int ENABLED_STATUS = 0;

    /**
     * 主键
     */
    @JsonIgnore
    private transient Long id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    @JsonIgnore
    private transient String password;
    /**
     * 账号状态（0正常 1停用）
     */
    private Integer status = 0;
    /**
     * 手机号
     */
    private String phonenumber;
    /**
     * 用户性别（0男，1女，2未知）
     */
    private Integer sex;
    /**
     * 用户类型（0管理员，1普通用户）
     */
    private Integer userType;
    /**
     * 权限
     */
    private String[] perms;
    /**
     * 用户角色
     */
    private String[] roles;

    @JsonIgnore
    public boolean isEnabled() {
        return status == 0;
    }

}
