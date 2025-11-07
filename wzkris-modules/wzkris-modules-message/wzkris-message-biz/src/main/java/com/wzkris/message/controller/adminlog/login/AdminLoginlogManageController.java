package com.wzkris.message.controller.adminlog.login;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckUserPerms;
import com.wzkris.message.domain.AdminLoginLogDO;
import com.wzkris.message.domain.req.userlog.AdminLoginLogQueryReq;
import com.wzkris.message.mapper.AdminLoginLogMapper;
import com.wzkris.message.service.AdminLoginLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "管理员登录日志管理")
@RestController
@RequestMapping("/admin-loginlog-manage")
@RequiredArgsConstructor
public class AdminLoginlogManageController extends BaseController {

    private final AdminLoginLogService adminLoginLogService;

    private final AdminLoginLogMapper adminLoginLogMapper;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @CheckUserPerms("msg-mod:admin-loginlog-mng:list")
    public Result<Page<AdminLoginLogDO>> list(AdminLoginLogQueryReq queryReq) {
        startPage();
        List<AdminLoginLogDO> list = adminLoginLogService.list(queryReq);
        return getDataTable(list);
    }

}
