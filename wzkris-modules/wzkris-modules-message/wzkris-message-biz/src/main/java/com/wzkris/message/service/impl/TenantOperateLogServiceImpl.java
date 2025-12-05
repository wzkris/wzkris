package com.wzkris.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.message.domain.TenantOperateLogDO;
import com.wzkris.message.domain.req.tenantlog.TenantOperateLogQueryReq;
import com.wzkris.message.domain.vo.tenantlog.TenantOperateLogInfoVO;
import com.wzkris.message.mapper.TenantOperateLogMapper;
import com.wzkris.message.service.TenantOperateLogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantOperateLogServiceImpl implements TenantOperateLogService {

    private final TenantOperateLogMapper tenantOperateLogMapper;

    @Override
    public List<TenantOperateLogDO> list(TenantOperateLogQueryReq queryReq) {
        return tenantOperateLogMapper.selectList(this.buildQueryWrapper(queryReq));
    }

    @Override
    public List<TenantOperateLogInfoVO> listInfoVO(TenantOperateLogQueryReq queryReq) {
        return tenantOperateLogMapper.selectListInfoVO(this.buildQueryWrapper(queryReq));
    }

    private LambdaQueryWrapper<TenantOperateLogDO> buildQueryWrapper(TenantOperateLogQueryReq queryReq) {
        return new LambdaQueryWrapper<TenantOperateLogDO>()
                .eq(ObjectUtils.isNotEmpty(queryReq.getMemberId()), TenantOperateLogDO::getMemberId, queryReq.getMemberId())
                .eq(ObjectUtils.isNotEmpty(queryReq.getSuccess()), TenantOperateLogDO::getSuccess, queryReq.getSuccess())
                .like(StringUtil.isNotBlank(queryReq.getTitle()), TenantOperateLogDO::getTitle, queryReq.getTitle())
                .like(StringUtil.isNotBlank(queryReq.getSubTitle()), TenantOperateLogDO::getSubTitle, queryReq.getSubTitle())
                .eq(StringUtil.isNotEmpty(queryReq.getOperType()), TenantOperateLogDO::getOperType, queryReq.getOperType())
                .like(StringUtil.isNotBlank(queryReq.getUsername()), TenantOperateLogDO::getUsername, queryReq.getUsername())
                .between(
                        queryReq.getParam("beginTime") != null && queryReq.getParam("endTime") != null,
                        TenantOperateLogDO::getOperTime,
                        queryReq.getParam("beginTime"),
                        queryReq.getParam("endTime"))
                .orderByDesc(TenantOperateLogDO::getOperId);
    }

}
