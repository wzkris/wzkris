package com.wzkris.usercenter.domain.req.member;

import com.wzkris.common.web.model.QueryReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "租户成员管理查询参数体")
public class MemberMngQueryReq extends QueryReq {

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "手机号码")
    private String phoneNumber;

    @Schema(description = "状态")
    private String status;

}
