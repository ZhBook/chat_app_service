package com.example.cloud.operator.blog.facade;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cloud.data.request.blog.*;
import com.example.cloud.data.response.blog.*;
import com.example.cloud.enums.IsDeleteEnum;
import com.example.cloud.enums.StateEnum;
import com.example.cloud.exception.BusinessException;
import com.example.cloud.operator.blog.entity.*;
import com.example.cloud.operator.blog.service.*;
import com.example.cloud.system.NoParamsBlogUserRequest;
import com.example.cloud.system.PagingBlogRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private BlogTagService blogTagService;

    @Autowired
    private BlogTagRelationService blogTagRelationService;

    final private Integer tagMax = 10;

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
    @Transactional
    public Boolean addBlog(BlogRequest request) {
        BlogList blogList = new BlogList();
        BeanUtils.copyProperties(request, blogList);
        Date date = new Date();
        blogList.setCreateDate(date);
        blogList.setCreateUserId(request.getId());
        blogList.setUpdateDate(date);
        blogList.setUpdateUserId(request.getId());
        blogList.setVersion(1);
        blogListService.save(blogList);

        if (StringUtils.isNotBlank(request.getTags())) {
            String[] tags = request.getTags().split(",");
            List<BlogTag> blogTags = blogTagService.list(new LambdaQueryWrapper<BlogTag>()
                    .in(Objects.nonNull(tags), BlogTag::getId, tags));
            List<BlogTagRelation> relations = blogTags.stream().map(tag -> {
                BlogTagRelation tagRelation = new BlogTagRelation();
                tagRelation.setTagId(tag.getId());
                tagRelation.setBlogId(blogList.getId());
                tagRelation.setTagName(tag.getName());
                tagRelation.setCreateUserId(request.getAuthorId());
                tagRelation.setCreateDatetime(date);
                return tagRelation;
            }).collect(Collectors.toList());
            blogTagRelationService.saveBatch(relations);
        }
        return Boolean.TRUE;
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
    public IPage<BlogCommentListResponse> getBlogComment(BlogCommentListRequest request) {
        IPage<BlogComment> page = new Page<>(request.getPageIndex(), request.getPageSize());

        page = blogCommentService.page(page, new LambdaQueryWrapper<BlogComment>()
                .eq(BlogComment::getBlogId, request.getBlogId())
                .eq(BlogComment::getIsDelete, IsDeleteEnum.NO.getCode())
                .orderByDesc(BlogComment::getCreateDate));
        List<BlogComment> blogCommentList = page.getRecords();

        Page<BlogCommentListResponse> responsePage = new Page<>(request.getPageIndex(), request.getPageSize());
        List<BlogCommentListResponse> responses = blogCommentList.stream().map(comment -> {
            BlogCommentListResponse blogCommentListResponse = new BlogCommentListResponse();
            BeanUtils.copyProperties(comment, blogCommentListResponse);
            return blogCommentListResponse;
        }).collect(Collectors.toList());
        responsePage.setRecords(responses);
        responsePage.setTotal(page.getTotal());
        return responsePage;
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

    /**
     * 创建tag
     *
     * @param request
     * @return
     */
    public Boolean createTag(BlogTagRequest request) {
        Long userId = request.getUserId();
        int count = blogTagService.count(new LambdaQueryWrapper<BlogTag>()
                .eq(BlogTag::getCreateUserId, userId)
                .eq(BlogTag::getIsDelete, IsDeleteEnum.NO));
        if (count >= tagMax) {
            throw new BusinessException("最多可创建10个标签");
        }
        BlogTag blogTag = new BlogTag();
        blogTag.setName(request.getName());
        blogTag.setCreateDate(new Date());
        blogTag.setCreateUserId(userId);
        return blogTagService.save(blogTag);
    }

    /**
     * 获取tag列表
     *
     * @return
     */
    public List<BlogTagListResponse> getTag(NoParamsBlogUserRequest request) {
        List<BlogTag> list = blogTagService.list(new LambdaQueryWrapper<BlogTag>()
                .eq(BlogTag::getCreateUserId, request.getUserId())
                .eq(BlogTag::getIsDelete, IsDeleteEnum.NO.getCode()));
        List<BlogTagListResponse> responseList = list.stream().map(tag -> {
            BlogTagListResponse blogTagListResponse = new BlogTagListResponse();
            BeanUtils.copyProperties(tag, blogTagListResponse);
            return blogTagListResponse;
        }).collect(Collectors.toList());
        return responseList;
    }

    /**
     * 获取blog对应的tag
     *
     * @param blogId
     * @return
     */
    public List<BlogTagListResponse> getBlogTag(Long blogId) {
        List<BlogTagRelation> relationList = blogTagRelationService.list(new LambdaQueryWrapper<BlogTagRelation>()
                .eq(BlogTagRelation::getBlogId, blogId));
        List<BlogTagListResponse> responseList = relationList.stream().map(relation -> {
            BlogTagListResponse blogTagListResponse = new BlogTagListResponse();
            blogTagListResponse.setId(relation.getId());
            blogTagListResponse.setName(relation.getTagName());
            return blogTagListResponse;
        }).collect(Collectors.toList());
        return responseList;
    }

    /**
     * 获取用户信息
     *
     * @param request
     * @return
     */
    public BlogUserInfoResponse getBlogUserInfo(NoParamsBlogUserRequest request) {
        BlogUserInfoResponse response = new BlogUserInfoResponse();
        Long userId = request.getUserId();
        Date createTime = request.getCreateTime();
        Date date = new Date();
        long betweenYear = DateUtil.betweenYear(date, createTime, false);
        long betweenMonth = DateUtil.betweenMonth(date, createTime, false);
        if (betweenYear <= 0) {
            response.setRunningDay(betweenMonth + "个月");
        } else {
            response.setRunningDay(betweenYear + "年" + betweenMonth + "个月");
        }
        int blogCount = blogListService.count(new LambdaQueryWrapper<BlogList>()
                .eq(BlogList::getCreateUserId, userId)
                .eq(BlogList::getIsDelete, IsDeleteEnum.NO.getCode()));
        int commentCount = blogCommentService.count(new LambdaQueryWrapper<BlogComment>()
                .eq(BlogComment::getCreateUserId, userId)
                .eq(BlogComment::getIsDelete, IsDeleteEnum.NO.getCode()));

        response.setNickname(request.getNickname());
        response.setCommentCount(commentCount);
        response.setBlogCount(blogCount);
        // todo 添加粉丝数量
        response.setFollowCount(0);
        return response;
    }
}
