package com.example.cloud.operator.system.facade;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.cloud.data.response.system.DictionariesValueResponse;
import com.example.cloud.enums.IsDeleteEnum;
import com.example.cloud.operator.blog.entity.BlogConfig;
import com.example.cloud.operator.blog.service.BlogConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: zhooke
 * @create: 2022-04-29 11:41
 * @description:
 **/
@Service
public class SystemFacade {

    @Autowired
    private BlogConfigService blogConfigService;

    /**
     * 查询字典表
     *
     * @param typeName
     * @return
     */
    public List<DictionariesValueResponse> getDictionaryValue(String typeName) {
        List<BlogConfig> configList = blogConfigService.list(new LambdaQueryWrapper<BlogConfig>()
                .eq(BlogConfig::getTypeName, typeName)
                .eq(BlogConfig::getIsDelete, IsDeleteEnum.NO.getCode()));
        List<DictionariesValueResponse> responses = new ArrayList<>();
        if (!configList.isEmpty()) {
            responses = configList.stream().map(config -> {
                DictionariesValueResponse dictionariesValueResponse = new DictionariesValueResponse();
                BeanUtils.copyProperties(config, dictionariesValueResponse);
                return dictionariesValueResponse;
            }).collect(Collectors.toList());
        }
        return responses;
    }
}
