package com.example.cloud.operator.login.facade;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.cloud.exception.BusinessException;
import com.example.cloud.operator.login.entity.UserInfo;
import com.example.cloud.operator.login.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author:70968 Date:2021-10-06 18:27
 */
@Service
public class UserFacade {
    @Autowired
    private UserInfoService userInfoService;

    public UserInfo login(String username, String password) {
        UserInfo one = userInfoService.getOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getUsername, username));
        if (Objects.isNull(one)) {
            throw new BusinessException("用户不存在");
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean matches = bCryptPasswordEncoder.matches(password, one.getPassword());
        if (matches) {
            return one;
        }
        throw new BusinessException("密码错误");
    }

    public UserInfo getUserByUsername(String username) {
        UserInfo one = userInfoService.getOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getUsername, username));
        return one;
    }

    public Boolean registerUser(UserInfo userInfo) {
        UserInfo user = userInfoService.getOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getPhone, userInfo.getPhone()));
        if (Objects.isNull(user)){
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            userInfo.setPassword(bCryptPasswordEncoder.encode(userInfo.getPassword()));
            userInfoService.save(userInfo);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
