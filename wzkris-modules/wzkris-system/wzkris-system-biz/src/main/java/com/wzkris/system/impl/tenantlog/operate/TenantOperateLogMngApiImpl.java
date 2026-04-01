package com.wzkris.system.impl.tenantlog.operate;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.system.api.tenantlog.operate.TenantOperateLogMngApi;
import com.wzkris.system.request.tenantlog.TenantOperateLogQueryRequest;
import com.wzkris.system.response.tenantlog.TenantOperateLogInfoResponse;
import com.wzkris.system.service.TenantOperateLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantOperateLogMngApiImpl extends AbstractApi implements TenantOperateLogMngApi {

    private final TenantOperateLogService tenantOperateLogService;

    @Override
    public Result<Page<TenantOperateLogInfoResponse>> page(TenantOperateLogQueryRequest request) {
        startPage();
        List<TenantOperateLogInfoResponse> list = tenantOperateLogService.listInfoVO(request);
        return getDataTable(list);
    }

}
