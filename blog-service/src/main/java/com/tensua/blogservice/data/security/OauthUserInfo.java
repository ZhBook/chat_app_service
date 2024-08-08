package com.tensua.blogservice.data.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tensua.blogservice.operator.login.entity.UserInfo;
import lombok.Data;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @TableName user_info
 */
@Data
public class OauthUserInfo extends UserInfo implements UserDetails, CredentialsContainer {

    private UserInfo userInfo;

    private List<String> permissions;

    /**
     * 权限
     */
    private Set<? extends GrantedAuthority> authorities;

    public OauthUserInfo(UserInfo userInfo, List<String> permissions) {
        this.userInfo = userInfo;
        this.permissions = permissions;
    }

    @Override
    public String getUsername() {
        return userInfo.getUsername();
    }

    @Override
    public String getPassword() {
        return userInfo.getPassword();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return Objects.equals(userInfo.getIsEnabled(), 0);
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return Objects.equals(userInfo.getIsEnabled(), 0);
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public void eraseCredentials() {
        this.setPassword(null);
    }

}