package com.wzkris.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.message.domain.TenantLoginLogDO;
import com.wzkris.message.domain.req.tenantlog.TenantLoginLogQueryReq;
import com.wzkris.message.mapper.TenantLoginLogMapper;
import com.wzkris.message.service.TenantLoginLogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantLoginLogServiceImpl implements TenantLoginLogService {

    private final TenantLoginLogMapper tenantLoginLogMapper;

    @Override
    public List<TenantLoginLogDO> list(TenantLoginLogQueryReq queryReq) {
        return tenantLoginLogMapper.selectList(this.buildQueryWrapper(queryReq));
    }

    private LambdaQueryWrapper<TenantLoginLogDO> buildQueryWrapper(TenantLoginLogQueryReq queryReq) {
        return new LambdaQueryWrapper<TenantLoginLogDO>()
                .eq(ObjectUtils.isNotEmpty(queryReq.getMemberId()), TenantLoginLogDO::getMemberId, queryReq.getMemberId())
                .eq(ObjectUtils.isNotEmpty(queryReq.getSuccess()), TenantLoginLogDO::getSuccess, queryReq.getSuccess())
                .like(StringUtil.isNotEmpty(queryReq.getUsername()), TenantLoginLogDO::getUsername, queryReq.getUsername())
                .like(
                        StringUtil.isNotEmpty(queryReq.getLoginLocation()),
                        TenantLoginLogDO::getLoginLocation,
                        queryReq.getLoginLocation())
                .between(
                        queryReq.getParam("beginTime") != null && queryReq.getParam("endTime") != null,
                        TenantLoginLogDO::getLoginTime,
                        queryReq.getParam("beginTime"),
                        queryReq.getParam("endTime"))
                .orderByDesc(TenantLoginLogDO::getLogId);
    }

}
