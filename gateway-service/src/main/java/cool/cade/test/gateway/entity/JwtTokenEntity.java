package cool.cade.test.gateway.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author :Ander
 * @date : 2022/8/3
 */
@AllArgsConstructor
@Data
public class JwtTokenEntity {
    private String token;
    private String refreshToken;
}
