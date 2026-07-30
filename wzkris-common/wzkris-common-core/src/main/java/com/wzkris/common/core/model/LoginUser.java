package com.wzkris.common.core.model;

import com.wzkris.common.core.enums.AuthTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import java.time.Instant;

/**
 * 统一登录用户模型
 *
 * <p>取代原 LoginAdminUser / LoginTenantUser / LoginCustomerUser / LoginClientUser 四套子类，
 * 通过 {@link #authType} 字段区分用户类型，差异化信息由认证流程在构建时填入 {@link #name} 和 {@link #tenantId}。
 *
 * @author wzkris
 */
@Getter
@Setter
@ToString
public class LoginUser implements BaseLoginUser {

    private Long uid;

    private AuthTypeEnum authType;

    private String name;

    @Nullable
    private String hint;

    @Nullable
    private Long tenantId;

    @Nullable
    private Instant userExpiredTime;

    @Nullable
    private String userExpiredReason;

    @Nullable
    private ActorInfo actor;

    @Override
    public String getName() {
        return this.name;
    }

}
