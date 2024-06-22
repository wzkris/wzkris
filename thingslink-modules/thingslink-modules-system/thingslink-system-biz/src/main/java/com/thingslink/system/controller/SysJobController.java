package com.thingslink.system.controller;

import com.thingslink.common.core.constant.CommonConstants;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.core.exception.job.TaskException;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.common.log.annotation.OperateLog;
import com.thingslink.common.log.enums.OperateType;
import com.thingslink.common.orm.model.BaseController;
import com.thingslink.common.orm.page.Page;
import com.thingslink.system.domain.SysJob;
import com.thingslink.system.service.SysJobService;
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
@RequestMapping("/job")
@RequiredArgsConstructor
public class SysJobController extends BaseController {
    private final SysJobService sysJobService;

    /**
     * 查询定时任务列表
     */
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('job:list')")
    public Result<Page<SysJob>> list(SysJob sysJob) {
        startPage();
        List<SysJob> list = sysJobService.selectJobList(sysJob);
        return getDataTable(list);
    }

    /**
     * 获取定时任务详细信息
     */
    @GetMapping("/{jobId}")
    @PreAuthorize("@ps.hasPerms('job:query')")
    public Result<?> getInfo(@PathVariable("jobId") Long jobId) {
        return success(sysJobService.selectJobById(jobId));
    }

    /**
     * 新增定时任务
     */
    @OperateLog(title = "定时任务", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @PreAuthorize("@ps.hasPerms('job:add')")
    public Result<?> add(@RequestBody SysJob sysJob) throws SchedulerException, TaskException {
        if (!CronUtils.isValid(sysJob.getCronExpression())) {
            return fail("新增任务'" + sysJob.getJobName() + "'失败，Cron表达式不正确");
        }
        else if (StringUtil.containsIgnoreCase(sysJob.getInvokeTarget(), CommonConstants.LOOKUP_RMI)) {
            return fail("新增任务'" + sysJob.getJobName() + "'失败，目标字符串不允许'rmi'调用");
        }
        else if (StringUtil.containsAnyIgnoreCase(sysJob.getInvokeTarget(), new String[]{CommonConstants.LOOKUP_LDAP, CommonConstants.LOOKUP_LDAPS})) {
            return fail("新增任务'" + sysJob.getJobName() + "'失败，目标字符串不允许'ldap(s)'调用");
        }
        else if (StringUtil.containsAnyIgnoreCase(sysJob.getInvokeTarget(), new String[]{CommonConstants.HTTP, CommonConstants.HTTPS})) {
            return fail("新增任务'" + sysJob.getJobName() + "'失败，目标字符串不允许'http(s)'调用");
        }
        else if (StringUtil.containsAnyIgnoreCase(sysJob.getInvokeTarget(), CommonConstants.JOB_ERROR_STR)) {
            return fail("新增任务'" + sysJob.getJobName() + "'失败，目标字符串存在违规");
        }
        else if (!ScheduleUtils.isWhiteList(sysJob.getInvokeTarget())) {
            return fail("新增任务'" + sysJob.getJobName() + "'失败，目标字符串不在白名单内");
        }
        return toRes(sysJobService.insertJob(sysJob));
    }

    /**
     * 修改定时任务
     */
    @OperateLog(title = "定时任务", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @PreAuthorize("@ps.hasPerms('job:edit')")
    public Result<?> edit(@RequestBody SysJob sysJob) throws SchedulerException, TaskException {
        if (!CronUtils.isValid(sysJob.getCronExpression())) {
            return fail("修改任务'" + sysJob.getJobName() + "'失败，Cron表达式不正确");
        }
        else if (StringUtil.containsIgnoreCase(sysJob.getInvokeTarget(), CommonConstants.LOOKUP_RMI)) {
            return fail("修改任务'" + sysJob.getJobName() + "'失败，目标字符串不允许'rmi'调用");
        }
        else if (StringUtil.containsAnyIgnoreCase(sysJob.getInvokeTarget(), new String[]{CommonConstants.LOOKUP_LDAP, CommonConstants.LOOKUP_LDAPS})) {
            return fail("修改任务'" + sysJob.getJobName() + "'失败，目标字符串不允许'ldap(s)'调用");
        }
        else if (StringUtil.containsAnyIgnoreCase(sysJob.getInvokeTarget(), new String[]{CommonConstants.HTTP, CommonConstants.HTTPS})) {
            return fail("修改任务'" + sysJob.getJobName() + "'失败，目标字符串不允许'http(s)'调用");
        }
        else if (StringUtil.containsAnyIgnoreCase(sysJob.getInvokeTarget(), CommonConstants.JOB_ERROR_STR)) {
            return fail("修改任务'" + sysJob.getJobName() + "'失败，目标字符串存在违规");
        }
        else if (!ScheduleUtils.isWhiteList(sysJob.getInvokeTarget())) {
            return fail("修改任务'" + sysJob.getJobName() + "'失败，目标字符串不在白名单内");
        }
        return toRes(sysJobService.updateJob(sysJob));
    }

    /**
     * 定时任务状态修改
     */
    @OperateLog(title = "定时任务", operateType = OperateType.UPDATE)
    @PostMapping("/edit_Status")
    @PreAuthorize("@ps.hasPerms('job:changeStatus')")
    public Result<?> editStatus(@RequestBody SysJob sysJob) throws SchedulerException {
        SysJob newSysJob = sysJobService.selectJobById(sysJob.getJobId());
        newSysJob.setStatus(sysJob.getStatus());
        return toRes(sysJobService.changeStatus(newSysJob));
    }

    /**
     * 定时任务立即执行一次
     */
    @OperateLog(title = "定时任务", operateType = OperateType.UPDATE)
    @PostMapping("/run")
    @PreAuthorize("@ps.hasPerms('job:changeStatus')")
    public Result<?> run(@RequestBody SysJob sysJob) throws SchedulerException {
        boolean result = sysJobService.run(sysJob);
        return result ? success() : fail("任务不存在或已过期！");
    }

    /**
     * 删除定时任务
     */
    @OperateLog(title = "定时任务", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('job:remove')")
    public Result<?> remove(@RequestBody Long[] jobIds) throws SchedulerException {
        sysJobService.deleteJobByIds(jobIds);
        return success();
    }
}
