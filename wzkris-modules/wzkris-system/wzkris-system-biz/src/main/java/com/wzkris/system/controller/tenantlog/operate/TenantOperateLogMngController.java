package com.wzkris.system.controller.tenantlog.operate;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckTenantPerms;
import com.wzkris.system.domain.req.tenantlog.TenantOperateLogQueryReq;
import com.wzkris.system.domain.vo.tenantlog.TenantOperateLogInfoVO;
import com.wzkris.system.mapper.TenantOperateLogMapper;
import com.wzkris.system.service.TenantOperateLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 操作日志记录
 *
 * @author wzkris
 */
@Tag(name = "租户操作日志管理")
@RestController
@RequestMapping("/tenant-operatelog-manage")
@RequiredArgsConstructor
public class TenantOperateLogMngController extends BaseController {

    private final TenantOperateLogMapper tenantOperateLogMapper;

    private final TenantOperateLogService tenantOperateLogService;

    @Operation(summary = "分页")
    @GetMapping("/page")
    @CheckTenantPerms("system-mod:tenant-operatelog-mng:page")
    public Result<Page<TenantOperateLogInfoVO>> page(TenantOperateLogQueryReq queryReq) {
        startPage();
        List<TenantOperateLogInfoVO> list = tenantOperateLogService.listInfoVO(queryReq);
        return getDataTable(list);
    }

}
