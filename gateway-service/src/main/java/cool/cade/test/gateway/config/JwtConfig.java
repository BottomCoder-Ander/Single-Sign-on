package cool.cade.test.gateway.config;

//import cool.cade.test.gateway.converter.ConfigStringToSignatureAlgorithmConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author :Ander
 * @date : 2022/6/30
 */
@Configuration
public class JwtConfig {
    /**
     * 自定义String 转 SignatureAlgorithm的转换器
     * 不过JJWT已经提供了，不必再自定义
     * @return
     */
//    @Bean
//    @ConfigurationPropertiesBinding
//    public ConfigStringToSignatureAlgorithmConverter stringToSignatureAlgorithmConverter(){
//        return new ConfigStringToSignatureAlgorithmConverter();
//    }

    @Bean
    public JwtProcessor jwtProcessor(JwtProperties jwtProperties){
        return new JwtProcessor(jwtProperties);
    }

    /**
     * JWT配置Bean
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "jwt", ignoreUnknownFields = false)
    public JwtProperties jwtProperties(){
        return new JwtProperties();
    }

}
