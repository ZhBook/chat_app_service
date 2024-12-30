package com.tensua.blogservice.operator.system.controller;

import com.tensua.blogservice.data.PageResult;
import com.tensua.blogservice.data.response.system.DictionariesValueResponse;
import com.tensua.blogservice.operator.system.facade.SystemFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: zhooke
 * @create: 2022-04-29 11:41
 * @description:
 **/
@RestController
@RequestMapping("/system")
public class SystemController {

    @Resource
    private SystemFacade systemFacade;

    /**
     * 查询字典表
     *
     * @param typeName
     * @return
     */
    @GetMapping("/dictionary/value")
    public PageResult<DictionariesValueResponse> getDictionaryValue(@RequestParam("type_name") String typeName) {
        return PageResult.pageSuccess(systemFacade.getDictionaryValue(typeName));
    }
}
