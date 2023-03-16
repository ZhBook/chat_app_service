package com.tensua.data.response.blog;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.base.Joiner;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author zhooke
 * @since 2022/5/12 15:25
 **/
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogReplyCommentResponse {
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
    private Long commentId;

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

    public void setIpAddress(String ipAddress) {
        String[] ipArray = StringUtils.split(ipAddress, ".");
        ipArray[2] = ipArray[2].replaceAll(".*", "*");
        ipArray[1] = ipArray[1].replaceAll(".*", "*");
        this.ipAddress = Joiner.on(".").join(ipArray);
    }
}
