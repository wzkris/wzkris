package com.wzkris.usercenter.request.admin;

import com.wzkris.common.orm.request.PagingRequest;
import com.wzkris.usercenter.enums.admin.AdminStatusEnum;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "管理查询参数体")
public class AdminMngPageRequest extends PagingRequest {

    @Parameter(description = "部门ID")
    private Long deptId;

    @Parameter(description = "用户名")
    private String username;

    @Parameter(description = "用户昵称")
    private String nickname;

    @Parameter(description = "用户邮箱")
    private String email;

    @Parameter(description = "手机号码")
    private String phoneNumber;

    @Parameter(description = "用户状态")
    private AdminStatusEnum status;
}
