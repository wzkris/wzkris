package com.thingslink.system.service.impl;

import com.thingslink.system.domain.SysJobLog;
import com.thingslink.system.mapper.SysJobLogMapper;
import com.thingslink.system.service.SysJobLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
    public List<SysJobLog> selectJobLogList(SysJobLog sysJobLog) {
        return sysJobLogMapper.list(sysJobLog);
    }

    /**
     * 通过调度任务日志ID查询调度信息
     *
     * @param jobLogId 调度任务日志ID
     * @return 调度任务日志对象信息
     */
    @Override
    public SysJobLog selectJobLogById(Long jobLogId) {
        return sysJobLogMapper.selectById(jobLogId);
    }

    /**
     * 新增任务日志
     *
     * @param sysJobLog 调度日志信息
     */
    @Override
    public void addJobLog(SysJobLog sysJobLog) {
        sysJobLogMapper.insert(sysJobLog);
    }

    /**
     * 批量删除调度日志信息
     *
     * @param logIds 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteJobLogByIds(Long[] logIds) {
        return sysJobLogMapper.deleteBatchIds(Arrays.asList(logIds));
    }

    /**
     * 删除任务日志
     *
     * @param jobId 调度日志ID
     */
    @Override
    public int deleteJobLogById(Long jobId) {
        return sysJobLogMapper.deleteById(jobId);
    }

    /**
     * 清空任务日志
     */
    @Override
    public void cleanJobLog() {
        sysJobLogMapper.cleanJobLog();
    }
}
