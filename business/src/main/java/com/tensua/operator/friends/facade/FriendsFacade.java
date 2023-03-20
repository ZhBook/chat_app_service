package com.tensua.operator.friends.facade;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tensua.data.request.friends.AddFriendsRequest;
import com.tensua.data.request.friends.HandleFriendsRequest;
import com.tensua.data.response.friends.FriendsResponse;
import com.tensua.data.response.friends.NewFriendResponse;
import com.tensua.data.response.login.UserInfoResponse;
import com.tensua.enums.StateEnum;
import com.tensua.exception.BusinessException;
import com.tensua.operator.friends.entity.FriendRequest;
import com.tensua.operator.friends.entity.UserRelation;
import com.tensua.operator.friends.service.FriendRequestService;
import com.tensua.operator.friends.service.UserRelationService;
import com.tensua.operator.login.entity.UserInfo;
import com.tensua.operator.login.service.UserInfoService;
import com.tensua.system.NoParamsUserBean;
import com.tensua.system.PagingUserBaseRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author:70968 Date:2021-10-16 08:56
 */
@Service
public class FriendsFacade {
    @Autowired
    private UserRelationService userRelationService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private FriendRequestService friendRequestService;

    /**
     * 返回好友列表，无分页
     *
     * @param request
     * @return
     */
    public List<FriendsResponse> getFriends(PagingUserBaseRequest request) {
        Long userId = request.getUserId();
        List<UserRelation> friends = userRelationService.list(new LambdaQueryWrapper<UserRelation>().eq(UserRelation::getUserId, userId));
        if (Objects.isNull(friends)) {
            return new ArrayList<>();
        }
        List<FriendsResponse> responses = friends.stream().map(f -> {
            FriendsResponse friendsResponse = new FriendsResponse();
            BeanUtils.copyProperties(f, friendsResponse);
            return friendsResponse;
        }).collect(Collectors.toList());
        return responses;
    }

    /**
     * 发送好友请求
     *
     * @param request
     * @return
     */
    public Boolean addFriends(AddFriendsRequest request) {
        FriendRequest one = friendRequestService.getOne(new LambdaQueryWrapper<FriendRequest>()
                .eq(FriendRequest::getSendUserId, request.getUserId())
                .eq(FriendRequest::getReceiveUserId, request.getFriendId()));
        if (Objects.nonNull(one)) {
            one.setMessage(request.getMessage());
            return Boolean.TRUE;
        }
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setCreateTime(new Date());
        friendRequest.setMessage(request.getMessage());
        friendRequest.setReceiveUserId(request.getFriendId());
        friendRequest.setSendUserId(request.getUserId());
        friendRequest.setSendHeadImgUrl(request.getHeadImgUrl());
        friendRequest.setSendUserNickname(request.getNickname());
        friendRequest.setSendUserUsername(request.getUsername());
        friendRequest.setIsAgree(0);
        friendRequest.setInfoState(0);
        friendRequestService.save(friendRequest);
        return Boolean.TRUE;
    }

    /**
     * 处理好友请求
     *
     * @param request
     * @return
     */
    @Transactional
    public Boolean handleFriends(HandleFriendsRequest request) {
        FriendRequest friendRequest = friendRequestService.getById(request.getRequestId());
        if (Objects.isNull(friendRequest)) {
            throw new BusinessException("记录不存在");
        }
        Integer isAgree = request.getIsAgree();
        //如果同意，则保存好友关系
        if (isAgree == 1) {
            Date date = new Date();
            //保存自己与好友的关系
            UserRelation userRelation = new UserRelation();
            userRelation.setUserId(friendRequest.getSendUserId());
            userRelation.setFriendId(friendRequest.getReceiveUserId());
            userRelation.setNickname(request.getFriendName());
            userRelation.setHeadImgUrl(request.getFriendHeadUrl());
            userRelation.setCreateTime(date);
            userRelation.setIsRelation("0");
            userRelationService.save(userRelation);
            //保存好友与自己的关系
            UserRelation friendRelation = new UserRelation();
            friendRelation.setUserId(friendRequest.getReceiveUserId());
            friendRelation.setFriendId(friendRequest.getSendUserId());
            friendRelation.setNickname(request.getNickname());
            friendRelation.setHeadImgUrl(request.getHeadImgUrl());
            friendRelation.setCreateTime(date);
            friendRelation.setIsRelation("0");
            userRelationService.save(friendRelation);
        }
        friendRequest.setIsAgree(request.getIsAgree());
        friendRequest.setInfoState(StateEnum.ENABLED.getCode());
        friendRequest.setUpdateTime(new Date());
        friendRequestService.updateById(friendRequest);
        return Boolean.TRUE;
    }

    /**
     * 模糊查询
     *
     * @param paging
     * @param column
     * @return
     */
    public Page searchFriends(PagingUserBaseRequest paging, String column) {

        List<UserInfo> list = userInfoService.list(new LambdaQueryWrapper<UserInfo>()
                .like(UserInfo::getMobile, column)
                .like(UserInfo::getUsername, column));
        List<UserInfoResponse> collect = list.stream().map(userInfo -> {
            UserInfoResponse response = new UserInfoResponse();
            BeanUtils.copyProperties(userInfo, response);
            return response;
        }).collect(Collectors.toList());
        Page page = new Page<>(paging.getPageIndex(), paging.getPageSize(), collect.size());
        page.setRecords(collect);
        return page;
    }

    /**
     * 获取新的好友请求
     *
     * @param request
     * @return
     */
    public List<NewFriendResponse> getNewFriends(NoParamsUserBean request) {
        List<FriendRequest> list = friendRequestService.list(new LambdaQueryWrapper<FriendRequest>()
                .eq(FriendRequest::getReceiveUserId, request.getUserId())
                .orderByDesc(FriendRequest::getCreateTime));
        List<NewFriendResponse> result = list.stream().map(l -> {
            NewFriendResponse response = new NewFriendResponse();
            BeanUtils.copyProperties(l, response);
            return response;
        }).collect(Collectors.toList());

        return result;
    }
}
