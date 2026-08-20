package com.wzkris.common.core.model;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.support.LoginUser;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import java.time.Instant;

/**
 * 统一登录用户模型
 *
 * @author wzkris
 */
@Getter
@Setter
@ToString
public class DefaultLoginUser implements LoginUser {

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
