package com.wzkris.user.controller;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.page.Page;
import com.wzkris.user.domain.AppUser;
import com.wzkris.user.mapper.AppUserMapper;
import com.wzkris.user.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户管理
 *
 * @author wzkris
 */
@Tag(name = "用户管理")
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

    @Operation(summary = "用户分页列表")
    @GetMapping("/{userId}")
    @PreAuthorize("@ps.hasPerms('app_user:query')")
    public Result<AppUser> query(@PathVariable Long userId) {
        return success(appUserMapper.selectById(userId));
    }
}
