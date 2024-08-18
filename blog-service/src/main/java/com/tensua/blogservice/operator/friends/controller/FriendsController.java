package com.tensua.blogservice.operator.friends.controller;

import com.tensua.blogservice.data.BaseResult;
import com.tensua.blogservice.data.PageResult;
import com.tensua.blogservice.data.request.friends.AddFriendsRequest;
import com.tensua.blogservice.data.request.friends.HandleFriendsRequest;
import com.tensua.blogservice.data.response.friends.FriendsResponse;
import com.tensua.blogservice.data.response.friends.NewFriendResponse;
import com.tensua.blogservice.data.response.login.UserInfoResponse;
import com.tensua.blogservice.data.system.NoParamsUserBean;
import com.tensua.blogservice.data.system.PagingUserBaseRequest;
import com.tensua.blogservice.operator.friends.facade.FriendsFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author:70968 Date:2021-10-16 08:49
 */
@RestController
@RequestMapping("/friends")
@Slf4j
public class FriendsController {
    @Resource
    private FriendsFacade friendsFacade;

    /**
     * 返回好友列表，无分页
     *
     * @param request
     * @return
     */
    @GetMapping
    public PageResult<List<FriendsResponse>> getFriends(PagingUserBaseRequest request) {
        return PageResult.pageSuccess(friendsFacade.getFriends(request));
    }

    /**
     * 添加好友
     *
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResult<Boolean> addFriends(@RequestBody AddFriendsRequest request) {
        return BaseResult.succeed(friendsFacade.addFriends(request));
    }

    /**
     * 处理好友请求
     *
     * @param request
     * @return
     */
    @PostMapping("/handle")
    public BaseResult<Boolean> handleFriends(@RequestBody HandleFriendsRequest request) {
        return BaseResult.succeed(friendsFacade.handleFriends(request));
    }

    /**
     * 查找好友
     * @param column
     * @return
     */
    @PostMapping("/search")
    public PageResult<List<UserInfoResponse>> searchFriends(@RequestBody PagingUserBaseRequest pagingUserBaseRequest, @RequestParam("column") String column){
        return PageResult.pageSuccess(friendsFacade.searchFriends(pagingUserBaseRequest,column));
    }

    /**
     * 获取新的好友请求
     * @param request
     * @return
     */
    @GetMapping("/request")
    public  PageResult<List<NewFriendResponse>> getNewFriends(NoParamsUserBean request){
        return PageResult.pageSuccess(friendsFacade.getNewFriends(request));
    }
}
