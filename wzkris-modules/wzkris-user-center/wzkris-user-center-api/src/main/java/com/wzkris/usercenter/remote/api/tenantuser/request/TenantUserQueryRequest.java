package com.wzkris.usercenter.remote.api.tenantuser.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantUserQueryRequest implements Serializable {

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "手机号")
    private String phoneNumber;

}
