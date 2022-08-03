package cool.cade.test.gateway.authentication.exception.handler;

/**
 * @author :Ander
 * @date : 2022/7/14
 */
public abstract class AbstractAuthenticationExceptionHandler implements ExceptionHandler{

    private Class<? extends Throwable> clazz;

    public AbstractAuthenticationExceptionHandler(Class<? extends Throwable> exceptionClass){
        this.clazz = exceptionClass;
    }

    public Class<? extends Throwable> getExceptionClass(){
        return clazz;
    }



}
