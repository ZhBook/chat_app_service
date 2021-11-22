package com.example.cloud.operator.file.controller;

import com.example.cloud.data.BaseResult;
import com.example.cloud.operator.file.facade.FileFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zhouhd
 * @since 2021/11/22 14:34
 **/
@RestController
public class FileController {

    @Autowired
    private FileFacade fileFacade;

    @PostMapping("/upload")
    public BaseResult upload(@RequestParam("file") MultipartFile file){
        return BaseResult.succeed(fileFacade.upload(file));
    }
}
