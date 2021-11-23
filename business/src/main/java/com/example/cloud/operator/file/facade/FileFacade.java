package com.example.cloud.operator.file.facade;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.example.cloud.exception.BusinessException;
import com.example.cloud.operator.file.entity.FileInfo;
import com.example.cloud.operator.file.mapper.FileInfoMapper;
import com.example.cloud.operator.file.properties.OSSProperties;
import com.example.cloud.operator.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author zhouhd
 * @since 2021/11/22 14:35
 **/
@Service
@Slf4j
public class FileFacade {

    @Resource
    private FileInfoMapper fileInfoMapper;
    @Autowired
    private OSSProperties ossProperties;

    private static final String PATH_SPLIT = "/";
    private static final String FILE_SPLIT = ".";
    private static final String IMG_PREFIX = "images";
    private static final String File_PREFIX = "files";


    public FileInfo upload(MultipartFile file) {
        FileInfo fileInfo = FileUtil.getFileInfo(file);
        //校验文件类型
        if (!fileInfo.getName().contains(FILE_SPLIT)) {
            throw new BusinessException("缺少后缀名");
        }
        OSS ossClient = new OSSClientBuilder().build(ossProperties.getEndpoint(), ossProperties.getAccessKey(), ossProperties.getAccessKeySecret());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        String fileSuffix = FileUtil.getFileSuffix(fileInfo.getName());
        String key = File_PREFIX + PATH_SPLIT + fileInfo.getId() + fileSuffix;
        if (fileInfo.getIsImg()) {
            key = IMG_PREFIX + PATH_SPLIT + fileInfo.getId() + fileSuffix;
        }
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    ossProperties.getBucketName(), key, file.getInputStream(), objectMetadata);
            ossClient.putObject(putObjectRequest);
            //https://tensua-file.oss-cn-hangzhou.aliyuncs.com/4200BBB6-0F39-4F78-9A37-56361E12549D.jpeg
            //返回路径
            fileInfo.setPath(key);
            fileInfo.setUrl(ossProperties.getUrl() + PATH_SPLIT + key);
            // 设置文件来源
            fileInfo.setSource("OSS");
            fileInfoMapper.insert(fileInfo);
        } catch (IOException e) {
            log.error("上传文件失败，原因：{}", e.getMessage());
        } finally {
            ossClient.shutdown();
        }

        return fileInfo;
    }
}
