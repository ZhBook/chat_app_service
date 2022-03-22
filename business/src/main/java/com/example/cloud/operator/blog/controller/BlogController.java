package com.example.cloud.operator.blog.controller;

import com.example.cloud.data.BaseResult;
import com.example.cloud.data.PageResult;
import com.example.cloud.data.request.blog.*;
import com.example.cloud.data.response.blog.*;
import com.example.cloud.operator.blog.facade.BlogFacade;
import com.example.cloud.system.NoParamsBlogUserRequest;
import com.example.cloud.system.PagingBlogRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhooke
 * @since 2022/3/8 14:40
 **/
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogFacade blogFacade;

    /**
     * 获取后台菜单列表
     *
     * @return
     */
    @GetMapping("menus")
    public BaseResult<List<BlogConfigResponse>> getMenusList() {
        return BaseResult.succeed(blogFacade.getMenusList());
    }

    /**
     * 获取博客列表
     *
     * @param request
     * @return
     */
    @PostMapping("/list")
    public PageResult<BlogListResponse> getBlogList(@RequestBody BlogListRequest request) {
        return PageResult.pageSuccess(blogFacade.getBlogList(request));
    }

    /**
     * 添加博客
     *
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResult<Boolean> addBlog(@RequestBody BlogRequest request) {
        return BaseResult.succeed(blogFacade.addBlog(request));
    }

    /**
     * 获取blog正文
     *
     * @param blogId
     * @return
     */
    @GetMapping("/{blogId}")
    public BaseResult<BlogResponse> getBlogById(@PathVariable("blogId") Long blogId) {
        return BaseResult.succeed(blogFacade.getBlogById(blogId));
    }

    /**
     * 通过blogId获取评论
     *
     * @param request
     * @return
     */
    @PostMapping("/comment:list")
    public PageResult<BlogCommentListResponse> getBlogComment(@RequestBody BlogCommentListRequest request) {
        return PageResult.pageSuccess(blogFacade.getBlogComment(request));
    }

    /**
     * 通过blogId进行评论
     *
     * @param request
     * @return
     */
    @PostMapping("/comment")
    public BaseResult<Boolean> blogComment(@RequestBody BlogCommentRequest request) {
        return BaseResult.succeed(blogFacade.blogComment(request));
    }

    /**
     * 获取最新评论列表
     *
     * @param request
     * @return
     */
    @GetMapping("/comment/newest")
    public BaseResult<List<BlogCommentListResponse>> blogCommentNewest(PagingBlogRequest request) {
        return BaseResult.succeed(blogFacade.blogCommentNewest(request));
    }

    /**
     * 获取博客访问前5
     *
     * @param request
     * @return
     */
    @GetMapping("/top5")
    public BaseResult<List<BlogListResponse>> blogTOP5(NoParamsBlogUserRequest request) {
        return BaseResult.succeed(blogFacade.blogTOP5(request));
    }

    /**
     * 创建tag
     *
     * @param request
     * @return
     */
    @PostMapping("/tag")
    public BaseResult<Boolean> createTag(@RequestBody BlogTagRequest request) {
        return BaseResult.succeed(blogFacade.createTag(request));
    }

    /**
     * 获取tag列表
     *
     * @return
     */
    @GetMapping("/tag")
    public BaseResult<List<BlogTagListResponse>> getTag(NoParamsBlogUserRequest request) {
        return BaseResult.succeed(blogFacade.getTag(request));
    }

    /**
     * 获取blog对应的tag
     *
     * @param blogId
     * @return
     */
    @GetMapping("/tag/relation/{blogId}")
    public BaseResult<List<BlogTagListResponse>> getBlogTag(@PathVariable("blogId") Long blogId) {
        return BaseResult.succeed(blogFacade.getBlogTag(blogId));
    }

    /**
     * 获取用户信息
     *
     * @param request
     * @return
     */
    @GetMapping("/user")
    public BaseResult<BlogUserInfoResponse> getBlogUserInfo(NoParamsBlogUserRequest request) {
        return BaseResult.succeed(blogFacade.getBlogUserInfo(request));
    }
}
