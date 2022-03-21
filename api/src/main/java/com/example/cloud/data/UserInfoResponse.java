package com.example.cloud.data;

import lombok.Data;

import java.util.Date;

/**
 * @author zhouhd
 * @since 2021/10/19 10:01
 **/
@Data
public class UserInfoResponse {
    private Long id;

    private String username;

    private String nickname;

    private String headImgUrl;

    private String EMail;

    private String mobile;

    private int sex;

    private String address;

    private Date createTime;
}
