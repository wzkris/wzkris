package com.wzkris.common.security.component;

import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.ClientPrincipal;
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

import java.util.Collections;
import java.util.Objects;
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

        // 从请求头读取权限信息
        Set<String> permissions;
        final String permissionsHeader = request.getHeader(CustomHeaderConstants.X_PERMISSIONS);
        if (StringUtil.isNotBlank(permissionsHeader)) {
            permissions = JsonUtil.toColl(permissionsHeader, java.util.Set.class, String.class);
        } else {
            permissions = Collections.emptySet();
        }

        // 先尝试读取 LoginUser（从 X_SECURITY_PRINCIPAL 请求头）
        final String loginUserHeader = request.getHeader(CustomHeaderConstants.X_USER_CONTEXT);
        if (StringUtil.isNotBlank(loginUserHeader)) {
            LoginUser loginUser = JsonUtil.parseObject(loginUserHeader, LoginUser.class);
            if (Objects.nonNull(loginUser)) {
                UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.authenticated(
                        loginUser,
                        getToken(request, loginUser.getAuthType().getValue()),
                        AuthorityUtils.createAuthorityList(permissions));
                authenticationToken.setDetails(this.authenticationDetailsSource.buildDetails(request));
                ctx.setAuthentication(authenticationToken);
                return ctx;
            }
        }

        // 再尝试读取 ClientPrincipal（从 X_CLIENT_PRINCIPAL 请求头）
        final String clientHeader = request.getHeader(CustomHeaderConstants.X_CLIENT_CONTEXT);
        if (StringUtil.isNotBlank(clientHeader)) {
            ClientPrincipal clientPrincipal = JsonUtil.parseObject(clientHeader, ClientPrincipal.class);
            if (Objects.nonNull(clientPrincipal)) {
                UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.authenticated(
                        clientPrincipal,
                        getToken(request, AuthTypeEnum.CLIENT.getValue()),
                        AuthorityUtils.createAuthorityList(permissions));
                authenticationToken.setDetails(this.authenticationDetailsSource.buildDetails(request));
                ctx.setAuthentication(authenticationToken);
                return ctx;
            }
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
