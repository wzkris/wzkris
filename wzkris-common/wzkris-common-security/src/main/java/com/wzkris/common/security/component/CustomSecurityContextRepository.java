package com.wzkris.common.security.component;

import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.LoginUser;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.core.utils.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.DeferredSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;

import java.util.Set;
import java.util.function.Supplier;

/**
 * 重写此类以支持网关统一认证
 *
 * @author wzkris
 * @date 2025/06/19 15:40
 */
@Slf4j
public final class CustomSecurityContextRepository implements SecurityContextRepository {

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

    private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        return loadDeferredContext(requestResponseHolder.getRequest()).get();
    }

    @Override
    public DeferredSecurityContext loadDeferredContext(HttpServletRequest request) {
        Supplier<SecurityContext> supplier = () -> readSecurityContextFromRequest(request);
        return new SupplierDeferredSecurityContext(supplier, this.securityContextHolderStrategy);
    }

    private SecurityContext readSecurityContextFromRequest(HttpServletRequest request) {
        SecurityContext ctx = securityContextHolderStrategy.createEmptyContext();

        final String context = request.getHeader(CustomHeaderConstants.X_SECURITY_PRINCIPAL);
        if (StringUtil.isNotBlank(context)) {
            LoginUser loginUser = JsonUtil.parseObject(context, LoginUser.class);

            // 从请求头读取权限信息
            Set<String> permissions = null;
            final String permissionsHeader = request.getHeader(CustomHeaderConstants.X_PERMISSIONS);
            if (StringUtil.isNotBlank(permissionsHeader)) {
                try {
                    permissions = JsonUtil.toColl(permissionsHeader, java.util.Set.class, String.class);
                } catch (Exception e) {
                    log.warn("Failed to parse permissions from header: {}", e.getMessage());
                }
            }

            // 使用解析的权限创建 Authentication
            UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.authenticated(
                    loginUser,
                    getToken(request, loginUser.getAuthType().getValue()),
                    permissions != null && !permissions.isEmpty()
                            ? AuthorityUtils.createAuthorityList(permissions.toArray(new String[0]))
                            : AuthorityUtils.NO_AUTHORITIES);
            authenticationToken.setDetails(this.authenticationDetailsSource.buildDetails(request));
            ctx.setAuthentication(authenticationToken);
        }

        return ctx;
    }

    private String getToken(HttpServletRequest request, String type) {
        return switch (AuthTypeEnum.fromValue(type)) {
            case ADMIN -> request.getHeader(CustomHeaderConstants.X_ADMIN_TOKEN);
            case TENANT -> request.getHeader(CustomHeaderConstants.X_TENANT_TOKEN);
            case CUSTOMER -> request.getHeader(CustomHeaderConstants.X_CUSTOMER_TOKEN);
            case CLIENT -> request.getHeader(CustomHeaderConstants.X_CLIENT_TOKEN);
            default -> null;
        };
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        // Empty 中心化认证架构不允许其他服务持久化安全上下文
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        return securityContextHolderStrategy.getContext().getAuthentication() != null;
    }

}
