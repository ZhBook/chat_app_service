package com.example.cloud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
    public TokenStore redisTokenStore() {
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
                //token转换，使用jwt   加上此配置即可
                .accessTokenConverter(accessTokenConverter())
                .authenticationManager(authenticationManager)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
        //数据库存储
//        endpoints.tokenStore(tokenStore())
//                .authenticationManager(authenticationManager)
//                //设置访问token端点的方法类型，允许get提交
//                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.GET);
    }

  /*对称加密
    //jwt转换器
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        //加密秘钥
        converter.setSigningKey("123");
        return converter;
    }*/

    /**
     * jwt 非对称加密
     * @return
     */
  @Bean
  public JwtAccessTokenConverter accessTokenConverter() {
      //私钥，jks文件路径，以及申请时的密码
      JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
      KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("example.com_keystore.jks"), "123456".toCharArray());
      //此处传入的是创建jks文件时的别名-alias ims.abc.com
      converter.setKeyPair(keyStoreKeyFactory.getKeyPair("example.com"));
      //公钥（读取public.txt的公钥信息）
      Resource resource = new ClassPathResource("public.txt");
      String publicKey = null;
      try {
          publicKey = inputStream2String(resource.getInputStream());
      } catch (final IOException e) {
          throw new RuntimeException(e);
      }
      converter.setVerifierKey(publicKey);
      return converter;
  }

    String inputStream2String(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = in.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }
}
