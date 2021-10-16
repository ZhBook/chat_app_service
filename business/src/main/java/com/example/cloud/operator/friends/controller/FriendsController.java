package com.example.cloud.operator.friends.controller;

import com.example.cloud.data.request.friends.AddFriendsRequest;
import com.example.cloud.data.request.friends.HandleFriendsRequest;
import com.example.cloud.enums.Result;
import com.example.cloud.operator.friends.facade.FriendsFacade;
import com.example.cloud.operator.login.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    public Result<List<UserInfo>> getFriends(@PathVariable("userId") Long userId) {
        return Result.succeed(friendsFacade.getFriends(userId));
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
