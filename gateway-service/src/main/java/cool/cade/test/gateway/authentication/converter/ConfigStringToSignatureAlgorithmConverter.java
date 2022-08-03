//package cool.cade.test.gateway.converter;
//
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.core.convert.converter.Converter;
//
///**
// * @author :Ander
// * @date : 2022/6/30
// */
//public class ConfigStringToSignatureAlgorithmConverter implements Converter<String, SignatureAlgorithm> {
//
//    @Override
//    public SignatureAlgorithm convert(String source) {
//        for (SignatureAlgorithm value : SignatureAlgorithm.values()) {
//            if(value.getValue().equalsIgnoreCase(source)) {
//                return value;
//            }
//        }
//        throw new IllegalArgumentException("不存在SignatureAlgorithm："+source);
//    }
//}
