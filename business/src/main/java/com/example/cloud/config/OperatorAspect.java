package com.example.cloud.config;

import com.example.cloud.exception.BusinessException;
import com.example.cloud.operator.login.entity.UserInfo;
import com.example.cloud.operator.login.service.UserInfoService;
import com.example.cloud.system.UserBeanRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        log.debug("调用了");
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
        Object[] args = pjp.getArgs();
        if (null != args && args.length > 0) {
            // 先检查是否有UserBeanRequest子类传参，如果有才查询请求的用户
            boolean needUser = false;
            for (Object arg : args) {
                if (arg instanceof UserBeanRequest) {
                    needUser = true;
                    break;
                }
            }
            // 需要查询当前用户信息
            if (needUser) {
                UserInfo loginUser = userInfoService.getLoginUser();
                for (Object arg : args) {
                    if (arg instanceof UserBeanRequest) {
                        UserBeanRequest request = (UserBeanRequest) arg;
                        if (null == loginUser) {
                            throw new BusinessException(403, "未登录");
                        }
                        BeanUtils.copyProperties(loginUser, request);
                        request.setUsername(loginUser.getUsername());
                    }
                }
            }
        }
        return pjp.proceed();
    }
}
