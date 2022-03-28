package com.example.cloud.secruity.service;

import com.example.cloud.api.UserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Security 用户服务实现
 */
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
        UserInfo userInfo = userFeignClient.getUserByMobile(username).getData();
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
