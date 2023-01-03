package com.tensua.data.request.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author: zhooke
 * @create: 2022-03-27 09:44
 * @description:
 **/
@Data
public class RegisterUserRequest {

    /**
     *
     */
    private String username;

    /**
     * 昵称
     */
    @NotBlank(message = "用户名不能为空")
    private String nickname;

    /**
     * 用户类型 CHAT-聊天 BLOG-博客
     */
    @NotBlank(message = "用户类型不能为空")
    private String userType;

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
    @Pattern(regexp = "^1(3|4|5|7|8)\\d{9}$", message = "手机号码格式错误")
    @NotBlank(message = "手机号码不能为空")
    private String mobile;

    /**
     *
     */
    private int sex;

    /**
     *
     */
    private String address;

    private String inviteCode;
}
