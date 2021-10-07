package com.example.cloud.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.cloud.operator.login.entity.UserInfo;
import com.example.cloud.operator.login.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Slf4j
@Component
public class AuthUserService implements UserDetailsService {

    @Autowired
    private UserInfoService userInfoService;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("username:" + username);
        // 查询数据库操作
        UserInfo users = userInfoService.getOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getUsername, username));
        if (StringUtils.isBlank(users.getPassword())) {
            throw new UsernameNotFoundException("用户不存在");
        }
//        String password = passwordEncoder.encode(users.getPassword());
        return new User(username, users.getPassword(), new ArrayList<>());
    }


}


