package com.tensua.blogservice.operator.system.facade;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tensua.blogservice.data.response.system.DictionariesValueResponse;
import com.tensua.blogservice.enums.IsDeleteEnum;
import com.tensua.blogservice.operator.blog.entity.BlogConfig;
import com.tensua.blogservice.operator.blog.service.BlogConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Resource
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
