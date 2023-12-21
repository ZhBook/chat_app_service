package com.tensua.data.request.photo;

import com.tensua.system.BlogUserRequest;
import lombok.Data;

/**
 * @author zhooke
 * @since 2023/12/11 16:39
 **/
@Data
public class PhotoUpdateRequest extends BlogUserRequest {

    private Long id;

    /**
     * 作者说明
     */
    private String makerNote;

    /**
     * 图片描述
     */
    private String photoDescribe;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String county;

    /**
     * 镇/街道
     */
    private String town;

    /**
     * 村
     */
    private String village;

    /**
     * 地址
     */
    private String address;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

}
