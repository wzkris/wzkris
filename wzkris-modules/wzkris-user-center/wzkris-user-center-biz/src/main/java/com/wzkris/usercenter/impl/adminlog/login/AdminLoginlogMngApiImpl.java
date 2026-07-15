package com.wzkris.usercenter.impl.adminlog.login;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.api.adminlog.login.AdminLoginlogMngApi;
import com.wzkris.usercenter.api.adminlog.login.request.AdminLoginLogMngPageRequest;
import com.wzkris.usercenter.api.adminlog.login.response.AdminLoginLogMngResponse;
import com.wzkris.usercenter.domain.AdminLoginLogDO;
import com.wzkris.usercenter.service.AdminLoginLogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminLoginlogMngApiImpl
        extends AbstractApi
        implements AdminLoginlogMngApi {

    private final AdminLoginLogService adminLoginLogService;

    @Override
    public Result<Page<AdminLoginLogMngResponse>> queryPage(AdminLoginLogMngPageRequest request) {
        IPage<AdminLoginLogDO> page = adminLoginLogService.page(request.buildPage(), buildQueryWrapper(request));
        return ok(Page.of(page, BeanUtil.convert(page.getRecords(), AdminLoginLogMngResponse.class)));
    }

    private LambdaQueryWrapper<AdminLoginLogDO> buildQueryWrapper(AdminLoginLogMngPageRequest request) {
        return new LambdaQueryWrapper<AdminLoginLogDO>()
                .eq(ObjectUtils.isNotEmpty(request.getAdminId()), AdminLoginLogDO::getAdminId, request.getAdminId())
                .eq(ObjectUtils.isNotEmpty(request.getSuccess()), AdminLoginLogDO::getSuccess, request.getSuccess())
                .eq(StringUtil.isNotEmpty(request.getTraceId()), AdminLoginLogDO::getTraceId, request.getTraceId())
                .like(StringUtil.isNotEmpty(request.getUsername()), AdminLoginLogDO::getUsername, request.getUsername())
                .like(StringUtil.isNotEmpty(request.getLoginLocation()), AdminLoginLogDO::getLoginLocation, request.getLoginLocation())
                .between(request.getBeginTime() != null && request.getEndTime() != null,
                        AdminLoginLogDO::getLoginTime,
                        request.getBeginTime(),
                        request.getEndTime())
                .orderByDesc(AdminLoginLogDO::getLogId);
    }

}
