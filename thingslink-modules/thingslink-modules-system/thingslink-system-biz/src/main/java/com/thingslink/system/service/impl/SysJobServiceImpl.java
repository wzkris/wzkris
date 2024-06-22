package com.thingslink.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.thingslink.common.core.exception.job.TaskException;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.system.constant.ScheduleConstants;
import com.thingslink.system.domain.SysJob;
import com.thingslink.system.mapper.SysJobMapper;
import com.thingslink.system.service.SysJobService;
import com.thingslink.system.utils.CronUtils;
import com.thingslink.system.utils.ScheduleUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 定时任务调度信息 服务层
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class SysJobServiceImpl implements SysJobService {
    private final Scheduler scheduler;
    private final SysJobMapper sysJobMapper;

    /**
     * 项目启动时，初始化定时器 主要是防止手动修改数据库导致未同步到定时任务处理（注：不能手动修改数据库ID和任务组名，否则会导致脏数据）
     */
    @PostConstruct
    public void init() throws SchedulerException, TaskException {
        scheduler.clear();
        List<SysJob> sysJobList = sysJobMapper.selectList(null);
        for (SysJob sysJob : sysJobList) {
            ScheduleUtils.createScheduleJob(scheduler, sysJob);
        }
    }

    /**
     * 获取quartz调度器的计划任务列表
     *
     * @param sysJob 调度信息
     * @return 任务列表
     */
    @Override
    public List<SysJob> list(SysJob sysJob) {
        LambdaQueryWrapper<SysJob> lqw = this.buildQueryWrapper(sysJob);
        return sysJobMapper.selectList(lqw);
    }

    private LambdaQueryWrapper<SysJob> buildQueryWrapper(SysJob sysJob) {
        return new LambdaQueryWrapper<SysJob>()
                .like(StringUtil.isNotNull(sysJob.getJobName()), SysJob::getJobName, sysJob.getJobName())
                .like(StringUtil.isNotNull(sysJob.getInvokeTarget()), SysJob::getInvokeTarget, sysJob.getInvokeTarget())
                .eq(StringUtil.isNotNull(sysJob.getJobGroup()), SysJob::getJobGroup, sysJob.getJobGroup())
                .eq(StringUtil.isNotNull(sysJob.getStatus()), SysJob::getStatus, sysJob.getStatus())
                .orderByDesc(SysJob::getJobId);
    }

    /**
     * 通过调度任务ID查询调度信息
     *
     * @param jobId 调度任务ID
     * @return 调度任务对象信息
     */
    @Override
    public SysJob selectJobById(Long jobId) {
        return sysJobMapper.selectById(jobId);
    }

    /**
     * 暂停任务
     *
     * @param sysJob 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int pauseJob(SysJob sysJob) throws SchedulerException {
        Long jobId = sysJob.getJobId();
        String jobGroup = sysJob.getJobGroup();
        sysJob.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = sysJobMapper.updateById(sysJob);
        if (rows > 0) {
            scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * 恢复任务
     *
     * @param sysJob 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int resumeJob(SysJob sysJob) throws SchedulerException {
        Long jobId = sysJob.getJobId();
        String jobGroup = sysJob.getJobGroup();
        sysJob.setStatus(ScheduleConstants.Status.NORMAL.getValue());
        int rows = sysJobMapper.updateById(sysJob);
        if (rows > 0) {
            scheduler.resumeJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * 删除任务后，所对应的trigger也将被删除
     *
     * @param sysJob 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteJob(SysJob sysJob) throws SchedulerException {
        Long jobId = sysJob.getJobId();
        String jobGroup = sysJob.getJobGroup();
        int rows = sysJobMapper.deleteById(jobId);
        if (rows > 0) {
            scheduler.deleteJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * 批量删除调度信息
     *
     * @param jobIds 需要删除的任务ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJobByIds(Long[] jobIds) throws SchedulerException {
        for (Long jobId : jobIds) {
            SysJob sysJob = sysJobMapper.selectById(jobId);
            deleteJob(sysJob);
        }
    }

    /**
     * 任务调度状态修改
     *
     * @param sysJob 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int changeStatus(SysJob sysJob) throws SchedulerException {
        int rows = 0;
        String status = sysJob.getStatus();
        if (ScheduleConstants.Status.NORMAL.getValue().equals(status)) {
            rows = resumeJob(sysJob);
        }
        else if (ScheduleConstants.Status.PAUSE.getValue().equals(status)) {
            rows = pauseJob(sysJob);
        }
        return rows;
    }

    /**
     * 立即运行任务
     *
     * @param sysJob 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean run(SysJob sysJob) throws SchedulerException {
        boolean result = false;
        Long jobId = sysJob.getJobId();
        String jobGroup = sysJob.getJobGroup();
        SysJob properties = sysJobMapper.selectById(sysJob.getJobId());
        // 参数
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(ScheduleConstants.TASK_PROPERTIES, properties);
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
        if (scheduler.checkExists(jobKey)) {
            result = true;
            scheduler.triggerJob(jobKey, dataMap);
        }
        return result;
    }

    /**
     * 新增任务
     *
     * @param sysJob 调度信息 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertJob(SysJob sysJob) throws SchedulerException, TaskException {
        sysJob.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = sysJobMapper.insert(sysJob);
        if (rows > 0) {
            ScheduleUtils.createScheduleJob(scheduler, sysJob);
        }
        return rows;
    }

    /**
     * 更新任务的时间表达式
     *
     * @param sysJob 调度信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateJob(SysJob sysJob) throws SchedulerException, TaskException {
        SysJob properties = sysJobMapper.selectById(sysJob.getJobId());
        int rows = sysJobMapper.updateById(sysJob);
        if (rows > 0) {
            updateSchedulerJob(sysJob, properties.getJobGroup());
        }
        return rows;
    }

    /**
     * 更新任务
     *
     * @param sysJob   任务对象
     * @param jobGroup 任务组名
     */
    public void updateSchedulerJob(SysJob sysJob, String jobGroup) throws SchedulerException, TaskException {
        Long jobId = sysJob.getJobId();
        // 判断是否存在
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
        if (scheduler.checkExists(jobKey)) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtils.createScheduleJob(scheduler, sysJob);
    }

    /**
     * 校验cron表达式是否有效
     *
     * @param cronExpression 表达式
     * @return 结果
     */
    @Override
    public boolean checkCronExpressionIsValid(String cronExpression) {
        return CronUtils.isValid(cronExpression);
    }
}
