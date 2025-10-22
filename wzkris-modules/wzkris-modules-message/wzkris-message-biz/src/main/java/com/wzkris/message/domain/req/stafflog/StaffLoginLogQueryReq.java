package com.wzkris.message.domain.req.stafflog;

import com.wzkris.common.web.model.QueryReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "筛选条件")
public class StaffLoginLogQueryReq extends QueryReq {

    @Schema(description = "用户ID")
    private Long staffId;

    @Schema(description = "用户名")
    private String staffName;

    @Schema(description = "登录状态")
    private Boolean success;

    @Schema(description = "登录地址")
    private String loginLocation;

}
