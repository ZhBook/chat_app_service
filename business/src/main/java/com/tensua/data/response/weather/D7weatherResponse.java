package com.tensua.data.response.weather;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author: zhooke
 * @create: 2023-05-27 17:37
 * @description:
 **/
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class D7weatherResponse {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String fxDate;
    private String sunrise;
    private String sunset;
    private String moonrise;
    private String moonset;
    private String moonPhase;
    private String moonPhaseIcon;
    private String tempMax;
    private String tempMin;
    private String iconDay;
    private String textDay;
    private String iconNight;
    private String textNight;
    private String wind360Day;
    private String windDirDay;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String windScaleDay;
    private String windSpeedDay;
    private String wind360Night;
    private String windDirNight;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String windScaleNight;
    private String windSpeedNight;
    private String humidity;
    private String precip;
    private String pressure;
    private String vis;
    private String cloud;
    private String uvIndex;
}
