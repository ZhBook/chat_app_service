package com.tensua.blogservice.data.system;

/**
 * @author: zhooke
 * @create: 2022-03-20 17:35
 * @description:
 **/

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public abstract class BlogUserRequest implements Serializable {

    private Long userId;

    private String username;

    private String nickname;

    private String password;

    private String headImgUrl;

    private String EMail;

    private String mobile;

    private int sex;

    private String address;

    private Date createTime;

    private int isEnabled;
}
