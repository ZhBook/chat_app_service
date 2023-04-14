package com.tensua.secruity.config;

import com.tensua.constant.SecurityConstant;
import com.tensua.secruity.filter.SecureUserTokenSupportFilter;
import com.tensua.secruity.filter.UsernameAuthenticationFilter;
import com.tensua.secruity.handle.SecureLoginSuccessHandler;
import com.tensua.secruity.provider.UsernameAuthenticationProvider;
import com.tensua.secruity.service.SecureUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring SecurityFilter 配置文件
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityFilterConfig {
    /**
     * 自定义登陆成功处理器
     */
    @Autowired
    private SecureLoginSuccessHandler secureLoginSuccessHandler;

    /**
     * 自定义用户名和密码登陆验证
     */
    @Autowired
    private UsernameAuthenticationProvider usernameAuthenticationProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth ->
                                auth.requestMatchers(SecurityConstant.HTTP_ACT_MATCHERS).permitAll()
//                .requestMatchers(HttpMethod.OPTIONS).permitAll()
                                        .anyRequest().authenticated()
                )
//                .oauth2Login(withDefaults())
//                .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                // 禁用缓存
//                .formLogin().loginPage("/oauth/login").successHandler(secureLoginSuccessHandler)
//                .and()
                .sessionManagement()
                // 使用无状态session，即不使用session缓存数据
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and().exceptionHandling().authenticationEntryPoint(new SecurityAuthenticationEntryPoint())
                .and().headers().frameOptions().disable()
                .and().csrf().disable()
                .cors().disable()
        ;
        http.addFilterAt(usernameAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new SecureUserTokenSupportFilter(), UsernamePasswordAuthenticationFilter.class);
//        http.authenticationProvider(usernameAuthenticationProvider);
        return http.build();
    }

//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http,
//                                                       PasswordEncoder passwordEncoder,
//                                                       UserDetailsService userDetailsService) throws Exception {
//        return http.getSharedObject(AuthenticationManagerBuilder.class)
//                .authenticationProvider(usernameAuthenticationProvider)
//                .userDetailsService(userDetailsService)
//                .passwordEncoder(passwordEncoder)
//                .and()
//                .build();
//    }

    //    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        return authenticationManagerBuilder.authenticationProvider(usernameAuthenticationProvider).build();
//    }
    @Bean
    DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(new SecureUserService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public ProviderManager authManagerBean(HttpSecurity security) throws Exception {
        return (ProviderManager) security.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authProvider())
                .authenticationProvider(usernameAuthenticationProvider)
                .build();
    }

//        @Bean
    public UserDetailsService userDetailsService() {
        return new SecureUserService();
    }

    private AbstractAuthenticationProcessingFilter usernameAuthenticationFilter() throws Exception {
        UsernameAuthenticationFilter authenticationFilter = new UsernameAuthenticationFilter(new AntPathRequestMatcher(SecurityConstant.LOGIN_URL, SecurityConstant.LOGIN_METHOD));
//        authenticationFilter.setAuthenticationManager(this.authenticationManager());
        authenticationFilter.setAuthenticationSuccessHandler(secureLoginSuccessHandler);
//        authenticationFilter.setAuthenticationFailureHandler(secureLoginFailureHandler);
        return authenticationFilter;
    }

}
