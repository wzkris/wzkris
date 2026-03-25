package com.wzkris.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzkris.common.core.enums.RiskLevelEnum;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.system.domain.AdminLoginLogDO;
import com.wzkris.system.mapper.AdminLoginLogMapper;
import com.wzkris.system.request.adminlog.AdminLoginLogQueryRequest;
import com.wzkris.system.service.AdminLoginLogService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 登录日志
 * @date : 2024/1/10 13:55
 */
@Service
@RequiredArgsConstructor
public class AdminLoginLogServiceImpl
        extends ServiceImpl<AdminLoginLogMapper, AdminLoginLogDO>
        implements AdminLoginLogService {

    @Override
    public List<AdminLoginLogDO> list(AdminLoginLogQueryRequest QueryRequest) {
        return baseMapper.selectList(this.buildQueryWrapper(QueryRequest));
    }

    private LambdaQueryWrapper<AdminLoginLogDO> buildQueryWrapper(AdminLoginLogQueryRequest QueryRequest) {
        return new LambdaQueryWrapper<AdminLoginLogDO>()
                .eq(ObjectUtils.isNotEmpty(QueryRequest.getAdminId()), AdminLoginLogDO::getAdminId, QueryRequest.getAdminId())
                .eq(ObjectUtils.isNotEmpty(QueryRequest.getSuccess()), AdminLoginLogDO::getSuccess, QueryRequest.getSuccess())
                .eq(StringUtil.isNotEmpty(QueryRequest.getTraceId()), AdminLoginLogDO::getTraceId, QueryRequest.getTraceId())
                .eq(StringUtil.isNotEmpty(QueryRequest.getRiskLevel()), AdminLoginLogDO::getRiskLevel, QueryRequest.getRiskLevel())
                .like(StringUtil.isNotEmpty(QueryRequest.getUsername()), AdminLoginLogDO::getUsername, QueryRequest.getUsername())
                .like(
                        StringUtil.isNotEmpty(QueryRequest.getLoginLocation()),
                        AdminLoginLogDO::getLoginLocation,
                        QueryRequest.getLoginLocation())
                .like(
                        StringUtil.isNotEmpty(QueryRequest.getAbnormalTag()),
                        AdminLoginLogDO::getAbnormalTags,
                        QueryRequest.getAbnormalTag())
                .ne(
                        Boolean.TRUE.equals(QueryRequest.getAbnormalOnly()),
                        AdminLoginLogDO::getRiskLevel,
                        RiskLevelEnum.LOW.getValue())
                .between(
                        QueryRequest.getParam("beginTime") != null && QueryRequest.getParam("endTime") != null,
                        AdminLoginLogDO::getLoginTime,
                        QueryRequest.getParam("beginTime"),
                        QueryRequest.getParam("endTime"))
                .orderByDesc(AdminLoginLogDO::getLogId);
    }

}

