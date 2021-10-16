package com.example.cloud.data.system;

import lombok.Data;

/**
 * @author:70968 Date:2021-10-16 10:28
 */
@Data
public class UserBeanRequest {

    private Long id;

    private String username;

    private String headImgUrl;

    private String EMail;

    private String phone;

    private String sex;

    private String address;

}
