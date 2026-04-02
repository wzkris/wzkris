package com.wzkris.system.controller.adminlog.operate;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.system.api.adminlog.operate.AdminOperateLogInfoApi;
import com.wzkris.system.request.adminlog.AdminOperateLogInfoQueryRequest;
import com.wzkris.system.response.adminlog.AdminOperateLogInfoResponse;
import com.wzkris.system.response.adminlog.AdminOperateLogMngResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "管理员个人操作日志信息")
@RestController
@RequestMapping("/admin-operatelog-info")
@RequiredArgsConstructor
public class AdminOperateLogInfoController {

    private final AdminOperateLogInfoApi adminOperateLogInfoApi;

    @Operation(summary = "分页")
    @GetMapping("/query-page")
    public Result<Page<AdminOperateLogInfoResponse>> queryPage(AdminOperateLogInfoQueryRequest request) {
        return adminOperateLogInfoApi.queryPage(request);
    }

}
