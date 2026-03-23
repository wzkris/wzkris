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
import com.wzkris.common.security.utils.SecurityUtil;
import com.wzkris.common.web.utils.BeanUtil;
import com.wzkris.usercenter.domain.RoleInfoDO;
import com.wzkris.usercenter.domain.req.StatusEditReq;
import com.wzkris.usercenter.domain.req.role.RoleMngAddReq;
import com.wzkris.usercenter.domain.req.role.RoleMngEditReq;
import com.wzkris.usercenter.domain.req.role.RoleMngQueryReq;
import com.wzkris.usercenter.domain.resp.CheckedSelectResp;
import com.wzkris.usercenter.domain.resp.CheckedSelectTreeResp;
import com.wzkris.usercenter.domain.resp.SelectResp;
import com.wzkris.usercenter.domain.resp.SelectTreeResp;
import com.wzkris.usercenter.mapper.DeptInfoMapper;
import com.wzkris.usercenter.mapper.RoleInfoMapper;
import com.wzkris.usercenter.mapper.RoleInheritanceMapper;
import com.wzkris.usercenter.mapper.RoleToMenuMapper;
import com.wzkris.usercenter.service.MenuInfoService;
import com.wzkris.usercenter.service.RoleInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    private final DeptInfoMapper deptInfoMapper;

    private final RoleInheritanceMapper roleInheritanceMapper;

    @Operation(summary = "角色分页")
    @GetMapping("/page")
    @CheckAdminPerms("user-mod:role-mng:page")
    public Result<Page<RoleInfoDO>> page(RoleMngQueryReq queryReq) {
        startPage();
        List<RoleInfoDO> list = roleInfoMapper.selectLists(this.buildQueryWrapper(queryReq));
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
        roleInfoMapper.checkDataScopes(roleId);
        return ok(roleInfoMapper.selectById(roleId));
    }

    @Operation(summary = "角色 - 菜单选择树")
    @GetMapping({"/menu-checked-selecttree/", "/menu-checked-selecttree/{roleId}"})
    @CheckAdminPerms(
            value = {"user-mod:role-mng:edit", "user-mod:role-mng:add"},
            mode = CheckMode.OR)
    public Result<CheckedSelectTreeResp> roleMenuSelectTree(@PathVariable(required = false) Long roleId) {
        roleInfoMapper.checkDataScopes(roleId);
        CheckedSelectTreeResp checkedSelectTreeResp = new CheckedSelectTreeResp();
        checkedSelectTreeResp.setCheckedKeys(
                roleId == null ? Collections.emptyList()
                        : roleToMenuMapper.listMenuIdByRoleIds(Collections.singletonList(roleId)));
        checkedSelectTreeResp.setSelectTrees(menuInfoService.listSystemSelectTree(SecurityUtil.getUid()));
        return ok(checkedSelectTreeResp);
    }

    @Operation(summary = "角色 - 部门选择树")
    @GetMapping({"/dept-checked-selecttree/", "/dept-checked-selecttree/{roleId}"})
    @CheckAdminPerms(
            value = {"user-mod:role-mng:edit", "user-mod:role-mng:add"},
            mode = CheckMode.OR)
    public Result<CheckedSelectTreeResp> roleDeptSelectTree(@PathVariable(required = false) Long roleId) {
        roleInfoMapper.checkDataScopes(roleId);
        CheckedSelectTreeResp checkedSelectTreeResp = new CheckedSelectTreeResp();
        checkedSelectTreeResp.setCheckedKeys(
                roleId == null ? Collections.emptyList() : deptInfoMapper.listDeptIdByRoleIds(Collections.singletonList(roleId)));
        checkedSelectTreeResp.setSelectTrees(deptInfoMapper.selectLists(null).stream().map(SelectTreeResp::new).toList());
        return ok(checkedSelectTreeResp);
    }

    @Operation(summary = "角色 - 继承选择列表")
    @GetMapping({"/hierarchy-checked-select/", "/hierarchy-checked-select/{roleId}"})
    @CheckAdminPerms(
            value = {"user-mod:role-mng:edit", "user-mod:role-mng:add"},
            mode = CheckMode.OR)
    public Result<CheckedSelectResp> roleInheritedSelect(@PathVariable(required = false) Long roleId) {
        roleInfoMapper.checkDataScopes(roleId);
        CheckedSelectResp checkedSelectResp = new CheckedSelectResp();
        checkedSelectResp.setCheckedKeys(roleId == null ?
                Collections.emptyList() : roleInheritanceMapper.listChildIdsByRoleId(roleId));
        checkedSelectResp.setSelects(roleInfoMapper.selectLists(null).stream()
                .filter(role -> !Objects.equals(role.getRoleId(), roleId))
                .map(SelectResp::new)
                .toList());
        return ok(checkedSelectResp);
    }

    @Operation(summary = "新增角色")
    @OperateLog(title = "角色管理", subTitle = "新增角色", type = OperateTypeEnum.INSERT)
    @PostMapping("/add")
    @CheckAdminPerms("user-mod:role-mng:add")
    public Result<Void> add(@Validated @RequestBody RoleMngAddReq req) {
        RoleInfoDO role = BeanUtil.convert(req, RoleInfoDO.class);
        return toRes(roleInfoService.saveRole(role, req.getMenuIds(), req.getDeptIds(), req.getChildIds()));
    }

    @Operation(summary = "修改角色")
    @OperateLog(title = "角色管理", subTitle = "修改角色", type = OperateTypeEnum.UPDATE)
    @PostMapping("/edit")
    @CheckAdminPerms("user-mod:role-mng:edit")
    public Result<Void> edit(@Validated @RequestBody RoleMngEditReq req) {
        if (req.getChildIds() != null && req.getChildIds().contains(req.getRoleId())) {
            return requestFail("角色不能继承自身");
        }
        if (req.getChildIds() != null && roleInheritanceMapper
                .listChildIdsRecursive(req.getChildIds())
                .contains(req.getRoleId())) {
            return requestFail("角色继承关系存在循环");
        }
        RoleInfoDO role = BeanUtil.convert(req, RoleInfoDO.class);
        return toRes(roleInfoService.modifyRole(role, req.getMenuIds(), req.getDeptIds(), req.getChildIds()));
    }

    @Operation(summary = "状态修改")
    @OperateLog(title = "用户管理", subTitle = "状态修改", type = OperateTypeEnum.UPDATE)
    @PostMapping("/edit-status")
    @CheckAdminPerms("user-mod:role-mng:edit")
    public Result<Void> editStatus(@RequestBody StatusEditReq editReq) {
        roleInfoMapper.checkDataScopes(editReq.getId());
        RoleInfoDO update = new RoleInfoDO(editReq.getId());
        update.setStatus(editReq.getStatus());
        return toRes(roleInfoMapper.updateById(update));
    }

    @Operation(summary = "删除角色")
    @OperateLog(title = "角色管理", subTitle = "删除角色", type = OperateTypeEnum.DELETE)
    @PostMapping("/remove")
    @CheckAdminPerms("user-mod:role-mng:remove")
    public Result<Void> remove(@RequestBody @NotEmpty(message = "{invalidParameter.id.invalid}") List<Long> roleIds) {
        roleInfoMapper.checkDataScopes(roleIds);
        if (roleInfoService.existAdmin(roleIds)) {
            return requestFail("当前角色已被分配用户");
        }
        if (roleInfoService.existChildRole(roleIds)) {
            return requestFail("当前角色已被其他角色继承");
        }
        return toRes(roleInfoService.removeByIds(roleIds));
    }

}



