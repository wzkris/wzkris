package com.wzkris.auth.httpclientimpl.loginuser.req;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.validator.annotation.EnumsCheck;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserQueryReq implements Serializable {

    @EnumsCheck(value = AuthTypeEnum.class, property = "value", message = "认证类型不正确")
    private String authType;

    @NotNull(message = "用户ID不能为空")
    private Long uid;

    @NotBlank(message = "sid不能为空")
    private String sid;

}
