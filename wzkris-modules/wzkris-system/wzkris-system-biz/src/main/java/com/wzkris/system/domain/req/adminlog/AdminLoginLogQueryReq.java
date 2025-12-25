package com.wzkris.system.domain.req.adminlog;

import com.wzkris.common.web.model.QueryReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "筛选条件")
public class AdminLoginLogQueryReq extends QueryReq {

    @Schema(description = "用户ID")
    private Long adminId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "登录状态")
    private Boolean success;

    @Schema(description = "登录地址")
    private String loginLocation;

}
