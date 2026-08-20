package com.wzkris.usercenter.controller.adminlog.login;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.Page;
import com.wzkris.usercenter.api.adminlog.login.AdminLoginlogInfoApi;
import com.wzkris.usercenter.api.adminlog.login.request.AdminLoginLogInfoPageRequest;
import com.wzkris.usercenter.api.adminlog.login.response.AdminLoginLogInfoPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "管理员个人登录日志信息")
@RestController
@RequestMapping("/admin-loginlog-info")
@RequiredArgsConstructor
public class AdminLoginlogInfoController {

    private final AdminLoginlogInfoApi adminLoginlogInfoApi;

    @Operation(summary = "分页")
    @GetMapping("/query-page")
    public Result<Page<AdminLoginLogInfoPageResponse>> queryPage(@ParameterObject AdminLoginLogInfoPageRequest request) {
        return adminLoginlogInfoApi.queryPage(request);
    }

}
