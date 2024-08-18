package com.tensua.blogservice.operator.login.facade;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tensua.blogservice.config.oauth.JwtUtil;
import com.tensua.blogservice.data.exception.BusinessException;
import com.tensua.blogservice.data.request.user.RegisterUserRequest;
import com.tensua.blogservice.data.request.user.UserInfoRequest;
import com.tensua.blogservice.data.response.login.LoginUserInfoResponse;
import com.tensua.blogservice.data.response.login.UserInfoResponse;
import com.tensua.blogservice.operator.blog.service.InviteCodeService;
import com.tensua.blogservice.operator.login.entity.UserInfo;
import com.tensua.blogservice.operator.login.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author:70968 Date:2021-10-06 18:27
 */
@Service
@Slf4j
public class UserFacade {
    @Resource
    private UserInfoService userInfoService;

    @Resource
    private InviteCodeService inviteCodeService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtUtil jwtUtil;

    /**
     * 用户登录验证接口
     *
     * @param username
     * @param password
     * @return
     */
    public LoginUserInfoResponse login(String username, String password) {
        UserInfo one = userInfoService.getOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getMobile, username)
        );
        if (Objects.isNull(one)) {
            throw new BusinessException("用户不存在");
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        if (!bCryptPasswordEncoder.matches(password, one.getPassword())) {
            throw new BusinessException("用户名或密码不正确！");
        }

        LoginUserInfoResponse loginUserInfoResponse = null;
        try {
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, password);

            Authentication authentication = authenticationManager.authenticate(auth);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String jwtToken = jwtUtil.getToken(userDetails.getUsername());
            loginUserInfoResponse = new LoginUserInfoResponse();
            BeanUtils.copyProperties(one, loginUserInfoResponse);
            loginUserInfoResponse.setToken(jwtToken);
            jwtUtil.insertToken(jwtToken, one);
        } catch (AuthenticationException | BeansException e) {
            log.error("登录失败", e);
            throw new BusinessException("登录失败，请稍后再试");
        }
        return loginUserInfoResponse;
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
    public LoginUserInfoResponse registerUser(RegisterUserRequest request) {
//        String inviteCode = request.getInviteCode();
//        InviteCode invite = inviteCodeService.getOne(new LambdaQueryWrapper<InviteCode>()
//                .eq(InviteCode::getCode, inviteCode)
//                .eq(InviteCode::getIsDelete, IsDeleteEnum.NO.getCode()));
//        if (Objects.isNull(invite) || Objects.nonNull(invite.getUserId())) {
//            throw new BusinessException("邀请码不正确");
//        }
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

//            invite.setUserId(userInfo.getId());
//            invite.setUpdateDatetime(new Date());
//            inviteCodeService.updateById(invite);
            LoginUserInfoResponse loginUserInfoResponse = new LoginUserInfoResponse();
            BeanUtils.copyProperties(userInfo, loginUserInfoResponse);
            String jwtToken = jwtUtil.getToken(userInfo.getUsername());
            loginUserInfoResponse.setToken(jwtToken);

            jwtUtil.insertToken(jwtToken, userInfo);
            return loginUserInfoResponse;
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
