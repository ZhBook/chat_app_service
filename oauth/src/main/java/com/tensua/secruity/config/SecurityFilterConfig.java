package com.tensua.secruity.config;

import com.tensua.constant.SecurityConstant;
import com.tensua.secruity.exception.SecurityAuthenticationEntryPoint;
import com.tensua.secruity.filter.SecureUserTokenSupportFilter;
import com.tensua.secruity.handle.SecureLoginFailureHandler;
import com.tensua.secruity.handle.SecureLoginSuccessHandler;
import com.tensua.secruity.provider.UsernameAuthenticationProvider;
import com.tensua.secruity.service.SecureUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/**
 * Spring SecurityFilter 配置文件
 */
@Configuration
//@RequiredArgsConstructor
@EnableWebSecurity
//@EnableMethodSecurity
public class SecurityFilterConfig {
    /**
     * 自定义登陆成功处理器
     */
    @Resource
    private SecureLoginSuccessHandler secureLoginSuccessHandler;

    /**
     * 自定义用户名和密码登陆验证
     */
    @Resource
    private UsernameAuthenticationProvider usernameAuthenticationProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService users() {
        return new SecureUserService();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(SecurityConstant.HTTP_ACT_MATCHERS).permitAll()
                                .anyRequest().authenticated()
                )
                // 禁用缓存
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.loginPage("/oauth/login")
                        .successHandler(secureLoginSuccessHandler)
                        .failureHandler(new SecureLoginFailureHandler())
                        .permitAll()

                ).sessionManagement(Customizer.withDefaults())

                // 使用无状态session，即不使用session缓存数据
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
                        httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(new SecurityAuthenticationEntryPoint()))
                .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
        ;

        http.addFilterBefore(new SecureUserTokenSupportFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(new SecureUserService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(usernameAuthenticationProvider);
        return authenticationManagerBuilder.build();
    }
}
