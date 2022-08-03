package cool.cade.test.gateway.component;

import cool.cade.test.gateway.config.JwtProperties;
import cool.cade.test.gateway.entity.JwtTokenEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.util.Date;
import java.util.UUID;

public class JwtProcessor {
    private JwtProperties jwtProperties;
    private RedisCache redisCache;

    public JwtProcessor(JwtProperties jwtProperties, RedisCache redisCache) {
        this.jwtProperties= jwtProperties;
        this.redisCache = redisCache;
    }

    public JwtTokenEntity createJwtTokenAndRefreshToken(String tokenSubject, String refreshTokenSubject){
        String token = this.createToken(tokenSubject);
        String refreshToken = this.createRefreshToken(refreshTokenSubject);
        return new JwtTokenEntity(token ,refreshToken);
    }


    public String genRefreshTokenCacheKey(String refreshToken){
        return  this.jwtProperties.getRefreshTokenCacheKeyPrefix() + ":" + refreshToken;
    }


    public static String getUUID(){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }

    private JwtBuilder getJwtBuilder(String subject, String uuid, int ttlSeconds) {


        // 使用当前系统时区, jjwt只支持Date
//        LocalDateTime expLocalDateTime = LocalDateTime.now(Clock.systemDefaultZone()).plus(ttlSeconds, ChronoUnit.SECONDS);
//        Date expTime = Date.from(expLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
//        long nowMillis = System.currentTimeMillis();
        Date now = new Date();

        Date expDate = DateUtils.addSeconds(now, ttlSeconds);

        return Jwts.builder()
                .setId(uuid)              //唯一的ID
                .setSubject(subject)   // 主题  用来保存pricipal，比如userId或者其他用户信息
                .setIssuer(jwtProperties.getIssuer())     // 签发者
                .setIssuedAt(now)      // 签发时间
                .signWith(jwtProperties.getSignatureAlgorithm(), jwtProperties.getSecretKey()) //签名算法，和密钥
                .setExpiration(expDate);
    }

    /**
     * 生成jtw
     * @param subject token中要存放的数据（json格式）
     * @return
     */
    public String createToken(String subject) {
        JwtBuilder builder = getJwtBuilder(subject, getUUID(), jwtProperties.getTTL());
        return builder.compact();
    }

    public String createRefreshToken(String subject) {
        JwtBuilder builder = getJwtBuilder(subject, getUUID(), jwtProperties.getRefreshTTL());
        return builder.compact();
    }

    /**
     * 创建JWT token
     * @param id
     * @param subject
     * @return
     */
    public String createToken(String id, String subject) {
        JwtBuilder builder = getJwtBuilder(subject, id, jwtProperties.getTTL());// 设置过期时间
        return builder.compact();
    }

    /**
     * 解析 jwt
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public Claims parseJWT(String jwt) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(jwt)
                .getBody();
    }

    public Integer getRefreshTTL() {
        return jwtProperties.getRefreshTTL();
    }


    public String extractTokenFromRequest(ServerHttpRequest request) {
        return request.getHeaders().getFirst(jwtProperties.getTokenHeader());
    }

    public String extractRefreshTokenFromRequest(ServerHttpRequest request) {
        return request.getHeaders().getFirst(jwtProperties.getRefreshTokenHeader());
    }



}