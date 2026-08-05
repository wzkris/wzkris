package com.wzkris.usercenter.impl.tenantlog.operate;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.BeanCopierUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.model.AbstractApi;
import com.wzkris.usercenter.api.tenantlog.operate.TenantOperateLogInfoApi;
import com.wzkris.usercenter.api.tenantlog.operate.request.TenantOperateLogInfoPageRequest;
import com.wzkris.usercenter.api.tenantlog.operate.response.TenantOperateLogInfoResponse;
import com.wzkris.usercenter.domain.TenantOperateLogDO;
import com.wzkris.usercenter.service.TenantOperateLogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantOperateLogInfoApiImpl
        extends AbstractApi
        implements TenantOperateLogInfoApi {

    private final TenantOperateLogService tenantOperateLogService;

    @Override
    public Result<Page<TenantOperateLogInfoResponse>> queryPage(TenantOperateLogInfoPageRequest request) {
        IPage<TenantOperateLogDO> page = tenantOperateLogService.page(request.buildPage(), buildQueryWrapper(request));
        return ok(Page.of(page, BeanCopierUtil.copyList(page.getRecords(), TenantOperateLogInfoResponse.class)));
    }

    private LambdaQueryWrapper<TenantOperateLogDO> buildQueryWrapper(TenantOperateLogInfoPageRequest request) {
        return new LambdaQueryWrapper<TenantOperateLogDO>()
                .eq(TenantOperateLogDO::getMemberId, SecurityUtil.getUid())
                .eq(ObjectUtils.isNotEmpty(request.getSuccess()), TenantOperateLogDO::getSuccess, request.getSuccess())
                .eq(StringUtil.isNotEmpty(request.getTraceId()), TenantOperateLogDO::getTraceId, request.getTraceId())
                .like(StringUtil.isNotBlank(request.getTitle()), TenantOperateLogDO::getTitle, request.getTitle())
                .like(StringUtil.isNotBlank(request.getSubTitle()), TenantOperateLogDO::getSubTitle, request.getSubTitle())
                .eq(StringUtil.isNotEmpty(request.getOperType()), TenantOperateLogDO::getOperType, request.getOperType())
                .like(StringUtil.isNotBlank(request.getUsername()), TenantOperateLogDO::getUsername, request.getUsername())
                .between(request.getBeginTime() != null && request.getEndTime() != null,
                        TenantOperateLogDO::getOperTime,
                        request.getBeginTime(), request.getEndTime())
                .orderByDesc(TenantOperateLogDO::getId);
    }

}
