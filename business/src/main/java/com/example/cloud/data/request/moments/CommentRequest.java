package com.example.cloud.data.request.moments;

import com.example.cloud.system.NoParamsUserBean;
import lombok.Data;

/**
 * @author zhooke
 * @since 2022/1/12 09:16
 **/
@Data
public class CommentRequest extends NoParamsUserBean {
    /**
     * 朋友圈信息id
     */
    private Long momentsId;

    /**
     *
     */
    private String context;
}
