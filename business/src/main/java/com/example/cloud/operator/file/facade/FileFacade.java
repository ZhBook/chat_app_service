package com.example.cloud.operator.file.facade;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.example.cloud.exception.BusinessException;
import com.example.cloud.operator.file.entity.FileInfo;
import com.example.cloud.operator.file.mapper.FileInfoMapper;
import com.example.cloud.operator.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhouhd
 * @since 2021/11/22 14:35
 **/
@Service
@Slf4j
public class FileFacade {

    @Resource
    private FileInfoMapper fileInfoMapper;

    private static String endpoint = "oss-cn-hangzhou.aliyuncs.com";
    private static String accessKeyId = "LTAI5tNxU5MV2bCV91eT4qnc";
    private static String accessKeySecret = "2IcjuSiFPT0RIey2zsRc3jz2ulyd7N";
    private static String bucketName = "hub-tensua-com";
    private static String key = "LTAI5tNxU5MV2bCV91eT4qnc";

    @Value("${upload.file.path}")
    private String path;

    private static final String FILE_SPLIT = ".";

    public FileInfo upload(MultipartFile file) {
        FileInfo fileInfo = FileUtil.getFileInfo(file);
        //校验文件类型
        if (!fileInfo.getName().contains(FILE_SPLIT)) {
            throw new BusinessException("缺少后缀名");
        }
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            InputStream inputStream = file.getInputStream();
            // new对象元信息
            ObjectMetadata meta = new ObjectMetadata();
            // 设置contentType
            meta.setContentType(file.getContentType());
            ossClient.createBucket(bucketName);
            ossClient.putObject(bucketName, key, inputStream, meta);
            //返回路径
            fileInfoMapper.insert(fileInfo);
        } catch (IOException e) {
            log.error("上传文件失败，原因：{}",e.getMessage());
        } finally {
            ossClient.shutdown();
        }


        return fileInfo;
    }
}
