package com.example.cloud.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class Users implements Serializable {
    private long id;
    private String username;
    private String password;
}
