package com.example.cloud.operator.moments.controller;

import com.example.cloud.data.BaseResult;
import com.example.cloud.data.PageResult;
import com.example.cloud.data.request.moments.MomentsPageRequest;
import com.example.cloud.data.request.moments.MomentsRequest;
import com.example.cloud.data.response.moments.MomentsResponse;
import com.example.cloud.operator.moments.facade.MomentsFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhooke
 * @since 2022/1/10 09:29
 **/
@RestController
@RequestMapping("/moments")
public class MomentsController {

    @Autowired
    private MomentsFacade momentsFacade;

    /**
     * 获取朋友圈信息
     * @param request
     * @return
     */
    @PostMapping("/all")
    public PageResult<MomentsResponse> getMoments(MomentsPageRequest request) {
        return PageResult.pageSuccess(momentsFacade.getMoments(request));
    }

    /**
     * 发布朋友圈信息
     * @return
     */
    @PostMapping("/publish")
    public BaseResult<Boolean> publishMoments(@RequestBody MomentsRequest request){
        return BaseResult.succeed(momentsFacade.publishMoments(request));
    }
}
