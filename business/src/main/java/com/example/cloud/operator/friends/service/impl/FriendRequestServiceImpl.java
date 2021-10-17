package com.example.cloud.operator.friends.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cloud.operator.friends.entity.FriendRequest;
import com.example.cloud.operator.friends.mapper.FriendRequestMapper;
import com.example.cloud.operator.friends.service.FriendRequestService;
import org.springframework.stereotype.Service;

/**
*
*/
@Service
public class FriendRequestServiceImpl extends ServiceImpl<FriendRequestMapper, FriendRequest>
implements FriendRequestService {

}
