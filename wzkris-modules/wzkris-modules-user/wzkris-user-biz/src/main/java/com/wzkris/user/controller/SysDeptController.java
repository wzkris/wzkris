package com.wzkris.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.security.annotation.CheckSystemPerms;
import com.wzkris.common.security.utils.SystemUserUtil;
import com.wzkris.common.validator.group.ValidationGroups;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.user.domain.SysDept;
import com.wzkris.user.domain.req.SysDeptQueryReq;
import com.wzkris.user.domain.req.SysDeptReq;
import com.wzkris.user.manager.SysDeptDataScopeManager;
import com.wzkris.user.mapper.SysDeptMapper;
import com.wzkris.user.service.SysDeptService;
import com.wzkris.user.service.SysTenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

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

    private final SysDeptDataScopeManager deptDataScopeManager;

    private final SysTenantService tenantService;

    @Operation(summary = "部门列表(不带分页)")
    @GetMapping("/list")
    @CheckSystemPerms("sys_dept:list")
    public Result<List<SysDept>> list(SysDeptQueryReq queryReq) {
        List<SysDept> depts = deptDataScopeManager.list(buildQueryWrapper(queryReq));
        return ok(depts);
    }

    private LambdaQueryWrapper<SysDept> buildQueryWrapper(SysDeptQueryReq queryReq) {
        return new LambdaQueryWrapper<SysDept>()
                .apply(queryReq.getParentId() != null && queryReq.getParentId() != 0,
                        "{0} = ANY(ancestors)", queryReq.getParentId())
                .and(queryReq.getDeptId() != null && queryReq.getDeptId() != 0,
                        w -> w.eq(SysDept::getDeptId, queryReq.getDeptId())
                                .or()
                                .apply("{0} = ANY(ancestors)", queryReq.getDeptId())
                )
                .like(StringUtil.isNotEmpty(queryReq.getDeptName()), SysDept::getDeptName, queryReq.getDeptName())
                .eq(StringUtil.isNotEmpty(queryReq.getStatus()), SysDept::getStatus, queryReq.getStatus())
                .orderByDesc(SysDept::getDeptSort, SysDept::getDeptId);
    }

    @Operation(summary = "根据部门编号获取详细信息")
    @GetMapping("/{deptId}")
    @CheckSystemPerms("sys_dept:query")
    public Result<?> getInfo(@PathVariable Long deptId) {
        // 校验权限
        deptDataScopeManager.checkDataScopes(deptId);
        return ok(deptMapper.selectById(deptId));
    }

    @Operation(summary = "新增部门")
    @OperateLog(title = "部门管理", subTitle = "新增部门", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckSystemPerms("sys_dept:add")
    public Result<?> add(@Validated @RequestBody SysDeptReq req) {
        // 校验权限
        deptDataScopeManager.checkDataScopes(req.getParentId());
        if (!tenantService.checkDeptLimit(SystemUserUtil.getTenantId())) {
            return err40000("部门数量已达上限，请联系管理员");
        }
        if (ObjectUtils.isNotEmpty(req.getParentId()) && req.getParentId() != 0) {
            SysDept info = deptMapper.selectById(req.getParentId());
            // 如果父节点为停用状态,则不允许新增子节点
            if (StringUtil.equals(CommonConstants.STATUS_DISABLE, info.getStatus())) {
                return err40000("无法在被禁用的部门下添加下级");
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
        deptDataScopeManager.checkDataScopes(req.getDeptId());
        if (Objects.equals(req.getParentId(), req.getDeptId())) {
            return err40000("修改部门'" + req.getDeptName() + "'失败，上级部门不能是自己");
        } else if (StringUtil.equals(CommonConstants.STATUS_DISABLE, req.getStatus())
                && deptMapper.existNormalSubDept(req.getDeptId())) {
            return err40000("该部门包含未停用的子部门");
        }
        return toRes(deptService.updateDept(BeanUtil.convert(req, SysDept.class)));
    }

    @Operation(summary = "删除部门")
    @OperateLog(title = "部门管理", subTitle = "删除部门", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckSystemPerms("sys_dept:remove")
    public Result<?> remove(@RequestBody Long deptId) {
        deptDataScopeManager.checkDataScopes(deptId);
        if (deptMapper.existSubDept(deptId)) {
            return err40000("存在下级部门,不允许删除");
        }
        if (deptMapper.existUser(deptId)) {
            return err40000("部门存在用户,不允许删除");
        }
        return toRes(deptService.deleteById(deptId));
    }

}
