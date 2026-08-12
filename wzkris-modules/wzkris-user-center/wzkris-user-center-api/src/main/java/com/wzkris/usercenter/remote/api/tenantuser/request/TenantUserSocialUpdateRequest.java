package com.wzkris.usercenter.remote.api.tenantuser.request;

import com.wzkris.usercenter.enums.social.SocialTypeEnum;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 租户用户社交账号绑定更新请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantUserSocialUpdateRequest implements Serializable {

    @Nonnull
    private Long tenantUserId;

    @Nonnull
    private SocialTypeEnum socialType;

    @Nonnull
    private String wxCode;

    @Nullable
    private String appid;

}