package com.thingslink.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.system.domain.SysJobLog;
import com.thingslink.system.mapper.SysJobLogMapper;
import com.thingslink.system.service.SysJobLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 定时任务调度日志信息 服务层
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class SysJobLogServiceImpl implements SysJobLogService {
    private final SysJobLogMapper sysJobLogMapper;

    /**
     * 获取quartz调度器日志的计划任务
     *
     * @param sysJobLog 调度日志信息
     * @return 调度任务日志集合
     */
    @Override
    public List<SysJobLog> list(SysJobLog sysJobLog) {
        LambdaQueryWrapper<SysJobLog> lqw = this.buildQueryWrapper(sysJobLog);
        return sysJobLogMapper.selectList(lqw);
    }

    private LambdaQueryWrapper<SysJobLog> buildQueryWrapper(SysJobLog jobLog) {
        return new LambdaQueryWrapper<SysJobLog>()
                .like(StringUtil.isNotNull(jobLog.getJobName()), SysJobLog::getJobName, jobLog.getJobName())
                .like(StringUtil.isNotNull(jobLog.getInvokeTarget()), SysJobLog::getInvokeTarget, jobLog.getInvokeTarget())
                .eq(StringUtil.isNotNull(jobLog.getJobGroup()), SysJobLog::getJobGroup, jobLog.getJobGroup())
                .eq(StringUtil.isNotNull(jobLog.getStatus()), SysJobLog::getStatus, jobLog.getStatus())
                .between(jobLog.getParams().get("beginTime") != null && jobLog.getParams().get("endTime") != null,
                        SysJobLog::getCreateAt, jobLog.getParams().get("beginTime"), jobLog.getParams().get("endTime"))
                .orderByDesc(SysJobLog::getJobLogId);
    }
}
