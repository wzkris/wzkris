package com.wzkris.usercenter.controller.role;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateTypeEnum;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckAdminPerms;
import com.wzkris.common.security.enums.CheckMode;
import com.wzkris.common.security.utils.AdminUtil;
import com.wzkris.common.validator.group.ValidationGroups;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.domain.RoleInfoDO;
import com.wzkris.usercenter.domain.req.EditStatusReq;
import com.wzkris.usercenter.domain.req.admin.AdminMngQueryReq;
import com.wzkris.usercenter.domain.req.role.RoleMngQueryReq;
import com.wzkris.usercenter.domain.req.role.RoleMngReq;
import com.wzkris.usercenter.domain.req.role.RoleToAdminsReq;
import com.wzkris.usercenter.domain.vo.CheckedSelectTreeVO;
import com.wzkris.usercenter.domain.vo.CheckedSelectVO;
import com.wzkris.usercenter.domain.vo.SelectVO;
import com.wzkris.usercenter.manager.AdminInfoDscManager;
import com.wzkris.usercenter.manager.DeptInfoDscManager;
import com.wzkris.usercenter.manager.RoleInfoDscManager;
import com.wzkris.usercenter.mapper.RoleInfoMapper;
import com.wzkris.usercenter.mapper.RoleToMenuMapper;
import com.wzkris.usercenter.service.MenuInfoService;
import com.wzkris.usercenter.service.RoleInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * 角色信息
 *
 * @author wzkris
 */
@Tag(name = "角色管理")
@Validated
@RestController
@RequestMapping("/role-manage")
@RequiredArgsConstructor
public class RoleMngController extends BaseController {

    private final RoleInfoMapper roleInfoMapper;

    private final RoleToMenuMapper roleToMenuMapper;

    private final RoleInfoService roleInfoService;

    private final MenuInfoService menuInfoService;

    private final AdminInfoDscManager adminInfoDscManager;

    private final RoleInfoDscManager roleInfoDscManager;

    private final DeptInfoDscManager deptInfoDscManager;

