package com.example.cloud.operator.common.controller;

import com.example.cloud.data.BaseResult;
import com.example.cloud.operator.common.facade.FileFacade;
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
public class FileController {

    @Autowired
    private FileFacade facade;

    @GetMapping("/qrcode")
    public BaseResult<byte[]> getQrCode(@RequestParam("content")String content){
        return BaseResult.succeed(facade.getQrCode(content));
    }

}
