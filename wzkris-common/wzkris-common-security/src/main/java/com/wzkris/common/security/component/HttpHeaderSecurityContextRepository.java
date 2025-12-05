package com.wzkris.common.security.component;

import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.model.MyPrincipal;
import com.wzkris.common.core.model.domain.LoginAdmin;
import com.wzkris.common.core.model.domain.LoginCustomer;
import com.wzkris.common.core.model.domain.LoginTenant;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.core.utils.StringUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.DeferredSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;

import java.util.function.Supplier;

/**
 * 重写此类以支持网关统一认证
 *
 * @author wzkris
 * @date 2025/06/19 15:40
 */
@Slf4j
public final class HttpHeaderSecurityContextRepository implements SecurityContextRepository {

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
        SecurityContext ctx = SecurityContextHolder.createEmptyContext();

        if (generateAdmin(request, ctx)) {
            return ctx;
        }

        if (generateTenant(request, ctx)) {
            return ctx;
        }

        if (generateCustomer(request, ctx)) {
            return ctx;
        }

        return ctx;
    }

    private boolean generateAdmin(HttpServletRequest request, SecurityContext ctx) {
        final String userinfo = request.getHeader(CustomHeaderConstants.X_ADMIN_INFO);
        if (StringUtil.isNotBlank(userinfo)) {
            ctx.setAuthentication(createAuthentication(JsonUtil.parseObject(userinfo, LoginAdmin.class), request,
                    request.getHeader(CustomHeaderConstants.X_ADMIN_TOKEN)));
            return true;
        }
        return false;
    }

    private boolean generateTenant(HttpServletRequest request, SecurityContext ctx) {
        final String tenantinfo = request.getHeader(CustomHeaderConstants.X_TENANT_INFO);
        if (StringUtil.isNotBlank(tenantinfo)) {
            ctx.setAuthentication(createAuthentication(JsonUtil.parseObject(tenantinfo, LoginTenant.class), request,
                    request.getHeader(CustomHeaderConstants.X_TENANT_TOKEN)));
            return true;
        }
        return false;
    }

    private boolean generateCustomer(HttpServletRequest request, SecurityContext ctx) {
        final String customerinfo = request.getHeader(CustomHeaderConstants.X_CUSTOMER_INFO);
        if (StringUtil.isNotBlank(customerinfo)) {
            ctx.setAuthentication(createAuthentication(JsonUtil.parseObject(customerinfo, LoginCustomer.class), request,
                    request.getHeader(CustomHeaderConstants.X_CUSTOMER_TOKEN)));
            return true;
        }
        return false;
    }

    private Authentication createAuthentication(MyPrincipal principal, HttpServletRequest request, String token) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principal, token,
                AuthorityUtils.createAuthorityList((principal).getPermissions()));
        authenticationToken.setDetails(this.authenticationDetailsSource.buildDetails(request));
        return authenticationToken;
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
