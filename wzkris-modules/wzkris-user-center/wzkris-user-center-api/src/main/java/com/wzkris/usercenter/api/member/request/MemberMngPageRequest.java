package com.wzkris.usercenter.api.member.request;

import com.wzkris.common.orm.request.PagingRequest;
import com.wzkris.usercenter.enums.member.MemberStatusEnum;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "租户成员管理查询参数体")
public class MemberMngPageRequest extends PagingRequest {

    @Parameter(description = "用户名")
    private String username;

    @Parameter(description = "手机号码")
    private String phoneNumber;

    @Parameter(description = "状态")
    private MemberStatusEnum status;

}
