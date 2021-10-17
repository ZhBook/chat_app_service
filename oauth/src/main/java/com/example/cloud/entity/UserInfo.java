package com.example.cloud.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;

/**
 * @TableName user_info
 */
@Data
public class UserInfo implements UserDetails {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     *
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     *
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     *
     */
    private String headImgUrl;

    /**
     *
     */
    @Email(message = "邮箱地址不正确")
    private String EMail;

    /**
     *
     */
    @Size(max = 11, min = 11, message = "手机号码长度错误")
    @Pattern(regexp = "1\\d{10}", message = "手机号格式错误")
    private String phone;

    /**
     *
     */
    private String sex;

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
    private String isDelete;

    /**
     * 启用标志：0启用，1不启用
     */
    private String isEnabled;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        if (StringUtils.equals(isEnabled,"0")) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }


}