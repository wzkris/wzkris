package com.wzkris.system.impl.tenantlog.login;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.enums.RiskLevelEnum;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.model.AbstractApi;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.system.api.tenantlog.login.TenantLoginlogInfoApi;
import com.wzkris.system.domain.TenantLoginLogDO;
import com.wzkris.system.request.tenantlog.TenantLoginLogInfoQueryRequest;
import com.wzkris.system.response.tenantlog.TenantLoginLogInfoResponse;
import com.wzkris.system.service.TenantLoginLogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantLoginlogInfoApiImpl
        extends AbstractApi
        implements TenantLoginlogInfoApi {

    private final TenantLoginLogService tenantLoginLogService;

    @Override
    public Result<Page<TenantLoginLogInfoResponse>> queryPage(TenantLoginLogInfoQueryRequest request) {
        startPage();
        List<TenantLoginLogDO> list = tenantLoginLogService.list(buildQueryWrapper(request));
        return getPageResult(BeanUtil.convert(list, TenantLoginLogInfoResponse.class));
    }

    private LambdaQueryWrapper<TenantLoginLogDO> buildQueryWrapper(TenantLoginLogInfoQueryRequest request) {
        return new LambdaQueryWrapper<TenantLoginLogDO>()
                .eq(TenantLoginLogDO::getMemberId, SecurityUtil.getUid())
                .eq(ObjectUtils.isNotEmpty(request.getSuccess()), TenantLoginLogDO::getSuccess, request.getSuccess())
                .eq(StringUtil.isNotEmpty(request.getTraceId()), TenantLoginLogDO::getTraceId, request.getTraceId())
                .eq(StringUtil.isNotEmpty(request.getRiskLevel()), TenantLoginLogDO::getRiskLevel, request.getRiskLevel())
                .like(StringUtil.isNotEmpty(request.getUsername()), TenantLoginLogDO::getUsername, request.getUsername())
                .like(StringUtil.isNotEmpty(request.getLoginLocation()), TenantLoginLogDO::getLoginLocation, request.getLoginLocation())
                .like(StringUtil.isNotEmpty(request.getAbnormalTag()), TenantLoginLogDO::getAbnormalTags, request.getAbnormalTag())
                .ne(Boolean.TRUE.equals(request.getAbnormalOnly()), TenantLoginLogDO::getRiskLevel, RiskLevelEnum.LOW.getValue())
                .between(request.getBeginTime() != null && request.getEndTime() != null,
                        TenantLoginLogDO::getLoginTime,
                        request.getBeginTime(),
                        request.getEndTime())
                .orderByDesc(TenantLoginLogDO::getLogId);
    }

}
