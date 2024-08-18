package com.tensua.blogservice.operator.file.controller;

import com.tensua.blogservice.data.BaseResult;
import com.tensua.blogservice.operator.file.facade.FileFacade;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author zhouhd
 * @since 2021/11/22 14:34
 **/
@RestController
@RequestMapping("/upload")
public class FileController {

    @Resource
    private FileFacade fileFacade;

    @PostMapping
    public BaseResult upload(@RequestParam("file") MultipartFile file){
        return BaseResult.succeed(fileFacade.upload(file));
    }
}
