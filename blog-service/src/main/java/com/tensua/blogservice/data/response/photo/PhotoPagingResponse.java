package com.tensua.blogservice.data.response.photo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zhooke
 * @since 2022/12/29 15:14
 **/
@Data
public class PhotoPagingResponse {

    private Long id;

    /**
     * 焦距
     */
    private String focalLength;

    /**
     * 光圈
     */
    private String fNumber;

    /**
     * 曝光时间
     */
    private String exposureTime;

    /**
     * ISO
     */
    private String iso;

    /**
     * 设备型号
     */
    private String model;

    /**
     * 制造商
     */
    private String manufacturer;

    /**
     * 作者
     */
    private String artist;

    /**
     * 方向
     */
    private String orientation;

    /**
     * 分辨率
     */
    private String resolutionUnit;

    /**
     * 软件
     */
    private String software;

    /**
     * Shutter Priority（快门优先，Tv）、Aperture Priority（光圈优先，Av）
     */
    private String exposureProgram;

    /**
     * 图片创建时间
     */
    private LocalDateTime datetimeOriginal;

    /**
     * 曝光补偿
     */
    private String exposureBiasValue;

    /**
     * 最大光圈
     */
    private String maxApertureValue;

    /**
     * 测光方式 平均式测光、中央重点测光、点测光等
     */
    private String meteringMode;

    /**
     * 是否使用闪光灯
     */
    private String flash;

    /**
     * 作者说明
     */
    private String makerNote;

    /**
     * 图像宽度 指横向像素数
     */
    private String exifimageWidth;

    /**
     * 图像高度 指纵向像素数
     */
    private String exifimageLength;

    /**
     * 图片地址
     */
    private String photoUrl;

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
