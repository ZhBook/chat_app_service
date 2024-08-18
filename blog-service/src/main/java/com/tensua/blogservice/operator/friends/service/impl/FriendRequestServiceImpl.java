package com.tensua.blogservice.operator.friends.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tensua.blogservice.operator.friends.entity.FriendRequest;
import com.tensua.blogservice.operator.friends.mapper.FriendRequestMapper;
import com.tensua.blogservice.operator.friends.service.FriendRequestService;
import org.springframework.stereotype.Service;

/**
*
*/
@Service
public class FriendRequestServiceImpl extends ServiceImpl<FriendRequestMapper, FriendRequest>
implements FriendRequestService {

}
