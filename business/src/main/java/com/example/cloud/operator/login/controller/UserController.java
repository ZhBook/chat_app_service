package com.example.cloud.operator.login.controller;

import com.example.cloud.enums.Result;
import com.example.cloud.operator.login.entity.UserInfo;
import com.example.cloud.operator.login.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author:70968 Date:2021-10-06 09:48
 */
@RestController
@RequestMapping("/api/v1/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserFacade userFacade;

    /**
     * 通过用户名和密码进行登录
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    public Result<UserInfo> login(@RequestParam("username") String username,
                                  @RequestParam("password") String password) {
        return Result.succeed(userFacade.login(username, password));
    }

    @GetMapping("/username/{username}")
    public Result<UserInfo> getUserByUsername(@PathVariable("username") String username){
        return Result.succeed(userFacade.getUserByUsername(username));
    }

    @PostMapping("/register")
    public Result<UserInfo> registerUser(@RequestBody UserInfo userInfo) {
        return Result.succeed(userFacade.registerUser(userInfo),"注册成功");
    }

    @PostMapping("/test")
    public Result<UserInfo> test() {
        return Result.succeed("访问成功了");
    }
}
