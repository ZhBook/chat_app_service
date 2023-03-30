package com.tensua.secruity.handler;

import com.tensua.data.BaseResult;
import com.tensua.enums.ResultCodeEnum;
import com.tensua.utils.IpAddressUtil;
import com.tensua.utils.WebUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Security 没有对应认证 PROVIDER
 */
@Component
@Slf4j
public class SecureNoAuthenticationHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {

        String ip = IpAddressUtil.get(httpServletRequest);
        log.error("No authentication provider to access, url=[{}], ip=[{}]", httpServletRequest.getRequestURI(), ip, e);
        WebUtil.writeJson(BaseResult.fail(ResultCodeEnum.NOT_LOGIN));
    }
}
