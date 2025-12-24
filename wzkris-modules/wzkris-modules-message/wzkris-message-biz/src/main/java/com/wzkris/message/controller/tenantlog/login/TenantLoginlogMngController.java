package com.wzkris.message.controller.tenantlog.login;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckTenantPerms;
import com.wzkris.message.domain.TenantLoginLogDO;
import com.wzkris.message.domain.req.tenantlog.TenantLoginLogQueryReq;
import com.wzkris.message.mapper.TenantLoginLogMapper;
import com.wzkris.message.service.TenantLoginLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "租户登录日志管理")
@RestController
@RequestMapping("/tenant-loginlog-manage")
@RequiredArgsConstructor
public class TenantLoginlogMngController extends BaseController {

    private final TenantLoginLogService tenantLoginLogService;

    private final TenantLoginLogMapper tenantLoginLogMapper;

    @Operation(summary = "分页")
    @GetMapping("/page")
    @CheckTenantPerms("msg-mod:tenant-loginlog-mng:page")
    public Result<Page<TenantLoginLogDO>> page(TenantLoginLogQueryReq queryReq) {
        startPage();
        List<TenantLoginLogDO> list = tenantLoginLogService.list(queryReq);
        return getDataTable(list);
    }

}
