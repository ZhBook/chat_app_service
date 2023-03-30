package com.tensua.secruity.handler;

import com.tensua.data.BaseResult;
import com.tensua.enums.ResultCodeEnum;
import com.tensua.utils.IpAddressUtil;
import com.tensua.utils.WebUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Security 登陆失败处理类
 */
@Component
@Slf4j
public class SecureLoginFailureHandler implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {

        String ip = IpAddressUtil.get(httpServletRequest);
        log.error("登录失败, url=[{}], ip=[{}], msg=[{}]", httpServletRequest.getRequestURI(), ip, e.getMessage(), e);

        if (e instanceof UsernameNotFoundException) {
            WebUtil.writeJson(BaseResult.fail(ResultCodeEnum.USER_USERNAME_NOT_FOUND));
            return;
        }
        if (e instanceof LockedException) {
            WebUtil.writeJson(BaseResult.fail(ResultCodeEnum.USER_LOCKED));
            return;
        }
        if (e instanceof BadCredentialsException) {
            WebUtil.writeJson(BaseResult.fail(ResultCodeEnum.USER_BAD_CREDENTIALS));
            return;
        }
        if (e instanceof AccountExpiredException) {
            WebUtil.writeJson(BaseResult.fail(ResultCodeEnum.USER_EXPIRED));
            return;
        }
        if (e instanceof DisabledException) {
            WebUtil.writeJson(BaseResult.fail(ResultCodeEnum.USER_NOT_ENABLE));
            return;
        }

        if(e instanceof InternalAuthenticationServiceException){
            WebUtil.writeJson(BaseResult.fail(ResultCodeEnum.LOGIN_FAILURE_INTERNAL_ERROR));
            return;
        }

        WebUtil.writeJson(BaseResult.fail(ResultCodeEnum.LOGIN_FAILURE));
    }
}
