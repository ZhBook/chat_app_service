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
    /**
     * 博主名称
     */
    String nickname;

    /**
     * 运行天数
     */
    String runningDay;

    /**
     * blog统计
     */
    Integer blogCount;

    /**
     * 评论统计
     */
    Integer commentCount;

    /**
     * 粉丝数
     */
    Integer followCount;
}
