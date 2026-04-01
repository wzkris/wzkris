package com.wzkris.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wzkris.usercenter.domain.RoleInfoDO;
import com.wzkris.usercenter.response.SelectResponse;
import jakarta.annotation.Nullable;

import java.util.List;

/**
 * 角色业务层
 *
 * @author wzkris
 */
public interface RoleInfoService extends IService<RoleInfoDO> {

    /**
     * 根据管理员ID查询关联角色(正常状态)
     *
     * @param adminId 管理员ID
     * @return 角色列表
     */
    List<RoleInfoDO> listByAdminId(Long adminId);

    /**
     * 根据管理员ID查询关联角色ID(正常状态)
     *
     * @param adminId 管理员ID
     * @return 角色列表
     */
    List<Long> listIdByAdminId(Long adminId);

    /**
     * 根据管理员ID查询关联角色(正常状态)包含继承角色
     *
     * @param adminId 管理员ID
     * @return 角色列表
     */
    List<RoleInfoDO> listInheritedByAdminId(Long adminId);

    /**
     * 根据管理员ID查询关联角色ID(正常状态)包含继承角色
     *
     * @param adminId 管理员ID
     * @return 角色列表
     */
    List<Long> listInheritedIdByAdminId(Long adminId);

    /**
     * ID获取角色组
     */
    String getRoleGroup(Long adminId);

    /**
     * 新增角色信息
     *
     * @param role    角色信息
     * @param menuIds 菜单组
     * @param deptIds 部门组
     */
    boolean saveRole(RoleInfoDO role, @Nullable List<Long> menuIds, @Nullable List<Long> deptIds, @Nullable List<Long> childIds);

    /**
     * 修改角色信息
     *
     * @param role     角色信息
     * @param menuIds  菜单组
     * @param deptIds  部门组
     * @param childIds 子角色 ID 列表
     */
    boolean updateRole(RoleInfoDO role, @Nullable List<Long> menuIds, @Nullable List<Long> deptIds, @Nullable List<Long> childIds);

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     */
    boolean removeRoles(List<Long> roleIds);

    /**
     * 校验角色是否被管理员关联
     *
     * @param roleIds 角色组
     */
    boolean existAdmin(List<Long> roleIds);

    /**
     * 校验是否被其他角色继承
     *
     * @param roleIds 角色组
     */
    boolean existChildRole(List<Long> roleIds);

    /**
     * 根据名称过滤后转换为下拉框
     *
     * @param roleName 角色名称（可为空，为空时返回所有角色）
     * @return 选项列表
     */
    List<SelectResponse> listRoleSelect(String roleName);

}
