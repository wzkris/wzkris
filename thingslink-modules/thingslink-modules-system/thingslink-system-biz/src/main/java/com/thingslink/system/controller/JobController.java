package com.thingslink.system.controller;

import com.thingslink.common.core.constant.CommonConstants;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.core.exception.job.TaskException;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.common.log.annotation.OperateLog;
import com.thingslink.common.log.enums.BusinessType;
import com.thingslink.common.orm.page.Page;
import com.thingslink.common.web.controller.BaseController;
import com.thingslink.system.domain.Job;
import com.thingslink.system.service.JobService;
import com.thingslink.system.utils.CronUtils;
import com.thingslink.system.utils.ScheduleUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 调度任务信息操作处理
 *
 * @author wzkris
 */
@Tag(name = "调度任务信息")
@RestController
@RequestMapping("job")
@RequiredArgsConstructor
public class JobController extends BaseController {
    private final JobService jobService;

    /**
     * 查询定时任务列表
     */
    @GetMapping("list")
    @PreAuthorize("hasAuthority('job:list')")
    public Result<Page<Job>> list(Job job) {
        startPage();
        List<Job> list = jobService.selectJobList(job);
        return getDataTable(list);
    }

    /**
     * 获取定时任务详细信息
     */
    @GetMapping(value = "{jobId}")
    @PreAuthorize("hasAuthority('job:query')")
    public Result<?> getInfo(@PathVariable("jobId") Long jobId) {
        return success(jobService.selectJobById(jobId));
    }

    /**
     * 新增定时任务
     */
    @OperateLog(title = "定时任务", businessType = BusinessType.INSERT)
    @PostMapping
    @PreAuthorize("hasAuthority('job:add')")
    public Result<?> add(@RequestBody Job job) throws SchedulerException, TaskException {
        if (!CronUtils.isValid(job.getCronExpression())) {
            return fail("新增任务'" + job.getJobName() + "'失败，Cron表达式不正确");
        }
        else if (StringUtil.containsIgnoreCase(job.getInvokeTarget(), CommonConstants.LOOKUP_RMI)) {
            return fail("新增任务'" + job.getJobName() + "'失败，目标字符串不允许'rmi'调用");
        }
        else if (StringUtil.containsAnyIgnoreCase(job.getInvokeTarget(), new String[]{CommonConstants.LOOKUP_LDAP, CommonConstants.LOOKUP_LDAPS})) {
            return fail("新增任务'" + job.getJobName() + "'失败，目标字符串不允许'ldap(s)'调用");
        }
        else if (StringUtil.containsAnyIgnoreCase(job.getInvokeTarget(), new String[]{CommonConstants.HTTP, CommonConstants.HTTPS})) {
            return fail("新增任务'" + job.getJobName() + "'失败，目标字符串不允许'http(s)'调用");
        }
        else if (StringUtil.containsAnyIgnoreCase(job.getInvokeTarget(), CommonConstants.JOB_ERROR_STR)) {
            return fail("新增任务'" + job.getJobName() + "'失败，目标字符串存在违规");
        }
        else if (!ScheduleUtils.isWhiteList(job.getInvokeTarget())) {
            return fail("新增任务'" + job.getJobName() + "'失败，目标字符串不在白名单内");
        }
        return toRes(jobService.insertJob(job));
    }

    /**
     * 修改定时任务
     */
    @OperateLog(title = "定时任务", businessType = BusinessType.UPDATE)
    @PutMapping
    @PreAuthorize("hasAuthority('job:edit')")
    public Result<?> edit(@RequestBody Job job) throws SchedulerException, TaskException {
        if (!CronUtils.isValid(job.getCronExpression())) {
            return fail("修改任务'" + job.getJobName() + "'失败，Cron表达式不正确");
        }
        else if (StringUtil.containsIgnoreCase(job.getInvokeTarget(), CommonConstants.LOOKUP_RMI)) {
            return fail("修改任务'" + job.getJobName() + "'失败，目标字符串不允许'rmi'调用");
        }
        else if (StringUtil.containsAnyIgnoreCase(job.getInvokeTarget(), new String[]{CommonConstants.LOOKUP_LDAP, CommonConstants.LOOKUP_LDAPS})) {
            return fail("修改任务'" + job.getJobName() + "'失败，目标字符串不允许'ldap(s)'调用");
        }
        else if (StringUtil.containsAnyIgnoreCase(job.getInvokeTarget(), new String[]{CommonConstants.HTTP, CommonConstants.HTTPS})) {
            return fail("修改任务'" + job.getJobName() + "'失败，目标字符串不允许'http(s)'调用");
        }
        else if (StringUtil.containsAnyIgnoreCase(job.getInvokeTarget(), CommonConstants.JOB_ERROR_STR)) {
            return fail("修改任务'" + job.getJobName() + "'失败，目标字符串存在违规");
        }
        else if (!ScheduleUtils.isWhiteList(job.getInvokeTarget())) {
            return fail("修改任务'" + job.getJobName() + "'失败，目标字符串不在白名单内");
        }
        return toRes(jobService.updateJob(job));
    }

    /**
     * 定时任务状态修改
     */
    @OperateLog(title = "定时任务", businessType = BusinessType.UPDATE)
    @PutMapping("changeStatus")
    @PreAuthorize("hasAuthority('job:changeStatus')")
    public Result<?> changeStatus(@RequestBody Job job) throws SchedulerException {
        Job newJob = jobService.selectJobById(job.getJobId());
        newJob.setStatus(job.getStatus());
        return toRes(jobService.changeStatus(newJob));
    }

    /**
     * 定时任务立即执行一次
     */
    @OperateLog(title = "定时任务", businessType = BusinessType.UPDATE)
    @PutMapping("run")
    @PreAuthorize("hasAuthority('job:changeStatus')")
    public Result<?> run(@RequestBody Job job) throws SchedulerException {
        boolean result = jobService.run(job);
        return result ? success() : fail("任务不存在或已过期！");
    }

    /**
     * 删除定时任务
     */
    @OperateLog(title = "定时任务", businessType = BusinessType.DELETE)
    @DeleteMapping("{jobIds}")
    @PreAuthorize("hasAuthority('job:remove')")
    public Result<?> remove(@PathVariable Long[] jobIds) throws SchedulerException, TaskException {
        jobService.deleteJobByIds(jobIds);
        return success();
    }
}
