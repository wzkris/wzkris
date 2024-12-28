package com.wzkris.user.domain.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 小程序注册请求体
 *
 * @author wzkris
 */
@Data
public class XcxRegisterReq {

    @NotBlank(message = "jscode {validate.notnull}")
    private String jscode;

    @NotBlank(message = "code {validate.notnull}")
    private String code;
}
