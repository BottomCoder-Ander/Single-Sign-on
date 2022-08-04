package cool.cade.common.utils;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import cool.cade.common.constant.BizCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

/**
 * reactive响应，工具
 * @author :Ander
 * @date : 2022/7/14
 */
@Slf4j
public class ReactiveResponseUtil {
    /**
     * 返回一个将响应结果写入response的Mono
     * @param response
     * @param code Http状态码
     * @param result ResponseResult
     * @return
     */
    public static Mono<Void> writeResponseResult(ServerHttpResponse response,
                                                 HttpStatus code,
                                                 ResponseResult<?> result){
        return Mono.defer(()->{
            response.getHeaders().add(
                    "content-Type",
                    "application/json;charset=UTF-8");
            response.setStatusCode(code);
            byte[] resultBytes = {};
            try {
                resultBytes = result.toJSONBytes();
            } catch (JsonProcessingException e) {
                log.warn("JsonProcessingException Occur!");
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                resultBytes = JSON.toJSONBytes(ResponseResult.error(BizCodeEnum.INTERNAL_ERROR));
            }
            return response.writeWith(Mono.just(response.bufferFactory().wrap(resultBytes)));
        });

    }
}
