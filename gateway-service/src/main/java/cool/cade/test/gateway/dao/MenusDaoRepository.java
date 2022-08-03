package cool.cade.test.gateway.dao;

import cool.cade.test.gateway.entity.MenuEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

/**
 * @author :Ander
 * @date : 2022/8/2
 */
@Repository
public interface MenusDaoRepository extends R2dbcRepository<MenuEntity, Long> {
}