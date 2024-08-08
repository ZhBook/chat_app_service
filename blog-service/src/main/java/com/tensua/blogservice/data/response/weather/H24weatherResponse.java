package com.tensua.blogservice.data.response.weather;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author: zhooke
 * @create: 2023-05-27 19:31
 * @description:
 **/
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class H24weatherResponse {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String fxTime;
    private String temp;
    private String icon;
    private String text;
    private String wind360;
    private String windDir;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String windScale;
    private String windSpeed;
    private String humidity;
    private String pop;
    private String precip;
    private String pressure;
    private String cloud;
    private String dew;
}
