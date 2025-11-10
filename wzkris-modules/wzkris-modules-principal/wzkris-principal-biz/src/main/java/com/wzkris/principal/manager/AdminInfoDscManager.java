package com.wzkris.principal.manager;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.orm.utils.DataScopeUtil;
import com.wzkris.common.security.utils.AdminUtil;
import com.wzkris.principal.domain.AdminInfoDO;
import com.wzkris.principal.domain.req.admin.AdminMngQueryReq;
import com.wzkris.principal.domain.vo.SelectVO;
import com.wzkris.principal.domain.vo.admin.AdminMngVO;
import com.wzkris.principal.mapper.AdminToRoleMapper;
import com.wzkris.principal.mapper.datascope.AdminInfoDscMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 用户管理 权限管理层
 *
 * @author wzkris
 */
@Component
@RequiredArgsConstructor
public class AdminInfoDscManager {

    private final AdminInfoDscMapper adminInfoDscMapper;

    private final AdminToRoleMapper adminToRoleMapper;

    public List<AdminInfoDO> list(Wrapper<AdminInfoDO> queryWrapper) {
        DataScopeUtil.putParameter("dept_id", AdminUtil.get().getDeptScopes());

        try {
            return adminInfoDscMapper.selectLists(queryWrapper);
        } finally {
            DataScopeUtil.remove();
        }
    }

    public List<AdminMngVO> listVO(Wrapper<AdminInfoDO> queryWrapper) {
        DataScopeUtil.putParameter("d.dept_id", AdminUtil.get().getDeptScopes());

        try {
            return adminInfoDscMapper.selectVOList(queryWrapper);
        } finally {
            DataScopeUtil.remove();
        }
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param queryReq 筛选条件
     * @param roleId   角色ID
     * @return 用户选择列表
     */
    public List<SelectVO> listAllocated(AdminMngQueryReq queryReq, Long roleId) {
        List<Long> adminIds = adminToRoleMapper.listAdminIdByRoleId(roleId);
        if (CollectionUtils.isEmpty(adminIds)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<AdminInfoDO> lqw = this.buildQueryWrapper(queryReq);
        lqw.in(AdminInfoDO::getAdminId, adminIds);
        return list(lqw).stream().map(SelectVO::new).toList();
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param queryReq 筛选条件
     * @param roleId   角色ID
     * @return 用户选择列表
     */
    public List<SelectVO> listUnallocated(AdminMngQueryReq queryReq, Long roleId) {
        List<Long> adminIds = adminToRoleMapper.listAdminIdByRoleId(roleId);

        LambdaQueryWrapper<AdminInfoDO> lqw = this.buildQueryWrapper(queryReq);
        if (!CollectionUtils.isEmpty(adminIds)) {
            lqw.notIn(AdminInfoDO::getAdminId, adminIds);
        }
        return list(lqw).stream().map(SelectVO::new).toList();
    }

    private LambdaQueryWrapper<AdminInfoDO> buildQueryWrapper(AdminMngQueryReq queryReq) {
        return new LambdaQueryWrapper<AdminInfoDO>()
                .like(ObjectUtils.isNotEmpty(queryReq.getUsername()), AdminInfoDO::getUsername, queryReq.getUsername())
                .like(ObjectUtils.isNotEmpty(queryReq.getNickname()), AdminInfoDO::getNickname, queryReq.getNickname())
                .like(
                        ObjectUtils.isNotEmpty(queryReq.getPhoneNumber()),
                        AdminInfoDO::getPhoneNumber,
                        queryReq.getPhoneNumber())
                .like(ObjectUtils.isNotEmpty(queryReq.getEmail()), AdminInfoDO::getEmail, queryReq.getEmail())
                .eq(ObjectUtils.isNotEmpty(queryReq.getStatus()), AdminInfoDO::getStatus, queryReq.getStatus())
                .eq(ObjectUtils.isNotEmpty(queryReq.getStatus()), AdminInfoDO::getStatus, queryReq.getStatus())
                .eq(ObjectUtils.isNotEmpty(queryReq.getDeptId()), AdminInfoDO::getDeptId, queryReq.getDeptId())
                .orderByDesc(AdminInfoDO::getAdminId);
    }

    /**
     * 校验是否有角色的数据权限
     *
     * @param adminIds 待操作的角色id数组
     */
    public void checkDataScopes(Collection<Long> adminIds) {
        if (CollectionUtils.isNotEmpty(adminIds)) {
            if (adminIds.contains(SecurityConstants.SUPER_ADMIN_ID)) {
                throw new AccessDeniedException("禁止访问超级管理员数据");
            }
            if (adminIds.contains(AdminUtil.getId())) {
                throw new AccessDeniedException("adminId：‘" + AdminUtil.getId() + "'禁止访问自身数据");
            }

            DataScopeUtil.putParameter("dept_id", AdminUtil.get().getDeptScopes());

            try {
                if (!adminInfoDscMapper.checkDataScopes(adminIds)) {
                    throw new AccessDeniedException("无此用户数据访问权限");
                }
            } finally {
                DataScopeUtil.remove();
            }
        }
    }

    public void checkDataScopes(Long adminId) {
        if (adminId != null) {
            this.checkDataScopes(Collections.singleton(adminId));
        }
    }

}
