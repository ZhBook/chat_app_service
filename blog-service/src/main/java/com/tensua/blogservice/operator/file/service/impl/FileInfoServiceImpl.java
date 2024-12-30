package com.tensua.blogservice.operator.file.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tensua.blogservice.operator.file.entity.FileInfo;
import com.tensua.blogservice.operator.file.mapper.FileInfoMapper;
import com.tensua.blogservice.operator.file.properties.OSSProperties;
import com.tensua.blogservice.operator.file.service.FileInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Service
@Slf4j
public class FileInfoServiceImpl extends ServiceImpl<FileInfoMapper, FileInfo>
        implements FileInfoService {

    @Resource
    private OSSProperties ossProperties;

    private static final String PATH_SPLIT = "/";
    private static final String FILE_SPLIT = ".";
    private static final String IMG_PREFIX = "blogCoverPicture/";

    /**
     * 获取所有图片
     *
     * @return
     */
    public List<FileInfo> getBlogCoverPicture() {
        OSS ossClient = new OSSClientBuilder().build(ossProperties.getEndpoint(), ossProperties.getAccessKey(), ossProperties.getAccessKeySecret());
        List<FileInfo> fileInfos = new ArrayList<>();
        try {
            // 列举文件。如果不设置keyPrefix，则列举存储空间下的所有文件。如果设置keyPrefix，则列举包含指定前缀的文件。
            ObjectListing objectListing = ossClient.listObjects(ossProperties.getBucketName(), IMG_PREFIX);
            List<OSSObjectSummary> sums = objectListing.getObjectSummaries();

            for (int i = 1; i < sums.size(); i++) {
                OSSObjectSummary ossObjectSummary = sums.get(i);
                FileInfo fileInfo = new FileInfo();
                fileInfo.setUrl(ossProperties.getUrl() + PATH_SPLIT + ossObjectSummary.getKey());
                fileInfo.setPath(ossObjectSummary.getKey());
                fileInfo.setSource(ossObjectSummary.getType());
                fileInfo.setSize(ossObjectSummary.getSize());
                fileInfos.add(fileInfo);
            }
        } catch (OSSException oe) {
            log.info("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
        } catch (ClientException ce) {
            log.info("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return fileInfos;
    }
}




