package com.example.cloud.operator.friends.facade;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cloud.data.PageResult;
import com.example.cloud.data.request.friends.AddFriendsRequest;
import com.example.cloud.data.request.friends.HandleFriendsRequest;
import com.example.cloud.enums.StateEnum;
import com.example.cloud.enums.UserStateEnum;
import com.example.cloud.exception.BusinessException;
import com.example.cloud.operator.friends.entity.FriendRequest;
import com.example.cloud.operator.friends.entity.UserRelation;
import com.example.cloud.operator.friends.service.FriendRequestService;
import com.example.cloud.operator.friends.service.UserRelationService;
import com.example.cloud.operator.login.entity.UserInfo;
import com.example.cloud.operator.login.service.UserInfoService;
import com.example.cloud.system.PagingUserBaseRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public PageResult<UserInfo> getFriends(PagingUserBaseRequest request) {
        Long userId = request.getId();
        List<UserRelation> friends = userRelationService.list(new LambdaQueryWrapper<UserRelation>().eq(UserRelation::getUserId, userId));
        List<Long> friendIds = friends.stream().map(f -> f.getFriendId()).collect(Collectors.toList());
        if (friendIds.isEmpty()) {
            return PageResult.pageSuccess(new Page<>());
        }
        List<UserInfo> list = userInfoService.list(new LambdaQueryWrapper<UserInfo>()
                .in(UserInfo::getId, friendIds)
                .eq(UserInfo::getIsDelete, UserStateEnum.NORMAL)
                .eq(UserInfo::getIsEnabled, UserStateEnum.ENABLED));
        if (list.isEmpty()) {
            return PageResult.pageSuccess(new Page<>());
        }
        return PageResult.pageSuccess(list);
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
            userRelation.setCreateTime(new Date());
            userRelation.setIsRelation("0");
            userRelationService.save(userRelation);
        }
        friendRequest.setIsAgree(isAgree);
        friendRequest.setInfoState(StateEnum.ENABLED.getCode());
        friendRequest.setUpdateTime(new Date());
        friendRequestService.updateById(friendRequest);
        return Boolean.TRUE;
    }
}
