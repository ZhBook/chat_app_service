package com.tensua.secruity.exception;

import cn.hutool.json.JSONUtil;
import com.tensua.data.BaseResult;
import com.tensua.enums.ResultCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * 匿名用户(token不存在、错误)，异常处理器
 *
 */
public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader(HttpHeaders.ORIGIN));

        response.getWriter().print(JSONUtil.toJsonStr(BaseResult.fail(ResultCodeEnum.UNAUTHORIZED)));
    }
}