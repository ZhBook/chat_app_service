package com.tensua.data.response.weather;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

/**
 * @author: zhooke
 * @create: 2023-05-27 19:35
 * @description:
 **/
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Indices1DWeatherResponse {
    private Date date;
    private String type;
    private String name;
    private String level;
    private String category;
    private String text;
}
