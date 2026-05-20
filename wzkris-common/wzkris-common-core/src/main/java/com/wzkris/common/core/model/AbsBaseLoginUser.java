package com.wzkris.common.core.model;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.enums.IdentityTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import java.time.Instant;

/**
 * BaseLoginUser 的通用基础实现，仅承载所有用户类型共享字段。
 */
@Getter
@Setter
@ToString
public abstract class AbsBaseLoginUser implements BaseLoginUser {

    private Long uid;

    private AuthTypeEnum authType;

    private IdentityTypeEnum identityType;

    @Nullable
    private String hint;

    @Nullable
    private Instant userExpiredTime;

    @Nullable
    private String userExpiredReason;

    /**
     * 实际操作者ID（impersonation 场景）。null 表示当前用户即操作者。
     */
    @Nullable
    private Long actorUid;

    /**
     * 实际操作者认证类型（impersonation 场景）。
     */
    @Nullable
    private AuthTypeEnum actorAuthType;

}
