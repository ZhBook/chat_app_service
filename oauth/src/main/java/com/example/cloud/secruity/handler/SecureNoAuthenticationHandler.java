package com.example.cloud.secruity.handler;

import com.example.cloud.data.BaseResult;
import com.example.cloud.enums.ResultCodeEnum;
import com.example.cloud.utils.IpAddressUtil;
import com.example.cloud.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
