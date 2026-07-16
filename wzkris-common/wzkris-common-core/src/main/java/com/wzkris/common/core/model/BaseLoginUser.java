package com.wzkris.common.core.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.wzkris.common.core.enums.AuthTypeEnum;
import org.springframework.lang.Nullable;

import java.security.Principal;
import java.time.Instant;
import java.util.List;

/**
 * 统一用户抽象视图，用于承载各类用户（管理员、租户、C 端等）的公共能力。
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public interface BaseLoginUser extends Principal {

    Long getUid();

    AuthTypeEnum getAuthType();

    /**
     * 是否为超级用户（bootstrap 账号或租户管理员）
     */
    boolean isSuperUser();

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

    /**
     * 用户角色列表（携带数据权限信息）
     */
    @Nullable
    List<UserRole> getRoles();

}
