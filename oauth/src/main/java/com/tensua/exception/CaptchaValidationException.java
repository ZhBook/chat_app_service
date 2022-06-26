package com.tensua.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;


public class CaptchaValidationException extends AuthenticationException {

    @Getter
    private int errorCode;

    @Getter
    private String errorMsg;


    public CaptchaValidationException(String errorMsg){
        super(errorMsg);
        this.errorCode = 40100;
        this.errorMsg = errorMsg;
    }
}
