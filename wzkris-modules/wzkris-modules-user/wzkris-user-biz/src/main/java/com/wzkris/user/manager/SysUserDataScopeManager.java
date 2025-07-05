package com.wzkris.user.manager;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.orm.utils.DataScopeUtil;
import com.wzkris.common.security.utils.SystemUserUtil;
import com.wzkris.user.domain.SysUser;
import com.wzkris.user.domain.req.SysUserQueryReq;
import com.wzkris.user.domain.vo.SelectVO;
import com.wzkris.user.domain.vo.SysUserVO;
import com.wzkris.user.mapper.SysUserRoleMapper;
import com.wzkris.user.mapper.datascope.SysUserDscMapper;
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
public class SysUserDataScopeManager {

    private final SysUserDscMapper userDscMapper;

    private final SysUserRoleMapper userRoleMapper;

    public List<SysUser> list(Wrapper<SysUser> queryWrapper) {
        DataScopeUtil.putParameter("dept_id", SystemUserUtil.getUser().getDeptScopes());

        try {
            return userDscMapper.selectLists(queryWrapper);
        } finally {
            DataScopeUtil.remove();
        }
    }

    public List<SysUserVO> listVO(Wrapper<SysUser> queryWrapper) {
        DataScopeUtil.putParameter("d.dept_id", SystemUserUtil.getUser().getDeptScopes());

        try {
            return userDscMapper.selectVOList(queryWrapper);
        } finally {
            DataScopeUtil.remove();
        }
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param queryReq 筛选条件
     * @param roleId   角色ID
     * @return 系统用户选择列表
     */
    public List<SelectVO> listAllocated(SysUserQueryReq queryReq, Long roleId) {
        List<Long> userIds = userRoleMapper.listUserIdByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<SysUser> lqw = this.buildQueryWrapper(queryReq);
        lqw.in(SysUser::getUserId, userIds);
        return list(lqw).stream().map(SelectVO::new).toList();
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param queryReq 筛选条件
     * @param roleId   角色ID
     * @return 系统用户选择列表
     */
    public List<SelectVO> listUnallocated(SysUserQueryReq queryReq, Long roleId) {
        List<Long> userIds = userRoleMapper.listUserIdByRoleId(roleId);

        LambdaQueryWrapper<SysUser> lqw = this.buildQueryWrapper(queryReq);
        if (!CollectionUtils.isEmpty(userIds)) {
            lqw.notIn(SysUser::getUserId, userIds);
        }
        return list(lqw).stream().map(SelectVO::new).toList();
    }

    private LambdaQueryWrapper<SysUser> buildQueryWrapper(SysUserQueryReq queryReq) {
        return new LambdaQueryWrapper<SysUser>()
                .eq(ObjectUtils.isNotEmpty(queryReq.getTenantId()), SysUser::getTenantId, queryReq.getTenantId())
                .like(ObjectUtils.isNotEmpty(queryReq.getUsername()), SysUser::getUsername, queryReq.getUsername())
                .like(ObjectUtils.isNotEmpty(queryReq.getNickname()), SysUser::getNickname, queryReq.getNickname())
                .like(
                        ObjectUtils.isNotEmpty(queryReq.getPhoneNumber()),
                        SysUser::getPhoneNumber,
                        queryReq.getPhoneNumber())
                .like(ObjectUtils.isNotEmpty(queryReq.getEmail()), SysUser::getEmail, queryReq.getEmail())
                .eq(ObjectUtils.isNotEmpty(queryReq.getStatus()), SysUser::getStatus, queryReq.getStatus())
                .eq(ObjectUtils.isNotEmpty(queryReq.getStatus()), SysUser::getStatus, queryReq.getStatus())
                .eq(ObjectUtils.isNotEmpty(queryReq.getDeptId()), SysUser::getDeptId, queryReq.getDeptId())
                .orderByDesc(SysUser::getUserId);
    }

    /**
     * 校验是否有角色的数据权限
     *
     * @param userIds 待操作的角色id数组
     */
    public void checkDataScopes(Collection<Long> userIds) {
        if (CollectionUtils.isNotEmpty(userIds)) {
            if (userIds.contains(SecurityConstants.SUPER_ADMIN_ID)) {
                throw new AccessDeniedException("禁止访问超级管理员数据");
            }
            if (userIds.contains(SystemUserUtil.getUserId())) {
                throw new AccessDeniedException("userId：‘" + SystemUserUtil.getUserId() + "'禁止访问自身数据");
            }

            DataScopeUtil.putParameter("dept_id", SystemUserUtil.getUser().getDeptScopes());

            try {
                if (!userDscMapper.checkDataScopes(userIds)) {
                    throw new AccessDeniedException("无此用户数据访问权限");
                }
            } finally {
                DataScopeUtil.remove();
            }
        }
    }

    public void checkDataScopes(Long userId) {
        if (userId != null) {
            this.checkDataScopes(Collections.singleton(userId));
        }
    }

}
