package com.tensua.operator.photo.facade;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tensua.data.request.photo.PhotoPagingRequest;
import com.tensua.data.response.photo.PhotoPagingResponse;
import com.tensua.enums.IsDeleteEnum;
import com.tensua.operator.photo.entity.PhotoExif;
import com.tensua.operator.photo.service.IPhotoExifService;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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
}
