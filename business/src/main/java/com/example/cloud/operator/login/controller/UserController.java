package com.example.cloud.operator.login.controller;

import com.example.cloud.data.BaseResult;
import com.example.cloud.data.request.user.UserInfoRequest;
import com.example.cloud.data.response.login.UserInfoResponse;
import com.example.cloud.operator.login.entity.UserInfo;
import com.example.cloud.operator.login.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author:70968 Date:2021-10-06 09:48
 */
@RestController
@RequestMapping("/users")
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
    public BaseResult<UserInfo> login(@RequestParam("username") String username,
                                      @RequestParam("password") String password) {
        return BaseResult.succeed(userFacade.login(username, password));
    }

    /**
     * 通过用户名获取用户信息
     * @param username
     * @return
     */
    @GetMapping("/username")
    public BaseResult<UserInfo> getUserByUsername(@RequestParam("username") String username){
        return BaseResult.succeed(userFacade.getUserByUsername(username));
    }

    /**
     * 通过手机号查询用户信息
     * @param mobile
     * @return
     */
    @GetMapping("/mobile/{mobile}")
    public BaseResult<UserInfo> getUserByMobile(@PathVariable("mobile")String mobile){
        return BaseResult.succeed(userFacade.getUserByMobile(mobile));
    }

    /**
     * 用户注册接口
     * @param userInfo
     * @return
     */
    @PostMapping("/register")
    public BaseResult<UserInfoResponse> registerUser(@RequestBody @Validated UserInfo userInfo) {
        return BaseResult.succeed(userFacade.registerUser(userInfo),"注册成功");
    }

    /**
     * 修改用户信息
     * @param request
     * @return
     */
    @PutMapping
    public BaseResult<UserInfoResponse> updateUserInfo(@RequestBody UserInfoRequest request ){
        return BaseResult.succeed(userFacade.updateUserInfo(request));
    }

    @PostMapping("/test")
    public BaseResult<UserInfo> test() {
        return BaseResult.succeed("访问成功了");
    }
}
