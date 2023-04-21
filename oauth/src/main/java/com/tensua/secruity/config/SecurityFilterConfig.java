package com.tensua.secruity.config;

import com.tensua.constant.SecurityConstant;
import com.tensua.secruity.filter.SecureUserTokenSupportFilter;
import com.tensua.secruity.handle.SecureLoginSuccessHandler;
import com.tensua.secruity.provider.UsernameAuthenticationProvider;
import com.tensua.secruity.service.SecureUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
    public UserDetailsService users() {
        return new SecureUserService();
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
                .formLogin().loginPage("/oauth/login").successHandler(secureLoginSuccessHandler).permitAll().and()
                .sessionManagement()
                // 使用无状态session，即不使用session缓存数据
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and().exceptionHandling().authenticationEntryPoint(new SecurityAuthenticationEntryPoint())
                .and().headers().frameOptions().disable()
                .and().csrf().disable()
                .cors().disable()
        ;
//        http.addFilterAt(usernameAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new SecureUserTokenSupportFilter(), UsernamePasswordAuthenticationFilter.class);
//        http.authenticationProvider(usernameAuthenticationProvider);

//        http.addFilterAt(usernameAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//
//        http.getSharedObject(AuthenticationManagerBuilder.class)
//                .authenticationProvider(usernameAuthenticationProvider);
        return http.build();
    }

//    /**
//     * TODO 四 4.4 基于用户名和密码或使用用户名和密码进行身份验证
//     * @param config
//     * @return
//     * @throws Exception
//     */
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }

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

//    private AbstractAuthenticationProcessingFilter usernameAuthenticationFilter() throws Exception {
//        UsernameAuthenticationFilter authenticationFilter = new UsernameAuthenticationFilter(new AntPathRequestMatcher(SecurityConstant.LOGIN_URL, SecurityConstant.LOGIN_METHOD));
//        authenticationFilter.setAuthenticationSuccessHandler(secureLoginSuccessHandler);
////        authenticationFilter.setAuthenticationFailureHandler(secureLoginFailureHandler);
//        return authenticationFilter;
//    }

}
