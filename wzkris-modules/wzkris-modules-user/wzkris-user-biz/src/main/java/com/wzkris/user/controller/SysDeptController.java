package com.wzkris.user.controller;

import cn.hutool.core.util.ObjUtil;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.user.domain.SysDept;
import com.wzkris.user.domain.vo.SelectTreeVO;
import com.wzkris.user.mapper.SysDeptMapper;
import com.wzkris.user.service.SysDeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门信息
 *
 * @author wzkris
 */
@Tag(name = "部门管理")
@RestController
@RequestMapping("/sys_dept")
@RequiredArgsConstructor
public class SysDeptController extends BaseController {
    private final SysDeptMapper deptMapper;
    private final SysDeptService deptService;

    @Operation(summary = "部门分页")
    @GetMapping("/list")
    @PreAuthorize("@ps.hasPerms('dept:list')")
    public Result<List<SysDept>> list(SysDept dept) {
        List<SysDept> depts = deptMapper.listChildren(dept);
        return ok(depts);
    }

    @Operation(summary = "查询部门列表（排除节点）")
    @GetMapping("/list/exclude/{deptId}")
    @PreAuthorize("@ps.hasPerms('dept:list')")
    public Result<List<SysDept>> excludeChild(@PathVariable Long deptId) {
        List<SysDept> depts = deptMapper.listChildren(new SysDept());
        depts.removeIf(d -> d.getDeptId().intValue() == deptId
                || StringUtil.split(d.getAncestors(), ",", true, true).contains(String.valueOf(deptId)));
        return ok(depts);
    }

    @Operation(summary = "根据部门编号获取详细信息")
    @GetMapping("/{deptId}")
    @PreAuthorize("@ps.hasPerms('dept:query')")
    public Result<?> getInfo(@PathVariable Long deptId) {
        // 校验权限
        deptService.checkDataScopes(deptId);
        return ok(deptMapper.selectById(deptId));
    }

    @Operation(summary = "新增部门")
    @OperateLog(title = "部门管理", subTitle = "新增部门", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @PreAuthorize("@ps.hasPerms('dept:add')")
    public Result<?> add(@Validated @RequestBody SysDept dept) {
        // 校验权限
        deptService.checkDataScopes(dept.getParentId());
        return toRes(deptService.insertDept(dept));
    }

    @Operation(summary = "修改部门")
    @OperateLog(title = "部门管理", subTitle = "修改部门", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @PreAuthorize("@ps.hasPerms('dept:edit')")
    public Result<?> edit(@Validated @RequestBody SysDept dept) {
        // 校验权限
        deptService.checkDataScopes(dept.getDeptId());
        if (ObjUtil.equals(dept.getParentId(), dept.getDeptId())) {
            return fail("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        }
        else if (StringUtil.equals(CommonConstants.STATUS_DISABLE, dept.getStatus())
                && deptMapper.listNormalChildrenById(dept.getDeptId()) > 0) {
            return fail("该部门包含未停用的子部门");
        }
        return toRes(deptService.updateDept(dept));
    }

    @Operation(summary = "删除部门")
    @OperateLog(title = "部门管理", subTitle = "删除部门", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @PreAuthorize("@ps.hasPerms('dept:remove')")
    public Result<?> remove(@RequestBody Long deptId) {
        if (deptService.hasChildByDeptId(deptId)) {
            return fail("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId)) {
            return fail("部门存在用户,不允许删除");
        }
        deptService.checkDataScopes(deptId);
        return toRes(deptMapper.deleteById(deptId));
    }

    @Operation(summary = "部门树列表")
    @GetMapping("/tree")
    @PreAuthorize("@ps.hasPerms('dept:list')")
    public Result<List<SelectTreeVO>> deptTree(SysDept sysDept) {
        return ok(deptService.listDeptTree(sysDept));
    }
}
