package com.example.cloud.operator.file.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName file_info
 */
@TableName(value ="file_info")
@Data
public class FileInfo implements Serializable {
    /**
     * 文件md5
     */
    @TableId
    private String id;

    /**
     * 文件名
     */
    private String name;

    /**
     * 是否图片 0：否，1：是
     */
    private Boolean isImg;

    /**
     * 文件类型
     */
    private String contentType;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 物理路径
     */
    private String path;

    /**
     * 
     */
    private String url;

    /**
     * 
     */
    private String source;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}