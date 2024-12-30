package com.tensua.blogservice.operator.moments.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户朋友圈信息
 * @TableName moments
 */
@TableName(value ="moments")
@Data
public class Moments implements Serializable {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 
     */
    private String isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}