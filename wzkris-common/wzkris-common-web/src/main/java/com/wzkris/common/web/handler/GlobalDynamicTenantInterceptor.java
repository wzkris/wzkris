package com.wzkris.common.web.handler;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.NumberUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.utils.DynamicTenantUtil;
import com.wzkris.common.security.utils.SysUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 全局动态租户拦截器，提供切换租户的能力
 * @date : 2024/11/15 09:55
 */
@Slf4j
public class GlobalDynamicTenantInterceptor implements AsyncHandlerInterceptor {

    // 全局租户忽略
    private static final String GLOBAL_IGNORE_TENANT = "globalIgnore";

    // 动态租户ID切换
    private static final String DYNAMIC_TENANT = "dynamicTenant";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // handle login request
        if (!SysUtil.isLogin()) {
            return true;
        }

        // handle super tenant request
        if (!SysUtil.isSuperTenant()) {
            return true;
        }

        Cookie[] cookies = request.getCookies();
        String tenantId = null;
        String globalIgnore = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (StringUtil.equals(GLOBAL_IGNORE_TENANT, cookie.getName())) {
                    globalIgnore = cookie.getValue();
                }
                else if (StringUtil.equals(DYNAMIC_TENANT, cookie.getName())) {
                    tenantId = cookie.getValue();
                }
            }
        }

        if (BooleanUtil.isTrue(Boolean.valueOf(globalIgnore))) {
            DynamicTenantUtil.enableIgnore();
            request.setAttribute(GLOBAL_IGNORE_TENANT, globalIgnore);
            return true;
        }

        if (NumberUtil.isNumber(tenantId)) {
            DynamicTenantUtil.set(Long.valueOf(tenantId));
            request.setAttribute(DYNAMIC_TENANT, tenantId);
            return true;
        }


        return true;
    }

    // 请求完成后的回调
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (request.getAttribute(GLOBAL_IGNORE_TENANT) != null) {
            DynamicTenantUtil.disableIgnore();
        }
        if (request.getAttribute(DYNAMIC_TENANT) != null) {
            DynamicTenantUtil.remove();
        }
    }

    // 渲染视图后的回调
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
