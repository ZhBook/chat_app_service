package com.example.cloud.exception;

import lombok.Getter;

/**
 * @author:70968 Date:2021-10-06 18:31
 */
public class BusinessException extends RuntimeException{
    @Getter
    private int errorCode;

    public BusinessException(String msg){
        super(msg);
        this.errorCode = 500;
    }
}