    @Operation(summary = "角色分页")
    @GetMapping("/page")
    @CheckAdminPerms("user-mod:role-mng:page")
    public Result<Page<RoleInfoDO>> page(RoleMngQueryReq queryReq) {
        startPage();
        List<RoleInfoDO> list = roleInfoDscManager.list(this.buildQueryWrapper(queryReq));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<RoleInfoDO> buildQueryWrapper(RoleMngQueryReq queryReq) {
        return new LambdaQueryWrapper<RoleInfoDO>()
                .like(StringUtil.isNotEmpty(queryReq.getRoleName()), RoleInfoDO::getRoleName, queryReq.getRoleName())
                .eq(StringUtil.isNotEmpty(queryReq.getStatus()), RoleInfoDO::getStatus, queryReq.getStatus())
                .orderByDesc(RoleInfoDO::getRoleSort, RoleInfoDO::getRoleId);
    }

    @Operation(summary = "角色详细信息")
    @GetMapping("/{roleId}")
    @CheckAdminPerms("user-mod:role-mng:query")
    public Result<RoleInfoDO> getInfo(@PathVariable Long roleId) {
        // 权限校验
        roleInfoDscManager.checkDataScopes(roleId);
        return ok(roleInfoMapper.selectById(roleId));
    }

    @Operation(summary = "角色-菜单选择树")
    @GetMapping({"/menu-checked-selecttree/", "/menu-checked-selecttree/{roleId}"})
    @CheckAdminPerms(
            value = {"user-mod:role-mng:edit", "user-mod:role-mng:add"},
            mode = CheckMode.OR)
    public Result<CheckedSelectTreeVO> roleMenuSelectTree(@PathVariable(required = false) Long roleId) {
        // 权限校验
        roleInfoDscManager.checkDataScopes(roleId);
        CheckedSelectTreeVO checkedSelectTreeVO = new CheckedSelectTreeVO();
        checkedSelectTreeVO.setCheckedKeys(
                roleId == null ? Collections.emptyList()
                        : roleToMenuMapper.listMenuIdByRoleIds(Collections.singletonList(roleId)));
        checkedSelectTreeVO.setSelectTrees(menuInfoService.listSystemSelectTree(AdminUtil.getId()));
        return ok(checkedSelectTreeVO);
    }

    @Operation(summary = "角色-部门选择树")
    @GetMapping({"/dept-checked-selecttree/", "/dept-checked-selecttree/{roleId}"})
    @CheckAdminPerms(
            value = {"user-mod:role-mng:edit", "user-mod:role-mng:add"},
            mode = CheckMode.OR)
    public Result<CheckedSelectTreeVO> roleDeptSelectTree(@PathVariable(required = false) Long roleId) {
        // 权限校验
        roleInfoDscManager.checkDataScopes(roleId);
        CheckedSelectTreeVO checkedSelectTreeVO = new CheckedSelectTreeVO();
        checkedSelectTreeVO.setCheckedKeys(
                roleId == null ? Collections.emptyList() : deptInfoDscManager.listDeptIdByRoleId(roleId));
        checkedSelectTreeVO.setSelectTrees(deptInfoDscManager.listSelectTree(null));
        return ok(checkedSelectTreeVO);
    }

    @Operation(summary = "角色-继承选择列表")
    @GetMapping({"/hierarchy-checked-select/", "/hierarchy-checked-select/{roleId}"})
    @CheckAdminPerms(
            value = {"user-mod:role-mng:edit", "user-mod:role-mng:add"},
            mode = CheckMode.OR)
    public Result<CheckedSelectVO> roleInheritedSelect(@PathVariable(required = false) Long roleId) {
        roleInfoDscManager.checkDataScopes(roleId);
        CheckedSelectVO checkedSelectVO = new CheckedSelectVO();
        checkedSelectVO.setCheckedKeys(roleId == null ?
                Collections.emptyList() : roleInfoDscManager.listInheritedIdByRoleId(roleId));
        checkedSelectVO.setSelects(roleInfoDscManager.listInheritedSelect(roleId));
        return ok(checkedSelectVO);
    }

    @Operation(summary = "新增角色")
    @OperateLog(title = "角色管理", subTitle = "新增角色", type = OperateTypeEnum.INSERT)
    @PostMapping("/add")
    @CheckAdminPerms("user-mod:role-mng:add")
    public Result<Void> add(@Validated @RequestBody RoleMngReq req) {
        if (CollectionUtils.isNotEmpty(req.getInheritedIds())) {
            if (req.getInherited()) {
                roleInfoService.checkInheritedRole(null, req.getInheritedIds());
            } else {
                return err40000("非继承角色不允许继承");
            }
        }

        RoleInfoDO role = BeanUtil.convert(req, RoleInfoDO.class);
        return toRes(roleInfoService.saveRole(role, req.getMenuIds(), req.getDeptIds(), req.getInheritedIds()));
    }

    @Operation(summary = "修改角色")
    @OperateLog(title = "角色管理", subTitle = "修改角色", type = OperateTypeEnum.UPDATE)
    @PostMapping("/edit")
    @CheckAdminPerms("user-mod:role-mng:edit")
    public Result<Void> edit(@Validated(value = ValidationGroups.Update.class) @RequestBody RoleMngReq req) {
        // 权限校验
        roleInfoDscManager.checkDataScopes(req.getRoleId());
        if (CollectionUtils.isNotEmpty(req.getInheritedIds())) {
            if (req.getInherited()) {
                roleInfoService.checkInheritedRole(req.getRoleId(), req.getInheritedIds());
            } else {
                return err40000("非继承角色不允许继承");
            }
        }

        RoleInfoDO role = BeanUtil.convert(req, RoleInfoDO.class);
        return toRes(roleInfoService.modifyRole(role, req.getMenuIds(), req.getDeptIds(), req.getInheritedIds()));
    }

    @Operation(summary = "状态修改")
    @OperateLog(title = "用户管理", subTitle = "状态修改", type = OperateTypeEnum.UPDATE)
    @PostMapping("/edit-status")
    @CheckAdminPerms("user-mod:role-mng:edit")
    public Result<Void> editStatus(@RequestBody EditStatusReq statusReq) {
        // 校验权限
        roleInfoDscManager.checkDataScopes(statusReq.getId());
        RoleInfoDO update = new RoleInfoDO(statusReq.getId());
        update.setStatus(statusReq.getStatus());
        return toRes(roleInfoMapper.updateById(update));
    }

    @Operation(summary = "删除角色")
    @OperateLog(title = "角色管理", subTitle = "删除角色", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckAdminPerms("user-mod:role-mng:remove")
    public Result<Void> remove(
            @RequestBody @NotEmpty(message = "{invalidParameter.id.invalid}") List<Long> roleIds) {
        // 权限校验
        roleInfoDscManager.checkDataScopes(roleIds);
        roleInfoService.existAdmin(roleIds);
        roleInfoService.existInherited(roleIds);
        return toRes(roleInfoService.removeByIds(roleIds));
    }

    @Operation(summary = "已授权的用户列表")
    @GetMapping("/authorized-admin-list")
    @CheckAdminPerms("user-mod:role-mng:grant-admin")
    public Result<Page<SelectVO>> allocatedList(AdminMngQueryReq queryReq, Long roleId) {
        // 校验角色权限
        roleInfoDscManager.checkDataScopes(roleId);
        startPage();
        List<SelectVO> list = adminInfoDscManager.listAllocated(queryReq, roleId);
        return getDataTable(list);
    }

    @Operation(summary = "未授权的用户列表")
    @GetMapping("/unauthorized-admin-list")
    @CheckAdminPerms("user-mod:role-mng:grant-admin")
    public Result<Page<SelectVO>> unallocatedList(AdminMngQueryReq queryReq, Long roleId) {
        // 校验角色权限
        roleInfoDscManager.checkDataScopes(roleId);
        startPage();
        List<SelectVO> list = adminInfoDscManager.listUnallocated(queryReq, roleId);
        return getDataTable(list);
    }

    @Operation(summary = "取消授权")
    @OperateLog(title = "角色管理", subTitle = "取消授权", type = OperateTypeEnum.GRANT)
    @PostMapping("/cancel-authorize-admin")
    @CheckAdminPerms("user-mod:role-mng:grant-admin")
    public Result<Void> cancelAuth(@RequestBody @Valid RoleToAdminsReq req) {
        // 权限校验
        roleInfoDscManager.checkDataScopes(req.getRoleId());
        // 校验用户权限
        adminInfoDscManager.checkDataScopes(req.getAdminIds());
        return toRes(roleInfoService.ungrantAdmins(req.getRoleId(), req.getAdminIds()));
    }

    @Operation(summary = "角色授权")
    @OperateLog(title = "角色管理", subTitle = "授权用户", type = OperateTypeEnum.GRANT)
    @PostMapping("/authorize-admin")
    @CheckAdminPerms("user-mod:role-mng:grant-admin")
    public Result<Void> batchAuth(@RequestBody @Valid RoleToAdminsReq req) {
        // 权限校验
        roleInfoDscManager.checkDataScopes(req.getRoleId());
        // 校验用户权限
        adminInfoDscManager.checkDataScopes(req.getAdminIds());
        return toRes(roleInfoService.grantAdmins(req.getRoleId(), req.getAdminIds()));
    }

}
