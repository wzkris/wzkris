package com.wzkris.usercenter.request.customer;

import com.wzkris.common.core.model.QueryRequest;
import com.wzkris.usercenter.enums.customer.CustomerStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CustomerMngPageRequest extends QueryRequest {

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "手机号码")
    private String phoneNumber;

    @Schema(description = "用户状态")
    private CustomerStatusEnum status;

}

