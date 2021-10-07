package com.example.cloud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;

/**
 * @author:70968 Date:2021-10-07 08:38
 */
@Configuration
@EnableAuthorizationServer
public class OAuthConfig extends AuthorizationServerConfigurerAdapter {

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private DataSource dataSource;
    /**
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //写到内存中的数据，仅用来测试
        /*clients.inMemory()
                //客户端 client_id
                .withClient("client")
                //客户端 secret
                .secret(passwordEncoder.encode("123"))
                //授权类型 授权码
                .authorizedGrantTypes("password", "refresh_token")
                //范围
                .scopes("app");*/
        //从数据库表中直接获取所有的信息 表oauth_client_details
        clients.jdbc(dataSource);
    }

    /**
     * 配置令牌端点的安全约束
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //允许表单登录
        security.allowFormAuthenticationForClients();
    }

    @Bean
    public TokenStore redisTokenStore (){
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * 设置token 比如token的有效时长等
     *
     * @return
     */
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setAccessTokenValiditySeconds(1000000);
        defaultTokenServices.setRefreshTokenValiditySeconds(200000);
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setReuseRefreshToken(false);
        defaultTokenServices.setTokenStore(redisTokenStore());
        return defaultTokenServices;
    }

    @Override
    //配置授权以及令牌的访问端点和令牌服务
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //redis token存储
        endpoints
                .tokenStore(new RedisTokenStore(redisConnectionFactory))
                .authenticationManager(authenticationManager);
        //数据库存储
//        endpoints.tokenStore(tokenStore())
//                .authenticationManager(authenticationManager)
//                //设置访问token端点的方法类型，允许get提交
//                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.GET);
    }

}
