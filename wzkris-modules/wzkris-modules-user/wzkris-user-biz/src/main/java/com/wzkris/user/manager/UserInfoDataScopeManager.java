package com.wzkris.user.manager;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.orm.utils.DataScopeUtil;
import com.wzkris.common.security.utils.LoginUserUtil;
import com.wzkris.user.domain.UserInfoDO;
import com.wzkris.user.domain.req.user.UserManageQueryReq;
import com.wzkris.user.domain.vo.SelectVO;
import com.wzkris.user.domain.vo.userinfo.UserManageVO;
import com.wzkris.user.mapper.UserToRoleMapper;
import com.wzkris.user.mapper.datascope.UserInfoDscMapper;
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
public class UserInfoDataScopeManager {

    private final UserInfoDscMapper userInfoDscMapper;

    private final UserToRoleMapper userToRoleMapper;

    public List<UserInfoDO> list(Wrapper<UserInfoDO> queryWrapper) {
        DataScopeUtil.putParameter("dept_id", LoginUserUtil.get().getDeptScopes());

        try {
            return userInfoDscMapper.selectLists(queryWrapper);
        } finally {
            DataScopeUtil.remove();
        }
    }

    public List<UserManageVO> listVO(Wrapper<UserInfoDO> queryWrapper) {
        DataScopeUtil.putParameter("d.dept_id", LoginUserUtil.get().getDeptScopes());

        try {
            return userInfoDscMapper.selectVOList(queryWrapper);
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
    public List<SelectVO> listAllocated(UserManageQueryReq queryReq, Long roleId) {
        List<Long> userIds = userToRoleMapper.listUserIdByRoleId(roleId);
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<UserInfoDO> lqw = this.buildQueryWrapper(queryReq);
        lqw.in(UserInfoDO::getUserId, userIds);
        return list(lqw).stream().map(SelectVO::new).toList();
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param queryReq 筛选条件
     * @param roleId   角色ID
     * @return 用户选择列表
     */
    public List<SelectVO> listUnallocated(UserManageQueryReq queryReq, Long roleId) {
        List<Long> userIds = userToRoleMapper.listUserIdByRoleId(roleId);

        LambdaQueryWrapper<UserInfoDO> lqw = this.buildQueryWrapper(queryReq);
        if (!CollectionUtils.isEmpty(userIds)) {
            lqw.notIn(UserInfoDO::getUserId, userIds);
        }
        return list(lqw).stream().map(SelectVO::new).toList();
    }

    private LambdaQueryWrapper<UserInfoDO> buildQueryWrapper(UserManageQueryReq queryReq) {
        return new LambdaQueryWrapper<UserInfoDO>()
                .like(ObjectUtils.isNotEmpty(queryReq.getUsername()), UserInfoDO::getUsername, queryReq.getUsername())
                .like(ObjectUtils.isNotEmpty(queryReq.getNickname()), UserInfoDO::getNickname, queryReq.getNickname())
                .like(
                        ObjectUtils.isNotEmpty(queryReq.getPhoneNumber()),
                        UserInfoDO::getPhoneNumber,
                        queryReq.getPhoneNumber())
                .like(ObjectUtils.isNotEmpty(queryReq.getEmail()), UserInfoDO::getEmail, queryReq.getEmail())
                .eq(ObjectUtils.isNotEmpty(queryReq.getStatus()), UserInfoDO::getStatus, queryReq.getStatus())
                .eq(ObjectUtils.isNotEmpty(queryReq.getStatus()), UserInfoDO::getStatus, queryReq.getStatus())
                .eq(ObjectUtils.isNotEmpty(queryReq.getDeptId()), UserInfoDO::getDeptId, queryReq.getDeptId())
                .orderByDesc(UserInfoDO::getUserId);
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
            if (userIds.contains(LoginUserUtil.getId())) {
                throw new AccessDeniedException("userId：‘" + LoginUserUtil.getId() + "'禁止访问自身数据");
            }

            DataScopeUtil.putParameter("dept_id", LoginUserUtil.get().getDeptScopes());

            try {
                if (!userInfoDscMapper.checkDataScopes(userIds)) {
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
