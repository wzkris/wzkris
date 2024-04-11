package com.thingslink.system.utils;

import com.thingslink.system.domain.SysJob;
import org.quartz.JobExecutionContext;

/**
 * 定时任务处理（允许并发执行）
 *
 * @author wzkris
 */
public class QuartzJobExecution extends AbstractQuartzJob {
    @Override
    protected void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception {
        JobInvokeUtil.invokeMethod(sysJob);
    }
}
