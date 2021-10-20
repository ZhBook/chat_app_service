package com.example.cloud.data.exception;

import com.example.cloud.enums.BaseResult;
import com.example.cloud.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.List;

/**
 * 用于处理业务层面的错误
 * 异常通用处理
 */
@ResponseBody
@Slf4j
public class DefaultExceptionAdvice {
    /**
     * IllegalArgumentException异常处理返回json
     * 返回状态码:400
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class})
    public BaseResult badRequestException(IllegalArgumentException e) {
        return defHandler("参数解析失败", e);
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
     * SQLException sql异常处理
     * 返回状态码:500
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({SQLException.class})
    public BaseResult handleSQLException(SQLException e) {
        return defHandler("服务运行SQLException异常", e);
    }

    /**
     * BusinessException 业务异常处理
     * 返回状态码:500
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(BusinessException.class)
    public BaseResult handleException(BusinessException e) {
        return defHandler(e.getMessage(), e);
    }

    /**
     * 所有异常统一处理
     * 返回状态码:500
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public BaseResult handleException(Exception e) {
        return defHandler("未知异常", e);
    }

    /**
     * 处理validator验证失败的异常
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResult handException(MethodArgumentNotValidException e){
        //循环处理第一个错误
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        ObjectError objectError = allErrors.get(0);
        return defHandler(objectError.getDefaultMessage(),e);
    }

    private BaseResult defHandler(String msg, Exception e) {
        log.error(msg, e);
        return BaseResult.failed(msg);
    }
}
