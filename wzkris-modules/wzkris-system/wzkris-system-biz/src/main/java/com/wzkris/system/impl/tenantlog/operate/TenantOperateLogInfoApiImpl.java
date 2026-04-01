package com.wzkris.system.impl.tenantlog.operate;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.system.api.tenantlog.operate.TenantOperateLogInfoApi;
import com.wzkris.system.request.tenantlog.TenantOperateLogQueryRequest;
import com.wzkris.system.response.tenantlog.TenantOperateLogInfoResponse;
import com.wzkris.system.service.TenantOperateLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantOperateLogInfoApiImpl extends AbstractApi implements TenantOperateLogInfoApi {

    private final TenantOperateLogService tenantOperateLogService;

    @Override
    public Result<Page<TenantOperateLogInfoResponse>> page(TenantOperateLogQueryRequest request) {
        startPage();
        request.setMemberId(SecurityUtil.getUid());
        List<TenantOperateLogInfoResponse> list = tenantOperateLogService.listInfoVO(request);
        return getDataTable(list);
    }

}
