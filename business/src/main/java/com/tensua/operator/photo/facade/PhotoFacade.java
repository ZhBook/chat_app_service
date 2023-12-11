package com.tensua.operator.photo.facade;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tensua.data.request.photo.PhotoBatchAddRequest;
import com.tensua.data.request.photo.PhotoPagingRequest;
import com.tensua.data.response.photo.PhotoPagingResponse;
import com.tensua.enums.IsDeleteEnum;
import com.tensua.operator.photo.entity.PhotoExif;
import com.tensua.operator.photo.service.IPhotoExifService;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhooke
 * @since 2022/7/4 10:10
 **/
@Service
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

            photoExif.setFocalLength("")
                    .setFNumber("")
                    .setExposureTime("")
                    .setIso("")
                    .setModel("")
                    .setManufacturer("")
                    .setArtist(artist)
                    .setOrientation("")
                    .setResolutionUnit("")
                    .setSoftware("")
                    .setExposureProgram("")
                    .setDatetimeOriginal(now)
                    .setExposureBiasValue("")
                    .setMaxApertureValue("")
                    .setMeteringMode("")
                    .setFlash("")
                    .setMakerNote(makerNote)
                    .setExifimageWidth("")
                    .setExifimageLength("")
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

            return photoExif;
        }).collect(Collectors.toList());

        return photoExifService.saveBatch(photoExifList);
    }
}
