package com.example.cloud.data.response.blog;

import com.fasterxml.jackson.annotation.JsonFormat;
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
     * blog所属作者id
     */
    private Long blogAuthorId;
    /**
     *
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    /**
     *
     */
    private String headImgUrl;
}
