package com.wzkris.common.security.component;

import com.wzkris.common.core.constant.CustomHeaderConstants;
import com.wzkris.common.core.support.LoginUser;
import com.wzkris.common.core.model.DefaultLoginUser;
import com.wzkris.common.core.model.RoleContext;
import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.security.authentication.RoleContextAuthenticationToken;
import com.wzkris.common.security.utils.BearerTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.DeferredSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
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
public final class CustomSecurityContextRepository implements SecurityContextRepository {

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

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

        // 从统一身份头读取主体（X_USER_CONTEXT）
        final String loginUserHeader = request.getHeader(CustomHeaderConstants.X_USER_CONTEXT);
        if (StringUtil.isBlank(loginUserHeader)) {
            return ctx;
        }

        LoginUser loginUser = JsonUtil.parseObject(loginUserHeader, DefaultLoginUser.class);

        // 从角色上下文头读取权限信息（X_ROLE_CONTEXT）
        RoleContext roleContext = null;
        final String roleContextHeader = request.getHeader(CustomHeaderConstants.X_ROLE_CONTEXT);
        if (StringUtil.isNotBlank(roleContextHeader)) {
            roleContext = JsonUtil.parseObject(roleContextHeader, RoleContext.class);
        }

        RoleContextAuthenticationToken authenticationToken = RoleContextAuthenticationToken.authenticated(
                loginUser,
                BearerTokenUtil.extractHeaderToken(request),
                roleContext);
        ctx.setAuthentication(authenticationToken);
        return ctx;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        // Empty 中心化认证架构不允许其他服务持久化安全上下文
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        return StringUtil.isNotBlank(request.getHeader(CustomHeaderConstants.X_USER_CONTEXT));
    }

}
