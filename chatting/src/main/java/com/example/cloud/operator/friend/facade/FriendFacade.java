package com.example.cloud.operator.friend.facade;

import com.example.cloud.api.UserFeignClient;
import com.example.cloud.data.BaseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author:70968 Date:2021-10-30 11:30
 */
@Service
public class FriendFacade {
    @Qualifier("userFeignFallbackClient")
    @Autowired
    private UserFeignClient userFeignClient;

    public Boolean addFriend(Long friendId, String message) {
        BaseResult<Boolean> result = userFeignClient.addFriend(friendId,message);
        return result.getData();
    }
}
