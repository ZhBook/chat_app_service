package com.tensua.secruity.handle;

import com.tensua.data.BaseResult;
import com.tensua.enums.ResultCodeEnum;
import com.tensua.secruity.token.SecureUserTokenService;
import com.tensua.utils.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Security 登陆成功处理类
 */
@Component
@Slf4j
public class SecureLoginFailureHandler implements AuthenticationFailureHandler {


    @Autowired
    private SecureUserTokenService customUserDetailsTokenService;


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        WebUtil.writeJson(BaseResult.fail(ResultCodeEnum.UNAUTHORIZED));
    }
}
