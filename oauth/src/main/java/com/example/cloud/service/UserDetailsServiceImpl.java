package com.example.cloud.service;

import com.example.cloud.api.UserFeignClient;
import com.example.cloud.entity.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserFeignClient userFeignClient;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("username:" + username);
        // 查询数据库操作
        UserInfo userInfo = userFeignClient.getUserByUsername(username).getData();
        if (Objects.nonNull(userInfo)){
            return userInfo;
        }

        else if (!userInfo.isEnabled()) {
            throw new DisabledException("该账户已被禁用!");
        } else if (!userInfo.isAccountNonLocked()) {
            throw new LockedException("该账号已被锁定!");
        } else if (!userInfo.isAccountNonExpired()) {
            throw new AccountExpiredException("该账号已过期!");
        }
        throw new UsernameNotFoundException("该账号已过期!");
    }


}


