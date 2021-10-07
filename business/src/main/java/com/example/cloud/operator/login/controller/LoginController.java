package com.example.cloud.operator.login.controller;

import com.example.cloud.model.Result;
import com.example.cloud.operator.login.entity.UserInfo;
import com.example.cloud.operator.login.facade.LoginFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:70968 Date:2021-10-06 09:48
 */
@RestController
public class LoginController {
    @Autowired
    private LoginFacade loginFacade;

    @PostMapping("/login")
    public Result<UserInfo> login(@RequestParam("username") String username,
                                  @RequestParam("password") String password) {
        return Result.succeed(loginFacade.login(username, password));
    }
}
