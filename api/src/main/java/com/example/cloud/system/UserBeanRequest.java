package com.example.cloud.system;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author:70968 Date:2021-10-16 10:28
 */
@Data
public abstract class UserBeanRequest implements Serializable {

    private static final long serialVersionUID = 5998226118926105515L;

    private Long id;

    private String username;

    private String nickname;

    private String password;

    private String headImgUrl;

    private String EMail;

    private String mobile;

    private int sex;

    private String address;

    private Date createTime;

    private int isDelete;

    private int isEnabled;
}
