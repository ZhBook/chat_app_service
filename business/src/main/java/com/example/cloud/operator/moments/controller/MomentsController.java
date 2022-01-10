package com.example.cloud.operator.moments.controller;

import com.example.cloud.data.PageResult;
import com.example.cloud.data.request.moments.MomentsRequest;
import com.example.cloud.data.response.moments.MomentsResponse;
import com.example.cloud.operator.moments.facade.MomentsFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhooke
 * @since 2022/1/10 09:29
 **/
@RestController
@RequestMapping("/moments")
public class MomentsController {

    @Autowired
    private MomentsFacade momentsFacade;

    @GetMapping
    public PageResult<MomentsResponse> getMoments(MomentsRequest request) {
        return PageResult.pageSuccess(momentsFacade.getMoments(request));
    }
}
