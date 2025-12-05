package com.wzkris.auth.security.filter;

import com.wzkris.auth.listener.event.LogoutEvent;
import com.wzkris.auth.service.TokenService;
import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.utils.SpringUtil;
import com.wzkris.common.core.utils.StringUtil;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.io.Serializable;

/**
 * 退出登录
 *
 * @author wzkris
 */
public class LogoutHandlerImpl implements LogoutHandler {

    private final TokenService tokenService;

    public LogoutHandlerImpl(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * 由于该过滤器链未配置安全上下文解析，authentication必定为null
     *
     * @param request        the HTTP request
     * @param response       the HTTP response
     * @param authentication the current principal details
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, @Nullable Authentication authentication) {
        String adminToken = request.getHeader(CustomHeaderConstants.X_ADMIN_TOKEN);
        if (StringUtil.isNotBlank(adminToken)) {
            Serializable id = tokenService.logoutByAccessToken(AuthTypeEnum.ADMIN.getValue(), adminToken);
            if (id != null) {
                SpringUtil.getContext().publishEvent(new LogoutEvent(id, AuthTypeEnum.ADMIN.getValue()));
            }
        }

        String tenantToken = request.getHeader(CustomHeaderConstants.X_TENANT_TOKEN);
        if (StringUtil.isNotBlank(tenantToken)) {
            Serializable id = tokenService.logoutByAccessToken(AuthTypeEnum.TENANT.getValue(), tenantToken);
            if (id != null) {
                SpringUtil.getContext().publishEvent(new LogoutEvent(id, AuthTypeEnum.TENANT.getValue()));
            }
        }

        String customerToken = request.getHeader(CustomHeaderConstants.X_CUSTOMER_TOKEN);
        if (StringUtil.isNotBlank(customerToken)) {
            Serializable id = tokenService.logoutByAccessToken(AuthTypeEnum.CUSTOMER.getValue(), customerToken);
            if (id != null) {
                SpringUtil.getContext().publishEvent(new LogoutEvent(id, AuthTypeEnum.CUSTOMER.getValue()));
            }
        }
    }

}
