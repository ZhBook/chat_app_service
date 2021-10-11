package com.example.cloud.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@Slf4j
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    private final UserDetailsService sysUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 这一步的配置是必不可少的，否则SpringBoot会自动配置一个AuthenticationManager,覆盖掉内存中的用户
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers( "/register/");
    }

    /**
     * 允许匿名访问所有接口 主要是 oauth 接口
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                authorizeRequests()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                .and().authorizeRequests().antMatchers("/oauth/**").permitAll()
                .antMatchers("/rsa/publicKey").permitAll()
                .anyRequest().authenticated();

        /**
         * access(String)	如果给定的SpEL表达式计算结果为true，则允许访问
         * anonyoms()	允许匿名访问
         * authenticated()	允许认证过的用户访问
         * denyAll()	无条件拒绝所有访问
         * fullyAuthenticated()	如果用户是完整认证的话（不是通过Rememeber-me功能认证的）就允许访问
         * hasAnyAuthority(String… authorities)	如果用户具备给定权限中的一个的话就允许访问
         * hasAnyRole(String… roles)	如果用户具备给定角色中的一个的话，就允许访问
         * hasAnyAuthority(String authorities)	如果用户具备给定权限的话就允许访问
         * hasIpAddress(String ipaddressExpression)	如果请求来自给定ip地址的话就允许访问
         * hasRole(String)	如果用户具备给定角色的话，就允许访问
         * not()	对其他访问方法的结果求反
         * permitAll()	无条件允许所有访问
         * rememberMe()	如果用户是通过Remember-me功能认证，就允许访问
         */
    }

    /**
     * 设置密码加密规则
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
        auth.authenticationProvider(daoAuthenticationProvider());

    }


    /**
     * 设置内存用户（本文把用户信息存到内存，也可以采用查询数据库的方式）
     *
     * @return
     */
    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        return userDetailsService;
    }

    /**
     * 用户名密码认证授权提供者
     *
     * @return
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setHideUserNotFoundExceptions(false); // 是否隐藏用户不存在异常，默认:true-隐藏；false-抛出异常；
        provider.setUserDetailsService(sysUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

}
