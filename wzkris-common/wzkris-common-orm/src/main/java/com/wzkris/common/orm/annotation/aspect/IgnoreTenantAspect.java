package com.wzkris.common.orm.annotation.aspect;


import com.wzkris.common.orm.annotation.IgnoreTenant;
import com.wzkris.common.orm.utils.DynamicTenantUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 忽略租户
 */
@Slf4j
@Aspect
public class IgnoreTenantAspect {

    @Around("@annotation(IgnoreTenant) || @within(IgnoreTenant)")
    public Object around(ProceedingJoinPoint point, IgnoreTenant IgnoreTenant) throws Throwable {
        return DynamicTenantUtil.ignoreWithThrowable(point::proceed);
    }

}
