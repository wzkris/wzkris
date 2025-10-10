package com.wzkris.user.controller.role;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.model.Result;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.log.annotation.OperateLog;
import com.wzkris.common.log.enums.OperateType;
import com.wzkris.common.orm.model.BaseController;
import com.wzkris.common.orm.model.Page;
import com.wzkris.common.security.annotation.CheckUserPerms;
import com.wzkris.common.security.annotation.enums.CheckMode;
import com.wzkris.common.security.utils.LoginUserUtil;
import com.wzkris.common.validator.group.ValidationGroups;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.user.domain.RoleInfoDO;
import com.wzkris.user.domain.req.EditStatusReq;
import com.wzkris.user.domain.req.role.RoleManageQueryReq;
import com.wzkris.user.domain.req.role.RoleManageReq;
import com.wzkris.user.domain.req.role.RoleToUsersReq;
import com.wzkris.user.domain.req.user.UserManageQueryReq;
import com.wzkris.user.domain.vo.CheckedSelectTreeVO;
import com.wzkris.user.domain.vo.CheckedSelectVO;
import com.wzkris.user.domain.vo.SelectVO;
import com.wzkris.user.manager.DeptInfoDataScopeManager;
import com.wzkris.user.manager.RoleInfoDataScopeManager;
import com.wzkris.user.manager.UserInfoDataScopeManager;
import com.wzkris.user.mapper.RoleInfoMapper;
import com.wzkris.user.mapper.RoleToMenuMapper;
import com.wzkris.user.service.MenuInfoService;
import com.wzkris.user.service.RoleInfoService;
import com.wzkris.user.service.TenantInfoService;
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
public class RoleManageController extends BaseController {

    private final RoleInfoMapper roleInfoMapper;

    private final RoleToMenuMapper roleToMenuMapper;

    private final RoleInfoService roleInfoService;

    private final MenuInfoService menuInfoService;

    private final TenantInfoService tenantInfoService;

    private final UserInfoDataScopeManager userInfoDataScopeManager;

    private final RoleInfoDataScopeManager roleInfoDataScopeManager;

    private final DeptInfoDataScopeManager deptInfoDataScopeManager;

    @Operation(summary = "角色分页")
    @GetMapping("/list")
    @CheckUserPerms("user-mod:role-mng:list")
    public Result<Page<RoleInfoDO>> listPage(RoleManageQueryReq queryReq) {
        startPage();
        List<RoleInfoDO> list = roleInfoDataScopeManager.list(this.buildQueryWrapper(queryReq));
        return getDataTable(list);
    }

    private LambdaQueryWrapper<RoleInfoDO> buildQueryWrapper(RoleManageQueryReq queryReq) {
        return new LambdaQueryWrapper<RoleInfoDO>()
                .like(StringUtil.isNotEmpty(queryReq.getRoleName()), RoleInfoDO::getRoleName, queryReq.getRoleName())
                .eq(StringUtil.isNotEmpty(queryReq.getStatus()), RoleInfoDO::getStatus, queryReq.getStatus())
                .orderByDesc(RoleInfoDO::getRoleSort, RoleInfoDO::getRoleId);
    }

    @Operation(summary = "角色详细信息")
    @GetMapping("/{roleId}")
    @CheckUserPerms("user-mod:role-mng:query")
    public Result<RoleInfoDO> getInfo(@PathVariable Long roleId) {
        // 权限校验
        roleInfoDataScopeManager.checkDataScopes(roleId);
        return ok(roleInfoMapper.selectById(roleId));
    }

    @Operation(summary = "角色-菜单选择树")
    @GetMapping({"/menu-checked-selecttree/", "/menu-checked-selecttree/{roleId}"})
    @CheckUserPerms(
            value = {"user-mod:role-mng:edit", "user-mod:role-mng:add"},
            mode = CheckMode.OR)
    public Result<CheckedSelectTreeVO> roleMenuSelectTree(@PathVariable(required = false) Long roleId) {
        // 权限校验
        roleInfoDataScopeManager.checkDataScopes(roleId);
        CheckedSelectTreeVO checkedSelectTreeVO = new CheckedSelectTreeVO();
        checkedSelectTreeVO.setCheckedKeys(
                roleId == null ? Collections.emptyList()
                        : roleToMenuMapper.listMenuIdByRoleIds(Collections.singletonList(roleId)));
        checkedSelectTreeVO.setSelectTrees(menuInfoService.listSystemSelectTree(LoginUserUtil.getId()));
        return ok(checkedSelectTreeVO);
    }

    @Operation(summary = "角色-部门选择树")
    @GetMapping({"/dept-checked-selecttree/", "/dept-checked-selecttree/{roleId}"})
    @CheckUserPerms(
            value = {"user-mod:role-mng:edit", "user-mod:role-mng:add"},
            mode = CheckMode.OR)
    public Result<CheckedSelectTreeVO> roleDeptSelectTree(@PathVariable(required = false) Long roleId) {
        // 权限校验
        roleInfoDataScopeManager.checkDataScopes(roleId);
        CheckedSelectTreeVO checkedSelectTreeVO = new CheckedSelectTreeVO();
        checkedSelectTreeVO.setCheckedKeys(
                roleId == null ? Collections.emptyList() : deptInfoDataScopeManager.listDeptIdByRoleId(roleId));
        checkedSelectTreeVO.setSelectTrees(deptInfoDataScopeManager.listSelectTree(null));
        return ok(checkedSelectTreeVO);
    }

