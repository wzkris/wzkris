package com.wzkris.user.domain.vo;

import com.wzkris.common.validator.annotation.Sensitive;
import com.wzkris.common.validator.impl.SensitiveStrategy;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "app用户信息")
public class AppUserProfileVO {

    @Schema(description = "用户昵称")
    private String nickname;

    @Sensitive(strategy = SensitiveStrategy.PHONE)
    @Schema(description = "手机号码")
    private String phoneNumber;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "用户性别")
    private String gender;

}
