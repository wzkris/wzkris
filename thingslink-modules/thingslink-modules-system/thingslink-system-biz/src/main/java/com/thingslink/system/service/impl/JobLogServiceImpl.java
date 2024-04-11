package com.thingslink.system.service.impl;

import com.thingslink.system.domain.JobLog;
import com.thingslink.system.mapper.JobLogMapper;
import com.thingslink.system.service.JobLogService;
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
public class JobLogServiceImpl implements JobLogService {
    private final JobLogMapper jobLogMapper;

    /**
     * 获取quartz调度器日志的计划任务
     *
     * @param jobLog 调度日志信息
     * @return 调度任务日志集合
     */
    @Override
    public List<JobLog> selectJobLogList(JobLog jobLog) {
        return jobLogMapper.list(jobLog);
    }

    /**
     * 通过调度任务日志ID查询调度信息
     *
     * @param jobLogId 调度任务日志ID
     * @return 调度任务日志对象信息
     */
    @Override
    public JobLog selectJobLogById(Long jobLogId) {
        return jobLogMapper.selectById(jobLogId);
    }

    /**
     * 新增任务日志
     *
     * @param jobLog 调度日志信息
     */
    @Override
    public void addJobLog(JobLog jobLog) {
        jobLogMapper.insert(jobLog);
    }

    /**
     * 批量删除调度日志信息
     *
     * @param logIds 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteJobLogByIds(Long[] logIds) {
        return jobLogMapper.deleteBatchIds(Arrays.asList(logIds));
    }

    /**
     * 删除任务日志
     *
     * @param jobId 调度日志ID
     */
    @Override
    public int deleteJobLogById(Long jobId) {
        return jobLogMapper.deleteById(jobId);
    }

    /**
     * 清空任务日志
     */
    @Override
    public void cleanJobLog() {
        jobLogMapper.cleanJobLog();
    }
}
