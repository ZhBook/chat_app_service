package com.example.cloud.operator.friends.facade;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.cloud.data.request.friends.AddFriendsRequest;
import com.example.cloud.data.request.friends.HandleFriendsRequest;
import com.example.cloud.data.response.friends.FriendsResponse;
import com.example.cloud.enums.StateEnum;
import com.example.cloud.exception.BusinessException;
import com.example.cloud.operator.friends.entity.FriendRequest;
import com.example.cloud.operator.friends.entity.UserRelation;
import com.example.cloud.operator.friends.service.FriendRequestService;
import com.example.cloud.operator.friends.service.UserRelationService;
import com.example.cloud.operator.login.service.UserInfoService;
import com.example.cloud.system.PagingUserBaseRequest;
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
        Long userId = request.getId();
        List<UserRelation> friends = userRelationService.list(new LambdaQueryWrapper<UserRelation>().eq(UserRelation::getUserId, userId));
        if (Objects.isNull(friends)) {
            return new ArrayList<>();
        }
        List<FriendsResponse> responses = new ArrayList<>();
        responses = friends.stream().map(f -> {
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
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setCreateTime(new Date());
        friendRequest.setMessage(request.getMessage());
        friendRequest.setReceiveUserId(request.getFriendId());
        friendRequest.setSendUserId(request.getId());
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
        FriendRequest friendRequest = friendRequestService.getById(request.getId());
        if (Objects.isNull(friendRequest)) {
            throw new BusinessException("记录不存在");
        }
        String isAgree = request.getIsAgree();
        //如果同意，则保存好友关系
        if (Objects.equals(isAgree, "1")) {
            UserRelation userRelation = new UserRelation();
            userRelation.setUserId(friendRequest.getSendUserId());
            userRelation.setFriendId(friendRequest.getReceiveUserId());
            userRelation.setFriendName(request.getFriendName());
            userRelation.setFriendHeadUrl(request.getFriendHeadUrl());
            userRelation.setCreateTime(new Date());
            userRelation.setIsRelation("0");
            userRelationService.save(userRelation);
        }
        friendRequest.setIsAgree(request.getIsAgree());
        friendRequest.setInfoState(StateEnum.ENABLED.getCode());
        friendRequest.setUpdateTime(new Date());
        friendRequestService.updateById(friendRequest);
        return Boolean.TRUE;
    }
}
