package cool.cade.common.constant;

/**
 * response result响应状态码
 */
public enum BizCodeEnum {


    SUCCESS(00000, "success"),
    AUTHORITY_FAIL(00001,"授权失败！"),
    UNAUTHENTICATED(00002,"未认证，请登录！"),
    TOKEN_EXPIRED(00003, "Token expired!"),
    TOKEN_INVALID(00004, "Token invalid!"),

    REFRESH_TOKEN_INVALID(00005, "RfreshToken invalid!"),

    INTERNAL_ERROR(00006, "Internal server error!");


    private int code;
    private String msg;

    BizCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}