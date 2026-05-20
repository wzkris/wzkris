package com.wzkris.auth.service;

import com.wzkris.common.security.model.AdminLoginUser;
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
    UsernamePasswordAuthenticationToken switchToTenant(AdminLoginUser fromAdmin, Long tenantId);

    /**
     * 由 impersonated TENANT 切回原 ADMIN。
     */
    @Nullable
    UsernamePasswordAuthenticationToken switchToAdmin(Long actorUid);

}
