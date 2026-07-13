package com.wzkris.usercenter.api.adminlog.login.request;

import com.wzkris.common.orm.request.PagingRequest;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "管理员个人登录日志筛选条件（info）")
public class AdminLoginLogInfoPageRequest extends PagingRequest {

    @Parameter(description = "用户名")
    private String username;

    @Parameter(description = "登录状态")
    private Boolean success;

    @Parameter(description = "登录地址")
    private String loginLocation;

    @Parameter(description = "链路追踪ID")
    private String traceId;

}
