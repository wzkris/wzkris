package com.wzkris.system.controller;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.system.domain.SysOperLog;
import com.wzkris.system.mapper.SysOperLogMapper;
import com.wzkris.system.service.SysOperLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 操作日志记录
 *
 * @author wzkris
 */
@Tag(name = "操作日志")
@RestController
@PreAuthorize("@SysUtil.isSuperTenant()")// 只允许超级租户访问
@RequestMapping("/operlog")
@RequiredArgsConstructor
public class SysOperlogController extends BaseController {

    private final SysOperLogService operLogService;
    private final SysOperLogMapper operLogMapper;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('operlog:list')")
    public Result<Page<SysOperLog>> list(SysOperLog sysOperLog) {
        startPage();
        List<SysOperLog> list = operLogService.list(sysOperLog);
        return getDataTable(list);
    }

    @OperateLog(title = "操作日志", subTitle = "删除日志", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('operlog:remove')")
    public Result<?> remove(@RequestBody Long[] operIds) {
        return toRes(operLogMapper.deleteByIds(Arrays.asList(operIds)));
    }

    @OperateLog(title = "操作日志", subTitle = "清空日志", operateType = OperateType.DELETE)
    @PostMapping("/clean")
    @PreAuthorize("@ps.hasPerms('operlog:remove')")
    public Result<?> clean() {
        operLogMapper.clearAll();
        return ok();
    }
}
