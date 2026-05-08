package com.wzkris.auth.remote.api.loginuser.request;

import com.wzkris.common.core.enums.AuthTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserQueryRequest implements Serializable {

    @NotNull(message = "认证类型不能为空")
    private AuthTypeEnum authType;

    @NotNull(message = "用户ID不能为空")
    private Long uid;

    @NotBlank(message = "sid不能为空")
    private String sid;

}


