package com.wzkris.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.enums.RiskLevelEnum;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.system.domain.AdminLoginLogDO;
import com.wzkris.system.domain.req.adminlog.AdminLoginLogQueryReq;
import com.wzkris.system.mapper.AdminLoginLogMapper;
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
public class AdminLoginLogServiceImpl implements AdminLoginLogService {

    private final AdminLoginLogMapper adminLoginLogMapper;

    @Override
    public List<AdminLoginLogDO> list(AdminLoginLogQueryReq queryReq) {
        return adminLoginLogMapper.selectList(this.buildQueryWrapper(queryReq));
    }

    private LambdaQueryWrapper<AdminLoginLogDO> buildQueryWrapper(AdminLoginLogQueryReq queryReq) {
        return new LambdaQueryWrapper<AdminLoginLogDO>()
                .eq(ObjectUtils.isNotEmpty(queryReq.getAdminId()), AdminLoginLogDO::getAdminId, queryReq.getAdminId())
                .eq(ObjectUtils.isNotEmpty(queryReq.getSuccess()), AdminLoginLogDO::getSuccess, queryReq.getSuccess())
                .eq(StringUtil.isNotEmpty(queryReq.getTraceId()), AdminLoginLogDO::getTraceId, queryReq.getTraceId())
                .eq(StringUtil.isNotEmpty(queryReq.getRiskLevel()), AdminLoginLogDO::getRiskLevel, queryReq.getRiskLevel())
                .like(StringUtil.isNotEmpty(queryReq.getUsername()), AdminLoginLogDO::getUsername, queryReq.getUsername())
                .like(
                        StringUtil.isNotEmpty(queryReq.getLoginLocation()),
                        AdminLoginLogDO::getLoginLocation,
                        queryReq.getLoginLocation())
                .like(
                        StringUtil.isNotEmpty(queryReq.getAbnormalTag()),
                        AdminLoginLogDO::getAbnormalTags,
                        queryReq.getAbnormalTag())
                .ne(
                        Boolean.TRUE.equals(queryReq.getAbnormalOnly()),
                        AdminLoginLogDO::getRiskLevel,
                        RiskLevelEnum.LOW.getValue())
                .between(
                        queryReq.getParam("beginTime") != null && queryReq.getParam("endTime") != null,
                        AdminLoginLogDO::getLoginTime,
                        queryReq.getParam("beginTime"),
                        queryReq.getParam("endTime"))
                .orderByDesc(AdminLoginLogDO::getLogId);
    }

}
