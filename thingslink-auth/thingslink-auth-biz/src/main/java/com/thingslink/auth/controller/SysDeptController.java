package com.thingslink.auth.controller;

import cn.hutool.core.util.ObjUtil;
import com.thingslink.auth.domain.SysDept;
import com.thingslink.auth.domain.vo.SelectTree;
import com.thingslink.auth.mapper.SysDeptMapper;
import com.thingslink.auth.service.SysDeptService;
import com.thingslink.common.core.constant.CommonConstants;
import com.thingslink.common.core.domain.Result;
import com.thingslink.common.core.utils.StringUtil;
import com.thingslink.common.log.annotation.OperateLog;
import com.thingslink.common.log.enums.BusinessType;
import com.thingslink.common.web.controller.BaseController;
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
@RequestMapping("/dept")
@RequiredArgsConstructor
public class SysDeptController extends BaseController {
    private final SysDeptMapper sysDeptMapper;
    private final SysDeptService sysDeptService;

    @Operation(summary = "部门分页")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('dept:list')")
    public Result<List<SysDept>> list(SysDept dept) {
        List<SysDept> depts = sysDeptMapper.listChildren(dept);
        return success(depts);
    }

    @Operation(summary = "查询部门列表（排除节点）")
    @GetMapping("/list/exclude/{deptId}")
    @PreAuthorize("hasAuthority('dept:list')")
    public Result<List<SysDept>> excludeChild(@PathVariable Long deptId) {
        List<SysDept> depts = sysDeptMapper.listChildren(new SysDept());
        depts.removeIf(d -> d.getDeptId().intValue() == deptId
                || StringUtil.split(d.getAncestors(), ",", true, true).contains(String.valueOf(deptId)));
        return success(depts);
    }

    @Operation(summary = "根据部门编号获取详细信息")
    @GetMapping("/{deptId}")
    @PreAuthorize("hasAuthority('dept:query')")
    public Result<?> getInfo(@PathVariable Long deptId) {
        // 校验权限
        sysDeptService.checkDataScopes(deptId);
        return success(sysDeptMapper.selectById(deptId));
    }

    @Operation(summary = "新增部门")
    @OperateLog(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    @PreAuthorize("hasAuthority('dept:add')")
    public Result<?> add(@Validated @RequestBody SysDept dept) {
        // 校验权限
        sysDeptService.checkDataScopes(dept.getParentId());
        // 校验租户ID
        sysDeptService.checkTenantId(dept);
        return toRes(sysDeptService.insertDept(dept));
    }

    @Operation(summary = "修改部门")
    @OperateLog(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @PreAuthorize("hasAuthority('dept:edit')")
    public Result<?> edit(@Validated @RequestBody SysDept dept) {
        // 校验权限
        sysDeptService.checkDataScopes(dept.getDeptId());
        // 校验租户ID
        sysDeptService.checkTenantId(dept);
        if (ObjUtil.equals(dept.getParentId(), dept.getDeptId())) {
            return fail("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        }
        else if (StringUtil.equals(CommonConstants.STATUS_DISABLE, dept.getStatus())
                && sysDeptMapper.listNormalChildrenById(dept.getDeptId()) > 0) {
            return fail("该部门包含未停用的子部门");
        }
        return toRes(sysDeptService.updateDept(dept));
    }

    @Operation(summary = "删除部门")
    @OperateLog(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    @PreAuthorize("hasAuthority('dept:remove')")
    public Result<?> remove(@PathVariable Long deptId) {
        if (sysDeptService.hasChildByDeptId(deptId)) {
            return fail("存在下级部门,不允许删除");
        }
        if (sysDeptService.checkDeptExistUser(deptId)) {
            return fail("部门存在用户,不允许删除");
        }
        sysDeptService.checkDataScopes(deptId);
        return toRes(sysDeptService.deleteDeptById(deptId));
    }

    @Operation(summary = "部门树列表")
    @GetMapping("/deptTree")
    @PreAuthorize("hasAuthority('dept:list')")
    public Result<List<SelectTree>> deptTree(SysDept deptVo) {
        return success(sysDeptService.listDeptTree(deptVo));
    }
}