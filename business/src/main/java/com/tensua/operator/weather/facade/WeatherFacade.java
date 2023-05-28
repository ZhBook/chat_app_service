package com.tensua.operator.weather.facade;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tensua.constant.RedisConstants;
import com.tensua.data.response.weather.D7weatherResponse;
import com.tensua.data.response.weather.H24weatherResponse;
import com.tensua.data.response.weather.Indices1DWeatherResponse;
import com.tensua.data.response.weather.NowWeatherResponse;
import com.tensua.exception.BusinessException;
import com.tensua.operator.blog.entity.BlogConfig;
import com.tensua.operator.blog.service.BlogConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: zhooke
 * @create: 2023-05-27 16:17
 * @description:
 **/
@Service
@Slf4j
public class WeatherFacade {

    final String NOW_WEATHER_URL = "/v7/weather/now?";

    final String D7_WEATHER_URL = "/v7/weather/7d?";

    final String H24_WEATHER_URL = "/v7/weather/24h?";

    final String INDICES_1d_WEATHER_URL = "/v7/indices/1d?";


    final String WEATHER_KEY = "weather_key";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private BlogConfigService blogConfigService;

    /**
     * 获取当前天气
     *
     * @param location
     * @param lang
     * @return
     */
    public NowWeatherResponse nowWeather(String location, String lang) {
        String weatherData = redisTemplate.opsForValue().get(RedisConstants.WEATHER_DATA_NOW + location);
        if (StringUtils.isNotBlank(weatherData)) {
            return JSON.parseObject(weatherData, NowWeatherResponse.class);
        }
        JSONObject result = this.getResult(location, lang, NOW_WEATHER_URL);
        NowWeatherResponse response = result.getObject("now", NowWeatherResponse.class);
        redisTemplate.opsForValue().set(RedisConstants.WEATHER_DATA_NOW + location, result.getJSONObject("now").toJSONString(), 1, TimeUnit.HOURS);
        return response;
    }

    /**
     * 获取7日天气
     *
     * @param location
     * @param lang
     * @return
     */
    public List<D7weatherResponse> d7Weather(String location, String lang) {
        String weatherData = redisTemplate.opsForValue().get(RedisConstants.WEATHER_DATA_D7 + location);
        if (StringUtils.isNotBlank(weatherData)) {
            return JSON.parseArray(weatherData, D7weatherResponse.class);
        }
        JSONObject result = this.getResult(location, lang, D7_WEATHER_URL);
        List<D7weatherResponse> responseList = result.getList("daily", D7weatherResponse.class);
        redisTemplate.opsForValue().set(RedisConstants.WEATHER_DATA_D7 + location, result.getJSONArray("daily").toJSONString(), 6, TimeUnit.HOURS);
        return responseList;
    }

    /**
     * 获取最近24小时天气
     *
     * @param location
     * @param lang
     * @return
     */
    public List<H24weatherResponse> h24Weather(String location, String lang) {
        String weatherData = redisTemplate.opsForValue().get(RedisConstants.WEATHER_DATA_H24 + location);
        if (StringUtils.isNotBlank(weatherData)) {
            return JSON.parseArray(weatherData, H24weatherResponse.class);
        }
        JSONObject result = this.getResult(location, lang, H24_WEATHER_URL);
        List<H24weatherResponse> responseList = result.getList("hourly", H24weatherResponse.class);
        redisTemplate.opsForValue().set(RedisConstants.WEATHER_DATA_H24 + location, result.getJSONArray("hourly").toJSONString(), 1, TimeUnit.HOURS);
        return responseList;
    }


    /**
     * 获取1天的生活指数
     *
     * @param location
     * @param lang
     * @return
     */
    public List<Indices1DWeatherResponse> indices1DWeather(String location, String lang) {
        String weatherData = redisTemplate.opsForValue().get(RedisConstants.WEATHER_DATA_INDICES_1D + location);
        if (StringUtils.isNotBlank(weatherData)) {
            return JSON.parseArray(weatherData, Indices1DWeatherResponse.class);
        }
        JSONObject result = this.getResult(location, lang, INDICES_1d_WEATHER_URL);
        List<Indices1DWeatherResponse> responseList = result.getList("daily", Indices1DWeatherResponse.class);
        redisTemplate.opsForValue().set(RedisConstants.WEATHER_DATA_INDICES_1D + location, result.getJSONArray("daily").toJSONString(), 6, TimeUnit.HOURS);
        return responseList;
    }

    /**
     * 通用调用接口
     *
     * @param location
     * @param lang
     * @param url
     * @return
     */
    private JSONObject getResult(String location, String lang, String url) {
        String weatherHost = redisTemplate.opsForValue().get(RedisConstants.WEATHER_HOST);
        BlogConfig blogConfig = blogConfigService.getOne(new LambdaQueryWrapper<BlogConfig>()
                .eq(BlogConfig::getTypeName, WEATHER_KEY));
        String key = blogConfig.getTypeValue();
        String urlString = String.format(weatherHost + url + "location=%s&key=%s&lang=%s", location, key, lang);
        String result = HttpUtil.get(urlString);
        JSONObject jsonResult = JSON.parseObject(result);
        if (!jsonResult.getString("code").equals("200")) {
            log.error("请求参数{},结果：{}", urlString, result);
            throw new BusinessException("获取天气信息失败，请稍微再试！");
        }
        return jsonResult;
    }
}
