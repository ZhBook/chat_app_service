package com.example.cloud.data.request.moments;

import com.example.cloud.system.NoParamsUserBean;
import lombok.Data;

/**
 * @author zhooke
 * @since 2022/1/11 09:48
 **/
@Data
public class MomentsRequest extends NoParamsUserBean {

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

}
