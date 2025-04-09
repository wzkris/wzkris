package com.wzkris.auth.domain.resp;

import com.wzkris.common.core.annotation.Sensitive;
import com.wzkris.common.core.annotation.impl.SensitiveStrategy;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户信息
 *
 * @author wzkris
 */
@Data
public class AppUserinfo {

    @Sensitive(strategy = SensitiveStrategy.PHONE)
    @Schema(description = "手机号密文")
    private String phoneNumber;
}
