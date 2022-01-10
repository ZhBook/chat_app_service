package com.example.cloud.operator.moments.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 朋友圈留言
 * @TableName moments_comment
 */
@TableName(value ="moments_comment")
@Data
public class MomentsComment implements Serializable {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 朋友圈信息id
     */
    private Long momentsId;

    /**
     * 留言用户id
     */
    private Long userId;

    /**
     * 
     */
    private String context;

    /**
     * 
     */
    private LocalDateTime createTime;

    /**
     * 
     */
    private String isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}