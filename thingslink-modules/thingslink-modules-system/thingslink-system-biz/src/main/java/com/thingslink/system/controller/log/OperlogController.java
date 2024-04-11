package com.thingslink.system.controller.log;

import com.thingslink.common.core.domain.Result;
import com.thingslink.common.log.annotation.OperateLog;
import com.thingslink.common.log.enums.BusinessType;
import com.thingslink.common.orm.page.Page;
import com.thingslink.common.web.controller.BaseController;
import com.thingslink.system.domain.OperLog;
import com.thingslink.system.mapper.OperLogMapper;
import com.thingslink.system.service.OperLogService;
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
public class OperlogController extends BaseController {

    private final OperLogService operLogService;
    private final OperLogMapper operLogMapper;

    @Operation(summary = "分页")
    @GetMapping("list")
    @PreAuthorize("hasAuthority('operlog:list')")
    public Result<Page<OperLog>> list(OperLog operLog) {
        startPage();
        List<OperLog> list = operLogService.list(operLog);
        return getDataTable(list);
    }

    @OperateLog(title = "操作日志", businessType = BusinessType.DELETE)
    @DeleteMapping("{operIds}")
    @PreAuthorize("hasAuthority('operlog:remove')")
    public Result<?> remove(@PathVariable Long[] operIds) {
        return toRes(operLogMapper.deleteBatchIds(Arrays.asList(operIds)));
    }

    @OperateLog(title = "操作日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("clean")
    @PreAuthorize("hasAuthority('operlog:remove')")
    public Result<?> clean() {
        operLogMapper.clearAll();
        return success();
    }
}
