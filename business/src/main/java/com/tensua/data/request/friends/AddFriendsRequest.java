package com.tensua.data.request.friends;

import com.tensua.system.NoParamsUserBean;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * @author:70968 Date:2021-10-16 10:43
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddFriendsRequest extends NoParamsUserBean {

    Long friendId;

    String message;
}
