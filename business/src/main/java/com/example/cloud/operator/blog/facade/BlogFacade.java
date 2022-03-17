package com.example.cloud.operator.blog.facade;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.cloud.data.response.blogconfig.BlogConfigResponse;
import com.example.cloud.enums.StateEnum;
import com.example.cloud.operator.blog.entity.BlogMenus;
import com.example.cloud.operator.blog.service.BlogMenusService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhooke
 * @since 2022/3/8 14:41
 **/
@Service
public class BlogFacade {
    @Autowired
    private BlogMenusService blogMenusService;

    /**
     * 获取后台菜单列表
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
}
