package com.tensua.data.exception;

import com.tensua.data.BaseResult;
import com.tensua.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.List;

/**
 * 用于处理业务层面的错误
 * 异常通用处理
 */
@RestControllerAdvice
@Slf4j
public class DefaultExceptionAdvice {
    /**
     * IllegalArgumentException异常处理返回json
     * 处理Assert异常
     * 返回状态码:400
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class})
    public BaseResult badRequestException(IllegalArgumentException e) {
        return defHandler(e.getMessage(), e);
    }

    /**
     * AccessDeniedException异常处理返回json
     * 返回状态码:403
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({AccessDeniedException.class})
    public BaseResult badMethodExpressException(AccessDeniedException e) {
        return defHandler("没有权限请求当前方法", e);
    }

    /**
     * BusinessException 业务异常处理
     * 返回状态码:500
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({BusinessException.class})
    public BaseResult handleException(BusinessException e) {
        return defHandler(e.getMessage(), e);
    }

    /**
     * SQLException sql异常处理
     * 返回状态码:500
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({SQLException.class})
    public BaseResult handleSQLException(SQLException e) {
        return defHandler("服务运行SQLException异常", e);
    }


//    /**
//     * 所有异常统一处理
//     * 返回状态码:500
//     */
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(Exception.class)
//    public BaseResult handleException(Exception e) {
//        return defHandler("服务器异常", e);
//    }

    /**
     * 处理validator验证失败的异常
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResult handException(MethodArgumentNotValidException e) {
        //循环处理第一个错误
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        ObjectError objectError = allErrors.get(0);
        return defHandler(objectError.getDefaultMessage(), e);
    }

    private BaseResult defHandler(String msg, Exception e) {
        log.error("错误原因：{},系统错误信息：{}",msg, e);
        return BaseResult.failed(msg);
    }
}
