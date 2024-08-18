package com.tensua.blogservice.operator.login.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tensua.blogservice.operator.login.entity.UserInfo;

/**
 *
 */
public interface UserInfoService extends IService<UserInfo> {

    UserInfo getLoginUser();

    UserInfo getUserInfoByUsername(String username);

    UserInfo getUserInfoByMobile(String mobile);
}
