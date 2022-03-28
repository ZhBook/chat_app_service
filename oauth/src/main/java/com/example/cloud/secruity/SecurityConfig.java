package com.example.cloud.secruity;

import com.example.cloud.constant.SecurityConstant;
import com.example.cloud.secruity.captcha.SecureCaptchaSupportFilter;
import com.example.cloud.secruity.filter.SmsAuthenticationFilter;
import com.example.cloud.secruity.filter.UsernameAuthenticationFilter;
import com.example.cloud.secruity.handler.*;
import com.example.cloud.secruity.provider.SmsAuthenticationProvider;
import com.example.cloud.secruity.provider.UsernameAuthenticationProvider;
import com.example.cloud.secruity.token.SecureUserTokenSupportFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 自定义登陆成功处理器
     */
    @Autowired
    private SecureLoginSuccessHandler secureLoginSuccessHandler;

    /**
     * 自定义登陆失败处理器
     */
    @Autowired
    private SecureLoginFailureHandler secureLoginFailureHandler;

    /**
     * 自定义注销成功处理器
     */
    @Autowired
    private SecureLogoutSuccessHandler secureLogoutSuccessHandler;

    /**
     * 自定义暂无权限处理器
     */
    @Autowired
    private SecureNoPermissionHandler secureNoPermissionHandler;

    /**
     * 自定义未登录的处理器
     */
    @Autowired
    private SecureNoAuthenticationHandler secureNoAuthenticationHandler;

    /**
     * 自定义用户名和密码登陆验证
     */
    @Autowired
    private UsernameAuthenticationProvider usernameAuthenticationProvider;

    /**
     * 自定义短信验证码登陆验证
     */
    @Autowired
    private SmsAuthenticationProvider smsAuthenticationProvider;

//
//    @Value("${server.servlet.context-path}")
//    private String servletContextPath;


    /**
     * Web Configure 核心配置
     */
    @Override
    public void configure(WebSecurity web) {
        // TODO 解决静态资源被拦截的问题
        web.ignoring().antMatchers(SecurityConstant.WEB_ACT_MATCHERS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(usernameAuthenticationProvider)
                .authenticationProvider(smsAuthenticationProvider);
    }

    /**
     * Http Security 核心配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //不进行权限验证的请求或资源(从配置文件中读取)
                .antMatchers(SecurityConstant.HTTP_ACT_MATCHERS).permitAll()
                //其他的需要登陆后才能访问
                .anyRequest().authenticated()
//                .and()
//                //配置未登录自定义处理类
//                .httpBasic()
//                .and()
                //配置登录地址
//                .formLogin()
//                .loginProcessingUrl(SecurityConstant.LOGIN_URL)
                //配置登录成功自定义处理类
//                .successHandler(secureLoginSuccessHandler)
                //配置登录失败自定义处理类
//                .failureHandler(secureLoginFailureHandler)
                .and()
                //配置登出地址
                .logout()
                .logoutUrl(SecurityConstant.LOGOUT_URL)
                //配置用户登出自定义处理类
                .logoutSuccessHandler(secureLogoutSuccessHandler)
                .and()
                //配置没有权限自定义处理类
                .exceptionHandling()
                .accessDeniedHandler(secureNoPermissionHandler)
                .authenticationEntryPoint(secureNoAuthenticationHandler)
                .and()
                // 取消跨站请求伪造防护
                .csrf()
                .disable();

        // 处理 Iframe 响应
        http.headers().frameOptions().disable();
        // 禁用缓存
        http.headers().cacheControl();
        // 基于 Token 不需要 session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Captcha / Token Filter
        http.addFilterBefore(new SecureUserTokenSupportFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new SecureCaptchaSupportFilter(), UsernamePasswordAuthenticationFilter.class);

        //自定义filter
        http.addFilterBefore(smsAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        //替换
        http.addFilterAt(usernameAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // 开启 Security 跨域
        http.cors();
    }


    private AbstractAuthenticationProcessingFilter smsAuthenticationFilter() throws Exception {
        SmsAuthenticationFilter authenticationFilter = new SmsAuthenticationFilter(new AntPathRequestMatcher(SecurityConstant.SMS_LOGIN_URL, SecurityConstant.LOGIN_METHOD));
        authenticationFilter.setAuthenticationManager(this.authenticationManager());
        authenticationFilter.setAuthenticationSuccessHandler(secureLoginSuccessHandler);
        authenticationFilter.setAuthenticationFailureHandler(secureLoginFailureHandler);
        return authenticationFilter;
    }


    private AbstractAuthenticationProcessingFilter usernameAuthenticationFilter() throws Exception {
        UsernameAuthenticationFilter authenticationFilter = new UsernameAuthenticationFilter(new AntPathRequestMatcher(SecurityConstant.LOGIN_URL, SecurityConstant.LOGIN_METHOD));
        authenticationFilter.setAuthenticationManager(this.authenticationManager());
        authenticationFilter.setAuthenticationSuccessHandler(secureLoginSuccessHandler);
        authenticationFilter.setAuthenticationFailureHandler(secureLoginFailureHandler);
        return authenticationFilter;
    }

}