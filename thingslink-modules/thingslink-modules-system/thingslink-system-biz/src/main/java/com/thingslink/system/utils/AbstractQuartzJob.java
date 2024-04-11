package com.thingslink.system.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import com.thingslink.common.core.utils.SpringUtil;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.system.constant.ScheduleConstants;
import com.thingslink.system.domain.Job;
import com.thingslink.system.domain.JobLog;
import com.thingslink.system.service.JobLogService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;

/**
 * 抽象quartz调用
 *
 * @author wzkris
 */
@Slf4j
public abstract class AbstractQuartzJob implements org.quartz.Job {

    /**
     * 线程本地变量
     */
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    @Override
    public void execute(JobExecutionContext context) {
        Job job = new Job();
        BeanUtil.copyProperties(context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES), job);
        try {
            before(context, job);
            doExecute(context, job);
            after(context, job, null);
        }
        catch (Exception e) {
            log.error("任务执行异常  - ：", e);
            after(context, job, e);
        }
    }

    /**
     * 执行前
     *
     * @param context 工作执行上下文对象
     * @param job     系统计划任务
     */
    protected void before(JobExecutionContext context, Job job) {
        threadLocal.set(System.currentTimeMillis());
    }

    /**
     * 执行后
     *
     * @param context 工作执行上下文对象
     * @param job     系统计划任务
     */
    protected void after(JobExecutionContext context, Job job, Exception e) {
        Long startTime = threadLocal.get();
        threadLocal.remove();

        final JobLog jobLog = new JobLog();
        jobLog.setJobName(job.getJobName());
        jobLog.setJobGroup(job.getJobGroup());
        jobLog.setInvokeTarget(job.getInvokeTarget());
        long runMs = System.currentTimeMillis() - startTime;
        jobLog.setJobMessage(jobLog.getJobName() + " 总共耗时：" + runMs + "毫秒");
        jobLog.setCreateAt(LocalDateTimeUtil.now());
        if (e != null) {
            jobLog.setStatus("1");
            String errorMsg = StringUtil.sub(ExceptionUtil.getMessage(e), 0, 2000);
            jobLog.setExceptionInfo(errorMsg);
        }
        else {
            jobLog.setStatus("0");
        }

        // 写入数据库当中
        SpringUtil.getBean(JobLogService.class).addJobLog(jobLog);
    }

    /**
     * 执行方法，由子类重载
     *
     * @param context 工作执行上下文对象
     * @param job     系统计划任务
     * @throws Exception 执行过程中的异常
     */
    protected abstract void doExecute(JobExecutionContext context, Job job) throws Exception;
}
