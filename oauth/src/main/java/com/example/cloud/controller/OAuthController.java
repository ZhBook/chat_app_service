package com.example.cloud.controller;

import cn.hutool.core.lang.Validator;
import cn.hutool.json.JSONUtil;
import com.example.cloud.api.UserFeignClient;
import com.example.cloud.data.UserInfoResponse;
import com.example.cloud.entity.AccessToken;
import com.example.cloud.entity.UserInfo;
import com.example.cloud.enums.BaseResult;
import com.example.cloud.utils.RequestUtils;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.security.KeyPair;
import java.security.Principal;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

/**
 * 获取RSA公钥接口
 * Created by macro on 2020/6/19.
 */
@RestController
@RequestMapping("/oauth")
@AllArgsConstructor
@Slf4j
public class OAuthController {

    @Autowired
    private KeyPair keyPair;

    @Resource
    private UserFeignClient userFeignClient;

    private TokenEndpoint tokenEndpoint;

    /**
     * OAuth2认证
     *
     * @param principal
     * @param parameters
     * @return
     * @throws HttpRequestMethodNotSupportedException
     */
    @PostMapping("/token")
    public Object postAccessToken(
            Principal principal,
            @RequestParam Map<String, String> parameters
    ) throws HttpRequestMethodNotSupportedException {

        /**
         * 获取登录认证的客户端ID
         *
         * 兼容两种方式获取Oauth2客户端信息（client_id、client_secret）
         * 方式一：client_id、client_secret放在请求路径中(注：当前版本已废弃)
         * 方式二：放在请求头（Request Headers）中的Authorization字段，且经过加密，例如 Basic Y2xpZW50OnNlY3JldA== 明文等于 client:secret
         */
        String clientId = RequestUtils.getOAuth2ClientId();
        log.info("OAuth认证授权 客户端ID:{}，请求参数：{}", clientId, JSONUtil.toJsonStr(parameters));

        //通过手机号码登陆
        String username = parameters.get("username");
        BaseResult<UserInfo> baseResult = userFeignClient.getUserByUsername(username);
        if (Validator.isMobile(username)) {
            baseResult = userFeignClient.getUserByMobile(username);
            parameters.put("username", baseResult.getData().getUsername());
        }
        /**
         * knife4j接口文档测试使用
         *
         * 请求头自动填充，token必须原生返回，否则显示 undefined undefined
         * 账号/密码:  client_id/client_secret : client/123456
         */

        OAuth2AccessToken accessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
        AccessToken result = new AccessToken();
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        BeanUtils.copyProperties(baseResult.getData(), userInfoResponse);
        result.setAccess_token(accessToken.getValue());
        result.setUserInfoResponse(userInfoResponse);
        return BaseResult.succeed(result);
    }

    @GetMapping("/token/rsa/publicKey")
    public Map<String, Object> getKey() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key).toJSONObject();
    }

}
