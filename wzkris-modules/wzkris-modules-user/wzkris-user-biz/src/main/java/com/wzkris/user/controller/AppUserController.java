package com.wzkris.user.controller;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.page.Page;
import com.wzkris.user.domain.AppUser;
import com.wzkris.user.mapper.AppUserMapper;
import com.wzkris.user.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理
 *
 * @author wzkris
 */
@Tag(name = "app用户管理")
@Validated
@RestController
@RequestMapping("/app_user")
@RequiredArgsConstructor
public class AppUserController extends BaseController {

    private final AppUserMapper appUserMapper;
    private final AppUserService appUserService;

    @Operation(summary = "用户分页列表")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('app_user:list')")
    public Result<Page<AppUser>> listPage(AppUser user) {
        startPage();
        List<AppUser> list = appUserService.list(user);
        return getDataTable(list);
    }

    @Operation(summary = "用户详细信息")
    @GetMapping("/{userId}")
    @PreAuthorize("@ps.hasPerms('app_user:query')")
    public Result<AppUser> query(@PathVariable Long userId) {
        return ok(appUserMapper.selectById(userId));
    }

    @Operation(summary = "导出")
    @OperateLog(title = "用户管理", operateType = OperateType.EXPORT)
    @PostMapping("/export")
    @PreAuthorize("@ps.hasPerms('app_user:export')")
    public void export(HttpServletResponse httpServletResponse, AppUser user) {
        List<AppUser> list = appUserService.list(user);

    }
}
