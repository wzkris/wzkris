package com.wzkris.common.security.support;

import com.wzkris.common.core.support.LoginUser;
import com.wzkris.common.core.model.RoleContext;
import com.wzkris.common.core.support.UserContextHelper;
import com.wzkris.common.security.utils.SecurityUtil;
import org.springframework.lang.Nullable;

/**
 * {@link UserContextHelper} 的安全层实现
 *
 * @author wzkris
 */
public class SecurityUserContextHelper implements UserContextHelper {

    @Override
    @Nullable
    public LoginUser getLoginUser() {
        return SecurityUtil.getLoginUser();
    }

    @Override
    @Nullable
    public RoleContext getRoleContext() {
        return SecurityUtil.getRoleContext();
    }

}
