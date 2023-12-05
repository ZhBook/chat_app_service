package com.tensua.operator.login.facade;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tensua.data.request.user.RegisterUserRequest;
import com.tensua.data.request.user.UserInfoRequest;
import com.tensua.data.response.login.UserInfoResponse;
import com.tensua.enums.IsDeleteEnum;
import com.tensua.exception.BusinessException;
import com.tensua.operator.blog.entity.InviteCode;
import com.tensua.operator.blog.service.InviteCodeService;
import com.tensua.operator.login.entity.UserInfo;
import com.tensua.operator.login.service.UserInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

/**
 * @author:70968 Date:2021-10-06 18:27
 */
@Service
public class UserFacade {
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private InviteCodeService inviteCodeService;

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
     * @param request
     * @return
     */
    @Transactional
    public UserInfoResponse registerUser(RegisterUserRequest request) {
        String inviteCode = request.getInviteCode();
        InviteCode invite = inviteCodeService.getOne(new LambdaQueryWrapper<InviteCode>()
                .eq(InviteCode::getCode, inviteCode)
                .eq(InviteCode::getIsDelete, IsDeleteEnum.NO.getCode()));
        if (Objects.isNull(invite) || Objects.nonNull(invite.getUserId())) {
            throw new BusinessException("邀请码不正确");
        }
        Long num = userInfoService.count(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getMobile, request.getMobile())
        );
        if (num.equals(0L)) {
            UserInfo userInfo = new UserInfo();
            BeanUtils.copyProperties(request, userInfo);
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            userInfo.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
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

            invite.setUserId(userInfo.getId());
            invite.setUpdateDatetime(new Date());
            inviteCodeService.updateById(invite);
            UserInfoResponse userInfoResponse = new UserInfoResponse();
            BeanUtils.copyProperties(userInfo, userInfoResponse);
            return userInfoResponse;
        }
        throw new BusinessException("该手机号已注册");
    }

    /**
     * 通过手机号码查询用户信息
     *
     * @param mobile
     * @return
     */
    public UserInfo getUserByMobile(String mobile) {
        return userInfoService.getOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getMobile, mobile));
    }

    /**
     * 修改用户信息
     *
     * @param request
     * @return
     */
    public UserInfoResponse updateUserInfo(UserInfoRequest request) {
        Long id = request.getUserId();
        UserInfo userInfo = userInfoService.getById(id);
        if (Objects.isNull(userInfo)) {
            throw new BusinessException("用户不存在");
        }
        userInfo.setNickname(request.getNewNickname());
        userInfo.setMobile(request.getNewMobile());
        userInfo.setEMail(request.getNewEMail());
        userInfo.setHeadImgUrl(request.getNewHeadImgUrl());
        userInfo.setSex(request.getNewSex());
        userInfo.setAddress(request.getNewAddress());
        userInfoService.updateById(userInfo);
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        BeanUtils.copyProperties(userInfo, userInfoResponse);
        return userInfoResponse;
    }
}
