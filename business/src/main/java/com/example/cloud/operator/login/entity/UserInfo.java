package com.example.cloud.operator.login.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * @TableName user_info
 */
@Data
@TableName("user_info")
public class UserInfo implements Serializable {


    private static final long serialVersionUID = 3198003632271441027L;
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     *
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

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
    @Size(max = 11,min = 11,message = "手机号码长度错误")
    @Pattern(regexp = "1\\d{10}", message = "手机号格式错误")
    private String phone;

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

}