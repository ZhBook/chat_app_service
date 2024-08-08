package com.tensua.blogservice.data.exception;

/**
 * @author:70968 Date:2021-10-06 18:31
 */
public class BusinessException extends BasicException{

    public BusinessException(int code,String msg){
        super(code,msg);
    }

    public BusinessException(String msg){
        super(msg);
    }
}
