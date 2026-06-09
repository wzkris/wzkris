package com.wzkris.auth.remote.interfaces.admin.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class AdminQueryOneRequest implements Serializable {

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "手机号")
    private String phoneNumber;

}
