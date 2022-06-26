package com.tensua.secruity.handler;

import com.tensua.constant.SecurityConstant;
import com.tensua.data.BaseResult;
import com.tensua.secruity.token.SecureUserTokenService;
import com.tensua.utils.WebUtil;
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
