package com.wzkris.user.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 修改个人信息
 */
@Data
public class EditOwnAppUserReq {
    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户性别")
    private String gender;
}
