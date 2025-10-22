package com.wzkris.principal.domain.req.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 修改个人信息
 */
@Data
public class CustomerInfoReq {

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户性别")
    private String gender;

}
