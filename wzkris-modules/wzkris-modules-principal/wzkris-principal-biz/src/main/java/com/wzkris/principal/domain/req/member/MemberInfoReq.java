package com.wzkris.principal.domain.req.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 修改个人信息
 */
@Data
public class MemberInfoReq {

    @Schema(description = "用户性别")
    private String gender;

}
