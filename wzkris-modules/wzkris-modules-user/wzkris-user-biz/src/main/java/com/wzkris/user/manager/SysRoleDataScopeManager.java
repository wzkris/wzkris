package com.wzkris.user.manager;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.utils.DataScopeUtil;
import com.wzkris.common.security.utils.SystemUserUtil;
import com.wzkris.user.domain.SysRole;
import com.wzkris.user.domain.vo.SelectVO;
import com.wzkris.user.mapper.datascope.SysRoleDscMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色管理 权限管理层
 *
 * @author wzkris
 */
@Component
@RequiredArgsConstructor
public class SysRoleDataScopeManager {

    private final SysRoleDscMapper roleDscMapper;

    public List<SysRole> list(Wrapper<SysRole> queryWrapper) {
        DataScopeUtil.putParameter("rd.dept_id", SystemUserUtil.getUser().getDeptScopes());

        try {
            return roleDscMapper.selectLists(queryWrapper);
        } finally {
            DataScopeUtil.remove();
        }
    }

    public List<Long> listInheritedIdByRoleId(Long roleId) {
        DataScopeUtil.putParameter("rd.dept_id", SystemUserUtil.getUser().getDeptScopes());

        try {
            return roleDscMapper.listInheritedIdByRoleId(roleId);
        } finally {
            DataScopeUtil.remove();
        }
    }

    /**
     * 查询可以被继承的角色
     *
     * @param roleId 继承角色ID
     */
    public List<SelectVO> listInheritedSelect(Long roleId) {
        return list(Wrappers.query(SysRole.class)
                .select("role_id", "role_name")
                .eq("status", CommonConstants.STATUS_ENABLE)
                .eq("inherited", false)
                .ne(Objects.nonNull(roleId), "r.role_id", roleId)
                .orderByAsc("role_id"))
                .stream()
                .map(SelectVO::new)
                .collect(Collectors.toList());
    }

    /**
     * 查询可选择角色
     *
     * @return 角色列表
     */
    public List<SelectVO> listSelect(String roleName) {
        return list(Wrappers.lambdaQuery(SysRole.class)
                .select(SysRole::getRoleId, SysRole::getRoleName)
                .eq(SysRole::getStatus, CommonConstants.STATUS_ENABLE)
                .like(StringUtil.isNotBlank(roleName), SysRole::getRoleName, roleName)
                .orderByAsc(SysRole::getRoleId))
                .stream()
                .map(SelectVO::new)
                .collect(Collectors.toList());
    }

    /**
     * 校验是否有角色的数据权限
     *
     * @param roleIds 待操作的角色id数组
     */
    public void checkDataScopes(Collection<Long> roleIds) {
        if (CollectionUtils.isNotEmpty(roleIds)) {
            DataScopeUtil.putParameter("rd.dept_id", SystemUserUtil.getUser().getDeptScopes());

            try {
                if (!roleDscMapper.checkDataScopes(roleIds)) {
                    throw new AccessDeniedException("无此角色数据访问权限");
                }
            } finally {
                DataScopeUtil.remove();
            }
        }
    }

    public void checkDataScopes(Long roleId) {
        if (roleId != null) {
            this.checkDataScopes(Collections.singleton(roleId));
        }
    }

}
