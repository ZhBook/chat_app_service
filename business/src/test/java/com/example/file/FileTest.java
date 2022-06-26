package com.example.file;

import com.tensua.BusinessApplication;
import com.tensua.operator.file.service.FileInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: zhooke
 * @create: 2022-04-10 09:36
 * @description:
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BusinessApplication.class)
public class FileTest {

    @Autowired
    private FileInfoService fileInfoService;

    @Test
    public void getFileList(){
        fileInfoService.getBlogCoverPicture();
    }
}
