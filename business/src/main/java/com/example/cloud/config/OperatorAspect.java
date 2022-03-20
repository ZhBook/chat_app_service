package com.example.cloud.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.cloud.exception.BusinessException;
import com.example.cloud.operator.login.entity.UserInfo;
import com.example.cloud.operator.login.service.UserInfoService;
import com.example.cloud.operator.utils.IpAddressUtil;
import com.example.cloud.operator.utils.WebUtil;
import com.example.cloud.system.BlogUserRequest;
import com.example.cloud.system.UserBeanRequest;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 操作人切面
 */
@Component
@Aspect
@Slf4j
public class OperatorAspect {

    @Autowired
    private UserInfoService userInfoService;

    @Pointcut("execution(* com.example.cloud.operator.*.controller..*.*(..))")
    public void pointCut() {
        log.debug("切面被调用");
    }

    /**
     * 捕获所有controller方法调用，遇到有UserBeanRequest子类传参的，获取当前请求的用户，自动设置UserBeanRequest中的操作人信息
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = WebUtil.getRequest();
        String ip = IpAddressUtil.get(request);

        Object[] args = pjp.getArgs();
        long start = System.currentTimeMillis();

        List<Object> requestList = filterArgs(args);

        if (null != args && args.length > 0) {
            // 先检查是否有UserBeanRequest子类传参，如果有才查询请求的用户
            boolean needUser = false;
            for (Object arg : args) {
                if (arg instanceof UserBeanRequest) {
                    needUser = true;
                    break;
                } else if (arg instanceof BlogUserRequest) {
                    needUser = true;
                    break;
                }
            }
            // 需要查询当前用户信息
            if (needUser) {
                for (Object arg : args) {
                    if (arg instanceof UserBeanRequest) {
                        UserInfo loginUser = userInfoService.getLoginUser();
                        UserBeanRequest userBeanRequest = (UserBeanRequest) arg;
                        if (null == loginUser) {
                            throw new BusinessException(403, "未登录");
                        }
                        BeanUtils.copyProperties(loginUser, userBeanRequest);
                    } else if (arg instanceof BlogUserRequest) {
                        UserInfo loginUser = userInfoService.getLoginUser();
                        BlogUserRequest blogUserRequest = (BlogUserRequest) arg;
                        blogUserRequest.setUserId(loginUser.getId());
                        if (null == loginUser) {
                            throw new BusinessException(403, "未登录");
                        }
                        BeanUtils.copyProperties(loginUser, blogUserRequest);
                    }
                }
            }
        }
        //返回日志
        Object proceed = null;
        JSONObject jsonObject = null;
        try {
            proceed = pjp.proceed();
            jsonObject = JSON.parseObject(JSON.toJSONString(proceed));
            String data = jsonObject.getString("data");
            if (StringUtils.isNotBlank(data) && data.length() > 200) {
                jsonObject.put("data", data.substring(0, 200));
            }
        } catch (Exception e) {
            long end = System.currentTimeMillis();
            String requestJsonStr = JSON.toJSONString(requestList);
            log.info(" 处理请求 | IP:{} | url:{} | 耗时: {}ms | method:{} | args:{} | 返回异常:"
                    , ip
                    , request.getRequestURI()
                    , end - start
                    , request.getMethod()
                    , requestJsonStr
            );
            throw e;
        }
        String requestJsonStr = JSON.toJSONString(requestList);
        long end = System.currentTimeMillis();
        log.info(" 处理请求 | IP:{} | url:{} | 耗时: {}ms | method:{} | args:{} | 返回:{}",
                ip,
                request.getRequestURI(),
                end - start,
                request.getMethod(),
                jsonObject
        );
        return proceed;
    }

    /**
     * 接口参数过滤HttpServletRequest、HttpServletResponse、MultipartFile
     *
     * @param args
     * @return
     */
    private List<Object> filterArgs(Object[] args) {
        return Lists.newArrayList(args).stream()
                .filter(arg -> !(arg instanceof HttpServletRequest || arg instanceof HttpServletResponse
                        || arg instanceof MultipartFile))
                .collect(Collectors.toList());
    }
}
