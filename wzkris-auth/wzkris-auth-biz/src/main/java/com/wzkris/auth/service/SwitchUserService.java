package com.wzkris.auth.service;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.LoginUser;
import jakarta.annotation.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * 登录态切换（ADMIN ↔ TENANT）。
 */
public interface SwitchUserService {

    /**
     * ADMIN 切换到指定租户的租户最高管理员。
     */
    @Nullable
    UsernamePasswordAuthenticationToken switchToTenant(LoginUser loginUser, Long tenantId, String actorSid);

    /**
     * 切回原 user
     */
    @Nullable
    UsernamePasswordAuthenticationToken switchBack(Long actorUid, AuthTypeEnum authTypeEnum);

}
