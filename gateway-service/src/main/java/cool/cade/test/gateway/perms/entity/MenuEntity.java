package cool.cade.test.gateway.perms.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author :Ander
 * @date : 2022/8/2
 */

@Data
@Table("sys_menu")
public class MenuEntity {
    @Id
    private Long id;

    private String menuName;

    private String path;

    private String component;

    private String perms;

}
