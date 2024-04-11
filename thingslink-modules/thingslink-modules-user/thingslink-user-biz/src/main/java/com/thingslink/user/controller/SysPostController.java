package com.thingslink.user.controller;

import com.thingslink.user.domain.SysPost;
import com.thingslink.user.mapper.SysPostMapper;
import com.thingslink.user.service.SysPostService;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.log.annotation.OperateLog;
import com.thingslink.common.log.enums.OperateType;
import com.thingslink.common.orm.page.Page;
import com.thingslink.common.orm.model.BaseController;
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
@RequestMapping("/post")
@RequiredArgsConstructor
public class SysPostController extends BaseController {
    private final SysPostMapper sysPostMapper;
    private final SysPostService sysPostService;

    @Operation(summary = "岗位分页")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('post:list')")
    public Result<Page<SysPost>> list(SysPost sysPost) {
        startPage();
        List<SysPost> list = sysPostService.list(sysPost);
        return BaseController.getDataTable(list);
    }

    @Operation(summary = "岗位详细信息")
    @GetMapping("/{postId}")
    @PreAuthorize("hasAuthority('post:query')")
    public Result<?> getInfo(@PathVariable Long postId) {
        return success(sysPostMapper.selectById(postId));
    }

    @Operation(summary = "新增岗位")
    @OperateLog(title = "岗位管理", operateType = OperateType.INSERT)
    @PostMapping
    @PreAuthorize("hasAuthority('post:add')")
    public Result<?> add(@Validated @RequestBody SysPost sysPost) {
        return toRes(sysPostMapper.insert(sysPost));
    }

    @Operation(summary = "修改岗位")
    @OperateLog(title = "岗位管理", operateType = OperateType.UPDATE)
    @PutMapping
    @PreAuthorize("hasAuthority('post:edit')")
    public Result<?> edit(@Validated @RequestBody SysPost sysPost) {
        return toRes(sysPostMapper.updateById(sysPost));
    }

    @Operation(summary = "修改岗位")
    @OperateLog(title = "岗位管理", operateType = OperateType.DELETE)
    @DeleteMapping("/{postIds}")
    @PreAuthorize("hasAuthority('post:remove')")
    public Result<?> remove(@PathVariable Long[] postIds) {
        return toRes(sysPostService.deleteByPostIds(postIds));
    }
}
