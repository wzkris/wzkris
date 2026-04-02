package com.wzkris.system.controller.adminlog.operate;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckAdminPerms;
import com.wzkris.system.api.adminlog.operate.AdminOperateLogMngApi;
import com.wzkris.system.request.adminlog.AdminOperateLogMngQueryRequest;
import com.wzkris.system.response.adminlog.AdminOperateLogMngResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 操作日志记录
 *
 * @author wzkris
 */
@Tag(name = "管理员操作日志管理")
@RestController
@RequestMapping("/admin-operatelog-manage")
@RequiredArgsConstructor
public class AdminOperateLogMngController {

    private final AdminOperateLogMngApi adminOperateLogMngApi;

    @Operation(summary = "分页")
    @GetMapping("/query-page")
    @CheckAdminPerms("system-mod:admin-operatelog-mng:page")
    public Result<Page<AdminOperateLogMngResponse>> queryPage(AdminOperateLogMngQueryRequest request) {
        return adminOperateLogMngApi.queryPage(request);
    }

}
