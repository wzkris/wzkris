package com.wzkris.user.manager;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.utils.DataScopeUtil;
import com.wzkris.common.security.utils.SystemUserUtil;
import com.wzkris.user.domain.SysDept;
import com.wzkris.user.domain.vo.SelectTreeVO;
import com.wzkris.user.mapper.datascope.SysDeptDscMapper;
import com.wzkris.user.service.SysDeptService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * 部门管理 权限管理层
 *
 * @author wzkris
 */
@Component
@RequiredArgsConstructor
public class SysDeptDataScopeManager {

    private final SysDeptDscMapper deptDscMapper;

    private final SysDeptService deptService;

    public List<SysDept> list(Wrapper<SysDept> queryWrapper) {
        DataScopeUtil.putParameter("dept_id", SystemUserUtil.getUser().getDeptScopes());

        try {
            return deptDscMapper.selectLists(queryWrapper);
        } finally {
            DataScopeUtil.remove();
        }
    }

    /**
     * 查询部门选择树
     *
     * @param deptName 筛选条件
     * @return 部门树信息集合
     */
    public List<SelectTreeVO> listSelectTree(String deptName) {
        LambdaQueryWrapper<SysDept> lqw = Wrappers.lambdaQuery(SysDept.class)
                .select(SysDept::getDeptId, SysDept::getParentId, SysDept::getDeptName)
                .like(StringUtil.isNotEmpty(deptName), SysDept::getDeptName, deptName);

        List<SysDept> depts = list(lqw);
        return deptService.buildSelectTree(depts);
    }

    /**
     * 根据角色ID查询关联部门id集合
     *
     * @param roleIds 角色id集合
     * @return 部门id集合
     */
    public List<Long> listDeptIdByRoleIds(List<Long> roleIds) {
        DataScopeUtil.putParameter("dept_id", SystemUserUtil.getUser().getDeptScopes());

        try {
            return deptDscMapper.listDeptIdByRoleIds(roleIds);
        } finally {
            DataScopeUtil.remove();
        }
    }

    public List<Long> listDeptIdByRoleId(Long roleId) {
        return listDeptIdByRoleIds(Collections.singletonList(roleId));
    }

    /**
     * 校验部门是否有数据权限
     *
     * @param deptIds 部门id
     */
    public void checkDataScopes(Collection<Long> deptIds) {
        if (CollectionUtils.isNotEmpty(deptIds)) {
            DataScopeUtil.putParameter("dept_id", SystemUserUtil.getUser().getDeptScopes());

            try {
                if (!deptDscMapper.checkDataScopes(new HashSet<>(deptIds))) {
                    throw new AccessDeniedException("无此部门数据访问权限");
                }
            } finally {
                DataScopeUtil.remove();
            }
        }
    }

    public void checkDataScopes(Long deptId) {
        if (deptId != null) {
            checkDataScopes(Collections.singleton(deptId));
        }
    }

}
