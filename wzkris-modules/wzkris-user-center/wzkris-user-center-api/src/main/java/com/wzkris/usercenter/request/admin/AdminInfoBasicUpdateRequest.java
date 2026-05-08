package com.wzkris.usercenter.request.admin;

import com.wzkris.usercenter.enums.user.GenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "修改个人基本信息参数体")
public class AdminInfoBasicUpdateRequest {

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户性别")
    private GenderEnum gender;

    @Schema(description = "头像")
    private String avatar;

}

