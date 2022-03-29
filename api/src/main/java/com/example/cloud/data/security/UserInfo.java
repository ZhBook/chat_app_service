package com.example.cloud.data.security;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

/**
 * @TableName user_info
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class UserInfo implements UserDetails, CredentialsContainer {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private Long id;

    /**
     *
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     *
     */
//    @JsonIgnore
    private String password;

    /**
     *
     */
    private String headImgUrl;

    /**
     *
     */
    private String EMail;

    /**
     *
     */
    private String mobile;

    /**
     *
     */
    private int sex;

    /**
     *
     */
    private String address;

    /**
     *
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 删除标志：0没删除，1删除
     */
    private int isDelete;

    /**
     * 启用标志：0启用，1不启用
     */
    private int isEnabled;

    /**
     * 权限
     */
    private Set<? extends GrantedAuthority> authorities;


    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return Objects.equals(isEnabled, 0);
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
        this.password = null;
    }


}