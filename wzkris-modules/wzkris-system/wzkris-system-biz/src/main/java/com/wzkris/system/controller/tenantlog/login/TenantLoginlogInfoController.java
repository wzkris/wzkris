package com.wzkris.system.controller.tenantlog.login;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.utils.TenantUtil;
import com.wzkris.system.domain.TenantLoginLogDO;
import com.wzkris.system.domain.req.tenantlog.TenantLoginLogQueryReq;
import com.wzkris.system.service.TenantLoginLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "租户个人登录日志信息")
@RestController
@RequestMapping("/tenant-loginlog-info")
@RequiredArgsConstructor
public class TenantLoginlogInfoController extends BaseController {

    private final TenantLoginLogService tenantLoginLogService;

    @Operation(summary = "分页")
    @GetMapping("/page")
    public Result<Page<TenantLoginLogDO>> page(TenantLoginLogQueryReq queryReq) {
        startPage();
        queryReq.setMemberId(TenantUtil.getId());
        List<TenantLoginLogDO> list = tenantLoginLogService.list(queryReq);
        return getDataTable(list);
    }

}
