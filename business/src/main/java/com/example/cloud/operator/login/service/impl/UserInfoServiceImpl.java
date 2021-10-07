package com.example.cloud.operator.login.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cloud.operator.login.entity.UserInfo;
import com.example.cloud.operator.login.service.UserInfoService;
import com.example.cloud.operator.login.mapper.UserInfoMapper;
import org.springframework.stereotype.Service;

/**
*
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
implements UserInfoService{

}
