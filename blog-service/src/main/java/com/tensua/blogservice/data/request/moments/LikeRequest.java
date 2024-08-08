package com.tensua.blogservice.data.request.moments;

import com.tensua.blogservice.data.system.NoParamsUserBean;
import lombok.Data;

/**
 * @author zhooke
 * @since 2022/1/12 09:08
 **/
@Data
public class LikeRequest extends NoParamsUserBean {
    /**
     *
     */
    private Long momentsId;
}
