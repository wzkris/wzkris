package com.wzkris.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.BeanUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckSystemPerms;
import com.wzkris.common.security.utils.SystemUserUtil;
import com.wzkris.user.domain.SysPost;
import com.wzkris.user.domain.req.SysPostQueryReq;
import com.wzkris.user.domain.req.SysPostReq;
import com.wzkris.user.mapper.SysPostMapper;
import com.wzkris.user.service.SysPostService;
import com.wzkris.user.service.SysTenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    @CheckSystemPerms("sys_post:list")
    public Result<Page<SysPost>> listPage(SysPostQueryReq req) {
        startPage();
        List<SysPost> list = postMapper.selectList(this.buildQueryWrapper(req));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<SysPost> buildQueryWrapper(SysPostQueryReq req) {
        return new LambdaQueryWrapper<SysPost>()
                .like(StringUtil.isNotEmpty(req.getPostName()), SysPost::getPostName, req.getPostName())
                .like(StringUtil.isNotEmpty(req.getPostCode()), SysPost::getPostCode, req.getPostCode())
                .eq(StringUtil.isNotEmpty(req.getStatus()), SysPost::getStatus, req.getStatus())
                .orderByDesc(SysPost::getPostSort);
    }

    @Operation(summary = "岗位详细信息")
    @GetMapping("/{postId}")
    @CheckSystemPerms("sys_post:query")
    public Result<SysPost> getInfo(@PathVariable Long postId) {
        return ok(postMapper.selectById(postId));
    }

    @Operation(summary = "新增岗位")
    @OperateLog(title = "岗位管理", subTitle = "新增岗位", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckSystemPerms("sys_post:add")
    public Result<Void> add(@Validated @RequestBody SysPostReq req) {
        if (!tenantService.checkPostLimit(SystemUserUtil.getTenantId())) {
            return err412("岗位数量已达上限，请联系管理员");
        }
        return toRes(postMapper.insert(BeanUtil.convert(req, SysPost.class)));
    }

    @Operation(summary = "修改岗位")
    @OperateLog(title = "岗位管理", subTitle = "修改岗位", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckSystemPerms("sys_post:edit")
    public Result<Void> edit(@Validated @RequestBody SysPostReq req) {
        return toRes(postMapper.updateById(BeanUtil.convert(req, SysPost.class)));
    }

    @Operation(summary = "删除岗位")
    @OperateLog(title = "岗位管理", subTitle = "删除岗位", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckSystemPerms("sys_post:remove")
    public Result<Void> remove(@RequestBody List<Long> postIds) {
        postService.checkPostUsed(postIds);
        return toRes(postService.deleteByPostIds(postIds));
    }

}
