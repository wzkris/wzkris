package com.wzkris.principal.domain.req.customer;

import com.wzkris.common.web.model.QueryReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CustomerManageQueryReq extends QueryReq {

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "手机号码")
    private String phoneNumber;

    @Schema(description = "用户状态")
    private String status;

}
