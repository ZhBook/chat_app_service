package com.example.cloud.data.request.friends;

import com.example.cloud.system.NoParamsUserBean;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * @author:70968 Date:2021-10-16 11:16
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HandleFriendsRequest extends NoParamsUserBean {

    Long requestId;
    Long friendId;
    String friendName;
    String friendHeadUrl;
    Integer isAgree;
}
