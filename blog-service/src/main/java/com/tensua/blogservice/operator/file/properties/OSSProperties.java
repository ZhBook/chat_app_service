package com.tensua.blogservice.operator.file.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * OSS协议配置
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = OSSProperties.PREFIX)
public class OSSProperties {
    public static final String PREFIX = "aliyun.oss";

    /**
     * 用户名
     */
    private String accessKey;
    /**
     * 密码
     */
    private String accessKeySecret;
    /**
     * 访问端点
     */
    private String endpoint;
    /**
     * 访问端点
     */
    private String url;
    /**
     * bucket名称
     */
    private String bucketName;
    /**
     * 区域
     */
    private String region;
    /**
     * path-style
     */
    private Boolean pathStyleAccessEnabled = true;
}
