package com.tensua.operator.photo.service.impl;

import com.tensua.operator.photo.entity.PhotoExif;
import com.tensua.operator.photo.mapper.PhotoExifMapper;
import com.tensua.operator.photo.service.IPhotoExifService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 照片信息 服务实现类
 * </p>
 *
 * @author zhooke
 * @since 2022-12-16
 */
@Service
public class PhotoExifServiceImpl extends ServiceImpl<PhotoExifMapper, PhotoExif> implements IPhotoExifService {

}
