package com.wzkris.common.core.model;

import com.wzkris.common.core.enums.AuthTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.List;

/**
 * BaseLoginUser 的通用基础实现，仅承载所有用户类型共享字段。
 */
@Getter
@Setter
@ToString
public abstract class AbsBaseLoginUser implements BaseLoginUser {

    private Long uid;

    private AuthTypeEnum authType;

    private boolean superUser;

    @Nullable
    private String hint;

    @Nullable
    private Instant userExpiredTime;

    @Nullable
    private String userExpiredReason;

    /**
     * 代操作实际操作者
     */
    @Nullable
    private ActorInfo actor;

    /**
     * 用户角色列表（携带数据权限信息）
     */
    @Nullable
    private List<UserRole> roles;

}
