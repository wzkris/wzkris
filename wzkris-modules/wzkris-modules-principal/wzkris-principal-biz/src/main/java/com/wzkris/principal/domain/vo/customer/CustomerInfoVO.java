package com.wzkris.principal.domain.vo.customer;

import com.wzkris.common.core.annotation.Sensitive;
import com.wzkris.common.core.enums.SensitiveStrategyEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "客户信息")
public class CustomerInfoVO {

    @Schema(description = "用户昵称")
    private String nickname;

    @Sensitive(strategy = SensitiveStrategyEnum.PHONE)
    @Schema(description = "手机号码")
    private String phoneNumber;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "用户性别")
    private String gender;

}
