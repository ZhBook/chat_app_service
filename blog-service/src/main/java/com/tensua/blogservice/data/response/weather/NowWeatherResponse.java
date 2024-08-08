package com.tensua.blogservice.data.response.weather;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author: zhooke
 * @create: 2023-05-27 16:20
 * @description:
 **/
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NowWeatherResponse {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String obsTime;
    private String temp;
    private String feelsLike;
    private String icon;
    private String text;
    private String wind360;
    private String windDir;
    private String windScale;
    private String windSpeed;
    private String humidity;
    private String precip;
    private String pressure;
    private String vis;
    private String cloud;
    private String dew;
}
