package com.wzkris.system.impl.tenantlog.login;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.enums.RiskLevelEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.system.api.tenantlog.login.TenantLoginlogMngApi;
import com.wzkris.system.api.tenantlog.request.TenantLoginLogMngPageRequest;
import com.wzkris.system.api.tenantlog.response.TenantLoginLogMngResponse;
import com.wzkris.system.domain.TenantLoginLogDO;
import com.wzkris.system.service.TenantLoginLogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantLoginlogMngApiImpl
        extends AbstractApi
        implements TenantLoginlogMngApi {

    private final TenantLoginLogService tenantLoginLogService;

    @Override
    public Result<Page<TenantLoginLogMngResponse>> queryPage(TenantLoginLogMngPageRequest request) {
        startPage(request);
        List<TenantLoginLogDO> list = tenantLoginLogService.list(buildQueryWrapper(request));
        return getPageResult(BeanUtil.convert(list, TenantLoginLogMngResponse.class));
    }

    private LambdaQueryWrapper<TenantLoginLogDO> buildQueryWrapper(TenantLoginLogMngPageRequest request) {
        return new LambdaQueryWrapper<TenantLoginLogDO>()
                .eq(ObjectUtils.isNotEmpty(request.getMemberId()), TenantLoginLogDO::getMemberId, request.getMemberId())
                .eq(ObjectUtils.isNotEmpty(request.getSuccess()), TenantLoginLogDO::getSuccess, request.getSuccess())
                .eq(StringUtil.isNotEmpty(request.getTraceId()), TenantLoginLogDO::getTraceId, request.getTraceId())
                .eq(ObjectUtils.isNotEmpty(request.getRiskLevel()), TenantLoginLogDO::getRiskLevel, request.getRiskLevel())
                .like(StringUtil.isNotEmpty(request.getUsername()), TenantLoginLogDO::getUsername, request.getUsername())
                .like(StringUtil.isNotEmpty(request.getLoginLocation()), TenantLoginLogDO::getLoginLocation, request.getLoginLocation())
                .like(StringUtil.isNotEmpty(request.getAbnormalTag()), TenantLoginLogDO::getAbnormalTags, request.getAbnormalTag())
                .ne(Boolean.TRUE.equals(request.getAbnormalOnly()), TenantLoginLogDO::getRiskLevel, RiskLevelEnum.LOW)
                .between(request.getBeginTime() != null && request.getEndTime() != null,
                        TenantLoginLogDO::getLoginTime,
                        request.getBeginTime(),
                        request.getEndTime())
                .orderByDesc(TenantLoginLogDO::getLogId);
    }

}
