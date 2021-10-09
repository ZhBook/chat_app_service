package com.example.cloud.operator.login.facade;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.cloud.data.Result;
import com.example.cloud.operator.login.entity.UserInfo;
import com.example.cloud.operator.login.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author:70968 Date:2021-10-06 11:10
 */
@Service
public class RegisterFacade {
    @Autowired
    private UserInfoService userInfoService;

    public Result registerUser(UserInfo userInfo) {
        UserInfo user = userInfoService.getOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getPhone, userInfo.getPhone()));
        if (Objects.isNull(user)){
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            userInfo.setPassword(bCryptPasswordEncoder.encode(userInfo.getPassword()));
            userInfoService.save(userInfo);
            return Result.succeed("添加成功");
        }
        return Result.failed("该用户已存在");
    }
}
