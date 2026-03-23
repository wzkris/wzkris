package com.wzkris.usercenter.domain.req.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 修改成员个人信息请求体
 */
@Data
@Schema(description = "修改成员个人信息参数体")
public class MemberInfoEditReq {

    @Schema(description = "用户性别")
    private String gender;

}
