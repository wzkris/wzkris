package com.wzkris.usercenter.impl.tenantlog.login;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.core.utils.BeanCopierUtil;
import com.wzkris.usercenter.api.tenantlog.login.TenantLoginlogMngApi;
import com.wzkris.usercenter.api.tenantlog.login.request.TenantLoginLogMngPageRequest;
import com.wzkris.usercenter.api.tenantlog.login.response.TenantLoginLogMngResponse;
import com.wzkris.usercenter.domain.TenantLoginLogDO;
import com.wzkris.usercenter.service.TenantLoginLogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantLoginlogMngApiImpl
        extends AbstractApi
        implements TenantLoginlogMngApi {

    private final TenantLoginLogService tenantLoginLogService;

    @Override
    public Result<Page<TenantLoginLogMngResponse>> queryPage(TenantLoginLogMngPageRequest request) {
        IPage<TenantLoginLogDO> page = tenantLoginLogService.page(request.buildPage(), buildQueryWrapper(request));
        return ok(Page.of(page, BeanCopierUtil.copyList(page.getRecords(), TenantLoginLogMngResponse.class)));
    }

    private LambdaQueryWrapper<TenantLoginLogDO> buildQueryWrapper(TenantLoginLogMngPageRequest request) {
        return new LambdaQueryWrapper<TenantLoginLogDO>()
                .eq(ObjectUtils.isNotEmpty(request.getMemberId()), TenantLoginLogDO::getMemberId, request.getMemberId())
                .eq(ObjectUtils.isNotEmpty(request.getSuccess()), TenantLoginLogDO::getSuccess, request.getSuccess())
                .eq(StringUtil.isNotEmpty(request.getTraceId()), TenantLoginLogDO::getTraceId, request.getTraceId())
                .like(StringUtil.isNotEmpty(request.getUsername()), TenantLoginLogDO::getUsername, request.getUsername())
                .like(StringUtil.isNotEmpty(request.getLoginLocation()), TenantLoginLogDO::getLoginLocation, request.getLoginLocation())
                .between(request.getBeginTime() != null && request.getEndTime() != null,
                        TenantLoginLogDO::getLoginTime,
                        request.getBeginTime(),
                        request.getEndTime())
                .orderByDesc(TenantLoginLogDO::getLogId);
    }

}
