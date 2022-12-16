package com.tensua.operator.photo.facade;

import cn.hutool.core.io.FileUtil;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author zhooke
 * @since 2022/7/4 10:10
 **/
@Slf4j
class PhotoFacadeTest {
    String url = "https://tensua-file.oss-cn-hangzhou.aliyuncs.com/blogCoverPicture/ec226cbefa8e9193f7264e1f493e333c.jpeg";

    @Test
    void getPhotoInfo() {
        try {
            URL result = new URL(url);

            InputStream inputStream = result.openStream();

            File file = FileUtil.file();

            Metadata metadata = JpegMetadataReader.readMetadata(inputStream);
            ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

            if (directory.containsTag(ExifSubIFDDirectory.TAG_MODEL)) {
                log.info("相机型号：" + directory.getDescription(ExifSubIFDDirectory.TAG_MODEL));
            }
            if (directory.containsTag(ExifSubIFDDirectory.TAG_EXPOSURE_TIME)) {
                log.info("曝光时间：" + directory.getDescription(ExifSubIFDDirectory.TAG_EXPOSURE_TIME));
            }
            if (directory.containsTag(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)) {
                log.info("拍摄时间：" + directory.getDescription(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL));
            }
            if (directory.containsTag(ExifSubIFDDirectory.TAG_APERTURE)) {
                log.info("光圈值：" + directory.getDescription(ExifSubIFDDirectory.TAG_APERTURE));
            }
            if (directory.containsTag(ExifSubIFDDirectory.TAG_FOCAL_LENGTH)) {
                log.info("焦距：" + directory.getDescription(ExifSubIFDDirectory.TAG_FOCAL_LENGTH));
            }
            if (directory.containsTag(ExifSubIFDDirectory.TAG_RELATED_IMAGE_HEIGHT)) {
                log.info("图片尺寸：" + directory.getDescription(ExifSubIFDDirectory.TAG_RELATED_IMAGE_HEIGHT));
            }
        } catch (JpegProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


}