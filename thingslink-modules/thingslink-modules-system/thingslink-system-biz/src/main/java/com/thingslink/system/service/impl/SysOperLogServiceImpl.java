package com.thingslink.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.system.domain.SysOperLog;
import com.thingslink.system.mapper.SysOperLogMapper;
import com.thingslink.system.service.SysOperLogService;
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
public class SysOperLogServiceImpl implements SysOperLogService {
    private final SysOperLogMapper sysOperLogMapper;

    @Override
    public List<SysOperLog> list(SysOperLog sysOperLog) {
        LambdaQueryWrapper<SysOperLog> lqw = this.buildQueryWrapper(sysOperLog);
        return sysOperLogMapper.selectList(lqw);
    }

    private LambdaQueryWrapper<SysOperLog> buildQueryWrapper(SysOperLog sysOperLog) {
        return new LambdaQueryWrapper<SysOperLog>()
                .like(StringUtil.isNotBlank(sysOperLog.getOperIp()), SysOperLog::getOperIp, sysOperLog.getOperIp())
                .eq(StringUtil.isNotBlank(sysOperLog.getStatus()), SysOperLog::getStatus, sysOperLog.getStatus())
                .like(StringUtil.isNotBlank(sysOperLog.getTitle()), SysOperLog::getTitle, sysOperLog.getTitle())
                .eq(StringUtil.isNotNull(sysOperLog.getOperType()), SysOperLog::getOperType, sysOperLog.getOperType())
                .eq(StringUtil.isNotNull(sysOperLog.getStatus()), SysOperLog::getStatus, sysOperLog.getStatus())
                .like(StringUtil.isNotBlank(sysOperLog.getOperName()), SysOperLog::getOperName, sysOperLog.getOperName())
                .between(sysOperLog.getParams().get("beginTime") != null && sysOperLog.getParams().get("endTime") != null,
                        SysOperLog::getOperTime, sysOperLog.getParams().get("beginTime"), sysOperLog.getParams().get("endTime"))
                .orderByDesc(SysOperLog::getOperId);
    }
}
