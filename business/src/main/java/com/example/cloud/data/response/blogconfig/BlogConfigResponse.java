package com.example.cloud.data.response.blogconfig;

import lombok.Data;

import java.util.List;

/**
 * @author zhooke
 * @since 2022/3/9 13:56
 **/
@Data
public class BlogConfigResponse {
    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String menuName;

    /**
     * code
     */
    private String menuCode;

    /**
     * 父级id
     */
    private Integer fatherId;

    /**
     * 角标
     */
    private Integer index;

    /**
     * 下级
     */
    private List<BlogConfigResponse> children;

}
