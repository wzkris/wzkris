package com.wzkris.common.log.annotation.aspect;

import cn.hutool.core.text.StrPool;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.enums.BizCode;
import com.wzkris.common.log.annotation.DeprecatedAPI;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 废弃API切面
 *
 * @author wzkris
 */
@Slf4j
@Aspect
public class DeprecatedAPIAspect {

    /**
     * 若是API则在调用前返回，方法则执行
     *
     * @param joinPoint 切点
     */
    @Around(value = "@annotation(deprecatedAPI)")
    public Object doBeforeAPI(ProceedingJoinPoint joinPoint, DeprecatedAPI deprecatedAPI) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RequestMapping requestMapping = AnnotationUtils.findAnnotation(signature.getMethod(), RequestMapping.class);

        if (requestMapping != null) {
            return Result.resp(BizCode.DEPRECATED_API);
        }

        log.warn(
                "废弃方法被调用：{}",
                signature.getDeclaringTypeName()
                        + StrPool.AT
                        + signature.getMethod().getName());
        return joinPoint.proceed();
    }
}
