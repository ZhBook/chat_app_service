package com.example.cloud.operator.login.facade;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.cloud.data.response.login.UserInfoResponse;
import com.example.cloud.exception.BusinessException;
import com.example.cloud.operator.login.entity.UserInfo;
import com.example.cloud.operator.login.mapper.UserInfoMapper;
import com.example.cloud.operator.login.service.UserInfoService;
import org.springframework.beans.BeanUtils;
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
     *
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
     *
     * @param username
     * @return
     */
    public UserInfo getUserByUsername(String username) {
        return userInfoService.getUserInfoByUsername(username);
    }

    /**
     * 用户注册接口
     *
     * @param userInfo
     * @return
     */
    public UserInfoResponse registerUser(UserInfo userInfo) {
        Integer num = userInfoMapper.selectCount(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getMobile, userInfo.getMobile())
        );
        if (num == 0) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            userInfo.setPassword(bCryptPasswordEncoder.encode(userInfo.getPassword()));
            String randomName;
            for (; ; ) {
                randomName = RandomUtil.randomString(18);
                UserInfo infoByUsername = userInfoService.getUserInfoByUsername(randomName);
                if (Objects.isNull(infoByUsername)) {
                    break;
                }
            }
            userInfo.setUsername(randomName);
            userInfoService.save(userInfo);

            UserInfoResponse userInfoResponse = new UserInfoResponse();
            BeanUtils.copyProperties(userInfo, userInfoResponse);
            return userInfoResponse;
        }
        throw new BusinessException("该手机号已注册");
    }

    /**
     * 通过手机号码查询用户信息
     * @param mobile
     * @return
     */
    public UserInfo getUserByMobile(String mobile) {
        return userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getMobile,mobile));
    }
}
