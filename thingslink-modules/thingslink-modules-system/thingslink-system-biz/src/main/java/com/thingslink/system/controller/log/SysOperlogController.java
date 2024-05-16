package com.thingslink.system.controller.log;

import com.thingslink.common.core.domain.Result;
import com.thingslink.common.log.annotation.OperateLog;
import com.thingslink.common.log.enums.OperateType;
import com.thingslink.common.orm.model.BaseController;
import com.thingslink.common.orm.page.Page;
import com.thingslink.system.domain.SysOperLog;
import com.thingslink.system.mapper.SysOperLogMapper;
import com.thingslink.system.service.SysOperLogService;
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
@RequestMapping("operlog")
@RequiredArgsConstructor
public class SysOperlogController extends BaseController {

    private final SysOperLogService sysOperLogService;
    private final SysOperLogMapper sysOperLogMapper;

    @Operation(summary = "分页")
    @GetMapping("list")
    @PreAuthorize("hasAuthority('operlog:list')")
    public Result<Page<SysOperLog>> list(SysOperLog sysOperLog) {
        startPage();
        List<SysOperLog> list = sysOperLogService.list(sysOperLog);
        return getDataTable(list);
    }

    @OperateLog(title = "操作日志", operateType = OperateType.DELETE)
    @DeleteMapping("{operIds}")
    @PreAuthorize("hasAuthority('operlog:remove')")
    public Result<?> remove(@PathVariable Long[] operIds) {
        return toRes(sysOperLogMapper.deleteBatchIds(Arrays.asList(operIds)));
    }

    @OperateLog(title = "操作日志", operateType = OperateType.DELETE)
    @DeleteMapping("clean")
    @PreAuthorize("hasAuthority('operlog:remove')")
    public Result<?> clean() {
        sysOperLogMapper.clearAll();
        return success();
    }
}
