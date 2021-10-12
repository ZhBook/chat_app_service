package com.example.cloud.operator.login.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName user_info
 */
@Data
@TableName("user_info")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String username;

    /**
     * 
     */
    private String password;

    /**
     * 
     */
    private String headImgUrl;

    /**
     * 
     */
    private String eMail;

    /**
     * 
     */
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
    private Date createTime;

    /**
     * 
     */
    private String isDelete;


}