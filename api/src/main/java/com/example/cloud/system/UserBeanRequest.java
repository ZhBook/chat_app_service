package com.example.cloud.system;

import lombok.Data;

import java.io.Serializable;

/**
 * @author:70968 Date:2021-10-16 10:28
 */
@Data
public abstract class UserBeanRequest implements Serializable {

    private Long id;

    private String username;

    private String headImgUrl;

    private String EMail;

    private String phone;

    private String sex;

    private String address;

}
