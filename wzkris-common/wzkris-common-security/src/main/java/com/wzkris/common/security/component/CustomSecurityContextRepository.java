package com.wzkris.common.security.component;

import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.utils.BearerTokenUtil;
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

        // 从统一身份头读取主体（X_USER_CONTEXT）
        final String loginUserHeader = request.getHeader(CustomHeaderConstants.X_USER_CONTEXT);
        if (StringUtil.isBlank(loginUserHeader)) {
            return ctx;
        }

        BaseLoginUser baseLoginUser = JsonUtil.parseObject(loginUserHeader, BaseLoginUser.class);
        UsernamePasswordAuthenticationToken authenticationToken = UsernamePasswordAuthenticationToken.authenticated(
                baseLoginUser,
                BearerTokenUtil.extractHeaderToken(request),
                AuthorityUtils.createAuthorityList(permissions));
        authenticationToken.setDetails(this.authenticationDetailsSource.buildDetails(request));
        ctx.setAuthentication(authenticationToken);
        return ctx;
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
