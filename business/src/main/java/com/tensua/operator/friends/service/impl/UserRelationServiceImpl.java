package com.tensua.operator.friends.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tensua.operator.friends.entity.UserRelation;
import com.tensua.operator.friends.mapper.UserRelationMapper;
import com.tensua.operator.friends.service.UserRelationService;
import org.springframework.stereotype.Service;

/**
*
*/
@Service
public class UserRelationServiceImpl extends ServiceImpl<UserRelationMapper, UserRelation>
implements UserRelationService{

}
