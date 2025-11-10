package com.wzkris.message.controller.tenantlog.operate;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckTenantPerms;
import com.wzkris.message.domain.req.tenantlog.TenantOperateLogQueryReq;
import com.wzkris.message.domain.vo.tenantlog.TenantOperateLogInfoVO;
import com.wzkris.message.mapper.TenantOperateLogMapper;
import com.wzkris.message.service.TenantOperateLogService;
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
@Tag(name = "租户成员操作日志管理")
@RestController
@RequestMapping("/tenant-operatelog-manage")
@RequiredArgsConstructor
public class TenantOperateLogMngController extends BaseController {

    private final TenantOperateLogMapper tenantOperateLogMapper;

    private final TenantOperateLogService tenantOperateLogService;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @CheckTenantPerms("msg-mod:tenant-operatelog-mng:list")
    public Result<Page<TenantOperateLogInfoVO>> list(TenantOperateLogQueryReq queryReq) {
        startPage();
        List<TenantOperateLogInfoVO> list = tenantOperateLogService.listInfoVO(queryReq);
        return getDataTable(list);
    }

}
