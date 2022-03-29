package com.example.cloud.secruity.handler;

import com.example.cloud.constant.SecurityConstant;
import com.example.cloud.data.BaseResult;
import com.example.cloud.data.security.SecureUserToken;
import com.example.cloud.data.security.UserInfo;
import com.example.cloud.data.response.user.UserInfoResponse;
import com.example.cloud.secruity.token.SecureUserTokenService;
import com.example.cloud.utils.IpAddressUtil;
import com.example.cloud.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

        SecureUserToken userToken = customUserDetailsTokenService.createToken(secureUser);
        String tk = customUserDetailsTokenService.saveToken(userToken);
        String jwt = userToken.getToken();

        String ip = IpAddressUtil.get(httpServletRequest);
        log.info("登录成功, url=[{}], ip=[{}]", httpServletRequest.getRequestURI(), ip);

        Map<String, Object> tokenMap = new HashMap<String, Object>() {{
            put(SecurityConstant.TOKEN_HEADER_KEY, tk);
            put(SecurityConstant.TOKEN_HEADER, jwt);
            UserInfoResponse userInfoResponse = new UserInfoResponse();
            BeanUtils.copyProperties(secureUser, userInfoResponse);
            put(SecurityConstant.USER_INFO, userInfoResponse);
        }};

        WebUtil.writeJson(BaseResult.succeed(tokenMap));
    }
}
