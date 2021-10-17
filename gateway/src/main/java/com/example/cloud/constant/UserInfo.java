package com.example.cloud.constant;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @TableName user_info
 */
@Data
public class UserInfo{


    private Integer id;

    private String username;

    private String password;

    private String headImgUrl;

    private String EMail;

    private String phone;

    private String sex;

    private String address;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String isDelete;

    private String isEnabled;

}