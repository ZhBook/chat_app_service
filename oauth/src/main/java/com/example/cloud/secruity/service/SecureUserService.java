package com.example.cloud.secruity.service;

import com.example.cloud.api.UserFeignClient;
import com.example.cloud.data.security.UserInfo;
import com.example.cloud.utils.PatternUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Security 用户服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SecureUserService implements UserDetailsService {

    private final UserFeignClient userFeignClient;

    /**
     * 加载用户信息
     */
    @Override
    public UserInfo loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询数据库操作
        UserInfo userInfo;
        log.info("当前用户名：{}",username);
        if (PatternUtil.checkMobile(username)) {
            userInfo = userFeignClient.getUserByMobile(username).getData();
            if (Objects.isNull(userInfo)){
                userInfo = userFeignClient.getUserByUsername(username).getData();
            }
        } else {
            userInfo = userFeignClient.getUserByUsername(username).getData();
        }
        if (Objects.isNull(userInfo)){
            throw new UsernameNotFoundException("账户不存在");
        }
        return userInfo;
    }

//    /**
//     * 加载用户权限
//     */
//    public Set<? extends GrantedAuthority> loadAuthorities(Long userId) {
//        Set<SimpleGrantedAuthority> authoritySet = new HashSet<>();
//        List<BaseUserRole> userRoles = baseUserRoleService.lambdaQuery().eq(BaseUserRole::getUserId, userId).list();
//        for (BaseUserRole userRole : userRoles) {
//            BaseRole baseRole = baseRoleService.getById(userRole.getRoleId());
//            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(baseRole.getCode());
//            authoritySet.add(authority);
//        }
//        return authoritySet;
//    }

}
