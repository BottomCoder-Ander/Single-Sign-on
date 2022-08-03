package cool.cade.test.gateway.dao;

import cool.cade.test.gateway.entity.UserEntity;
import io.jsonwebtoken.lang.Collections;
import io.r2dbc.spi.Row;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * @author :Ander
 * @date : 2022/8/2
 */
@Slf4j
@Repository
public class UserDaoRepositoryImpl implements UserDaoRepository {

    DatabaseClient databaseClient;

    public UserDaoRepositoryImpl(DatabaseClient client) {
        this.databaseClient = client;
    }

    @Override
    public Mono<UserEntity> findByName(String userName) {
        final String sql =
                "SELECT  " +
                "   usr.`id`, `user_name`, `password`, usr.`status`, `sex`, `phonenumber`, `user_type`, GROUP_CONCAT(DISTINCT m.`perms` separator ';') perms, GROUP_CONCAT(DISTINCT r.`role_key` separator ';') roles  " +
                "FROM  " +
                " `sys_user` usr   " +
                "   LEFT JOIN `sys_user_role` ur ON usr.`id` = ur.`user_id`  " +
                "   LEFT JOIN `sys_role` r ON ur.`role_id` = r.`id`  " +
                "   LEFT JOIN `sys_role_menu` rm ON ur.`role_id` = rm.`role_id`  " +
                "   LEFT JOIN `sys_menu` m ON m.`id` = rm.`menu_id`  " +
                "WHERE   " +
                " usr.`user_name` = :userName  " +
                "GROUP BY   " +
                " usr.`id`";

        log.debug(sql);

        return databaseClient.sql(sql)
                .bind("userName", userName)
                .map(this::rowToUser)
                .one();
    }

    private UserEntity rowToUser(Row row) {
        // CHAR和VARCHAR都对应String类型
        UserEntity userEntity = new UserEntity();
        userEntity.setId(row.get("id", Long.class));
        userEntity.setPassword(row.get("password", String.class));
        userEntity.setUserName(row.get("user_name", String.class));
        userEntity.setSex(Integer.valueOf(row.get("sex", String.class)));
        userEntity.setStatus(Integer.valueOf(row.get("status", String.class)));
        userEntity.setUserType(Integer.valueOf(row.get("user_type", String.class)));
        userEntity.setPhonenumber(row.get("phonenumber", String.class));
        userEntity.setPerms(row.get("perms", String.class).split(";"));
        userEntity.setRoles(row.get("roles", String.class).split(";"));

        return userEntity;
    }
}
