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
    public List<AdminLoginLogDO> list(AdminLoginLogQueryRequest request) {
        return baseMapper.selectList(this.buildQueryWrapper(request));
    }

    private LambdaQueryWrapper<AdminLoginLogDO> buildQueryWrapper(AdminLoginLogQueryRequest request) {
        return new LambdaQueryWrapper<AdminLoginLogDO>()
                .eq(ObjectUtils.isNotEmpty(request.getAdminId()), AdminLoginLogDO::getAdminId, request.getAdminId())
                .eq(ObjectUtils.isNotEmpty(request.getSuccess()), AdminLoginLogDO::getSuccess, request.getSuccess())
                .eq(StringUtil.isNotEmpty(request.getTraceId()), AdminLoginLogDO::getTraceId, request.getTraceId())
                .eq(StringUtil.isNotEmpty(request.getRiskLevel()), AdminLoginLogDO::getRiskLevel, request.getRiskLevel())
                .like(StringUtil.isNotEmpty(request.getUsername()), AdminLoginLogDO::getUsername, request.getUsername())
                .like(StringUtil.isNotEmpty(request.getLoginLocation()),
                        AdminLoginLogDO::getLoginLocation,
                        request.getLoginLocation())
                .like(StringUtil.isNotEmpty(request.getAbnormalTag()),
                        AdminLoginLogDO::getAbnormalTags,
                        request.getAbnormalTag())
                .ne(Boolean.TRUE.equals(request.getAbnormalOnly()),
                        AdminLoginLogDO::getRiskLevel,
                        RiskLevelEnum.LOW.getValue())
                .between(request.getBeginTime() != null && request.getEndTime() != null,
                        AdminLoginLogDO::getLoginTime,
                        request.getBeginTime(),
                        request.getEndTime())
                .orderByDesc(AdminLoginLogDO::getLogId);
    }

}

