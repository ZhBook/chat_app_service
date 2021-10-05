package com.example.cloud.service.Impl;

import com.example.cloud.dao.UserDao;
import com.example.cloud.pojo.Users;
import com.example.cloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public Users userInfo(String username) {
        return userDao.getUsers(username);
    }
}
