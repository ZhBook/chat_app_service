package com.example.cloud.operator.login.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cloud.operator.login.entity.UserInfo;
import com.example.cloud.operator.login.mapper.UserInfoMapper;
import com.example.cloud.operator.login.service.UserInfoService;
import com.example.cloud.operator.utils.WebUtil;
import com.nimbusds.jose.JWSObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

/**
 *
 */
@Slf4j
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
        implements UserInfoService {

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
        String realToken = accessToken.replace("Bearer ", "");
        JWSObject jwsObject = JWSObject.parse(realToken);
        String userStr = jwsObject.getPayload().toString();

        JSONObject jsonObject = JSONUtil.parseObj(userStr);
        String username = jsonObject.getStr("user_name");
        UserInfo user = this.getUserInfoByUsername(username);
        request.setAttribute(WebUtil.REQ_ATTR_LOGIN_USER_KEY, user);
        return user;
    }
}
