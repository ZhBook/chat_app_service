package com.tensua.blogservice.operator.moments.controller;

import com.tensua.blogservice.data.BaseResult;
import com.tensua.blogservice.data.PageResult;
import com.tensua.blogservice.data.request.moments.CommentRequest;
import com.tensua.blogservice.data.request.moments.LikeRequest;
import com.tensua.blogservice.data.request.moments.MomentsPageRequest;
import com.tensua.blogservice.data.request.moments.MomentsRequest;
import com.tensua.blogservice.data.response.moments.MomentsResponse;
import com.tensua.blogservice.operator.moments.facade.MomentsFacade;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zhooke
 * @since 2022/1/10 09:29
 **/
@RestController
@RequestMapping("/moments")
public class MomentsController {

    @Resource
    private MomentsFacade momentsFacade;

    /**
     * 获取朋友圈信息
     *
     * @param request
     * @return
     */
    @PostMapping("/all")
    public PageResult<MomentsResponse> getMoments(@RequestBody MomentsPageRequest request) {
        return PageResult.pageSuccess(momentsFacade.getMoments(request));
    }

    /**
     * 发布朋友圈信息
     *
     * @return
     */
    @PostMapping("/publish")
    public BaseResult<Boolean> publishMoments(@RequestBody MomentsRequest request) {
        return BaseResult.succeed(momentsFacade.publishMoments(request));
    }

    /**
     * 点赞朋友圈
     * @param request
     * @return
     */
    @PostMapping("/likes")
    public BaseResult<Boolean> likeMoments(@RequestBody LikeRequest request) {
        return BaseResult.succeed(momentsFacade.likeMoments(request));
    }

    /**
     * 评论朋友圈信息
     * @param request
     * @return
     */
    @PostMapping("/comment")
    public BaseResult<Boolean> commentMoments(@RequestBody CommentRequest request){
        return BaseResult.succeed(momentsFacade.commentMoments(request));
    }
}
