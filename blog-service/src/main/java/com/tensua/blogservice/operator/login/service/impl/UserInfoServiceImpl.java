package com.tensua.blogservice.operator.login.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tensua.blogservice.config.oauth.JwtUtil;
import com.tensua.blogservice.data.constant.SecurityConstant;
import com.tensua.blogservice.data.exception.BusinessException;
import com.tensua.blogservice.data.security.SecureUserToken;
import com.tensua.blogservice.operator.login.entity.UserInfo;
import com.tensua.blogservice.operator.login.mapper.UserInfoMapper;
import com.tensua.blogservice.operator.login.service.UserInfoService;
import com.tensua.blogservice.utils.WebUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 *
 */
@Slf4j
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
        implements UserInfoService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private JwtUtil jwtUtil;

    @Override
    public UserInfo getLoginUser() {
        try {
            HttpServletRequest request = WebUtil.getCurrentServletRequest();
            if (null == request) {
                log.info("LoginUserServiceImpl异常调用，获取HttpServletRequest为null");
                return null;
            }
            return getFromRequest(request);
        } catch (Exception e) {
            log.error("获取当前登录用户出错:", e);
        }
        return null;
    }

    /**
     * 通过用户名获取用户信息
     *
     * @param username
     * @return
     */
    @Override
    public UserInfo getUserInfoByUsername(String username) {
        return this.getOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getUsername, username));
    }

    @Override
    public UserInfo getUserInfoByMobile(String mobile) {
        return this.getOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getMobile, mobile));
    }

    /**
     * 从request中获取用户信息
     *
     * @param request
     * @return
     */
    private UserInfo getFromRequest(HttpServletRequest request) {
        String tokenKey = request.getHeader(SecurityConstant.TOKEN_HEADER);

        if (StringUtils.isEmpty(tokenKey)) {
            log.info("获取token为空");
            return null;
        }
        SecureUserToken secureUserToken = jwtUtil.taskToken(tokenKey);

        if (Objects.isNull(secureUserToken.getSecureUser())) {
            throw new BusinessException("用户信息不存在");
        }
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(secureUserToken.getSecureUser(), userInfo);
        return userInfo;
    }

}
