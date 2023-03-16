package com.tensua.data.response.blog;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.Joiner;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @author: zhooke
 * @create: 2022-03-20 17:14
 * @description:
 **/
@Data
public class BlogCommentListResponse {
    /**
     *
     */
    private Long id;

    /**
     *
     */
    private Long blogId;

    /**
     *
     */
    private String comment;

    /**
     *
     */
    private String email;

    /**
     *
     */
    private Long createUserId;

    /**
     *
     */
    private String createUserName;

    /**
     * blog所属作者id
     */
    private Long blogAuthorId;
    /**
     *
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    /**
     *
     */
    private String headImgUrl;

    /**
     * ip地址
     */
    private String ipAddress;

    /**
     * 浏览器标识
     */
    private String browserModel;

    List<BlogReplyCommentResponse> blogReplyCommentList;

    public void setIpAddress(String ipAddress) {
        String[] ipArray = StringUtils.split(ipAddress, ".");
        ipArray[2] = ipArray[2].replaceAll(".*", "*");
        ipArray[1] = ipArray[1].replaceAll(".*", "*");
        this.ipAddress = Joiner.on(".").join(ipArray);
    }
}
