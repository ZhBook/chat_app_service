package com.tensua.operator.blog.facade;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.BetweenFormater;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tensua.constant.RedisConstants;
import com.tensua.data.request.blog.*;
import com.tensua.data.response.blog.*;
import com.tensua.enums.BlogEnums;
import com.tensua.enums.IsDeleteEnum;
import com.tensua.enums.StateEnum;
import com.tensua.exception.BusinessException;
import com.tensua.operator.blog.entity.*;
import com.tensua.operator.blog.service.*;
import com.tensua.operator.file.entity.FileInfo;
import com.tensua.operator.file.service.FileInfoService;
import com.tensua.operator.login.entity.UserInfo;
import com.tensua.operator.login.service.UserInfoService;
import com.tensua.operator.utils.IpAddressUtil;
import com.tensua.operator.utils.PushUtil;
import com.tensua.operator.utils.WebUtil;
import com.tensua.system.NoParamsBlogUserRequest;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author zhooke
 * @since 2022/3/8 14:41
 **/
@Service
@Slf4j
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

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private BlogConfigService blogConfigService;

    @Autowired
    private FileInfoService fileInfoService;

    @Autowired
    private ReplyCommentService replyCommentService;

    final private static Integer tagMax = 10;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

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
        Long userId = request.getUserId();
        IPage<BlogList> page = new Page<>(request.getPageIndex(), request.getPageSize());
        IPage<BlogList> listIPage = blogListService.page(page, new LambdaQueryWrapper<BlogList>()
                .eq(Objects.nonNull(id), BlogList::getId, id)
                .eq(Objects.nonNull(userId), BlogList::getCreateUserId, userId)
                .eq(Objects.nonNull(isOriginal), BlogList::getIsOriginal, isOriginal)
                .eq(BlogList::getIsDraft, 0)
                .like(StringUtils.isNotBlank(title), BlogList::getTitle, title)
                .eq(Objects.nonNull(isPrivate), BlogList::getIsPrivate, isPrivate)
                .eq(BlogList::getIsDelete, IsDeleteEnum.NO.getCode())
                .orderByDesc(BlogList::getIsTop)
                .orderByDesc(BlogList::getCreateDate));
        List<BlogList> blogLists = listIPage.getRecords();
        List<Long> BlogIds = blogLists.stream().map(BlogList::getId).collect(Collectors.toList());
        List<Long> userIds = blogLists.stream().map(BlogList::getCreateUserId).collect(Collectors.toList());

        // 获取所有评论数量
        HashMap<String, String> commentHashMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(BlogIds)) {
            List<Map<String, Object>> commentListMap = blogCommentService.listMaps(new QueryWrapper<BlogComment>()
                    .select("blog_id as blogId, count(1) as total ")
                    .in("blog_id", BlogIds)
                    .eq("is_delete", IsDeleteEnum.NO.getCode())
                    .groupBy("blog_id"));
            commentListMap.forEach(map -> commentHashMap.put(map.get("blogId").toString(), map.get("total").toString()));
        }
        // 获取所有的用户名
        HashMap<String, String> userHashMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(userIds)) {
            List<Map<String, Object>> userListMap = userInfoService.listMaps(new QueryWrapper<UserInfo>()
                    .select("id,nickname")
                    .in("id", userIds));
            userListMap.forEach(map -> userHashMap.put(map.get("id").toString(), map.get("nickname").toString()));
        }

        List<BlogListResponse> collect = blogLists.stream().map(blog -> {
            BlogListResponse blogListResponse = new BlogListResponse();
            BeanUtils.copyProperties(blog, blogListResponse);
            int commentTotal = Integer.parseInt(Objects.isNull(commentHashMap.get(blog.getId().toString())) ? "0" : commentHashMap.get(blog.getId().toString()));
            blogListResponse.setCommentNum(commentTotal);
            blogListResponse.setAuthorName(userHashMap.get(blog.getCreateUserId().toString()));
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
        blogList.setId(null);
        blogList.setCreateDate(date);
        blogList.setCreateUserId(request.getId());
        blogList.setUpdateDate(date);
        blogList.setUpdateUserId(request.getId());
        blogList.setVersion(1);
        if (StringUtils.isBlank(blogList.getPicture())) {
            List<FileInfo> pictures = fileInfoService.getBlogCoverPicture();
            ThreadLocalRandom random = ThreadLocalRandom.current();
            blogList.setPicture(pictures.get(random.nextInt(pictures.size())).getUrl());
        }
        blogListService.save(blogList);

        if (StringUtils.isNotBlank(request.getTags())) {
            String[] tags = request.getTags().split(",");
            List<BlogTag> blogTags = blogTagService.list(new LambdaQueryWrapper<BlogTag>()
                    .in(BlogTag::getId, tags));
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
        HttpServletRequest httpServletRequest = WebUtil.getRequest();
        String ip = IpAddressUtil.get(httpServletRequest);
        BlogList blog = blogListService.getOne(new LambdaQueryWrapper<BlogList>()
                .eq(BlogList::getId, blogId)
                .eq(BlogList::getIsDelete, IsDeleteEnum.NO.getCode()));
        if (Objects.isNull(blog)) {
            throw new BusinessException("博客不存在");
        }
        Date redisResult = (Date) redisTemplate.opsForValue().get(String.format(RedisConstants.BLOG_READ_COUNT + "%s:%s", blogId, ip));
        BlogResponse blogResponse = new BlogResponse();
        BeanUtils.copyProperties(blog, blogResponse);
        if (Objects.isNull(redisResult)) {
            blog.setBlogBrowse(blog.getBlogBrowse() + 1);
            redisTemplate.opsForValue().set(String.format(RedisConstants.BLOG_READ_COUNT + "%s:%s", blogId, ip), new Date(), 10, TimeUnit.HOURS);
        }
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
        Page<BlogCommentListResponse> responsePage = new Page<>(request.getPageIndex(), request.getPageSize());

        Integer commentType = request.getCommentType();
        page = blogCommentService.page(page, new LambdaQueryWrapper<BlogComment>()
                .eq(BlogComment::getBlogId, request.getBlogId())
                .eq(BlogComment::getType, Objects.isNull(commentType) ? BlogEnums.CommentType.BLOG_COMMENT.getType() : commentType)
                .eq(BlogComment::getIsDelete, IsDeleteEnum.NO.getCode())
                .orderByDesc(BlogComment::getCreateDate));
        List<BlogComment> blogCommentList = page.getRecords();

        if (CollectionUtils.isEmpty(blogCommentList)) {
            return responsePage;
        }

        List<Long> commentIds = blogCommentList.stream().map(BlogComment::getId).collect(Collectors.toList());

        List<ReplyComment> replyComments = replyCommentService.list(new LambdaQueryWrapper<ReplyComment>()
                .in(ReplyComment::getCommentId, commentIds)
                .eq(ReplyComment::getIsDelete, IsDeleteEnum.NO.getCode()));

        List<BlogReplyCommentResponse> commentResponseList = replyComments.stream().map(reply -> {
            BlogReplyCommentResponse blogReplyCommentResponse = new BlogReplyCommentResponse();
            BeanUtils.copyProperties(reply, blogReplyCommentResponse);
            return blogReplyCommentResponse;
        }).collect(Collectors.toList());

        List<BlogCommentListResponse> responses = blogCommentList.stream().map(comment -> {
            BlogCommentListResponse blogCommentListResponse = new BlogCommentListResponse();
            BeanUtils.copyProperties(comment, blogCommentListResponse);
            List<BlogReplyCommentResponse> replyCommentResponseList = commentResponseList.stream().filter(reply -> Objects.equals(reply.getCommentId(), comment.getId())).collect(Collectors.toList());
            blogCommentListResponse.setBlogReplyCommentList(replyCommentResponseList);
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
        HttpServletRequest httpServletRequest = WebUtil.getRequest();
        String ip = IpAddressUtil.get(httpServletRequest);
        blogPrevention(ip);

        Long blogId = request.getBlogId();
        Integer commentType = request.getCommentType();
        if (BlogEnums.CommentType.SYSTEM_COMMENT.getType().equals(commentType)) {
            Assert.notNull(blogConfigService.getById(blogId), "评论的文章不存在");
        } else {
            Assert.notNull(blogListService.getById(blogId), "评论的文章不存在");
        }
        String comment = request.getComment();

        Date date = new Date();
        BlogComment blogComment = new BlogComment();
        blogComment.setIpAddress(ip);
        blogComment.setBrowserModel(request.getBrowserModel());
        blogComment.setBlogAuthorId(request.getBlogAuthorId());
        blogComment.setBlogId(blogId);
        blogComment.setComment(comment);
        blogComment.setCreateDate(date);
        blogComment.setType(request.getCommentType());
        blogComment.setCreateUserId(request.getUserId());
        blogComment.setCreateUserName(request.getNickname());
        blogComment.setEmail(request.getEMail());
        blogComment.setHeadImgUrl(request.getHeadImgUrl());

        String msg = String.format("ip：%s\n发布时间：%s\n内容：%s\n", ip, DateUtil.format(date, "yyyy-MM-dd HH:mm:ss"), comment);
        PushUtil.pushDingTalk(msg);
        return blogCommentService.save(blogComment);
    }

    /**
     * ip防恶意评论校验规则
     *
     * @param ip
     */
    private void blogPrevention(String ip) {
        /**
         * 防恶意评论校验规则
         *  1.当天：最多500条评论次数
         *  2.小时：最多30条
         *  3.分钟：最多10条
         *  4.秒：最多3条
         */
        Date now = new Date();
        Integer dayTime = (Integer) redisTemplate.opsForValue().get(RedisConstants.BLOG_PREVENTION_DAY + DateUtil.format(now, "yyyy-MM-dd") + ":" + ip);
        Integer hourTime = (Integer) redisTemplate.opsForValue().get(RedisConstants.BLOG_PREVENTION_HOUR + DateUtil.format(now, "yyyy-MM-dd HH") + ":" + ip);
        Integer minuteTime = (Integer) redisTemplate.opsForValue().get(RedisConstants.BLOG_PREVENTION_MINUTE + DateUtil.format(now, "yyyy-MM-dd HH:mm") + ":" + ip);
        Integer secondTime = (Integer) redisTemplate.opsForValue().get(RedisConstants.BLOG_PREVENTION_SECOND + DateUtil.format(now, "yyyy-MM-dd HH:mm:ss") + ":" + ip);
        if (Objects.nonNull(dayTime) && dayTime > 500) {
            throw new BusinessException("每天最多500条评论");
        }
        if (Objects.nonNull(hourTime) && hourTime > 30) {
            throw new BusinessException("每小时最多30条评论");
        }
        if (Objects.nonNull(minuteTime) && minuteTime > 10) {
            throw new BusinessException("每分钟最多10条评论");
        }
        if (Objects.nonNull(secondTime) && secondTime > 3) {
            throw new BusinessException("每秒最多3条评论");
        }
        redisTemplate.opsForValue().increment(RedisConstants.BLOG_PREVENTION_DAY + DateUtil.format(now, "yyyy-MM-dd") + ":" + ip, 1);
        redisTemplate.opsForValue().increment(RedisConstants.BLOG_PREVENTION_HOUR + DateUtil.format(now, "yyyy-MM-dd HH") + ":" + ip, 1);
        redisTemplate.opsForValue().increment(RedisConstants.BLOG_PREVENTION_MINUTE + DateUtil.format(now, "yyyy-MM-dd HH:mm") + ":" + ip, 1);
        redisTemplate.opsForValue().increment(RedisConstants.BLOG_PREVENTION_SECOND + DateUtil.format(now, "yyyy-MM-dd HH:mm:ss") + ":" + ip, 1);

        DateTime end = DateUtil.endOfMonth(now);
        redisTemplate.expireAt(RedisConstants.BLOG_PREVENTION_DAY + DateUtil.format(now, "yyyy-MM-dd") + ":" + ip, end);
        redisTemplate.expireAt(RedisConstants.BLOG_PREVENTION_HOUR + DateUtil.format(now, "yyyy-MM-dd HH") + ":" + ip, end);
        redisTemplate.expireAt(RedisConstants.BLOG_PREVENTION_MINUTE + DateUtil.format(now, "yyyy-MM-dd HH:mm") + ":" + ip, end);
        redisTemplate.expireAt(RedisConstants.BLOG_PREVENTION_SECOND + DateUtil.format(now, "yyyy-MM-dd HH:mm:ss") + ":" + ip, end);
    }

    /**
     * 获取最新20条评论列表
     *
     * @param blogAuthorId
     * @return
     */
    public List<BlogCommentListResponse> blogCommentNewest(Long blogAuthorId) {
        List<BlogComment> records = blogCommentService.list(new LambdaQueryWrapper<BlogComment>()
                .eq(BlogComment::getBlogAuthorId, blogAuthorId)
                .eq(BlogComment::getType, BlogEnums.CommentType.BLOG_COMMENT.getType())
                .eq(BlogComment::getIsDelete, IsDeleteEnum.NO.getCode())
                .orderByDesc(BlogComment::getCreateDate)
                .last("limit 20"));
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
                .eq(BlogList::getIsDelete, IsDeleteEnum.NO.getCode())
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
        Long count = blogTagService.count(new LambdaQueryWrapper<BlogTag>()
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
        String userId = "";
        if (Objects.isNull(request) || Objects.isNull(request.getUserId())) {
            BlogConfig blog_user_id = blogConfigService.getOne(new LambdaQueryWrapper<BlogConfig>()
                    .eq(BlogConfig::getTypeName, "blog_user_id")
                    .eq(BlogConfig::getIsDelete, IsDeleteEnum.NO.getCode()));
            userId = blog_user_id.getTypeValue();
        } else {
            userId = request.getUserId().toString();
        }
        List<BlogTag> list = blogTagService.list(new LambdaQueryWrapper<BlogTag>()
                .eq(BlogTag::getCreateUserId, userId)
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
        // 从blog配置文件中获取默认blog user id
        if (Objects.isNull(userId)) {
            BlogConfig blog_user_id = blogConfigService.getOne(new LambdaQueryWrapper<BlogConfig>()
                    .eq(BlogConfig::getTypeName, "blog_user_id")
                    .eq(BlogConfig::getIsDelete, IsDeleteEnum.NO.getCode()));
            UserInfo userInfo = userInfoService.getById(blog_user_id.getTypeValue());
            userId = userInfo.getId();
            BeanUtils.copyProperties(userInfo, request);
            request.setUserId(userInfo.getId());
        }
        Date createTime = request.getCreateTime();
        String betweenDay = DateUtil.formatBetween(new Date(), createTime, BetweenFormater.Level.HOUR);
        response.setRunningDay(betweenDay);
        Long blogCount = blogListService.count(new LambdaQueryWrapper<BlogList>()
                .eq(BlogList::getCreateUserId, userId)
                .eq(BlogList::getIsDelete, IsDeleteEnum.NO.getCode()));
        Long commentCount = blogCommentService.count(new LambdaQueryWrapper<BlogComment>()
                .eq(BlogComment::getBlogAuthorId, userId)
                .eq(BlogComment::getIsDelete, IsDeleteEnum.NO.getCode()));

        response.setNickname(request.getNickname());
        response.setCommentCount(commentCount);
        response.setBlogCount(blogCount);
        // todo 添加粉丝数量
        response.setFollowCount(0L);
        return response;
    }

    /**
     * 删除tag标签
     *
     * @param tagId
     * @param request
     * @return
     */
    @Transactional
    public Boolean delTag(Long tagId, NoParamsBlogUserRequest request) {
        BlogTag blogTag = blogTagService.getOne(new LambdaQueryWrapper<BlogTag>()
                .eq(BlogTag::getId, tagId)
                .eq(BlogTag::getCreateUserId, request.getUserId())
                .eq(BlogTag::getIsDelete, IsDeleteEnum.NO.getCode()));
        if (Objects.isNull(blogTag)) {
            throw new BusinessException("标签不存在");
        }
        blogTag.setIsDelete(IsDeleteEnum.YES.getCode());
        blogTagService.updateById(blogTag);
        blogTagRelationService.remove(new LambdaQueryWrapper<BlogTagRelation>()
                .eq(BlogTagRelation::getTagId, tagId)
                .eq(BlogTagRelation::getCreateUserId, request.getUserId()));
        return Boolean.TRUE;
    }

    /**
     * 删除blog
     *
     * @param blogId
     * @param request
     * @return
     */
    public Boolean deleteBlogById(Long blogId, NoParamsBlogUserRequest request) {
        BlogList blog = blogListService.getOne(new LambdaQueryWrapper<BlogList>()
                .eq(BlogList::getId, blogId)
                .eq(BlogList::getCreateUserId, request.getUserId())
                .eq(BlogList::getIsDelete, IsDeleteEnum.NO.getCode()));
        if (Objects.isNull(blog)) {
            throw new BusinessException("删除博客失败");
        }
        blog.setUpdateDate(new Date());
        blog.setUpdateUserId(request.getUserId());
        blog.setIsDelete(IsDeleteEnum.YES.getCode());
        return blogListService.updateById(blog);
    }

    /**
     * 获取博客草稿箱
     *
     * @param request
     * @return
     */
    public IPage<BlogDraftListResponse> getBlogDraftList(BlogDraftRequest request) {
        IPage<BlogList> page = new Page<>(request.getPageIndex(), request.getPageSize());
        IPage<BlogList> listIPage = blogListService.page(page, new LambdaQueryWrapper<BlogList>()
                .eq(BlogList::getIsDraft, 1)
                .eq(BlogList::getIsDelete, IsDeleteEnum.NO.getCode())
                .eq(BlogList::getCreateUserId, request.getUserId())
                .orderByDesc(BlogList::getCreateDate));
        List<BlogList> blogLists = listIPage.getRecords();

        List<BlogDraftListResponse> draftListResponses = blogLists.stream().map(blog -> {
            BlogDraftListResponse blogDraftListResponse = new BlogDraftListResponse();
            BeanUtils.copyProperties(blog, blogDraftListResponse);
            return blogDraftListResponse;
        }).collect(Collectors.toList());
        Page<BlogDraftListResponse> responsePage = new Page<>(request.getPageIndex(), request.getPageSize());
        responsePage.setRecords(draftListResponses);
        responsePage.setTotal(listIPage.getTotal());
        return responsePage;
    }

    /**
     * 更新blog
     *
     * @param request
     * @return
     */
    @Transactional
    public Boolean updateBlog(BlogRequest request) {
        if (Objects.isNull(request.getBlogId())) {
            throw new BusinessException("博客id不能为空");
        }
        BlogList blogList = blogListService.getById(request.getBlogId());
        if (Objects.isNull(blogList)) {
            throw new BusinessException("博客不存在");
        }
        Date date = new Date();
        BeanUtils.copyProperties(request, blogList);
        blogList.setUpdateDate(date);
        blogList.setUpdateUserId(request.getId());
        blogList.setVersion(blogList.getVersion() + 1);
        if (StringUtils.isBlank(request.getPicture())) {
            List<FileInfo> pictures = fileInfoService.getBlogCoverPicture();
            ThreadLocalRandom random = ThreadLocalRandom.current();
            blogList.setPicture(pictures.get(random.nextInt(pictures.size())).getUrl());
        }
        blogListService.updateById(blogList);

        if (StringUtils.isNotBlank(request.getTags())) {
            String[] tags = request.getTags().split(",");
            List<BlogTag> blogTags = blogTagService.list(new LambdaQueryWrapper<BlogTag>()
                    .in(BlogTag::getId, tags));
            List<BlogTagRelation> relations = blogTags.stream().map(tag -> {
                BlogTagRelation tagRelation = new BlogTagRelation();
                tagRelation.setTagId(tag.getId());
                tagRelation.setBlogId(blogList.getId());
                tagRelation.setTagName(tag.getName());
                tagRelation.setCreateUserId(request.getAuthorId());
                tagRelation.setCreateDatetime(date);
                return tagRelation;
            }).collect(Collectors.toList());
            blogTagRelationService.remove(new LambdaQueryWrapper<BlogTagRelation>()
                    .eq(BlogTagRelation::getBlogId, blogList.getId()));
            blogTagRelationService.saveBatch(relations);
        }
        return Boolean.TRUE;
    }

    /**
     * 回复评论
     *
     * @param request
     * @return
     */
    public Boolean replyComment(BlogReplyCommentRequest request) {
        HttpServletRequest httpServletRequest = WebUtil.getRequest();
        String ip = IpAddressUtil.get(httpServletRequest);
        ReplyComment replyComment = new ReplyComment();
        BeanUtils.copyProperties(request, replyComment);
        replyComment.setHeadImgUrl(request.getHeadImgUrl());
        replyComment.setIpAddress(ip);
        replyComment.setCreateDate(new Date());
        replyComment.setCreateUserId(request.getUserId());
        replyComment.setCreateUserName(request.getUsername());
        return replyCommentService.save(replyComment);
    }
}
