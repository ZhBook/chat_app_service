package com.tensua.blogservice.data.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

public class TokenValidationException extends AuthenticationException {

    @Getter
    private int errorCode;

    @Getter
    private String errorMsg;


    public TokenValidationException(String errorMsg){
        super(errorMsg);
        this.errorCode = 40100;
        this.errorMsg = errorMsg;
    }
}
