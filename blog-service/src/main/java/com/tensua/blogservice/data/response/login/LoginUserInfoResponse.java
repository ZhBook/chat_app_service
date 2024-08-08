package com.tensua.blogservice.data.response.login;

import com.tensua.blogservice.operator.login.entity.UserInfo;
import lombok.Data;

/**
 * @author zhooke
 * @since 2024/2/23 11:38
 **/
@Data
public class LoginUserInfoResponse extends UserInfo {

    private String token;
}
