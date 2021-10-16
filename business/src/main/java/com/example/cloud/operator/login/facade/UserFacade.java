package com.example.cloud.operator.login.facade;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.cloud.exception.BusinessException;
import com.example.cloud.operator.login.entity.UserInfo;
import com.example.cloud.operator.login.mapper.UserInfoMapper;
import com.example.cloud.operator.login.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author:70968 Date:2021-10-06 18:27
 */
@Service
public class UserFacade {
    @Autowired
    private UserInfoService userInfoService;

    @Resource
    private UserInfoMapper userInfoMapper;

    /**
     * 用户登录验证接口
     * @param username
     * @param password
     * @return
     */
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

    /**
     * 通过用户名获取用户信息
     * @param username
     * @return
     */
    public UserInfo getUserByUsername(String username) {
        UserInfo one = userInfoService.getOne(new LambdaQueryWrapper<UserInfo>().eq(UserInfo::getUsername, username));
        return one;
    }

    /**
     * 用户注册接口
     * @param userInfo
     * @return
     */
    public UserInfo registerUser(UserInfo userInfo) {
        Integer integer = userInfoMapper.selectCount(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getPhone, userInfo.getPhone())
        );
        if (integer == 0) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            userInfo.setPassword(bCryptPasswordEncoder.encode(userInfo.getPassword()));
            userInfoService.save(userInfo);
            return userInfo;
        }
        throw new BusinessException("该账户已存在");
    }
}
