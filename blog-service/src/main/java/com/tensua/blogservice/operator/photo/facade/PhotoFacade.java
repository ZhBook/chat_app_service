package com.tensua.blogservice.operator.photo.facade;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.google.common.collect.Lists;
import com.tensua.blogservice.data.exception.BusinessException;
import com.tensua.blogservice.data.request.photo.PhotoBatchAddRequest;
import com.tensua.blogservice.data.request.photo.PhotoPagingRequest;
import com.tensua.blogservice.data.request.photo.PhotoUpdateRequest;
import com.tensua.blogservice.data.response.photo.PhotoPagingResponse;
import com.tensua.blogservice.data.system.BlogUserRequest;
import com.tensua.blogservice.enums.IsDeleteEnum;
import com.tensua.blogservice.operator.photo.entity.PhotoExif;
import com.tensua.blogservice.operator.photo.service.IPhotoExifService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author zhooke
 * @since 2022/7/4 10:10
 **/
@Service
@Slf4j
public class PhotoFacade {

    @Resource
    private IPhotoExifService photoExifService;

    /**
     * 分页查询图片
     *
     * @param request
     * @return
     */
    public IPage<PhotoPagingResponse> pagingPhoto(PhotoPagingRequest request) {
        Integer pageIndex = request.getPageIndex();
        Integer pageSize = request.getPageSize();

        IPage<PhotoExif> page = new Page<>(pageIndex, pageSize);
        page = photoExifService.page(page, new LambdaQueryWrapper<PhotoExif>()
                .eq(PhotoExif::getIsDelete, IsDeleteEnum.NO.getCode())
                .orderByDesc(PhotoExif::getCreateDate)
        );
        List<PhotoExif> photoExifList = page.getRecords();

        IPage<PhotoPagingResponse> responsePage = new Page<>(pageIndex, pageSize);

        if (CollectionUtils.isEmpty(photoExifList)) {
            return responsePage;
        }
        List<PhotoPagingResponse> responseList = photoExifList.stream().map(photo -> {
            PhotoPagingResponse response = new PhotoPagingResponse();
            BeanUtils.copyProperties(photo, response);
            return response;
        }).collect(Collectors.toList());

        return responsePage.setRecords(responseList)
                .setTotal(page.getTotal());
    }

    /**
     * 批量添加图片
     *
     * @param request
     * @return
     */
    public Boolean batchAddPhoto(PhotoBatchAddRequest request) {
        List<String> photoUrls = request.getPhotoUrls();
        String artist = request.getArtist();
        String makerNote = request.getMakerNote();
        String photoDescribe = request.getPhotoDescribe();
        String province = request.getProvince();
        String city = request.getCity();
        String county = request.getCounty();
        String town = request.getTown();
        String village = request.getVillage();
        String address = request.getAddress();
        String longitude = request.getLongitude();
        String latitude = request.getLatitude();
        Long userId = request.getUserId();
        String username = request.getUsername();

        List<PhotoExif> photoExifList = photoUrls.stream().map(photoUrl -> {

            PhotoExif photoExif = new PhotoExif();
            Date now = new Date();
            try {
                InputStream is = new URL(photoUrl).openStream();
                Metadata metadata = ImageMetadataReader.readMetadata(is);
                for (Directory directory : metadata.getDirectories()) {
                    photoExif
                            .setIso(directory.getString(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT))
                            .setModel(directory.getString(ExifSubIFDDirectory.TAG_MODEL))
                            .setManufacturer(directory.getString(ExifSubIFDDirectory.TAG_MAKE))
                            .setArtist(artist)
                            .setOrientation(directory.getString(ExifSubIFDDirectory.TAG_ORIENTATION))
                            .setResolutionUnit(directory.getString(ExifSubIFDDirectory.TAG_RESOLUTION_UNIT))
                            .setSoftware(directory.getString(ExifSubIFDDirectory.TAG_SOFTWARE))
                            .setExposureProgram(directory.getString(ExifSubIFDDirectory.TAG_EXPOSURE_PROGRAM))
                            .setDatetimeOriginal(now)
                            .setExposureBiasValue(directory.getString(ExifSubIFDDirectory.TAG_EXPOSURE_BIAS))
                            .setMaxApertureValue(directory.getString(ExifSubIFDDirectory.TAG_MAX_SAMPLE_VALUE))
                            .setMeteringMode(directory.getString(ExifSubIFDDirectory.TAG_METERING_MODE))
                            .setFlash(directory.getString(ExifSubIFDDirectory.TAG_FLASH))
                            .setMakerNote(makerNote)
                            .setExifimageWidth(directory.getString(ExifSubIFDDirectory.TAG_EXIF_IMAGE_WIDTH))
                            .setExifimageLength(directory.getString(ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT))
                            .setPhotoUrl(photoUrl)
                            .setPhotoDescribe(photoDescribe)
                            .setProvince(province)
                            .setCity(city)
                            .setCounty(county)
                            .setTown(town)
                            .setVillage(village)
                            .setAddress(address)
                            .setLongitude(longitude)
                            .setLatitude(latitude)
                            .setCreateUserId(userId)
                            .setCreateUserName(username)
                            .setCreateDate(now)
                            .setUpdateUserId(userId)
                            .setUpdateUserName(username)
                            .setUpdateDate(now);
                }
            } catch (Throwable e) {
                log.error("获取图片数据失败");
            }
            return photoExif;
        }).collect(Collectors.toList());
        return photoExifService.saveBatch(photoExifList);

    }

