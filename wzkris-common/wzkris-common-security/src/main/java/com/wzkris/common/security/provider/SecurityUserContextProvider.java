package com.wzkris.common.security.provider;

import com.wzkris.common.core.context.UserContextProvider;
import com.wzkris.common.core.model.BaseLoginUser;
import com.wzkris.common.core.model.RoleContext;
import com.wzkris.common.security.authentication.RoleContextAuthenticationToken;
import com.wzkris.common.security.model.LoginTenantUser;
import org.springframework.context.annotation.Primary;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;

/**
 * {@link UserContextProvider} 的安全层实现
 *
 * <p>直接读取 Spring Security 的 {@link SecurityContextHolder}（唯一真相源），
 * 不维护任何 ThreadLocal 副本。由 common-orm / common-log 等基础设施层通过依赖注入消费。
 *
 * @author wzkris
 */
@Primary
public class SecurityUserContextProvider implements UserContextProvider {

    private final SecurityContextHolderStrategy strategy = SecurityContextHolder.getContextHolderStrategy();

    @Nullable
    private BaseLoginUser getLoginUserIfPresent() {
        Authentication authentication = strategy.getContext().getAuthentication();
        if (authentication != null
                && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof BaseLoginUser loginUser) {
            return loginUser;
        }
        return null;
    }

    @Override
    @Nullable
    public BaseLoginUser getBaseLoginUser() {
        return getLoginUserIfPresent();
    }

    @Override
    @Nullable
    public Long getTenantId() {
        BaseLoginUser loginUser = getLoginUserIfPresent();
        // 仅租户用户携带 tenantId，admin 切换租户后 principal 即为 LoginTenantUser
        return loginUser instanceof LoginTenantUser tenantUser ? tenantUser.getTenantId() : null;
    }

    @Override
    @Nullable
    public RoleContext getRoleContext() {
        Authentication authentication = strategy.getContext().getAuthentication();
        if (authentication instanceof RoleContextAuthenticationToken rcToken) {
            return rcToken.getRoleContext();
        }
        return null;
    }

}
