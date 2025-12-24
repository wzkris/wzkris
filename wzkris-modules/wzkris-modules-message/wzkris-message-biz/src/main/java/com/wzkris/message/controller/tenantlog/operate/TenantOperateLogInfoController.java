package com.wzkris.message.controller.tenantlog.operate;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.utils.TenantUtil;
import com.wzkris.message.domain.req.tenantlog.TenantOperateLogQueryReq;
import com.wzkris.message.domain.vo.tenantlog.TenantOperateLogInfoVO;
import com.wzkris.message.service.TenantOperateLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "租户个人操作日志信息")
@RestController
@RequestMapping("/tenant-operatelog-info")
@RequiredArgsConstructor
public class TenantOperateLogInfoController extends BaseController {

    private final TenantOperateLogService tenantOperateLogService;

    @Operation(summary = "分页")
    @GetMapping("/page")
    public Result<Page<TenantOperateLogInfoVO>> page(TenantOperateLogQueryReq queryReq) {
        startPage();
        queryReq.setMemberId(TenantUtil.getId());
        List<TenantOperateLogInfoVO> list = tenantOperateLogService.listInfoVO(queryReq);
        return getDataTable(list);
    }

}
