package com.wzkris.auth.remote.interfaces.customer.request;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 客户社交账号绑定更新请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSocialUpdateRequest implements Serializable {

    @Nonnull
    private Long customerId;

    @Nonnull
    private String socialType;

    @Nonnull
    private String wxCode;

    @Nullable
    private String appid;

}