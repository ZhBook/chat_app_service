package com.tensua.blogservice.data.response.login;

import lombok.Data;

/**
 * @author zhooke
 * @since 2024/2/23 11:38
 **/
@Data
public class LoginUserInfoResponse {

    private Long id;

    /**
     *
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 用户类型 CHAT-聊天 BLOG-博客
     */
    private String userType;

    /**
     *
     */
    private String headImgUrl;

    /**
     *
     */
    private String EMail;

    /**
     *
     */
    private String mobile;

    /**
     *
     */
    private int sex;

}
