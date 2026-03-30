package com.wzkris.system.impl.tenantlog.login;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.system.api.tenantlog.login.TenantLoginlogInfoApi;
import com.wzkris.system.domain.TenantLoginLogDO;
import com.wzkris.system.request.tenantlog.TenantLoginLogQueryRequest;
import com.wzkris.system.response.tenantlog.TenantLoginLogResponse;
import com.wzkris.system.service.TenantLoginLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantLoginlogInfoApiImpl extends AbstractApi implements TenantLoginlogInfoApi {

    private final TenantLoginLogService tenantLoginLogService;

    @Override
    public Result<Page<TenantLoginLogResponse>> queryPage(TenantLoginLogQueryRequest request) {
        startPage();
        request.setMemberId(SecurityUtil.getUid());
        List<TenantLoginLogDO> list = tenantLoginLogService.list(request);
        return getDataTable(BeanUtil.convert(list, TenantLoginLogResponse.class));
    }

}
