package com.wzkris.common.core.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.enums.IdentityTypeEnum;
import org.springframework.lang.Nullable;

import java.security.Principal;
import java.time.Instant;

/**
 * 统一用户抽象视图，用于承载各类用户（管理员、租户、C 端等）的公共能力。
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public interface BaseLoginUser extends Principal {

    Long getUid();

    AuthTypeEnum getAuthType();

    IdentityTypeEnum getIdentityType();

    /**
     * 标签
     */
    @Nullable
    String getHint();

    /**
     * 用户整体过期时间（如账号被设置的失效时间），可为空。
     */
    @Nullable
    Instant getUserExpiredTime();

    /**
     * 用户被标记为过期/冻结时的人类可读原因，可为空。
     */
    @Nullable
    String getUserExpiredReason();

    /**
     * 实际操作者ID（impersonation 场景）
     */
    @Nullable
    Long getActorUid();

    /**
     * 实际操作者认证类型（impersonation 场景）。
     */
    @Nullable
    AuthTypeEnum getActorAuthType();

}
