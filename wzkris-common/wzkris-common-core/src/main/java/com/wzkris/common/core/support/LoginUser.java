package com.wzkris.common.core.support;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.ActorInfo;
import org.springframework.lang.Nullable;

import java.security.Principal;
import java.time.Instant;

/**
 * 统一登录用户抽象视图，用于承载各类用户的公共能力。
 */
public interface LoginUser extends Principal {

    Long getUid();

    AuthTypeEnum getAuthType();

    /**
     * 租户ID，仅 TENANT 类型用户有值，其余为 null
     */
    @Nullable
    Long getTenantId();

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
     * 代操作实际操作者
     */
    @Nullable
    ActorInfo getActor();

}
