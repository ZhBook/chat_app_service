package com.example.cloud.entity;

import com.example.cloud.data.UserInfoResponse;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author zhouhd
 * @since 2021/10/21 15:21
 **/
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccessToken {
    String access_token;
    UserInfoResponse userInfoResponse;
}
