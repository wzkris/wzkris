package com.wzkris.common.web.handler;

import cn.hutool.core.util.NumberUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.utils.DynamicTenantUtil;
import com.wzkris.common.security.utils.LoginUserUtil;
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

    // 动态租户ID切换
    private static final String DYNAMIC_TENANT = "dynamicTenant";

    private static final String IGNORE_TYPE = "all";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // handle login request
        if (!LoginUserUtil.isLogin()) {
            return true;
        }

        // handle super tenant request
        if (!LoginUserUtil.isSuperTenant()) {
            return true;
        }

        String dynamicTenant = request.getHeader(DYNAMIC_TENANT);

        if (NumberUtil.isNumber(dynamicTenant)) {
            DynamicTenantUtil.set(Long.valueOf(dynamicTenant));
        }
        else if (StringUtil.equals(dynamicTenant, IGNORE_TYPE)) {
            DynamicTenantUtil.enableIgnore();
        }
        else if (dynamicTenant != null) {
            return false;// 不是合法请求头数据则直接返回
        }

        request.setAttribute(DYNAMIC_TENANT, dynamicTenant);
        return true;
    }

    // 请求完成后的回调
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Object dynamicTenant = request.getAttribute(DYNAMIC_TENANT);
        if (dynamicTenant != null) {
            if (NumberUtil.isNumber(dynamicTenant.toString())) {
                DynamicTenantUtil.remove();
            }
            else if (StringUtil.equals(dynamicTenant.toString(), IGNORE_TYPE)) {
                DynamicTenantUtil.disableIgnore();
            }
        }
    }

    // 渲染视图后的回调
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
