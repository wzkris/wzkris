package com.thingslink.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.system.domain.OperLog;
import com.thingslink.system.mapper.OperLogMapper;
import com.thingslink.system.service.OperLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作日志 服务层处理
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class OperLogServiceImpl implements OperLogService {
    private final OperLogMapper operLogMapper;

    @Override
    public List<OperLog> list(OperLog operLog) {
        LambdaQueryWrapper<OperLog> lqw = this.buildQueryWrapper(operLog);
        return operLogMapper.selectList(lqw);
    }

    private LambdaQueryWrapper<OperLog> buildQueryWrapper(OperLog operLog) {
        return new LambdaQueryWrapper<OperLog>()
                .like(StringUtil.isNotBlank(operLog.getOperIp()), OperLog::getOperIp, operLog.getOperIp())
                .eq(StringUtil.isNotBlank(operLog.getStatus()), OperLog::getStatus, operLog.getStatus())
                .like(StringUtil.isNotBlank(operLog.getTitle()), OperLog::getTitle, operLog.getTitle())
                .eq(StringUtil.isNotNull(operLog.getBusinessType()), OperLog::getBusinessType, operLog.getBusinessType())
                .eq(StringUtil.isNotNull(operLog.getStatus()), OperLog::getStatus, operLog.getStatus())
                .like(StringUtil.isNotBlank(operLog.getOperName()), OperLog::getOperName, operLog.getOperName())
                .between(operLog.getParams().get("beginTime") != null && operLog.getParams().get("endTime") != null,
                        OperLog::getOperTime, operLog.getParams().get("beginTime"), operLog.getParams().get("endTime"))
                .orderByDesc(OperLog::getOperId);
    }
}
