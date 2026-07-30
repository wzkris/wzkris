package com.wzkris.usercenter.controller.adminlog.login;

import com.wzkris.common.core.enums.AuthTypeEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckPerms;
import com.wzkris.usercenter.api.adminlog.login.AdminLoginlogMngApi;
import com.wzkris.usercenter.api.adminlog.login.request.AdminLoginLogMngPageRequest;
import com.wzkris.usercenter.api.adminlog.login.response.AdminLoginLogMngResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "管理员登录日志管理")
@RestController
@RequestMapping("/admin-loginlog-manage")
@RequiredArgsConstructor
public class AdminLoginlogMngController {

    private final AdminLoginlogMngApi adminLoginlogMngApi;

    @Operation(summary = "分页")
    @GetMapping("/query-page")
    @CheckPerms(checkTypes = AuthTypeEnum.ADMIN, value = "system-mod:admin-loginlog-mng:page")
    public Result<Page<AdminLoginLogMngResponse>> queryPage(@ParameterObject AdminLoginLogMngPageRequest request) {
        return adminLoginlogMngApi.queryPage(request);
    }

}
