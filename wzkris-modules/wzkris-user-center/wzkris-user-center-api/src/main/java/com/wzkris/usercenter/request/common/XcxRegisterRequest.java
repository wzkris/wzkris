package com.wzkris.usercenter.request.common;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 小程序注册请求体
 *
 * @author wzkris
 */
@Data
public class XcxRegisterRequest {

    @NotBlank(message = "jscode {validate.notnull}")
    private String jscode;

    @NotBlank(message = "code {validate.notnull}")
    private String code;

}

