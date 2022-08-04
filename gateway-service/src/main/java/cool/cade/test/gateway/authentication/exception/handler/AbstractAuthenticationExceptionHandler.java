package cool.cade.test.gateway.authentication.exception.handler;

import cool.cade.test.gateway.authentication.exception.AuthenticationException;

/**
 * 异常处理器的抽象类，实现{@link ExceptionHandler}
 * @author :Ander
 * @date : 2022/7/14
 */
public abstract class AbstractAuthenticationExceptionHandler implements ExceptionHandler{

    /**
     * 要处理的异常类，如{@link AuthenticationException#getClass()}
     */
    private Class<? extends Throwable> clazz;

    public AbstractAuthenticationExceptionHandler(Class<? extends Throwable> exceptionClass){
        this.clazz = exceptionClass;
    }

    public Class<? extends Throwable> getExceptionClass(){
        return clazz;
    }



}