    /**
     * 更新图片介绍
     *
     * @param request
     * @return
     */
    public Boolean updatePhoto(PhotoUpdateRequest request) {
        Long id = request.getId();
        PhotoExif photoExif = photoExifService.lambdaQuery()
                .eq(PhotoExif::getId, id)
                .eq(PhotoExif::getIsDelete, IsDeleteEnum.NO.getCode())
                .one();
        if (Objects.isNull(photoExif)) {
            throw new BusinessException("图片信息不存在");
        }
        BeanUtils.copyProperties(request, photoExif);

        return photoExif.setUpdateDate(new Date())
                .setUpdateUserId(request.getUserId())
                .setUpdateUserName(request.getNickname())
                .updateById();
    }

    /**
     * 获取图片信息
     *
     * @param photoId
     * @return
     */
    public List<PhotoPagingResponse> getPhotoInfo(Long photoId, BlogUserRequest request) {
        List<PhotoExif> photoExifList = photoExifService.list(new LambdaQueryWrapper<PhotoExif>()
                .eq(PhotoExif::getId, photoId)
                .eq(PhotoExif::getCreateUserId, request.getUserId())
                .eq(PhotoExif::getIsDelete, IsDeleteEnum.NO.getCode())
        );
        if (CollectionUtils.isEmpty(photoExifList)) {
            return Lists.newArrayList();
        }
        return photoExifList.stream().map(photo -> {
            PhotoPagingResponse response = new PhotoPagingResponse();
            BeanUtils.copyProperties(photo, response);
            return response;
        }).collect(Collectors.toList());
    }

    /**
     * 删除图片
     *
     * @param photoId
     * @return
     */
    public Boolean deletePhoto(Long photoId, BlogUserRequest request) {
        PhotoExif photoExif = photoExifService.lambdaQuery()
                .eq(PhotoExif::getId, photoId)
                .eq(PhotoExif::getCreateUserId, request.getUserId())
                .eq(PhotoExif::getIsDelete, IsDeleteEnum.NO.getCode())
                .one();
        if (Objects.isNull(photoExif)) {
            throw new BusinessException("图片信息不存在");
        }

        return photoExif.setIsDelete(IsDeleteEnum.YES.getCode())
                .setUpdateDate(new Date())
                .setUpdateUserId(request.getUserId())
                .setUpdateUserName(request.getNickname())
                .updateById();
    }
}
