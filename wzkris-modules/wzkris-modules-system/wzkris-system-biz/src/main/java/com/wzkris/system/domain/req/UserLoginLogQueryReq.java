package com.wzkris.system.domain.req;

import com.wzkris.common.web.model.QueryReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "筛选条件")
public class UserLoginLogQueryReq extends QueryReq {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "登录状态（0正常 1异常）")
    private String status;

    @Schema(description = "登录地址")
    private String loginLocation;

}
