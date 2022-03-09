package com.example.cloud.operator.blogconfig.controller;

import com.example.cloud.data.BaseResult;
import com.example.cloud.data.response.blogconfig.BlogConfigResponse;
import com.example.cloud.operator.blogconfig.facade.BlogConfigFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhooke
 * @since 2022/3/8 14:40
 **/
@RestController
@RequestMapping("/blog")
public class BlogConfigController {

    @Autowired
    private BlogConfigFacade blogConfigFacade;

    @GetMapping("menus")
    public BaseResult<List<BlogConfigResponse>> getMenusList(){
        return BaseResult.succeed(blogConfigFacade.getMenusList());
    }
}
