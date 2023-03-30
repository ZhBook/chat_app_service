package com.tensua;

import com.tensua.operator.file.service.FileInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author: zhooke
 * @create: 2022-04-10 09:36
 * @description:
 **/
@SpringBootTest(classes = BusinessApplication.class)
public class FileTest {

    @Autowired
    private FileInfoService fileInfoService;

    @Test
    public void getFileList(){
        fileInfoService.getBlogCoverPicture();
    }
}
