package com.example.cloud.operator.login.controller;

import com.example.cloud.model.Result;
import com.example.cloud.operator.login.entity.UserInfo;
import com.example.cloud.operator.login.facade.RegisterFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:70968
 * Date:2021-10-06 11:03
 */
@RestController
public class RegisterController {

    @Autowired
    private RegisterFacade registerFacade;

    @PostMapping("/register")
    public Result<Boolean> registerUser(@RequestBody UserInfo userInfo) {
        return Result.succeed(registerFacade.registerUser(userInfo));
    }
}
