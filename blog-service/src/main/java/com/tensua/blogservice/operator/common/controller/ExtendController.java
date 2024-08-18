package com.tensua.blogservice.operator.common.controller;

import com.tensua.blogservice.data.BaseResult;
import com.tensua.blogservice.operator.common.facade.ExtendFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zhooke
 * @since 2021/12/31 13:52
 **/
@RestController
@RequestMapping("/file")
public class ExtendController {

    @Resource
    private ExtendFacade facade;

    @GetMapping("/qrcode")
    public BaseResult<byte[]> getQrCode(@RequestParam("content")String content){
        return BaseResult.succeed(facade.getQrCode(content));
    }

}
