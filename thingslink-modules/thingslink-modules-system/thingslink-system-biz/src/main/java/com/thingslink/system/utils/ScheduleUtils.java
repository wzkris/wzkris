package com.thingslink.system.utils;

import com.thingslink.common.core.constant.CommonConstants;
import com.thingslink.common.core.exception.job.TaskException;
import com.thingslink.common.core.exception.job.TaskException.Code;
import com.thingslink.common.core.utils.SpringUtil;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.system.constant.ScheduleConstants;
import com.thingslink.system.domain.SysJob;
import org.quartz.*;

/**
 * 定时任务工具类
 *
 * @author wzkris
 */
public class ScheduleUtils {
    /**
     * 得到quartz任务类
     *
     * @param sysJob 执行计划
     * @return 具体执行任务类
     */
    private static Class<? extends org.quartz.Job> getQuartzJobClass(SysJob sysJob) {
        boolean isConcurrent = "0".equals(sysJob.getConcurrent());
        return isConcurrent ? QuartzJobExecution.class : QuartzDisallowConcurrentExecution.class;
    }

    /**
     * 构建任务触发对象
     */
    public static TriggerKey getTriggerKey(Long jobId, String jobGroup) {
        return TriggerKey.triggerKey(ScheduleConstants.TASK_CLASS_NAME + jobId, jobGroup);
    }

    /**
     * 构建任务键对象
     */
    public static JobKey getJobKey(Long jobId, String jobGroup) {
        return JobKey.jobKey(ScheduleConstants.TASK_CLASS_NAME + jobId, jobGroup);
    }

    /**
     * 创建定时任务
     */
    public static void createScheduleJob(Scheduler scheduler, SysJob sysJob) throws SchedulerException, TaskException {
        Class<? extends org.quartz.Job> jobClass = getQuartzJobClass(sysJob);
        // 构建job信息
        Long jobId = sysJob.getJobId();
        String jobGroup = sysJob.getJobGroup();
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(getJobKey(jobId, jobGroup)).build();

        // 表达式调度构建器
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(sysJob.getCronExpression());
        cronScheduleBuilder = handleCronScheduleMisfirePolicy(sysJob, cronScheduleBuilder);

        // 按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(jobId, jobGroup))
                .withSchedule(cronScheduleBuilder).build();

        // 放入参数，运行时的方法可以获取
        jobDetail.getJobDataMap().put(ScheduleConstants.TASK_PROPERTIES, sysJob);

        // 判断是否存在
        if (scheduler.checkExists(getJobKey(jobId, jobGroup))) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(getJobKey(jobId, jobGroup));
        }

        // 判断任务是否过期
        if (StringUtil.isNotNull(CronUtils.getNextExecution(sysJob.getCronExpression()))) {
            // 执行调度任务
            scheduler.scheduleJob(jobDetail, trigger);
        }

        // 暂停任务
        if (sysJob.getStatus().equals(ScheduleConstants.Status.PAUSE.getValue())) {
            scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
    }

    /**
     * 设置定时任务策略
     */
    public static CronScheduleBuilder handleCronScheduleMisfirePolicy(SysJob sysJob, CronScheduleBuilder cb)
            throws TaskException {
        return switch (sysJob.getMisfirePolicy()) {
            case ScheduleConstants.MISFIRE_DEFAULT -> cb;
            case ScheduleConstants.MISFIRE_IGNORE_MISFIRES -> cb.withMisfireHandlingInstructionIgnoreMisfires();
            case ScheduleConstants.MISFIRE_FIRE_AND_PROCEED -> cb.withMisfireHandlingInstructionFireAndProceed();
            case ScheduleConstants.MISFIRE_DO_NOTHING -> cb.withMisfireHandlingInstructionDoNothing();
            default -> throw new TaskException("The task misfire policy '" + sysJob.getMisfirePolicy()
                    + "' cannot be used in cron schedule tasks", Code.CONFIG_ERROR);
        };
    }

    /**
     * 检查包名是否为白名单配置
     *
     * @param invokeTarget 目标字符串
     * @return 结果
     */
    public static boolean isWhiteList(String invokeTarget) {
        String packageName = StringUtil.subBefore(invokeTarget, "(", false);
        int count = StringUtil.count(packageName, ".");
        if (count > 1) {
            return StringUtil.containsAnyIgnoreCase(invokeTarget, CommonConstants.JOB_WHITELIST_STR);
        }
        Object obj = SpringUtil.getBean(StringUtil.split(invokeTarget, ".").get(0));
        return StringUtil.containsAnyIgnoreCase(obj.getClass().getPackage().getName(), CommonConstants.JOB_WHITELIST_STR);
    }
}
