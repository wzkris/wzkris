package com.wzkris.principal.domain.req.staff;

import com.wzkris.common.web.model.QueryReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "员工管理查询参数体")
public class StaffManageQueryReq extends QueryReq {

    @Schema(description = "用户名")
    private String staffName;

    @Schema(description = "手机号码")
    private String phoneNumber;

    @Schema(description = "状态")
    private String status;

}
