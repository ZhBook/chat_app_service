package com.example.cloud.data.request.moments;

import com.example.cloud.system.UserBeanRequest;
import lombok.Data;

/**
 * @author zhooke
 * @since 2022/1/11 09:48
 **/
@Data
public class MomentsRequest extends UserBeanRequest {

    /**
     * 朋友圈所属用户id
     */
    private Long userId;

    /**
     * 发布的内容
     */
    private String context;

    /**
     * 点赞数
     */
    private Long likes;

    /**
     * 图片地址
     */
    private String images;

    /**
     * 视频地址
     */
    private String video;

}
