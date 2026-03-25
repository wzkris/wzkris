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
    public List<TenantLoginLogDO> list(TenantLoginLogQueryRequest QueryRequest) {
        return baseMapper.selectList(this.buildQueryWrapper(QueryRequest));
    }

    private LambdaQueryWrapper<TenantLoginLogDO> buildQueryWrapper(TenantLoginLogQueryRequest QueryRequest) {
        return new LambdaQueryWrapper<TenantLoginLogDO>()
                .eq(ObjectUtils.isNotEmpty(QueryRequest.getMemberId()), TenantLoginLogDO::getMemberId, QueryRequest.getMemberId())
                .eq(ObjectUtils.isNotEmpty(QueryRequest.getSuccess()), TenantLoginLogDO::getSuccess, QueryRequest.getSuccess())
                .eq(StringUtil.isNotEmpty(QueryRequest.getTraceId()), TenantLoginLogDO::getTraceId, QueryRequest.getTraceId())
                .eq(StringUtil.isNotEmpty(QueryRequest.getRiskLevel()), TenantLoginLogDO::getRiskLevel, QueryRequest.getRiskLevel())
                .like(StringUtil.isNotEmpty(QueryRequest.getUsername()), TenantLoginLogDO::getUsername, QueryRequest.getUsername())
                .like(
                        StringUtil.isNotEmpty(QueryRequest.getLoginLocation()),
                        TenantLoginLogDO::getLoginLocation,
                        QueryRequest.getLoginLocation())
                .like(
                        StringUtil.isNotEmpty(QueryRequest.getAbnormalTag()),
                        TenantLoginLogDO::getAbnormalTags,
                        QueryRequest.getAbnormalTag())
                .ne(
                        Boolean.TRUE.equals(QueryRequest.getAbnormalOnly()),
                        TenantLoginLogDO::getRiskLevel,
                        RiskLevelEnum.LOW.getValue())
                .between(
                        QueryRequest.getParam("beginTime") != null && QueryRequest.getParam("endTime") != null,
                        TenantLoginLogDO::getLoginTime,
                        QueryRequest.getParam("beginTime"),
                        QueryRequest.getParam("endTime"))
                .orderByDesc(TenantLoginLogDO::getLogId);
    }

}

