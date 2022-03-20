package com.example.cloud.data.response.blog;

import lombok.Data;

import java.util.Date;

/**
 * @author: zhooke
 * @create: 2022-03-20 17:14
 * @description:
 **/
@Data
public class BlogCommentListResponse {
    /**
     *
     */
    private Long id;

    /**
     *
     */
    private Long blogId;

    /**
     *
     */
    private String comment;

    /**
     *
     */
    private String email;

    /**
     *
     */
    private Long createUserId;

    /**
     *
     */
    private String createUserName;

    /**
     *
     */
    private Date createDate;
}
