package com.wzkris.user.api.domain.request;

import jakarta.annotation.Nonnull;
import lombok.Data;

/**
 * 登录信息
 *
 * @author wzkris
 */
@Data
public class LoginInfoReq {

    @Nonnull
    private Long userId;

    private String loginIp;

    private Long loginDate;
}
