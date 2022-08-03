package cool.cade.test.gateway.config;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author :Ander
 * @date : 2022/6/30
 */

@Data
public class JwtProperties {
    private String base64SecretKey = "VGhpcyRpcyNhX3NlY3JldD1rZXk=";
    /**
     * token超时时间，单位秒
     */
    private Integer TTL = 60;

    /**
     * refresh token 超时时间，单位秒
     */
    private Integer refreshTTL = 300;

    /**
     * secretekey加密算法
     */
    private String algorithm = "AES";
    /**
     * jwt签名算法
     */
    private SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    /**
     * 签发者
     */
    private String issuer = "Ander";
    
    private byte[] decodedJwtKey = null;

    private SecretKey secretKey = null;

    SecretKey key = null;

    private String tokenHeader = "X-Token";

    private String refreshTokenHeader = "X-Token-Refresh";

    /**
     * 缓存key前缀
     */
    private String REFRESH_TOKEN_CACHE_KEY_PREFIX = "TEST:JWT_REFRESH_TOKEN:";
    public byte[] getDecodedJwtKey() {
        if(decodedJwtKey == null) {
            decodedJwtKey = Base64.decode(base64SecretKey);
        }
        return decodedJwtKey;
    }

    public SecretKey getSecretKey(){
        if(secretKey == null) {
            byte[] decodedJwtKey = getDecodedJwtKey();
            secretKey = new SecretKeySpec(decodedJwtKey, 0,
                    decodedJwtKey.length, algorithm);
        }
        return secretKey;
    }

    public String getRefreshTokenCacheKeyPrefix(){
        return REFRESH_TOKEN_CACHE_KEY_PREFIX;
    }

}
