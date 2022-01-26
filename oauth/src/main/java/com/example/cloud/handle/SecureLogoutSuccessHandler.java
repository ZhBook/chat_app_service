package com.example.cloud.handle;

import cn.hutool.json.JSONUtil;
import com.example.cloud.data.BaseResult;
import com.example.cloud.entity.AuthConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Security 注销成功处理类
 */
@Slf4j
@Component
public class SecureLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        String token = httpServletRequest.getHeader(AuthConstants.AUTHORIZATION_KEY);
        String bearer = token.replace(AuthConstants.JWT_PREFIX, "");
        consumerTokenServices.revokeToken(bearer);
        httpServletResponse.setHeader("Content-type", "application/json;charset=" + AuthConstants.UTF8);
        httpServletResponse.setCharacterEncoding(AuthConstants.UTF8);
        httpServletResponse.getWriter().write(JSONUtil.toJsonStr(BaseResult.succeed("退出成功")));
    }
}
