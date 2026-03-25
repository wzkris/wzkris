package com.wzkris.system.impl.adminlog.operate;

import com.wzkris.common.core.model.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.system.api.adminlog.operate.AdminOperateLogInfoApi;
import com.wzkris.system.domain.AdminOperateLogDO;
import com.wzkris.system.request.adminlog.AdminOperateLogQueryRequest;
import com.wzkris.system.response.adminlog.AdminOperateLogResponse;
import com.wzkris.system.service.AdminOperateLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOperateLogInfoApiImpl extends BaseController implements AdminOperateLogInfoApi {

    private final AdminOperateLogService adminOperateLogService;

    @Override
    public Result<Page<AdminOperateLogResponse>> queryPage(AdminOperateLogQueryRequest request) {
        startPage();
        request.setAdminId(SecurityUtil.getUid());
        List<AdminOperateLogDO> list = adminOperateLogService.list(request);
        return getDataTable(BeanUtil.convert(list, AdminOperateLogResponse.class));
    }

}
