package com.example.cloud.controller;

import com.example.cloud.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author:70968 Date:2021-10-07 12:03
 */
@RestController
@Slf4j
public class LoginBackController {
    @GetMapping("/login/error")
    public Result loginError(HttpServletRequest request) {
        AuthenticationException authenticationException = (AuthenticationException) request.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        log.info("authenticationException={}", authenticationException);
        Result result = new Result();
        result.setCode(201);

        if (authenticationException instanceof UsernameNotFoundException || authenticationException instanceof BadCredentialsException) {
            result.setMsg("用户名或密码错误");
        } else if (authenticationException instanceof DisabledException) {
            result.setMsg("用户已被禁用");
        } else if (authenticationException instanceof LockedException) {
            result.setMsg("账户被锁定");
        } else if (authenticationException instanceof AccountExpiredException) {
            result.setMsg("账户过期");
        } else if (authenticationException instanceof CredentialsExpiredException) {
            result.setMsg("证书过期");
        } else {
            result.setMsg("登录失败");
        }
        return result;
    }
}
