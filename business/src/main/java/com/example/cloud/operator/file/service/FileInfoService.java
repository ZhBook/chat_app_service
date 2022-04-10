package com.example.cloud.operator.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.cloud.operator.file.entity.FileInfo;

import java.util.List;

/**
 *
 */
public interface FileInfoService extends IService<FileInfo> {

    List<FileInfo> getBlogCoverPicture();
}
