package com.wzkris.user.controller;

import cn.hutool.core.util.ObjUtil;
import com.wzkris.common.core.annotation.group.ValidationGroups;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.BeanUtil;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.security.oauth2.annotation.CheckSystemPerms;
import com.wzkris.common.security.utils.LoginUtil;
import com.wzkris.common.web.model.BaseController;
import com.wzkris.user.domain.SysDept;
import com.wzkris.user.domain.req.SysDeptQueryReq;
import com.wzkris.user.domain.req.SysDeptReq;
import com.wzkris.user.mapper.SysDeptMapper;
import com.wzkris.user.service.SysDeptService;
import com.wzkris.user.service.SysTenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

    private final SysTenantService tenantService;

    @Operation(summary = "部门列表(不带分页)")
    @GetMapping("/list")
    @CheckSystemPerms("sys_dept:list")
    public Result<List<SysDept>> list(SysDeptQueryReq queryReq) {
        List<SysDept> depts = deptMapper.listChildren(queryReq);
        return ok(depts);
    }

    @Operation(summary = "根据部门编号获取详细信息")
    @GetMapping("/{deptId}")
    @CheckSystemPerms("sys_dept:query")
    public Result<?> getInfo(@PathVariable Long deptId) {
        // 校验权限
        deptService.checkDataScopes(deptId);
        return ok(deptMapper.selectById(deptId));
    }

    @Operation(summary = "新增部门")
    @OperateLog(title = "部门管理", subTitle = "新增部门", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckSystemPerms("sys_dept:add")
    public Result<?> add(@Validated @RequestBody SysDeptReq req) {
        // 校验权限
        deptService.checkDataScopes(req.getParentId());
        if (!tenantService.checkDeptLimit(LoginUtil.getTenantId())) {
            return error412("部门数量已达上限，请联系管理员");
        }
        if (StringUtil.isNotNull(req.getParentId()) && req.getParentId() != 0) {
            SysDept info = deptMapper.selectById(req.getParentId());
            // 如果父节点为停用状态,则不允许新增子节点
            if (StringUtil.equals(CommonConstants.STATUS_DISABLE, info.getStatus())) {
                return error412("无法在被禁用的部门下添加下级");
            }
        }
        return toRes(deptService.insertDept(BeanUtil.convert(req, SysDept.class)));
    }

    @Operation(summary = "修改部门")
    @OperateLog(title = "部门管理", subTitle = "修改部门", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckSystemPerms("sys_dept:edit")
    public Result<?> edit(@Validated(value = ValidationGroups.Update.class) @RequestBody SysDeptReq req) {
        // 校验权限
        deptService.checkDataScopes(req.getDeptId());
        if (ObjUtil.equals(req.getParentId(), req.getDeptId())) {
            return error412("修改部门'" + req.getDeptName() + "'失败，上级部门不能是自己");
        } else if (StringUtil.equals(CommonConstants.STATUS_DISABLE, req.getStatus())
                && deptMapper.checkExistNormalChildren(req.getDeptId())) {
            return error412("该部门包含未停用的子部门");
        }
        return toRes(deptService.updateDept(BeanUtil.convert(req, SysDept.class)));
    }

    @Operation(summary = "删除部门")
    @OperateLog(title = "部门管理", subTitle = "删除部门", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckSystemPerms("sys_dept:remove")
    public Result<?> remove(@RequestBody Long deptId) {
        deptService.checkDataScopes(deptId);
        if (deptMapper.checkExistChildren(deptId)) {
            return error412("存在下级部门,不允许删除");
        }
        if (deptMapper.checkExistUser(deptId)) {
            return error412("部门存在用户,不允许删除");
        }
        return toRes(deptService.deleteById(deptId));
    }

}
