package com.wzkris.usercenter.domain.req.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 修改个人信息请求体
 */
@Data
@Schema(description = "修改个人信息参数体")
public class AdminInfoEditReq {

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户性别")
    private String gender;

}
