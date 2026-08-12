package com.wzkris.usercenter.api.tenantuser.request;

import com.wzkris.usercenter.enums.user.GenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "修改用户个人信息参数体")
public class TenantUserInfoBasicUpdateRequest {

    @Schema(description = "用户性别")
    private GenderEnum gender;

    @Schema(description = "头像")
    private String avatar;

}

