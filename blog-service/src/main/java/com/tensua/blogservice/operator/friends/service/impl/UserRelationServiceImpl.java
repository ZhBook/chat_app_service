package com.tensua.blogservice.operator.friends.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tensua.blogservice.operator.friends.entity.UserRelation;
import com.tensua.blogservice.operator.friends.mapper.UserRelationMapper;
import com.tensua.blogservice.operator.friends.service.UserRelationService;
import org.springframework.stereotype.Service;

/**
*
*/
@Service
public class UserRelationServiceImpl extends ServiceImpl<UserRelationMapper, UserRelation>
implements UserRelationService {

}
