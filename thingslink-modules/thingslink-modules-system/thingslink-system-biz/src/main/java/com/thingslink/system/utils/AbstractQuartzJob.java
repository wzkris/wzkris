package com.thingslink.system.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import com.thingslink.common.core.utils.SpringUtil;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.system.constant.ScheduleConstants;
import com.thingslink.system.domain.SysJob;
import com.thingslink.system.domain.SysJobLog;
import com.thingslink.system.mapper.SysJobLogMapper;
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
        SysJob sysJob = new SysJob();
        BeanUtil.copyProperties(context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES), sysJob);
        try {
            before(context, sysJob);
            doExecute(context, sysJob);
            after(context, sysJob, null);
        }
        catch (Exception e) {
            log.error("任务执行异常  - ：", e);
            after(context, sysJob, e);
        }
    }

    /**
     * 执行前
     *
     * @param context 工作执行上下文对象
     * @param sysJob  系统计划任务
     */
    protected void before(JobExecutionContext context, SysJob sysJob) {
        threadLocal.set(System.currentTimeMillis());
    }

    /**
     * 执行后
     *
     * @param context 工作执行上下文对象
     * @param sysJob  系统计划任务
     */
    protected void after(JobExecutionContext context, SysJob sysJob, Exception e) {
        Long startTime = threadLocal.get();
        threadLocal.remove();

        final SysJobLog sysJobLog = new SysJobLog();
        sysJobLog.setJobName(sysJob.getJobName());
        sysJobLog.setJobGroup(sysJob.getJobGroup());
        sysJobLog.setInvokeTarget(sysJob.getInvokeTarget());
        long runMs = System.currentTimeMillis() - startTime;
        sysJobLog.setJobMessage(sysJobLog.getJobName() + " 总共耗时：" + runMs + "毫秒");
        sysJobLog.setCreateAt(DateUtil.current());
        if (e != null) {
            sysJobLog.setStatus("1");
            String errorMsg = StringUtil.sub(ExceptionUtil.getMessage(e), 0, 2000);
            sysJobLog.setExceptionInfo(errorMsg);
        }
        else {
            sysJobLog.setStatus("0");
        }

        // 写入数据库当中
        SpringUtil.getBean(SysJobLogMapper.class).insert(sysJobLog);
    }

    /**
     * 执行方法，由子类重载
     *
     * @param context 工作执行上下文对象
     * @param sysJob  系统计划任务
     * @throws Exception 执行过程中的异常
     */
    protected abstract void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception;
}
