package com.tensua.blogservice.config.oauth;

import com.google.common.collect.Lists;
import com.tensua.blogservice.data.security.OauthUserInfo;
import com.tensua.blogservice.operator.login.entity.UserInfo;
import com.tensua.blogservice.operator.login.service.UserInfoService;
import com.tensua.blogservice.utils.PatternUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author zhooke
 * @since 2024/2/23 11:06
 **/
@Component
@Slf4j
public class DaoUserDetailsService implements UserDetailsService {

    @Resource
    private UserInfoService userInfoService;

    /**
     * 加载用户信息
     */
    @Override
    public OauthUserInfo loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询数据库操作
        UserInfo userInfo;
        log.info("当前用户名：{}", username);
        if (PatternUtil.checkMobile(username)) {
            userInfo = userInfoService.getUserInfoByMobile(username);
            if (Objects.isNull(userInfo)) {
                userInfo = userInfoService.getUserInfoByUsername(username);
            }
        } else {
            userInfo = userInfoService.getUserInfoByUsername(username);
        }
        if (Objects.isNull(userInfo)) {
            throw new UsernameNotFoundException("账户不存在");
        }
        return new OauthUserInfo(userInfo, Lists.newArrayList());
    }
}
