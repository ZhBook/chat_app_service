package com.tensua.blogservice.operator.weather.controller;

import com.alibaba.fastjson2.JSONArray;
import com.tensua.blogservice.data.BaseResult;
import com.tensua.blogservice.data.response.weather.D7weatherResponse;
import com.tensua.blogservice.data.response.weather.H24weatherResponse;
import com.tensua.blogservice.data.response.weather.Indices1DWeatherResponse;
import com.tensua.blogservice.data.response.weather.NowWeatherResponse;
import com.tensua.blogservice.operator.weather.facade.WeatherFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: zhooke
 * @create: 2023-05-27 16:16
 * @description:
 **/
@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Resource
    private WeatherFacade weatherFacade;

    /**
     * 获取当前天气
     *
     * @param location
     * @param lang
     * @return
     */
    @GetMapping("/now")
    public BaseResult<NowWeatherResponse> nowWeather(@RequestParam("location") String location,
                                                     @RequestParam(value = "lang", defaultValue = "zh") String lang) {
        return BaseResult.succeed(weatherFacade.nowWeather(location, lang));
    }

    /**
     * 获取7日天气
     *
     * @param location
     * @param lang
     * @return
     */
    @GetMapping("/d7")
    public BaseResult<List<D7weatherResponse>> d7Weather(@RequestParam("location") String location,
                                                         @RequestParam(value = "lang", defaultValue = "zh") String lang) {
        return BaseResult.succeed(weatherFacade.d7Weather(location, lang));
    }

    /**
     * 获取最近24小时天气
     *
     * @param location
     * @param lang
     * @return
     */
    @GetMapping("/h24")
    public BaseResult<List<H24weatherResponse>> h24Weather(@RequestParam("location") String location,
                                                           @RequestParam(value = "lang", defaultValue = "zh") String lang) {
        return BaseResult.succeed(weatherFacade.h24Weather(location, lang));
    }

    /**
     * 获取1天的生活指数
     *
     * @param location
     * @param lang
     * @return
     */
    @GetMapping("/indices/1d")
    public BaseResult<List<Indices1DWeatherResponse>> indices1DWeather(@RequestParam("location") String location,
                                                                       @RequestParam(value = "lang", defaultValue = "zh") String lang) {
        return BaseResult.succeed(weatherFacade.indices1DWeather(location, lang));
    }

    /**
     * 获取地区经纬度
     *
     * @param keywords
     * @return
     */
    @GetMapping("/location")
    public BaseResult<JSONArray> getLocationData(@RequestParam("keywords") String keywords) {
        return BaseResult.succeed(weatherFacade.getLocationData(keywords));
    }
}
