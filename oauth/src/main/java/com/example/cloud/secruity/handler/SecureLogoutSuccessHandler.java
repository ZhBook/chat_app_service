package com.example.cloud.secruity.handler;

import com.example.cloud.constant.SecurityConstant;
import com.example.cloud.data.BaseResult;
import com.example.cloud.secruity.token.SecureUserTokenService;
import com.example.cloud.utils.WebUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Security 注销成功处理类
 */
@Component
public class SecureLogoutSuccessHandler implements LogoutSuccessHandler {



    @Resource
    private SecureUserTokenService customUserDetailsTokenService;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        String token = httpServletRequest.getHeader(SecurityConstant.TOKEN_HEADER_KEY);
        customUserDetailsTokenService.destroyToken(token);
        WebUtil.writeJson(BaseResult.succeed());
    }
}
