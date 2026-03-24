package com.wzkris.usercenter.domain.req.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 修改客户个人信息请求体
 */
@Data
@Schema(description = "修改客户个人信息参数体")
public class CustomerInfoEditReq {

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户性别")
    private String gender;

}
