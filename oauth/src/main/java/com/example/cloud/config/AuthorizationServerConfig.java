package com.example.cloud.config;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.example.cloud.constants.UserInfo;
import com.example.cloud.enums.Result;
import com.example.cloud.enums.ResultCode;
import com.example.cloud.extension.CaptchaTokenGranter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author:70968 Date:2021-10-07 08:38
 */
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

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
     * @throws Exception
     */
    /*@Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //允许表单登录
        security.allowFormAuthenticationForClients();
    }*/

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

    /**
     * 配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //redis token存储
        endpoints
                .tokenStore(new RedisTokenStore(redisConnectionFactory))
                //token转换，使用jwt   加上此配置即可
                .accessTokenConverter(accessTokenConverter())
                .authenticationManager(authenticationManager)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
        // 添加自定义授权模式
        List<TokenGranter> granterList = new ArrayList<>(Arrays.asList(endpoints.getTokenGranter()));
        granterList.add(new CaptchaTokenGranter(endpoints.getTokenServices(), endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory(), authenticationManager

        ));

        CompositeTokenGranter compositeTokenGranter = new CompositeTokenGranter(granterList);
        endpoints
                .authenticationManager(authenticationManager)
                .tokenGranter(compositeTokenGranter)
                //.userDetailsService(sysUserDetailsService)
                /** refresh token有两种使用方式：重复使用(true)、非重复使用(false)，默认为true
                 *  1 重复使用：access token过期刷新时， refresh token过期时间未改变，仍以初次生成的时间为准
                 *  2 非重复使用：access token过期刷新时， refresh token过期时间延续，在refresh token有效期内刷新便永不失效达到无需再次登录的目的
                 */
                .reuseRefreshTokens(true); // 自定义的TokenService
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
      KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "123456".toCharArray());
      //此处传入的是创建jks文件时的别名-alias ims.abc.com
      converter.setKeyPair(keyStoreKeyFactory.getKeyPair("mytestkey"));
      //公钥（读取public.txt的公钥信息）
      Resource resource = new ClassPathResource("public.txt");
      String publicKey;
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
        String line;
        while ((line = in.readLine()) != null) {
            buffer.append(line);
        }
        return buffer.toString();
    }

    /**
     * 密钥库中获取密钥对(公钥+私钥)
     */
    @Bean
    public KeyPair keyPair() {
//        Resource pathResource = new PathResource("D:\\Tools\\jwt.jks");

        KeyStoreKeyFactory factory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "123456".toCharArray());
        KeyPair keyPair = factory.getKeyPair("mytestkey", "123456".toCharArray());
        return keyPair;
    }

    /**
     * 本地加载JWT验签公钥
     * @return
     */
    @SneakyThrows
    @Bean
    public RSAPublicKey rsaPublicKey() {
        Resource resource = new ClassPathResource("public.txt");
        InputStream is = resource.getInputStream();
        String publicKeyData = IoUtil.read(is).toString();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec((Base64.decode(publicKeyData)));

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKey rsaPublicKey = (RSAPublicKey)keyFactory.generatePublic(keySpec);
        return rsaPublicKey;
    }

    /**
     * 自定义认证异常响应数据
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, e) -> {
            response.setStatus(HttpStatus.HTTP_OK);
            response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Cache-Control", "no-cache");
            Result result = Result.failed(ResultCode.FAILED.getMessage());
            response.getWriter().print(JSONUtil.toJsonStr(result));
            response.getWriter().flush();
        };
    }

    /**
     * JWT内容增强
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return (accessToken, authentication) -> {
            Map<String, Object> additionalInfo = CollectionUtil.newHashMap();
            UserInfo userInfo = (UserInfo) authentication.getUserAuthentication().getPrincipal();
            additionalInfo.put("userId", userInfo.getId());
            additionalInfo.put("username", userInfo.getUsername());
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
            return accessToken;
        };
    }

    /**
     * 密码编码器
     * 委托方式，根据密码的前缀选择对应的encoder，例如：{bcypt}前缀->标识BCYPT算法加密；{noop}->标识不使用任何加密即明文的方式
     * 密码判读 DaoAuthenticationProvider#additionalAuthenticationChecks
     */
    /*@Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }*/
}
