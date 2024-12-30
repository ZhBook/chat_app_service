package com.tensua.blogservice.data.response.weather;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author: zhooke
 * @create: 2023-05-27 19:35
 * @description:
 **/
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Indices1DWeatherResponse {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String date;
    private String type;
    private String name;
    private String level;
    private String category;
    private String text;
}
