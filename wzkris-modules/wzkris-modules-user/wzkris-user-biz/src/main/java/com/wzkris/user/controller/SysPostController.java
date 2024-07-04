package com.wzkris.user.controller;

import com.wzkris.common.core.domain.Result;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.page.Page;
import com.wzkris.user.domain.SysPost;
import com.wzkris.user.mapper.SysPostMapper;
import com.wzkris.user.service.SysPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 岗位信息操作处理
 *
 * @author wzkris
 */
@Tag(name = "岗位管理")
@RestController
@RequestMapping("/sys_post")
@RequiredArgsConstructor
public class SysPostController extends BaseController {
    private final SysPostMapper sysPostMapper;
    private final SysPostService sysPostService;

    @Operation(summary = "岗位分页")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('post:list')")
    public Result<Page<SysPost>> list(SysPost sysPost) {
        startPage();
        List<SysPost> list = sysPostService.list(sysPost);
        return getDataTable(list);
    }

    @Operation(summary = "岗位详细信息")
    @GetMapping("/{postId}")
    @PreAuthorize("@ps.hasPerms('post:query')")
    public Result<?> getInfo(@PathVariable Long postId) {
        return success(sysPostMapper.selectById(postId));
    }

    @Operation(summary = "新增岗位")
    @OperateLog(title = "岗位管理", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @PreAuthorize("@ps.hasPerms('post:add')")
    public Result<?> add(@Validated @RequestBody SysPost sysPost) {
        return toRes(sysPostMapper.insert(sysPost));
    }

    @Operation(summary = "修改岗位")
    @OperateLog(title = "岗位管理", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @PreAuthorize("@ps.hasPerms('post:edit')")
    public Result<?> edit(@Validated @RequestBody SysPost sysPost) {
        return toRes(sysPostMapper.updateById(sysPost));
    }

    @Operation(summary = "修改岗位")
    @OperateLog(title = "岗位管理", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('post:remove')")
    public Result<?> remove(@RequestBody List<Long> postIds) {
        return toRes(sysPostService.deleteByPostIds(postIds));
    }
}