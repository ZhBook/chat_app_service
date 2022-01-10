package com.example.cloud.operator.moments.facade;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cloud.data.request.moments.MomentsRequest;
import com.example.cloud.data.response.moments.MomentsResponse;
import com.example.cloud.enums.StateEnum;
import com.example.cloud.operator.friends.entity.UserRelation;
import com.example.cloud.operator.friends.service.UserRelationService;
import com.example.cloud.operator.moments.entity.Moments;
import com.example.cloud.operator.moments.entity.MomentsComment;
import com.example.cloud.operator.moments.service.MomentsCommentService;
import com.example.cloud.operator.moments.service.MomentsLikesService;
import com.example.cloud.operator.moments.service.MomentsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public IPage<MomentsResponse> getMoments(MomentsRequest request) {
        Page<Moments> page = new Page<>(request.getPageIndex(), request.getPageSize());
        List<Long> ids = new ArrayList<>();
        ids.add(request.getId());

        // 查出所有的好友
        ids.addAll(
                userRelationService.list(new LambdaQueryWrapper<UserRelation>()
                                .eq(UserRelation::getUserId, request.getId()))
                        .stream().map(UserRelation::getFriendId).collect(Collectors.toList())
        );

        // 查出好友的前几条朋友圈信息
        IPage<Moments> momentsIPage = momentsService.page(page, new LambdaQueryWrapper<Moments>()
                .in(Moments::getUserId, ids)
                .eq(Moments::getIsDelete, StateEnum.ENABLED)
                .orderByDesc(Moments::getCreateTime));
        List<Moments> momentsList = momentsIPage.getRecords();

        ArrayList<MomentsResponse> responseLike = new ArrayList<>();
        momentsList.forEach(moments -> {
            MomentsResponse response = new MomentsResponse();
            Long momentsId = moments.getId();
            // 朋友圈评论
            List<MomentsComment> commentLists = momentsCommentService.list(new LambdaQueryWrapper<MomentsComment>()
                    .eq(MomentsComment::getMomentsId, momentsId)
                    .eq(MomentsComment::getIsDelete,StateEnum.ENABLED));

            response.setMomentsLikesResponses();
            // 朋友圈点赞


        });
        return null;
    }
}
