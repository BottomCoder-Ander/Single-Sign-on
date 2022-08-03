package cool.cade.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cool.cade.common.constant.StatusCodeEnum;

import java.util.HashMap;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> {
    private static final long serialVersionUID = 1L;

    private static final JacksonUtil jacksonUtil = new JacksonUtil();
    /**
     * code:状态码
     * msg:消息
     * data:数据
     */

    private int code;
    private String msg;
    private T data;


    public ResponseResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResponseResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResponseResult(StatusCodeEnum statusCodeEnum) {
        this.code = statusCodeEnum.getCode();
        this.msg = statusCodeEnum.getMsg();
    }

    public Integer getCode() {
        return code;
    }

    public ResponseResult<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ResponseResult<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public ResponseResult<T> setData(T data) {
        this.data = data;
        return this;
    }

    public static<T> ResponseResult<T> ok(T data){
        return new ResponseResult<T>(StatusCodeEnum.SUCCESS.getCode(),
                StatusCodeEnum.SUCCESS.getMsg(), data);
    }

    public static<T> ResponseResult<T> ok(){
        return new ResponseResult<T>(StatusCodeEnum.SUCCESS);
    }

    public static <T> ResponseResult<T> error(int code, String msg, T data) {
        return new ResponseResult<>(code, msg, data);
    }

    public static <T> ResponseResult<T>  error(StatusCodeEnum codeEnum, T data) {
        return ResponseResult.error(codeEnum.getCode(), codeEnum.getMsg(), data);
    }

    public static <T> ResponseResult<T>  error(StatusCodeEnum codeEnum) {
        return ResponseResult.error(codeEnum.getCode(), codeEnum.getMsg(), null);
    }

    public static <T> ResponseResult<T> error() {
        return ResponseResult.error(StatusCodeEnum.INTERNAL_ERROR,null);
    }

    /**
     * 转换成Json字符串，异常抛出去，让全局异常捕获，或者外部按需处理
     * @return
     * @throws JsonProcessingException
     */
    public String toJsonString() throws JsonProcessingException {
        return jacksonUtil.toJsonString(this);
    }

    // 结果等同于toJsonString().getBytes(StandardCharsets.UTF_8)
    public byte[] toJSONBytes() throws JsonProcessingException {
        return jacksonUtil.toJSONBytes(this);
    }

}