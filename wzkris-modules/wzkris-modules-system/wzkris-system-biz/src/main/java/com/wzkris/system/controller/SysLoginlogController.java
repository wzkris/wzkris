package com.wzkris.system.controller;

import com.wzkris.auth.api.RemoteTokenApi;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.page.Page;
import com.wzkris.system.domain.SysLoginLog;
import com.wzkris.system.mapper.SysLoginLogMapper;
import com.wzkris.system.service.SysLoginLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 登录日志
 * @date : 2023/8/26 16:25
 */
@Tag(name = "登录日志")
@RestController
@RequestMapping("/loginlog")
@RequiredArgsConstructor
public class SysLoginlogController extends BaseController {

    private final RemoteTokenApi RemoteTokenApi;
    private final SysLoginLogService sysLoginLogService;
    private final SysLoginLogMapper sysLoginLogMapper;

    @Operation(summary = "分页")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('loginlog:list')")
    public Result<Page<SysLoginLog>> list(SysLoginLog loginLog) {
        startPage();
        List<SysLoginLog> list = sysLoginLogService.list(loginLog);
        return getDataTable(list);
    }

    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('loginlog:remove')")
    public Result<?> remove(@RequestBody List<Long> logIds) {
        return toRes(sysLoginLogMapper.deleteByIds(logIds));
    }

    @PostMapping("/clean")
    @PreAuthorize("@ps.hasPerms('loginlog:remove')")
    public Result<?> clean() {
        sysLoginLogMapper.clearAll();
        return success();
    }

    @PostMapping("/unlock")
    @PreAuthorize("@ps.hasPerms('loginlog:unlock')")
    public Result<?> unlock(@RequestBody String username) {
        RemoteTokenApi.unlockAccount(username);
        return success();
    }
}
