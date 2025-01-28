package com.wzkris.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.BeanUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.excel.utils.ExcelUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.security.oauth2.annotation.CheckPerms;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.user.domain.AppUser;
import com.wzkris.user.domain.export.AppUserExport;
import com.wzkris.user.domain.req.AppUserQueryReq;
import com.wzkris.user.domain.req.EditStatusReq;
import com.wzkris.user.mapper.AppUserMapper;
import com.wzkris.user.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
    @CheckPerms("app_user:list")
    public Result<Page<AppUser>> listPage(AppUserQueryReq req) {
        startPage();
        List<AppUser> list = appUserMapper.selectList(this.buildQueryWrapper(req));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<AppUser> buildQueryWrapper(AppUserQueryReq req) {
        return new LambdaQueryWrapper<AppUser>()
                .eq(StringUtil.isNotBlank(req.getStatus()), AppUser::getStatus, req.getStatus())
                .like(StringUtil.isNotBlank(req.getNickname()), AppUser::getNickname, req.getNickname())
                .like(StringUtil.isNotBlank(req.getPhoneNumber()), AppUser::getPhoneNumber, req.getPhoneNumber())
                .between(req.getParams().get("beginTime") != null && req.getParams().get("endTime") != null,
                        AppUser::getCreateAt, req.getParams().get("beginTime"), req.getParams().get("endTime"))
                .orderByDesc(AppUser::getUserId);
    }

    @Operation(summary = "用户详细信息")
    @GetMapping("/{userId}")
    @CheckPerms("app_user:query")
    public Result<AppUser> query(@PathVariable Long userId) {
        return ok(appUserMapper.selectById(userId));
    }

    @Operation(summary = "状态修改")
    @OperateLog(title = "系统用户", subTitle = "状态修改", operateType = OperateType.UPDATE)
    @PostMapping("/edit_status")
    @CheckPerms("app_user:edit")
    public Result<Void> editStatus(@RequestBody EditStatusReq statusReq) {
        // 校验权限
        AppUser update = new AppUser(statusReq.getId());
        update.setStatus(statusReq.getStatus());
        return toRes(appUserMapper.updateById(update));
    }

    @Operation(summary = "导出")
    @OperateLog(title = "用户管理", operateType = OperateType.EXPORT)
    @PostMapping("/export")
    @CheckPerms("app_user:export")
    public void export(HttpServletResponse response, AppUserQueryReq req) {
        List<AppUser> list = appUserMapper.selectList(this.buildQueryWrapper(req));
        List<AppUserExport> convert = BeanUtil.convert(list, AppUserExport.class);
        ExcelUtil.exportExcel(convert, "用户数据", AppUserExport.class, response);
    }
}
