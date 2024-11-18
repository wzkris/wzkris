package com.wzkris.user.controller;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.MapstructUtil;
import com.wzkris.common.excel.utils.ExcelUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.page.Page;
import com.wzkris.common.security.utils.SysUtil;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.user.domain.SysPost;
import com.wzkris.user.domain.export.SysPostExport;
import com.wzkris.user.domain.req.SysPostQueryReq;
import com.wzkris.user.mapper.SysPostMapper;
import com.wzkris.user.service.SysPostService;
import com.wzkris.user.service.SysTenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
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
    private final SysPostMapper postMapper;
    private final SysPostService postService;
    private final SysTenantService tenantService;

    @Operation(summary = "岗位分页")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('post:list')")
    public Result<Page<SysPost>> listPage(SysPostQueryReq req) {
        startPage();
        List<SysPost> list = postMapper.selectList(this.buildQueryWrapper(req));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<SysPost> buildQueryWrapper(SysPostQueryReq req) {
        return new LambdaQueryWrapper<SysPost>()
                .like(ObjUtil.isNotEmpty(req.getPostName()), SysPost::getPostName, req.getPostName())
                .like(ObjUtil.isNotEmpty(req.getPostCode()), SysPost::getPostCode, req.getPostCode())
                .eq(ObjUtil.isNotEmpty(req.getStatus()), SysPost::getStatus, req.getStatus())
                .orderByDesc(SysPost::getPostSort);
    }


    @Operation(summary = "岗位详细信息")
    @GetMapping("/{postId}")
    @PreAuthorize("@ps.hasPerms('post:query')")
    public Result<SysPost> getInfo(@PathVariable Long postId) {
        return ok(postMapper.selectById(postId));
    }

    @Operation(summary = "新增岗位")
    @OperateLog(title = "岗位管理", subTitle = "新增岗位", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @PreAuthorize("@ps.hasPerms('post:add')")
    public Result<Void> add(@Validated @RequestBody SysPost sysPost) {
        if (!tenantService.checkPostLimit(SysUtil.getTenantId())) {
            return fail("岗位数量已达上限，请联系管理员");
        }
        return toRes(postMapper.insert(sysPost));
    }

    @Operation(summary = "修改岗位")
    @OperateLog(title = "岗位管理", subTitle = "修改岗位", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @PreAuthorize("@ps.hasPerms('post:edit')")
    public Result<Void> edit(@Validated @RequestBody SysPost sysPost) {
        return toRes(postMapper.updateById(sysPost));
    }

    @Operation(summary = "删除岗位")
    @OperateLog(title = "岗位管理", subTitle = "删除岗位", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('post:remove')")
    public Result<Void> remove(@RequestBody List<Long> postIds) {
        return toRes(postService.deleteByPostIds(postIds));
    }

    @Operation(summary = "导出")
    @OperateLog(title = "岗位管理", subTitle = "导出岗位数据", operateType = OperateType.EXPORT)
    @PostMapping("/export")
    @PreAuthorize("@ps.hasPerms('post:export')")
    public void export(HttpServletResponse response, SysPostQueryReq req) {
        List<SysPost> list = postMapper.selectList(this.buildQueryWrapper(req));
        List<SysPostExport> convert = MapstructUtil.convert(list, SysPostExport.class);
        ExcelUtil.exportExcel(convert, "岗位数据", SysPostExport.class, response);
    }
}
