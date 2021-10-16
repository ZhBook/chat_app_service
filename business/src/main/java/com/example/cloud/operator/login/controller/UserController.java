package com.example.cloud.operator.login.controller;

import com.example.cloud.enums.Result;
import com.example.cloud.operator.login.entity.UserInfo;
import com.example.cloud.operator.login.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author:70968 Date:2021-10-06 09:48
 */
@RestController
@RequestMapping("/users")
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

    /**
     * 通过用户名获取用户信息
     * @param username
     * @return
     */
    @GetMapping("/username/{username}")
    public Result<UserInfo> getUserByUsername(@PathVariable("username") String username){
        return Result.succeed(userFacade.getUserByUsername(username));
    }

    /**
     * 用户注册接口
     * @param userInfo
     * @return
     */
    @PostMapping("/register")
    public Result<UserInfo> registerUser(@Valid @RequestBody UserInfo userInfo) {
        return Result.succeed(userFacade.registerUser(userInfo),"注册成功");
    }

    @PostMapping("/test")
    public Result<UserInfo> test() {
        return Result.succeed("访问成功了");
    }
}
