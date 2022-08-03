package cool.cade.test.gateway.config;


import cool.cade.common.constant.StatusCodeEnum;
import cool.cade.common.utils.JacksonUtil;
import cool.cade.common.utils.ResponseResult;
import cool.cade.test.gateway.authentication.exception.handler.JwtAuthenticationExceptionHandlerFactories;
import cool.cade.test.gateway.authentication.filter.ExceptionHandlerFilter;
import cool.cade.test.gateway.authentication.filter.JwtAuthenticationTokenFilter;
import cool.cade.test.gateway.authentication.handler.*;
import cool.cade.test.gateway.authorization.manager.GlobalReactiveAuthorizationManager;
import cool.cade.test.gateway.jackson.deserializer.GrantedAuthrorityDeserializer;
import cool.cade.test.gateway.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
//@EnableReactiveMethodSecurity
public class SecurityConfig  {
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Value("security.exclude-auth-page")
    private static String[] excludeAuthPage;

    // 构造器注入
    public SecurityConfig() {
    }

    @Bean
    public JacksonUtil jacksonUtil(){
        JacksonUtil jacksonUtil = new JacksonUtil();
        jacksonUtil.addDeserializer(GrantedAuthority.class,new GrantedAuthrorityDeserializer(),
                "GrantedAuthority Deserializer");
        return jacksonUtil;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * 用于替换掉默认的AuthenticationManager
     * @param reactiveUserDetailsService
     * @return
     */
    @Bean
    ReactiveAuthenticationManager reactiveAuthenticationManager(
            ReactiveUserDetailsService reactiveUserDetailsService,
            PasswordEncoder passwordEncoder){

        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager
                = new UserDetailsRepositoryReactiveAuthenticationManager(reactiveUserDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }

    @Bean
    ExceptionHandlerFilter jwtExceptionHandlerFilter(){
        ExceptionHandlerFilter exceptionHandlerFilter = new ExceptionHandlerFilter();
        exceptionHandlerFilter.setExceptionHandler(
                JwtAuthenticationExceptionHandlerFactories.createDelegatingExceptionHandler());
        return exceptionHandlerFilter;
    }

    /**
     * 旧版本可能通过继承WebSecurityConfigurerAdapter重写configure进行配置，新版直接注册一个springSecurityFilterChain即可
     * 那些handler可以直接new，不过这里采用spring ioc注入的方式
     * @param http
     * @return
     */
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(
            ServerHttpSecurity http,
            SimpleRejectServerAuthenticationEntryPoint simpleRejectServerAuthenticationEntryPoint,
            UserPasswordAuthenticateSuccessHandler authenticationSuccessHandler,
            UserPasswordAuthenticateFailureHandler authenticateFailureHandler,
            UserLogoutHandler logoutHandler,
            UserLogoutSuccessHandler logoutSuccessHandler,
            JwtAuthenticationTokenFilter tokenFilter,
            ReactiveAuthenticationManager reactiveAuthenticationManager,
            GlobalReactiveAuthorizationManager globalReactiveAuthorizationManager,
            ExceptionHandlerFilter exceptionHandlerFilter) {

        // @formatter:off
        http.csrf(ServerHttpSecurity.CsrfSpec::disable) // 关闭csrf
            .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable) // 默认是enable的，关闭它
            .authenticationManager(reactiveAuthenticationManager) // 设置根authenticationManager，也可以每个Filter单独设置
            .exceptionHandling(
                    handling->
                            handling.authenticationEntryPoint(simpleRejectServerAuthenticationEntryPoint) // 用户未认证入口
                            .accessDeniedHandler(
                                    // 无权限，响应403
                                    (exchange, deniedException) ->
                                        ResponseUtil.writeResponseResult(
                                                exchange.getResponse(), HttpStatus.FORBIDDEN, ResponseResult.error(StatusCodeEnum.AUTHORITY_FAIL))

                            )
            )
            // ServerHttpSecurity没有sessionManagement，要设置Session为无状态stateless的，可以通过下面的办法
            // https://github.com/spring-projects/spring-security/issues/6552#issuecomment-519398510
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
            .authorizeExchange(
                    exchangeSpec->exchangeSpec.pathMatchers(excludeAuthPage).permitAll() // 白名单
                        .pathMatchers(HttpMethod.OPTIONS).permitAll() // option请求放行。如果已经通过其他方式处理跨域就不用这个了
//                        .anyExchange().authenticated()// 除上面外的所有请求全部需要鉴权认证
                        .anyExchange().access(globalReactiveAuthorizationManager)
            )
            // 不需要用户密码验证的话，可以把它关掉formLogin(ServerHttpSecurity.FormLoginSpec::disable)
            .formLogin(
                    form->form
                        //默认请求路径是/login。loginPage未登录时调用的接口 ,因为我们已经设置了entryPoint，这里不必设置了
                        // loginPage实际上就设置了一个路径匹配器和entryPoint(跳转到你给的路径，这里/login)
                        // 如果没有使用loginPage，并且没有自定义entryPoint，它就给你返回一个自动生成的登录页面
                       .loginPage("/auth/login")
                        .authenticationSuccessHandler(authenticationSuccessHandler)
                        .authenticationFailureHandler(authenticateFailureHandler)
//                        .requiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/auth/login"))
//                            .authenticationManager() 可以在这里设置manager

            )
            .logout(
                    logout->logout.logoutUrl("/auth/logout")
                        .logoutHandler(logoutHandler)  // 这里和login一样，也可以不设置，然后通过controller来处理
                        .logoutSuccessHandler(logoutSuccessHandler)
            )
            .addFilterAfter(tokenFilter, SecurityWebFiltersOrder.AUTHENTICATION) // 放在authentication后面
            .addFilterAt(exceptionHandlerFilter, SecurityWebFiltersOrder.HTTP_BASIC); // 放在JwtTokenFilter之前即可
        // @formatter:on


        return http.build();
    }
}
