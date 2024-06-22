package com.thingslink.system.service;

import com.thingslink.common.core.exception.job.TaskException;
import com.thingslink.system.domain.SysJob;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * 定时任务调度信息信息 服务层
 *
 * @author wzkris
 */
public interface SysJobService {
    /**
     * 获取quartz调度器的计划任务
     *
     * @param sysJob 调度信息
     * @return 调度任务集合
     */
    List<SysJob> list(SysJob sysJob);

    /**
     * 通过调度任务ID查询调度信息
     *
     * @param jobId 调度任务ID
     * @return 调度任务对象信息
     */
    SysJob selectJobById(Long jobId);

    /**
     * 暂停任务
     *
     * @param sysJob 调度信息
     * @return 结果
     */
    int pauseJob(SysJob sysJob) throws SchedulerException;

    /**
     * 恢复任务
     *
     * @param sysJob 调度信息
     * @return 结果
     */
    int resumeJob(SysJob sysJob) throws SchedulerException;

    /**
     * 删除任务后，所对应的trigger也将被删除
     *
     * @param sysJob 调度信息
     * @return 结果
     */
    int deleteJob(SysJob sysJob) throws SchedulerException;

    /**
     * 批量删除调度信息
     *
     * @param jobIds 需要删除的任务ID
     * @return 结果
     */
    void deleteJobByIds(Long[] jobIds) throws SchedulerException;

    /**
     * 任务调度状态修改
     *
     * @param sysJob 调度信息
     * @return 结果
     */
    int changeStatus(SysJob sysJob) throws SchedulerException;

    /**
     * 立即运行任务
     *
     * @param sysJob 调度信息
     * @return 结果
     */
    boolean run(SysJob sysJob) throws SchedulerException;

    /**
     * 新增任务
     *
     * @param sysJob 调度信息
     * @return 结果
     */
    int insertJob(SysJob sysJob) throws SchedulerException, TaskException;

    /**
     * 更新任务
     *
     * @param sysJob 调度信息
     * @return 结果
     */
    int updateJob(SysJob sysJob) throws SchedulerException, TaskException;

    /**
     * 校验cron表达式是否有效
     *
     * @param cronExpression 表达式
     * @return 结果
     */
    boolean checkCronExpressionIsValid(String cronExpression);
}
