package com.example.cloud.data.response.friends;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author:70968 Date:2021-10-16 09:36
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FriendsResponse {
    Long id;
    Long userId;
    Long friendId;
    String friendNickname;
    String friendHeadUrl;
}
