package com.wzkris.common.web.handler;

import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.utils.DynamicTenantUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 请求参数拦截
 */
public class RequestParamsInterceptor implements AsyncHandlerInterceptor {

    private static final String COOKIE_TENANT_ID = "dynamicTenant";

    private static final String REQUEST_ID_HEADER = "custom_request_id";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (StringUtil.equals(COOKIE_TENANT_ID, cookie.getName())) {
                    request.setAttribute(REQUEST_ID_HEADER, "r");
                    DynamicTenantUtil.set(Long.valueOf(cookie.getValue()));
                }
            }
        }
        return true;
    }

    // 请求完成后的回调
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Object attribute = request.getAttribute(REQUEST_ID_HEADER);
        if (attribute != null) {
            DynamicTenantUtil.remove();
        }
    }

    // 渲染视图后的回调
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
