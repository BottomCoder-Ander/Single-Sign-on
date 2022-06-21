package cool.cade.common.utils;


import com.fasterxml.jackson.annotation.JsonInclude;
import cool.cade.common.constant.StatusCodeEnum;

import java.util.HashMap;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> {
    private static final long serialVersionUID = 1L;

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

    public static<T> ResponseResult ok(){
        return new ResponseResult<T>(StatusCodeEnum.SUCCESS);
    }

    public static<T> ResponseResult error(int code, String msg) {
        return new ResponseResult<T>(code, msg);
    }
    public static<T> ResponseResult error(StatusCodeEnum codeEnum) {
        return ResponseResult.error(codeEnum.getCode(), codeEnum.getMsg());
    }
    public static<T> ResponseResult error() {
        return ResponseResult.error(StatusCodeEnum.INTERNAL_ERROR);
    }

}