package com.wzkris.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzkris.common.core.enums.RiskLevelEnum;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.system.domain.TenantLoginLogDO;
import com.wzkris.system.mapper.TenantLoginLogMapper;
import com.wzkris.system.request.tenantlog.TenantLoginLogQueryRequest;
import com.wzkris.system.service.TenantLoginLogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantLoginLogServiceImpl
        extends ServiceImpl<TenantLoginLogMapper, TenantLoginLogDO>
        implements TenantLoginLogService {

    @Override
    public List<TenantLoginLogDO> list(TenantLoginLogQueryRequest request) {
        return baseMapper.selectList(this.buildQueryWrapper(request));
    }

    private LambdaQueryWrapper<TenantLoginLogDO> buildQueryWrapper(TenantLoginLogQueryRequest request) {
        return new LambdaQueryWrapper<TenantLoginLogDO>()
                .eq(ObjectUtils.isNotEmpty(request.getMemberId()), TenantLoginLogDO::getMemberId, request.getMemberId())
                .eq(ObjectUtils.isNotEmpty(request.getSuccess()), TenantLoginLogDO::getSuccess, request.getSuccess())
                .eq(StringUtil.isNotEmpty(request.getTraceId()), TenantLoginLogDO::getTraceId, request.getTraceId())
                .eq(StringUtil.isNotEmpty(request.getRiskLevel()), TenantLoginLogDO::getRiskLevel, request.getRiskLevel())
                .like(StringUtil.isNotEmpty(request.getUsername()), TenantLoginLogDO::getUsername, request.getUsername())
                .like(StringUtil.isNotEmpty(request.getLoginLocation()),
                        TenantLoginLogDO::getLoginLocation,
                        request.getLoginLocation())
                .like(StringUtil.isNotEmpty(request.getAbnormalTag()),
                        TenantLoginLogDO::getAbnormalTags,
                        request.getAbnormalTag())
                .ne(Boolean.TRUE.equals(request.getAbnormalOnly()),
                        TenantLoginLogDO::getRiskLevel,
                        RiskLevelEnum.LOW.getValue())
                .between(request.getBeginTime() != null && request.getEndTime() != null,
                        TenantLoginLogDO::getLoginTime,
                        request.getBeginTime(), request.getEndTime())
                .orderByDesc(TenantLoginLogDO::getLogId);
    }

}

