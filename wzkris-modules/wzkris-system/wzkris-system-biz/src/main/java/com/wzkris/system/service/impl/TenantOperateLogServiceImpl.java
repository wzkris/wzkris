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
    public List<TenantOperateLogDO> list(TenantOperateLogQueryRequest request) {
        return baseMapper.selectList(this.buildQueryWrapper(request));
    }

    @Override
    public List<TenantOperateLogInfoResponse> listInfoVO(TenantOperateLogQueryRequest request) {
        return baseMapper.selectListInfoVO(this.buildQueryWrapper(request));
    }

    private LambdaQueryWrapper<TenantOperateLogDO> buildQueryWrapper(TenantOperateLogQueryRequest request) {
        return new LambdaQueryWrapper<TenantOperateLogDO>()
                .eq(ObjectUtils.isNotEmpty(request.getMemberId()), TenantOperateLogDO::getMemberId, request.getMemberId())
                .eq(ObjectUtils.isNotEmpty(request.getSuccess()), TenantOperateLogDO::getSuccess, request.getSuccess())
                .like(StringUtil.isNotBlank(request.getTitle()), TenantOperateLogDO::getTitle, request.getTitle())
                .like(StringUtil.isNotBlank(request.getSubTitle()), TenantOperateLogDO::getSubTitle, request.getSubTitle())
                .eq(StringUtil.isNotEmpty(request.getOperType()), TenantOperateLogDO::getOperType, request.getOperType())
                .like(StringUtil.isNotBlank(request.getUsername()), TenantOperateLogDO::getUsername, request.getUsername())
                .between(request.getBeginTime() != null && request.getEndTime() != null,
                        TenantOperateLogDO::getOperTime,
                        request.getBeginTime(), request.getEndTime())
                .orderByDesc(TenantOperateLogDO::getOperId);
    }

}

