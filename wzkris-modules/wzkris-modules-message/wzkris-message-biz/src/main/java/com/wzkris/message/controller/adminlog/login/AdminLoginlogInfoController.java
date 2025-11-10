package com.wzkris.message.controller.adminlog.login;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.utils.AdminUtil;
import com.wzkris.message.domain.AdminLoginLogDO;
import com.wzkris.message.domain.req.adminlog.AdminLoginLogQueryReq;
import com.wzkris.message.service.AdminLoginLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "管理员登录日志信息")
@RestController
@RequestMapping("/admin-loginlog-info")
@RequiredArgsConstructor
public class AdminLoginlogInfoController extends BaseController {

    private final AdminLoginLogService adminLoginLogService;

    @Operation(summary = "分页")
    @GetMapping("/list")
    public Result<Page<AdminLoginLogDO>> list(AdminLoginLogQueryReq queryReq) {
        startPage();
        queryReq.setAdminId(AdminUtil.getId());
        List<AdminLoginLogDO> list = adminLoginLogService.list(queryReq);
        return getDataTable(list);
    }

}
