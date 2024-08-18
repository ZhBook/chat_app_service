package com.tensua.blogservice.operator.moments.facade;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tensua.blogservice.data.request.moments.CommentRequest;
import com.tensua.blogservice.data.request.moments.LikeRequest;
import com.tensua.blogservice.data.request.moments.MomentsPageRequest;
import com.tensua.blogservice.data.request.moments.MomentsRequest;
import com.tensua.blogservice.data.response.moments.MomentsCommentResponse;
import com.tensua.blogservice.data.response.moments.MomentsLikesResponse;
import com.tensua.blogservice.data.response.moments.MomentsResponse;
import com.tensua.blogservice.enums.StateEnum;
import com.tensua.blogservice.operator.friends.entity.UserRelation;
import com.tensua.blogservice.operator.friends.service.UserRelationService;
import com.tensua.blogservice.operator.moments.entity.Moments;
import com.tensua.blogservice.operator.moments.entity.MomentsComment;
import com.tensua.blogservice.operator.moments.entity.MomentsLikes;
import com.tensua.blogservice.operator.moments.service.MomentsCommentService;
import com.tensua.blogservice.operator.moments.service.MomentsLikesService;
import com.tensua.blogservice.operator.moments.service.MomentsService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author zhooke
 * @since 2022/1/10 09:30
 **/
@Service
public class MomentsFacade {
    @Resource
    private MomentsService momentsService;

    @Resource
    private UserRelationService userRelationService;

    @Resource
    private MomentsLikesService momentsLikesService;

    @Resource
    private MomentsCommentService momentsCommentService;

    /**
     * 获取朋友圈信息
     *
     * @param request
     * @return
     */
    public IPage<MomentsResponse> getMoments(MomentsPageRequest request) {
        IPage<Moments> page = new Page<>(request.getPageIndex(), request.getPageSize());
        List<Long> ids = new ArrayList<>();
        ids.add(request.getUserId());

        List<Long> relationList = userRelationService.list(new LambdaQueryWrapper<UserRelation>()
                        .eq(UserRelation::getUserId, request.getUserId()))
                .stream().map(UserRelation::getFriendId).collect(Collectors.toList());
        // 查出所有的好友
        if (!relationList.isEmpty()) {
            ids.addAll(relationList);
        }

        // 查出好友的前几条朋友圈信息
        page = momentsService.page(page, new LambdaQueryWrapper<Moments>()
                .in(Moments::getUserId, ids)
                .eq(Moments::getIsDelete, StateEnum.ENABLED.getCode())
                .orderByDesc(Moments::getCreateTime));
        List<Moments> momentsList = page.getRecords();

        List<MomentsResponse> responses = momentsList.stream().map(moments -> {
            MomentsResponse response = new MomentsResponse();
            Long momentsId = moments.getId();
            BeanUtils.copyProperties(moments, response);
            // 朋友圈评论
            List<MomentsComment> commentLists = momentsCommentService.list(new LambdaQueryWrapper<MomentsComment>()
                    .eq(MomentsComment::getMomentsId, momentsId)
                    .eq(MomentsComment::getIsDelete, StateEnum.ENABLED.getCode()));
            List<MomentsCommentResponse> momentsCommentResponses = commentLists.stream().map(comment -> {
                MomentsCommentResponse momentsCommentResponse = new MomentsCommentResponse();
                BeanUtils.copyProperties(comment, momentsCommentResponse);
                return momentsCommentResponse;
            }).collect(Collectors.toList());
            response.setMomentsCommentResponses(momentsCommentResponses);
            // 朋友圈点赞
            List<MomentsLikes> momentsLikes = momentsLikesService.list(new LambdaQueryWrapper<MomentsLikes>()
                    .eq(MomentsLikes::getMomentsId, momentsId));
            List<MomentsLikesResponse> momentsLikesResponses = momentsLikes.stream().map(like -> {
                MomentsLikesResponse momentsLikesResponse = new MomentsLikesResponse();
                BeanUtils.copyProperties(like, momentsLikesResponse);
                return momentsLikesResponse;
            }).collect(Collectors.toList());
            response.setMomentsLikesResponses(momentsLikesResponses);
            return response;
        }).collect(Collectors.toList());

        Page<MomentsResponse> responsePage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        responsePage.setRecords(responses);
        return responsePage;
    }

    /**
     * 发布朋友圈信息
     *
     * @return
     */
    public Boolean publishMoments(MomentsRequest request) {
        Moments moments = new Moments();
        moments.setUserId(request.getUserId());
        moments.setContext(request.getContext());
        moments.setImages(request.getImages());
        moments.setVideo(request.getVideo());
        moments.setCreateTime(new Date());
        momentsService.save(moments);
        return Boolean.TRUE;
    }

    /**
     * 点赞朋友圈
     *
     * @param likeRequest
     * @return
     */
    public Boolean likeMoments(LikeRequest likeRequest) {
        MomentsLikes momentsLikes = momentsLikesService.getOne(new LambdaQueryWrapper<MomentsLikes>()
                .eq(MomentsLikes::getMomentsId, likeRequest.getMomentsId())
                .eq(MomentsLikes::getUserId, likeRequest.getUserId()));
        if (Objects.nonNull(momentsLikes)) {
            momentsLikesService.removeById(momentsLikes);
            return Boolean.TRUE;
        }
        MomentsLikes likes = new MomentsLikes();
        likes.setMomentsId(likeRequest.getMomentsId());
        likes.setUserId(likeRequest.getUserId());
        likes.setCreateTime(new Date());
        momentsLikesService.save(likes);
        return Boolean.TRUE;
    }

    /**
     * 评论朋友圈信息
     *
     * @param request
     * @return
     */
    public Boolean commentMoments(CommentRequest request) {
        MomentsComment momentsComment = new MomentsComment();
        momentsComment.setMomentsId(request.getMomentsId());
        momentsComment.setContext(request.getContext());
        momentsComment.setUserId(request.getUserId());
        momentsComment.setCreateTime(new Date());
        momentsCommentService.save(momentsComment);
        return Boolean.TRUE;
    }
}
