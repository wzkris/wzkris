package com.thingslink.system.controller.log;

import com.thingslink.auth.api.RemoteTokenApi;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.orm.model.BaseController;
import com.thingslink.common.orm.page.Page;
import com.thingslink.system.domain.SysLoginLog;
import com.thingslink.system.mapper.SysLoginLogMapper;
import com.thingslink.system.service.SysLoginLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 登录日志
 * @date : 2023/8/26 16:25
 */
@Tag(name = "登录日志")
@RestController
@RequestMapping("loginlog")
@RequiredArgsConstructor
public class SysLoginlogController extends BaseController {

    private final RemoteTokenApi RemoteTokenApi;
    private final SysLoginLogService sysLoginLogService;
    private final SysLoginLogMapper sysLoginLogMapper;

    @Operation(summary = "分页")
    @GetMapping("list")
    @PreAuthorize("@ps.hasPerms('loginlog:list')")
    public Result<Page<SysLoginLog>> list(SysLoginLog loginLog) {
        startPage();
        List<SysLoginLog> list = sysLoginLogService.list(loginLog);
        return getDataTable(list);
    }

    @DeleteMapping("{logIds}")
    @PreAuthorize("@ps.hasPerms('loginlog:remove')")
    public Result<?> remove(@PathVariable Long[] logIds) {
        return toRes(sysLoginLogMapper.deleteBatchIds(Arrays.asList(logIds)));
    }

    @DeleteMapping("clean")
    @PreAuthorize("@ps.hasPerms('loginlog:remove')")
    public Result<?> clean() {
        sysLoginLogMapper.clearAll();
        return success();
    }

    @GetMapping("unlock/{username}")
    @PreAuthorize("@ps.hasPerms('loginlog:unlock')")
    public Result<?> unlock(@PathVariable String username) {
        RemoteTokenApi.unlockAccount(username);
        return success();
    }
}
