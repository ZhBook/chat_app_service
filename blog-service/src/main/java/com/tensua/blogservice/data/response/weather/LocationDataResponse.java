package com.tensua.blogservice.data.response.weather;

import lombok.Data;

import java.util.List;

/**
 * @author zhooke
 * @since 2023/6/19 11:15
 **/
@Data
public class LocationDataResponse {

    private String citycode;

    private String adcode;

    private String name;

    private String center;

    private String level;

    private List<String> districts;
}
