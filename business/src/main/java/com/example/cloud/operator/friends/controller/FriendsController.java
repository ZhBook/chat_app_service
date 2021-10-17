package com.example.cloud.operator.friends.controller;

import com.example.cloud.data.PageResult;
import com.example.cloud.data.request.friends.AddFriendsRequest;
import com.example.cloud.data.request.friends.HandleFriendsRequest;
import com.example.cloud.enums.Result;
import com.example.cloud.operator.friends.facade.FriendsFacade;
import com.example.cloud.operator.login.entity.UserInfo;
import com.example.cloud.system.NoParamsUserBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author:70968 Date:2021-10-16 08:49
 */
@RestController
@RequestMapping("/friends")
@Slf4j
public class FriendsController {
    @Autowired
    private FriendsFacade friendsFacade;

    /**
     * 返回好友列表，无分页
     *
     * @param request
     * @return
     */
    @GetMapping
    public Result<PageResult<UserInfo>> getFriends(@RequestBody NoParamsUserBean request) {
        return Result.succeed(friendsFacade.getFriends(request));
    }

    /**
     * 添加好友
     *
     * @param request
     * @return
     */
    @PostMapping("/add")
    public Result<Boolean> addFriends(@RequestBody AddFriendsRequest request) {
        return Result.succeed(friendsFacade.addFriends(request));
    }

    /**
     * 处理好友请求
     * @param request
     * @return
     */
    @PostMapping("/handle")
    public Result<Boolean> handleFriends(@RequestBody HandleFriendsRequest request) {
        return Result.succeed(friendsFacade.handleFriends(request));
    }

}
