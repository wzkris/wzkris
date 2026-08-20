package com.wzkris.usercenter.controller.adminlog.operate;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckPerms;
import com.wzkris.usercenter.api.adminlog.operate.AdminOperateLogMngApi;
import com.wzkris.usercenter.api.adminlog.operate.request.AdminOperateLogMngPageRequest;
import com.wzkris.usercenter.api.adminlog.operate.response.AdminOperateLogMngPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "管理员操作日志管理")
@RestController
@RequestMapping("/admin-operatelog-manage")
@RequiredArgsConstructor
public class AdminOperateLogMngController {

    private final AdminOperateLogMngApi adminOperateLogMngApi;

    @Operation(summary = "分页")
    @GetMapping("/query-page")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, value = "system-mod:admin-operatelog-mng:page")
    public Result<Page<AdminOperateLogMngPageResponse>> queryPage(@ParameterObject AdminOperateLogMngPageRequest request) {
        return adminOperateLogMngApi.queryPage(request);
    }

}
