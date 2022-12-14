package cool.cade.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.core.serializer.Deserializer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Json工具类，统一管理和解耦
 * @author :Ander
 * @date : 2022/6/30
 */
@Component
public final class JacksonUtil {

    /**
     * 避免频繁new objectMapper， 用一个ThreadLocal即可。全局共用一个一般也没什么问题
     */
    private  final ThreadLocal<ObjectMapper> objectMapperThreadLocal;

    private final List<SimpleModule> modules;

    public JacksonUtil(){
        objectMapperThreadLocal = new ThreadLocal<>();
        modules =  new CopyOnWriteArrayList<>();
    }

    /**
     * 增加一个序列化器
     * @param clazz
     * @param deserializer
     * @param name
     * @param <T>
     */
    public <T> void addDeserializer(Class<T> clazz, JsonDeserializer<? extends T> deserializer, String name){
        SimpleModule simpleModule = new SimpleModule(name);
        simpleModule.addDeserializer(clazz, deserializer);
        modules.add(simpleModule);
    }

    /**
     * 获取到objectMapper
     * @return
     */
    public ObjectMapper getObjectMapper(){
        ObjectMapper objectMapper = objectMapperThreadLocal.get();
        if(objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.registerModules(modules);
            objectMapperThreadLocal.set(objectMapper);
        }
        return objectMapper;
    }

    /**
     * 对象转化为Json String
     * @param obj
     * @return
     * @throws JsonProcessingException
     */
    public String toJsonString(Object obj) throws JsonProcessingException {
        return getObjectMapper().writeValueAsString(obj);
    }

    /**
     * 对象转化为jsonBytes
     * @param obj
     * @return
     * @throws JsonProcessingException
     */
    public byte[] toJSONBytes(Object obj) throws JsonProcessingException {
        return getObjectMapper().writeValueAsBytes(obj);
    }

    /**
     * json string 转为 T类型的对象
     * @param str
     * @param clazz
     * @return
     * @param <T>
     * @throws JsonProcessingException
     */
    public <T> T jsonStrToObject(String str, Class<T> clazz) throws JsonProcessingException {
        return getObjectMapper().readValue(str, clazz);
    }

}
