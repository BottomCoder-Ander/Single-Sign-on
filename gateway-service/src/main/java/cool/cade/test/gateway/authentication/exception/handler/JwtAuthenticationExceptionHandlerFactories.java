package cool.cade.test.gateway.authentication.exception.handler;

import cool.cade.test.gateway.authentication.exception.AuthenticationException;

/**
 * @author :Ander
 * @date : 2022/7/21
 */
public final class JwtAuthenticationExceptionHandlerFactories {

    private JwtAuthenticationExceptionHandlerFactories(){}

    public static ExceptionHandler createDelegatingExceptionHandler(){
        DelegatingAuthenticationExceptionHandler handler = new DelegatingAuthenticationExceptionHandler(AuthenticationException.class);
        handler.setDelegateExceptionHandlers(
                new JwtTokenExpiredAuthenticationExceptionHandler(),
                new JwtTokenInvalidAuthenticationExceptionHandler(),
                new JwtRefreshTokenErrorAuthenticationExceptionHandler(),
                new ServerInternalAuthenticationExceptionHandler()
        );
        return handler;
    }

}
