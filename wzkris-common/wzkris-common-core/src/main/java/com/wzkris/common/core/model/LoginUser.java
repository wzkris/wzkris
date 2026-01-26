package com.wzkris.common.core.model;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.enums.IdentityTypeEnum;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.security.Principal;
import java.util.Date;

/**
 * 核心信息
 *
 * @author wzkris
 */
@Data
public class LoginUser implements Principal {

    private Long uid;

    private AuthTypeEnum authType;

    private IdentityTypeEnum identityType;

    @Nullable
    private String phoneNumber;

    @Nullable
    private String username;

    @Nullable
    private Long tenantId;

    @Nullable
    private String hint;

    @Nullable
    private Date userExpiredTime;

    @Nullable
    private String userExpiredReason;

    @Override
    public String getName() {
        return username;
    }

}
