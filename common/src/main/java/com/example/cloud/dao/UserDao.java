package com.example.cloud.dao;

import com.example.cloud.pojo.Users;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
    Users getUsers(String username);
}
