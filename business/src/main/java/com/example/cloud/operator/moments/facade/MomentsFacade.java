package com.example.cloud.operator.moments.facade;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cloud.data.request.moments.MomentsPageRequest;
import com.example.cloud.data.request.moments.MomentsRequest;
import com.example.cloud.data.response.moments.MomentsCommentResponse;
import com.example.cloud.data.response.moments.MomentsLikesResponse;
import com.example.cloud.data.response.moments.MomentsResponse;
import com.example.cloud.enums.StateEnum;
import com.example.cloud.operator.friends.entity.UserRelation;
import com.example.cloud.operator.friends.service.UserRelationService;
import com.example.cloud.operator.moments.entity.Moments;
import com.example.cloud.operator.moments.entity.MomentsComment;
import com.example.cloud.operator.moments.entity.MomentsLikes;
import com.example.cloud.operator.moments.service.MomentsCommentService;
import com.example.cloud.operator.moments.service.MomentsLikesService;
import com.example.cloud.operator.moments.service.MomentsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhooke
 * @since 2022/1/10 09:30
 **/
@Service
public class MomentsFacade {
    @Autowired
    private MomentsService momentsService;

    @Autowired
    private UserRelationService userRelationService;

    @Autowired
    private MomentsLikesService momentsLikesService;

    @Autowired
    private MomentsCommentService momentsCommentService;

    /**
     * 获取朋友圈信息
     *
     * @param request
     * @return
     */
    public IPage<MomentsResponse> getMoments(MomentsPageRequest request) {
        Page<Moments> page = new Page<>(request.getPageIndex(), request.getPageSize());
        List<Long> ids = new ArrayList<>();
        ids.add(request.getId());

        List<Long> relationList = userRelationService.list(new LambdaQueryWrapper<UserRelation>()
                        .eq(UserRelation::getUserId, request.getId()))
                .stream().map(UserRelation::getFriendId).collect(Collectors.toList());
        // 查出所有的好友
        if (!relationList.isEmpty()){
            ids.addAll(relationList);
        }

        // 查出好友的前几条朋友圈信息
        IPage<Moments> momentsIPage = momentsService.page(page, new LambdaQueryWrapper<Moments>()
                .in(Moments::getUserId, ids)
                .eq(Moments::getIsDelete, StateEnum.ENABLED.getCode())
                .orderByDesc(Moments::getCreateTime));
        List<Moments> momentsList = momentsIPage.getRecords();

        List<MomentsResponse> responses = momentsList.stream().map(moments -> {
            MomentsResponse response = new MomentsResponse();
            Long momentsId = moments.getId();
            BeanUtils.copyProperties(moments, response);
            // 朋友圈评论
            List<MomentsComment> commentLists = momentsCommentService.list(new LambdaQueryWrapper<MomentsComment>()
                    .eq(MomentsComment::getMomentsId, momentsId)
                    .eq(MomentsComment::getIsDelete, StateEnum.ENABLED));
            List<MomentsCommentResponse> momentsCommentResponses = commentLists.stream().map(comment -> {
                MomentsCommentResponse momentsCommentResponse = new MomentsCommentResponse();
                BeanUtils.copyProperties(comment, momentsCommentResponse);
                return momentsCommentResponse;
            }).collect(Collectors.toList());
            response.setMomentsCommentResponses(momentsCommentResponses);
            // 朋友圈点赞
            List<MomentsLikes> momentsLikes = momentsLikesService.list(new LambdaQueryWrapper<MomentsLikes>()
                    .eq(MomentsLikes::getMomentsId, moments));
            List<MomentsLikesResponse> momentsLikesResponses = momentsLikes.stream().map(like -> {
                MomentsLikesResponse momentsLikesResponse = new MomentsLikesResponse();
                BeanUtils.copyProperties(like, momentsLikesResponse);
                return momentsLikesResponse;
            }).collect(Collectors.toList());
            response.setMomentsLikesResponses(momentsLikesResponses);
            return response;
        }).collect(Collectors.toList());

        Page<MomentsResponse> responsePage = new Page<>(momentsIPage.getCurrent(), momentsIPage.getSize(), momentsIPage.getTotal());
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
        moments.setUserId(request.getId());
        moments.setContext(request.getContext());
        moments.setImages(request.getImages());
        moments.setVideo(request.getVideo());
        moments.setCreateTime(new Date());
        momentsService.save(moments);
        return Boolean.TRUE;
    }
}
