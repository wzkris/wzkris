package com.wzkris.usercenter.api.customer.request;

import com.wzkris.common.orm.request.PagingRequest;
import com.wzkris.usercenter.enums.customer.CustomerStatusEnum;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "客户管理查询参数体")
public class CustomerMngPageRequest extends PagingRequest {

    @Parameter(description = "用户昵称")
    private String nickname;

    @Parameter(description = "手机号码")
    private String phoneNumber;

    @Parameter(description = "用户状态")
    private CustomerStatusEnum status;

}
