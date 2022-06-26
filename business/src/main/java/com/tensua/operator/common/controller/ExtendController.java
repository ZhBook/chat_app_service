package com.tensua.operator.common.controller;

import com.tensua.data.BaseResult;
import com.tensua.operator.common.facade.ExtendFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhooke
 * @since 2021/12/31 13:52
 **/
@RestController
@RequestMapping("/file")
public class ExtendController {

    @Autowired
    private ExtendFacade facade;

    @GetMapping("/qrcode")
    public BaseResult<byte[]> getQrCode(@RequestParam("content")String content){
        return BaseResult.succeed(facade.getQrCode(content));
    }

}
