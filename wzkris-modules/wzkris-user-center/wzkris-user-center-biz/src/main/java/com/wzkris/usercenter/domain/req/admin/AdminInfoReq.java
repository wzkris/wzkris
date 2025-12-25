package com.wzkris.usercenter.domain.req.admin;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 修改个人信息
 */
@Data
public class AdminInfoReq {

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户性别")
    private String gender;

}
