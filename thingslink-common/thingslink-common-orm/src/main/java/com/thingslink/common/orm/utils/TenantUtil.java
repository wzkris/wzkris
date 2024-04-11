package com.thingslink.common.orm.utils;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.baomidou.mybatisplus.core.plugins.IgnoreStrategy;
import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 租户工具
 * @date : 2023/12/11 10:54
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TenantUtil {

    private static final ThreadLocal<Long> LOCAL_DYNAMIC_TENANT = new TransmittableThreadLocal<>();

    /**
     * 开启忽略租户(开启后需手动调用 {@link #disableIgnore()} 关闭)
     * 开启后则不会再走租户拦截器
     */
    public static void enableIgnore() {
        InterceptorIgnoreHelper.handle(IgnoreStrategy.builder().tenantLine(true).build());
    }

    /**
     * 关闭忽略租户
     */
    public static void disableIgnore() {
        InterceptorIgnoreHelper.clearIgnoreStrategy();
    }

    /**
     * 设置动态租户，若设置了值则一定会走租户拦截
     * 当前线程内生效
     */
    public static void setDynamic(Long tenantId) {
        LOCAL_DYNAMIC_TENANT.set(tenantId);
    }

    /**
     * 获取动态租户
     * 当前线程内生效
     */
    public static Long getDynamic() {
        return LOCAL_DYNAMIC_TENANT.get();
    }

    /**
     * 清除动态租户
     */
    public static void clearDynamic() {
        LOCAL_DYNAMIC_TENANT.remove();
    }

}
