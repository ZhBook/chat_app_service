package com.tensua.data.request.user;

import com.tensua.system.UserBeanRequest;
import lombok.Data;

/**
 * @author zhooke
 * @since 2022/3/24 15:52
 **/
@Data
public class UserInfoRequest extends UserBeanRequest {

    private String newNickname;

    private String newHeadImgUrl;

    private String newEMail;

    private String newMobile;

    private int newSex;

    private String newAddress;

}
