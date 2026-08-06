package com.wzkris.usercenter.api.customer.response;

import com.wzkris.common.core.annotation.Sensitive;
import com.wzkris.common.core.enums.SensitiveStrategyEnum;
import com.wzkris.usercenter.enums.user.GenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Schema(description = "客户信息")
public class CustomerInfoQueryResponse {

    @Schema(description = "用户昵称")
    private String nickname;

    @Sensitive(strategy = SensitiveStrategyEnum.PHONE)
    @Schema(description = "手机号码")
    private String phoneNumber;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "用户性别")
    private GenderEnum gender;

    @Schema(description = "登录时间")
    private OffsetDateTime loginDate;

}

