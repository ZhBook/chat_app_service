package com.example.cloud.operator.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.cloud.operator.blog.entity.BlogComment;
import com.example.cloud.operator.blog.service.BlogCommentService;
import com.example.cloud.operator.blog.mapper.BlogCommentMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class BlogCommentServiceImpl extends ServiceImpl<BlogCommentMapper, BlogComment>
implements BlogCommentService{

}




