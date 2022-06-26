package com.tensua.operator.login.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tensua.operator.login.entity.UserInfo;

/**
*
*/
public interface UserInfoService extends IService<UserInfo> {

    UserInfo getLoginUser();

    UserInfo getUserInfoByUsername(String username);
}
