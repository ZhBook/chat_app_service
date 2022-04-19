package com.example.cloud.operator.moments.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

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
    @TableId(value = "id", type = IdType.AUTO)
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 
     */
    private String isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}