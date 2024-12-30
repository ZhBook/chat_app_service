package com.tensua.blogservice.data.response.moments;

import lombok.Data;

import java.util.Date;

/**
 * @author zhooke
 * @since 2022/1/10 16:58
 **/
@Data
public class MomentsLikesResponse {
    /**
     *
     */
    private Long id;

    /**
     *
     */
    private Long momentsId;

    /**
     *
     */
    private Long userId;

    /**
     *
     */
    private Date createTime;
}
