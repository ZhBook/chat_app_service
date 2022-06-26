package com.tensua.operator.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tensua.operator.file.entity.FileInfo;

import java.util.List;

/**
 *
 */
public interface FileInfoService extends IService<FileInfo> {

    List<FileInfo> getBlogCoverPicture();
}
