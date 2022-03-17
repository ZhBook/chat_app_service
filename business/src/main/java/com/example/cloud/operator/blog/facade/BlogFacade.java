package com.example.cloud.operator.blog.facade;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cloud.data.request.blog.BlogListRequest;
import com.example.cloud.data.request.blog.BlogRequest;
import com.example.cloud.data.response.blog.BlogConfigResponse;
import com.example.cloud.data.response.blog.BlogListResponse;
import com.example.cloud.enums.IsDeleteEnum;
import com.example.cloud.enums.StateEnum;
import com.example.cloud.operator.blog.entity.BlogList;
import com.example.cloud.operator.blog.entity.BlogMenus;
import com.example.cloud.operator.blog.service.BlogListService;
import com.example.cloud.operator.blog.service.BlogMenusService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author zhooke
 * @since 2022/3/8 14:41
 **/
@Service
public class BlogFacade {
    @Autowired
    private BlogMenusService blogMenusService;

    @Autowired
    private BlogListService blogListService;

    /**
     * 获取后台菜单列表
     *
     * @return
     */
    public List<BlogConfigResponse> getMenusList() {
        List<BlogMenus> menusList = blogMenusService.list(new LambdaQueryWrapper<BlogMenus>()
                .eq(BlogMenus::getIsDisable, StateEnum.ENABLED.getCode())
                .orderByDesc(BlogMenus::getIndex));
        ArrayList<BlogConfigResponse> responses = new ArrayList<>();
        menusList.stream().forEach(menu -> {
            if (menu.getFatherId().equals(0)) {
                BlogConfigResponse blogConfigResponse = new BlogConfigResponse();
                BeanUtils.copyProperties(blogConfigResponse, menu);

            }
        });
        return responses;
    }

    /**
     * 获取博客列表
     *
     * @param request
     * @return
     */
    public IPage<BlogListResponse> getBlogList(BlogListRequest request) {
        Long id = request.getId();
        Integer isOriginal = request.getIsOriginal();
        String title = request.getTitle();
        Integer isPrivate = request.getIsPrivate();
        IPage<BlogList> page = new Page<>(request.getPageIndex(), request.getPageSize());
        page = blogListService.page(page, new LambdaQueryWrapper<BlogList>()
                .eq(Objects.nonNull(id), BlogList::getId, id)
                .eq(Objects.nonNull(isOriginal), BlogList::getIsOriginal, isOriginal)
                .like(StringUtils.isNotBlank(title), BlogList::getTitle, title)
                .eq(Objects.nonNull(isPrivate), BlogList::getIsPrivate, isPrivate)
                .eq(BlogList::getIsDelete, IsDeleteEnum.NO.getCode()));
        List<BlogList> blogLists = page.getRecords();
        List<BlogListResponse> collect = blogLists.stream().map(blog -> {
            BlogListResponse blogListResponse = new BlogListResponse();
            BeanUtils.copyProperties(blog, blogListResponse);
            return blogListResponse;
        }).collect(Collectors.toList());
        Page<BlogListResponse> responsePage = new Page<>(request.getPageIndex(), request.getPageSize());
        responsePage.setRecords(collect);
        responsePage.setTotal(page.getTotal());
        return responsePage;
    }

    /**
     * 添加博客
     *
     * @param request
     * @return
     */
    public Boolean addBlog(BlogRequest request) {
        BlogList blogList = new BlogList();
        BeanUtils.copyProperties(request, blogList);
        Date date = new Date();
        blogList.setCreateDate(date);
        blogList.setCreateUserId(request.getId());
        blogList.setUpdateDate(date);
        blogList.setUpdateUserId(request.getId());
        blogList.setVersion(1);
        return blogListService.save(blogList);
    }
}
