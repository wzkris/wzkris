package com.thingslink.system.controller.log;

import com.thingslink.auth.api.RemoteTokenApi;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.orm.page.Page;
import com.thingslink.common.web.controller.BaseController;
import com.thingslink.system.domain.LoginLog;
import com.thingslink.system.mapper.LoginLogMapper;
import com.thingslink.system.service.LoginLogService;
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
public class LoginlogController extends BaseController {

    private final RemoteTokenApi RemoteTokenApi;
    private final LoginLogService loginLogService;
    private final LoginLogMapper loginLogMapper;

    @Operation(summary = "分页")
    @GetMapping("list")
    @PreAuthorize("hasAuthority('loginlog:list')")
    public Result<Page<LoginLog>> list(LoginLog loginLog) {
        startPage();
        List<LoginLog> list = loginLogService.list(loginLog);
        return getDataTable(list);
    }

    @DeleteMapping("{logIds}")
    @PreAuthorize("hasAuthority('loginlog:remove')")
    public Result<?> remove(@PathVariable Long[] logIds) {
        return toRes(loginLogMapper.deleteBatchIds(Arrays.asList(logIds)));
    }

    @DeleteMapping("clean")
    @PreAuthorize("hasAuthority('loginlog:remove')")
    public Result<?> clean() {
        loginLogMapper.clearAll();
        return success();
    }

    @GetMapping("unlock/{username}")
    @PreAuthorize("hasAuthority('loginlog:unlock')")
    public Result<?> unlock(@PathVariable String username) {
        RemoteTokenApi.unlockAccount(username);
        return success();
    }
}
