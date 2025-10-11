package com.wzkris.principal.manager;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.utils.DataScopeUtil;
import com.wzkris.common.security.utils.LoginUserUtil;
import com.wzkris.principal.domain.DeptInfoDO;
import com.wzkris.principal.domain.vo.SelectTreeVO;
import com.wzkris.principal.mapper.datascope.DeptInfoDscMapper;
import com.wzkris.principal.service.DeptInfoService;
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
public class DeptInfoDataScopeManager {

    private final DeptInfoDscMapper deptInfoDscMapper;

    private final DeptInfoService deptInfoService;

    public List<DeptInfoDO> list(Wrapper<DeptInfoDO> queryWrapper) {
        DataScopeUtil.putParameter("dept_id", LoginUserUtil.get().getDeptScopes());

        try {
            return deptInfoDscMapper.selectLists(queryWrapper);
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
        LambdaQueryWrapper<DeptInfoDO> lqw = Wrappers.lambdaQuery(DeptInfoDO.class)
                .select(DeptInfoDO::getDeptId, DeptInfoDO::getParentId, DeptInfoDO::getDeptName)
                .like(StringUtil.isNotEmpty(deptName), DeptInfoDO::getDeptName, deptName);

        List<DeptInfoDO> depts = list(lqw);
        return deptInfoService.buildSelectTree(depts);
    }

    /**
     * 根据角色ID查询关联部门id集合
     *
     * @param roleIds 角色id集合
     * @return 部门id集合
     */
    public List<Long> listDeptIdByRoleIds(List<Long> roleIds) {
        DataScopeUtil.putParameter("dept_id", LoginUserUtil.get().getDeptScopes());

        try {
            return deptInfoDscMapper.listDeptIdByRoleIds(roleIds);
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
            DataScopeUtil.putParameter("dept_id", LoginUserUtil.get().getDeptScopes());

            try {
                if (!deptInfoDscMapper.checkDataScopes(new HashSet<>(deptIds))) {
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