    @Operation(summary = "角色-继承选择列表")
    @GetMapping({"/hierarchy-checked-select/", "/hierarchy-checked-select/{roleId}"})
    @CheckUserPerms(
            value = {"user-mod:role-mng:edit", "user-mod:role-mng:add"},
            mode = CheckMode.OR)
    public Result<CheckedSelectVO> roleInheritedSelect(@PathVariable(required = false) Long roleId) {
        roleInfoDataScopeManager.checkDataScopes(roleId);
        CheckedSelectVO checkedSelectVO = new CheckedSelectVO();
        checkedSelectVO.setCheckedKeys(roleId == null ?
                Collections.emptyList() : roleInfoDataScopeManager.listInheritedIdByRoleId(roleId));
        checkedSelectVO.setSelects(roleInfoDataScopeManager.listInheritedSelect(roleId));
        return ok(checkedSelectVO);
    }

    @Operation(summary = "新增角色")
    @OperateLog(title = "角色管理", subTitle = "新增角色", operateType = OperateType.INSERT)
    @PostMapping("/add")
    @CheckUserPerms("user-mod:role-mng:add")
    public Result<Void> add(@Validated @RequestBody RoleManageReq req) {
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
    @OperateLog(title = "角色管理", subTitle = "修改角色", operateType = OperateType.UPDATE)
    @PostMapping("/edit")
    @CheckUserPerms("user-mod:role-mng:edit")
    public Result<Void> edit(@Validated(value = ValidationGroups.Update.class) @RequestBody RoleManageReq req) {
        // 权限校验
        roleInfoDataScopeManager.checkDataScopes(req.getRoleId());
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
    @OperateLog(title = "用户管理", subTitle = "状态修改", operateType = OperateType.UPDATE)
    @PostMapping("/edit-status")
    @CheckUserPerms("user-mod:role-mng:edit")
    public Result<Void> editStatus(@RequestBody EditStatusReq statusReq) {
        // 校验权限
        roleInfoDataScopeManager.checkDataScopes(statusReq.getId());
        RoleInfoDO update = new RoleInfoDO(statusReq.getId());
        update.setStatus(statusReq.getStatus());
        return toRes(roleInfoMapper.updateById(update));
    }

    @Operation(summary = "删除角色")
    @OperateLog(title = "角色管理", subTitle = "删除角色", operateType = OperateType.DELETE)
    @PostMapping("/remove")
    @CheckUserPerms("user-mod:role-mng:remove")
    public Result<Void> remove(
            @RequestBody @NotEmpty(message = "{invalidParameter.id.invalid}") List<Long> roleIds) {
        // 权限校验
        roleInfoDataScopeManager.checkDataScopes(roleIds);
        roleInfoService.existUser(roleIds);
        roleInfoService.existInherited(roleIds);
        return toRes(roleInfoService.removeByIds(roleIds));
    }

    @Operation(summary = "已授权的用户列表")
    @GetMapping("/authorized-user-list")
    @CheckUserPerms("user-mod:role-mng:grant-user")
    public Result<Page<SelectVO>> allocatedList(UserManageQueryReq queryReq, Long roleId) {
        // 校验角色权限
        roleInfoDataScopeManager.checkDataScopes(roleId);
        startPage();
        List<SelectVO> list = userInfoDataScopeManager.listAllocated(queryReq, roleId);
        return getDataTable(list);
    }

    @Operation(summary = "未授权的用户列表")
    @GetMapping("/unauthorized-user-list")
    @CheckUserPerms("user-mod:role-mng:grant-user")
    public Result<Page<SelectVO>> unallocatedList(UserManageQueryReq queryReq, Long roleId) {
        // 校验角色权限
        roleInfoDataScopeManager.checkDataScopes(roleId);
        startPage();
        List<SelectVO> list = userInfoDataScopeManager.listUnallocated(queryReq, roleId);
        return getDataTable(list);
    }

    @Operation(summary = "取消授权")
    @OperateLog(title = "角色管理", subTitle = "取消授权", operateType = OperateType.GRANT)
    @PostMapping("/cancel-authorize-user")
    @CheckUserPerms("user-mod:role-mng:grant-user")
    public Result<Void> cancelAuth(@RequestBody @Valid RoleToUsersReq req) {
        // 权限校验
        roleInfoDataScopeManager.checkDataScopes(req.getRoleId());
        // 校验用户权限
        userInfoDataScopeManager.checkDataScopes(req.getUserIds());
        return toRes(roleInfoService.ungrantUsers(req.getRoleId(), req.getUserIds()));
    }

    @Operation(summary = "角色授权")
    @OperateLog(title = "角色管理", subTitle = "授权用户", operateType = OperateType.GRANT)
    @PostMapping("/authorize-user")
    @CheckUserPerms("user-mod:role-mng:grant-user")
    public Result<Void> batchAuth(@RequestBody @Valid RoleToUsersReq req) {
        // 权限校验
        roleInfoDataScopeManager.checkDataScopes(req.getRoleId());
        // 校验用户权限
        userInfoDataScopeManager.checkDataScopes(req.getUserIds());
        return toRes(roleInfoService.grantUsers(req.getRoleId(), req.getUserIds()));
    }

}
