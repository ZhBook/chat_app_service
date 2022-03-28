package com.example.cloud.operator.login.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cloud.data.AuthConstants;
import com.example.cloud.data.RedisKeyGenerator;
import com.example.cloud.data.SecureUserToken;
import com.example.cloud.exception.BusinessException;
import com.example.cloud.operator.login.entity.UserInfo;
import com.example.cloud.operator.login.mapper.UserInfoMapper;
import com.example.cloud.operator.login.service.UserInfoService;
import com.example.cloud.operator.utils.WebUtil;
import com.example.cloud.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Objects;

/**
 *
 */
@Slf4j
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
        implements UserInfoService {

    @Resource
    private RedisTemplate<String, SecureUserToken> redisTemplate;

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

    /**
     * 从request中获取用户信息
     *
     * @param request
     * @return
     */
    private UserInfo getFromRequest(HttpServletRequest request) throws ParseException {
        String accessToken = request.getHeader(WebUtil.ACCESS_TOKEN_KEY);

        if (StringUtils.isEmpty(accessToken)) {
            log.info("获取token为空");
            return null;
        }
        String tokenKey = request.getHeader(AuthConstants.TOKEN_HEADER_KEY);
        SecureUserToken secureUserToken = verifyToken(tokenKey, accessToken.replace(AuthConstants.JWT_PREFIX, ""));

        com.example.cloud.data.UserInfo secureUser = secureUserToken.getSecureUser();
        if (Objects.isNull(secureUser)) {
            throw new BusinessException("用户信息不存在");
        }
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(secureUser, userInfo);
        return userInfo;
    }

    /**
     * 获取 Token
     */
    public SecureUserToken taskToken(String key) {
        return redisTemplate.opsForValue().get(RedisKeyGenerator.getLoginTokenKey(key));
    }

    /**
     * 验证 Token
     */
    public SecureUserToken verifyToken(String key, String token) {
        SecureUserToken secureUserToken = taskToken(key);
        if (null == secureUserToken) {
            throw new BusinessException("token expired");
        }
        if (!Objects.equals(secureUserToken.getToken(), token)) {
            throw new BusinessException("jwt token mismatching");
        }
        JwtUtil.parse(secureUserToken.getToken());
        return secureUserToken;
    }
}
