package com.wzkris.system.impl.adminlog.login;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.system.api.adminlog.login.AdminLoginlogMngApi;
import com.wzkris.system.domain.AdminLoginLogDO;
import com.wzkris.system.request.adminlog.AdminLoginLogQueryRequest;
import com.wzkris.system.response.adminlog.AdminLoginLogResponse;
import com.wzkris.system.service.AdminLoginLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminLoginlogMngApiImpl extends AbstractApi implements AdminLoginlogMngApi {

    private final AdminLoginLogService adminLoginLogService;

    @Override
    public Result<Page<AdminLoginLogResponse>> queryPage(AdminLoginLogQueryRequest request) {
        startPage();
        List<AdminLoginLogDO> list = adminLoginLogService.list(request);
        return getDataTable(BeanUtil.convert(list, AdminLoginLogResponse.class));
    }

}
