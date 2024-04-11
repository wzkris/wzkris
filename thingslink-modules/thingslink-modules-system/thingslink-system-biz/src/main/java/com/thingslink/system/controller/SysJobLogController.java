package com.thingslink.system.controller;

import com.thingslink.common.core.domain.Result;
import com.thingslink.common.log.annotation.OperateLog;
import com.thingslink.common.log.enums.OperateType;
import com.thingslink.common.orm.page.Page;
import com.thingslink.common.orm.model.BaseController;
import com.thingslink.system.domain.SysJobLog;
import com.thingslink.system.service.SysJobLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 调度日志操作处理
 *
 * @author wzkris
 */
@Tag(name = "调度日志")
@RestController
@RequestMapping("job/log")
@RequiredArgsConstructor
public class SysJobLogController extends BaseController {
    private final SysJobLogService sysJobLogService;

    /**
     * 查询定时任务调度日志列表
     */
    @GetMapping("list")
    @PreAuthorize("hasAuthority('job:list')")
    public Result<Page<SysJobLog>> list(SysJobLog sysJobLog) {
        startPage();
        List<SysJobLog> list = sysJobLogService.selectJobLogList(sysJobLog);
        return getDataTable(list);
    }

    /**
     * 根据调度编号获取详细信息
     */
    @GetMapping(value = "{jobLogId}")
    @PreAuthorize("hasAuthority('job:query')")
    public Result<?> getInfo(@PathVariable Long jobLogId) {
        return success(sysJobLogService.selectJobLogById(jobLogId));
    }

    /**
     * 删除定时任务调度日志
     */
    @OperateLog(title = "定时任务调度日志", operateType = OperateType.DELETE)
    @DeleteMapping("{jobLogIds}")
    @PreAuthorize("hasAuthority('job:remove')")
    public Result<?> remove(@PathVariable Long[] jobLogIds) {
        return toRes(sysJobLogService.deleteJobLogByIds(jobLogIds));
    }

    /**
     * 清空定时任务调度日志
     */
    @OperateLog(title = "调度日志", operateType = OperateType.DELETE)
    @DeleteMapping("clean")
    @PreAuthorize("hasAuthority('job:remove')")
    public Result<?> clean() {
        sysJobLogService.cleanJobLog();
        return success();
    }
}