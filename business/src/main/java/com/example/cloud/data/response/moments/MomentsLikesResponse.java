package com.example.cloud.data.response.moments;

import com.baomidou.mybatisplus.annotation.TableId;
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
    @TableId
    private Integer id;

    /**
     *
     */
    private Integer momentsId;

    /**
     *
     */
    private Integer userId;

    /**
     *
     */
    private Date createTime;
}
