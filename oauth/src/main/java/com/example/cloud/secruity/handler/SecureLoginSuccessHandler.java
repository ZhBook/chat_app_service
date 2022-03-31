package com.example.cloud.secruity.handler;

import com.example.cloud.constant.SecurityConstant;
import com.example.cloud.data.BaseResult;
import com.example.cloud.data.response.user.UserInfoResponse;
import com.example.cloud.data.security.SecureUserToken;
import com.example.cloud.data.security.UserInfo;
import com.example.cloud.secruity.token.SecureUserTokenService;
import com.example.cloud.utils.IpAddressUtil;
import com.example.cloud.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Security 登陆成功处理类
 */
@Component
@Slf4j
public class SecureLoginSuccessHandler implements AuthenticationSuccessHandler {


    @Resource
    private SecureUserTokenService customUserDetailsTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {

        UserInfo secureUser = (UserInfo) authentication.getPrincipal();
        Object credentials = authentication.getCredentials();
        //检查token是否已存在
        String username = secureUser.getUsername();
        String key = DigestUtils.md5DigestAsHex(username.getBytes());
        SecureUserToken secureUserToken = customUserDetailsTokenService.taskToken(key);
        if (Objects.isNull(secureUserToken)){
            secureUserToken = customUserDetailsTokenService.createToken(secureUser);
            key = customUserDetailsTokenService.saveToken(secureUserToken);
        }
        String jwt = secureUserToken.getToken();

        String ip = IpAddressUtil.get(httpServletRequest);
        log.info("登录成功, url=[{}], ip=[{}]", httpServletRequest.getRequestURI(), ip);

        UserInfoResponse userInfoResponse = new UserInfoResponse();
        BeanUtils.copyProperties(secureUser, userInfoResponse);
        Map<String, Object> tokenMap = new HashMap<String, Object>() {{
            put(SecurityConstant.TOKEN_HEADER, jwt);
            put(SecurityConstant.USER_INFO, userInfoResponse);
        }};
        tokenMap.put(SecurityConstant.TOKEN_HEADER_KEY, key);

        WebUtil.writeJson(BaseResult.succeed(tokenMap));
    }
}
