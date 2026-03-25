package com.wzkris.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.system.domain.TenantOperateLogDO;
import com.wzkris.system.mapper.TenantOperateLogMapper;
import com.wzkris.system.request.tenantlog.TenantOperateLogQueryRequest;
import com.wzkris.system.response.tenantlog.TenantOperateLogInfoResponse;
import com.wzkris.system.service.TenantOperateLogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantOperateLogServiceImpl
        extends ServiceImpl<TenantOperateLogMapper, TenantOperateLogDO>
        implements TenantOperateLogService {

    @Override
    public List<TenantOperateLogDO> list(TenantOperateLogQueryRequest QueryRequest) {
        return baseMapper.selectList(this.buildQueryWrapper(QueryRequest));
    }

    @Override
    public List<TenantOperateLogInfoResponse> listInfoVO(TenantOperateLogQueryRequest QueryRequest) {
        return baseMapper.selectListInfoVO(this.buildQueryWrapper(QueryRequest));
    }

    private LambdaQueryWrapper<TenantOperateLogDO> buildQueryWrapper(TenantOperateLogQueryRequest QueryRequest) {
        return new LambdaQueryWrapper<TenantOperateLogDO>()
                .eq(ObjectUtils.isNotEmpty(QueryRequest.getMemberId()), TenantOperateLogDO::getMemberId, QueryRequest.getMemberId())
                .eq(ObjectUtils.isNotEmpty(QueryRequest.getSuccess()), TenantOperateLogDO::getSuccess, QueryRequest.getSuccess())
                .like(StringUtil.isNotBlank(QueryRequest.getTitle()), TenantOperateLogDO::getTitle, QueryRequest.getTitle())
                .like(StringUtil.isNotBlank(QueryRequest.getSubTitle()), TenantOperateLogDO::getSubTitle, QueryRequest.getSubTitle())
                .eq(StringUtil.isNotEmpty(QueryRequest.getOperType()), TenantOperateLogDO::getOperType, QueryRequest.getOperType())
                .like(StringUtil.isNotBlank(QueryRequest.getUsername()), TenantOperateLogDO::getUsername, QueryRequest.getUsername())
                .between(
                        QueryRequest.getParam("beginTime") != null && QueryRequest.getParam("endTime") != null,
                        TenantOperateLogDO::getOperTime,
                        QueryRequest.getParam("beginTime"),
                        QueryRequest.getParam("endTime"))
                .orderByDesc(TenantOperateLogDO::getOperId);
    }

}

