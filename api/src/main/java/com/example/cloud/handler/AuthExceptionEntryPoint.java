package com.example.cloud.handler;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) {
        Throwable cause = authException.getCause();
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        try {
            JSONObject body = new JSONObject();
            body.put("code", 1001);
            body.put("msg", "token无效");
            if (!(cause instanceof InvalidTokenException)) {
                body.put("code", 1002);
                body.put("msg", "token丢失");
            }
            response.getWriter().write(body.toJSONString());
        } catch (IOException e) {
            log.error("token认证失败", e);
        }
    }
}

