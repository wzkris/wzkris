package com.wzkris.common.orm.filter;

import com.wzkris.common.core.constant.HeaderConstants;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.utils.DynamicTenantUtil;
import com.wzkris.common.security.utils.SystemUserUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author wzkris
 * @version : V1.0.0
 * @description : 全局动态租户拦截器，提供切换租户的能力
 * @date : 2025/07/01 11:10
 */
public class DynamicTenantSwitchingFilter extends OncePerRequestFilter {

    private static final String IGNORE_TYPE = "all";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String dynamicTenant = request.getHeader(HeaderConstants.X_TENANT_ID);

        if (dynamicTenant == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!SystemUserUtil.isLogin() || !SystemUserUtil.isSuperTenant()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (NumberUtils.isCreatable(dynamicTenant)) {
                DynamicTenantUtil.set(Long.valueOf(dynamicTenant));
            } else if (StringUtil.equals(dynamicTenant, IGNORE_TYPE)) {
                DynamicTenantUtil.enableIgnore();
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
                return; // 不是合法请求头数据则直接返回
            }

            filterChain.doFilter(request, response);
        } finally {
            if (NumberUtils.isCreatable(dynamicTenant)) {
                DynamicTenantUtil.remove();
            } else if (StringUtil.equals(dynamicTenant, IGNORE_TYPE)) {
                DynamicTenantUtil.disableIgnore();
            }
        }
    }

}
