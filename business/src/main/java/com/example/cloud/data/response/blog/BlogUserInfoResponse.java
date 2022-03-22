package com.example.cloud.data.response.blog;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 *
 * @author zhooke
 * @since 2022/3/22 11:30
 **/
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogUserInfoResponse {
    String nickname;

    String runningDay;

    Integer blogCount;

    Integer commentCount;

    Integer followCount;
}
