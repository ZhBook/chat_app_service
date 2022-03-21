package com.example.cloud.operator.blog.facade;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cloud.data.request.blog.BlogCommentListRequest;
import com.example.cloud.data.request.blog.BlogCommentRequest;
import com.example.cloud.data.request.blog.BlogListRequest;
import com.example.cloud.data.request.blog.BlogRequest;
import com.example.cloud.data.response.blog.BlogCommentListResponse;
import com.example.cloud.data.response.blog.BlogConfigResponse;
import com.example.cloud.data.response.blog.BlogListResponse;
import com.example.cloud.data.response.blog.BlogResponse;
import com.example.cloud.enums.IsDeleteEnum;
import com.example.cloud.enums.StateEnum;
import com.example.cloud.exception.BusinessException;
import com.example.cloud.operator.blog.entity.BlogComment;
import com.example.cloud.operator.blog.entity.BlogList;
import com.example.cloud.operator.blog.entity.BlogMenus;
import com.example.cloud.operator.blog.service.BlogCommentService;
import com.example.cloud.operator.blog.service.BlogListService;
import com.example.cloud.operator.blog.service.BlogMenusService;
import com.example.cloud.system.NoParamsBlogUserRequest;
import com.example.cloud.system.PagingBlogRequest;
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

    @Autowired
    private BlogCommentService blogCommentService;

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
        IPage<BlogList> listIPage = blogListService.page(page, new LambdaQueryWrapper<BlogList>()
                .eq(Objects.nonNull(id), BlogList::getId, id)
                .eq(Objects.nonNull(isOriginal), BlogList::getIsOriginal, isOriginal)
                .like(StringUtils.isNotBlank(title), BlogList::getTitle, title)
                .eq(Objects.nonNull(isPrivate), BlogList::getIsPrivate, isPrivate)
                .eq(BlogList::getIsDelete, IsDeleteEnum.NO.getCode())
                .orderByDesc(BlogList::getCreateDate));
        List<BlogList> blogLists = listIPage.getRecords();
        List<BlogListResponse> collect = blogLists.stream().map(blog -> {
            BlogListResponse blogListResponse = new BlogListResponse();
            BeanUtils.copyProperties(blog, blogListResponse);
            int comment = blogCommentService.count(new LambdaQueryWrapper<BlogComment>()
                    .eq(BlogComment::getBlogId, blog.getId()));
            blogListResponse.setCommentNum(comment);
            return blogListResponse;
        }).collect(Collectors.toList());
        Page<BlogListResponse> responsePage = new Page<>(request.getPageIndex(), request.getPageSize());
        responsePage.setRecords(collect);
        responsePage.setTotal(listIPage.getTotal());
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

    /**
     * 获取blog正文
     *
     * @param blogId
     * @return
     */
    public BlogResponse getBlogById(Long blogId) {
        BlogList blog = blogListService.getById(blogId);
        if (Objects.isNull(blog)) {
            throw new BusinessException("博客不存在");
        }
        BlogResponse blogResponse = new BlogResponse();
        BeanUtils.copyProperties(blog, blogResponse);
        blog.setBlogBrowse(blog.getBlogBrowse() + 1);
        blogListService.updateById(blog);
        return blogResponse;
    }

    /**
     * 通过blogId获取评论
     *
     * @param request
     * @return
     */
    public List<BlogCommentListResponse> getBlogComment(BlogCommentListRequest request) {
        IPage<BlogComment> page = new Page<>(request.getPageIndex(), request.getPageSize());

        page = blogCommentService.page(page, new LambdaQueryWrapper<BlogComment>()
                .eq(BlogComment::getBlogId, request.getBlogId())
                .eq(BlogComment::getIsDelete, IsDeleteEnum.NO.getCode())
                .orderByDesc(BlogComment::getCreateDate));
        List<BlogComment> blogCommentList = page.getRecords();
        if (Objects.isNull(page) || CollectionUtil.isEmpty(blogCommentList)) {
            return CollectionUtil.newArrayList();
        }
        List<BlogCommentListResponse> responses = blogCommentList.stream().map(comment -> {
            BlogCommentListResponse blogCommentListResponse = new BlogCommentListResponse();
            BeanUtils.copyProperties(comment, blogCommentListResponse);
            return blogCommentListResponse;
        }).collect(Collectors.toList());
        return responses;
    }

    /**
     * 通过blogId进行评论
     *
     * @param request
     * @return
     */
    public Boolean blogComment(BlogCommentRequest request) {
        Long blogId = request.getBlogId();
        BlogList blog = blogListService.getById(blogId);
        if (Objects.isNull(blog)) {
            throw new BusinessException("博客不存在");
        }
        Date date = new Date();
        BlogComment blogComment = new BlogComment();
        blogComment.setBlogId(blogId);
        blogComment.setComment(request.getComment());
        blogComment.setCreateDate(date);
        blogComment.setCreateUserId(request.getUserId());
        blogComment.setCreateUserName(request.getNickname());
        blogComment.setEmail(request.getEMail());
        blogComment.setHeadImgUrl(request.getHeadImgUrl());
        return blogCommentService.save(blogComment);
    }

    /**
     * 获取最新评论列表
     *
     * @param request
     * @return
     */
    public List<BlogCommentListResponse> blogCommentNewest(PagingBlogRequest request) {
        IPage<BlogComment> page = new Page<>(request.getPageIndex(), request.getPageSize());

        page = blogCommentService.page(page, new LambdaQueryWrapper<BlogComment>()
                .eq(Objects.nonNull(request), BlogComment::getCreateUserId, request.getUserId())
                .orderByDesc(BlogComment::getCreateDate));
        List<BlogComment> records = page.getRecords();
        if (Objects.isNull(records)) {
            return CollectionUtil.newArrayList();
        }
        List<BlogCommentListResponse> responseList = records.stream().map(record -> {
            BlogCommentListResponse blogCommentListResponse = new BlogCommentListResponse();
            BeanUtils.copyProperties(record, blogCommentListResponse);
            return blogCommentListResponse;
        }).collect(Collectors.toList());
        return responseList;
    }

    /**
     * 获取博客访问前5
     *
     * @param request
     * @return
     */
    public List<BlogListResponse> blogTOP5(NoParamsBlogUserRequest request) {
        IPage<BlogList> page = new Page<>(1, 5);
        page = blogListService.page(page, new LambdaQueryWrapper<BlogList>()
                .eq(Objects.nonNull(request.getUserId()), BlogList::getCreateUserId, request.getUserId())
                .orderByDesc(BlogList::getBlogBrowse)
                .orderByDesc(BlogList::getCreateDate));
        List<BlogList> records = page.getRecords();
        if (Objects.isNull(records)) {
            return CollectionUtil.newArrayList();
        }
        List<BlogListResponse> responseList = records.stream().map(record -> {
            BlogListResponse blogListResponse = new BlogListResponse();
            BeanUtils.copyProperties(record, blogListResponse);
            return blogListResponse;
        }).collect(Collectors.toList());
        return responseList;
    }
}
