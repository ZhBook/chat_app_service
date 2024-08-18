package com.tensua.blogservice.operator.login.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @TableName user_info
 */
@Data
@TableName("user_info")
public class UserInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 3198003632271441027L;
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
    private String userType;

    /**
     *
     */
//    @JsonIgnore
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
    @Pattern(regexp = "^1(3|4|5|7|8)\\d{9}$",message = "手机号码格式错误")
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

    /**
     *
     */
    private Date createTime;

    /**
     * 删除标志：0没删除，1删除
     */
    private int isDelete;

    /**
     * 启用标志：0启用，1不启用
     */
    private int isEnabled;

}