package com.tensua.data.response.moments;

import lombok.Data;

import java.util.Date;

/**
 * @author zhooke
 * @since 2022/1/10 11:51
 **/
@Data
public class MomentsCommentResponse {
    /**
     *
     */
    private Long id;

    /**
     * 朋友圈信息id
     */
    private Long momentsId;

    /**
     * 评论用户id
     */
    private Long userId;

    /**
     * 评论内容
     */
    private String context;

    /**
     *
     */
    private Date createTime;
}
