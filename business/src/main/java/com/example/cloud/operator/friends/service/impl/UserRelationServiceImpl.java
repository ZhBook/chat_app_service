package com.example.cloud.operator.friends.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cloud.operator.friends.entity.UserRelation;
import com.example.cloud.operator.friends.mapper.UserRelationMapper;
import com.example.cloud.operator.friends.service.UserRelationService;
import org.springframework.stereotype.Service;

/**
*
*/
@Service
public class UserRelationServiceImpl extends ServiceImpl<UserRelationMapper, UserRelation>
implements UserRelationService{

}
