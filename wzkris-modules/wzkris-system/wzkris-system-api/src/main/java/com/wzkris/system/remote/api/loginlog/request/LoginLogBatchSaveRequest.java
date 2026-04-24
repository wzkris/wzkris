package com.wzkris.system.remote.api.loginlog.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "登录日志批量保存参数")
public class LoginLogBatchSaveRequest {

    @Schema(description = "登录日志事件列表")
    @NotEmpty(message = "{invalidParameter.param.invalid}")
    private List<LoginLogEventRequest> loginLogEventRequests;

}
