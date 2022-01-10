package com.example.cloud.data.response.moments;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author zhooke
 * @since 2022/1/10 10:42
 **/
@Data
public class MomentsResponse {
    /**
     *
     */
    private Long id;

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

    /**
     *
     */
    private Date createTime;

    /**
     * 评论
     */
    private List<MomentsCommentResponse> momentsCommentResponses;

    /**
     * 点赞
     */
    private List<MomentsLikesResponse> momentsLikesResponses;
}
