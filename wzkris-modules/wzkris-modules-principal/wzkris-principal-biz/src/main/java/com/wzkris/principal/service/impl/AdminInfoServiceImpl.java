package com.wzkris.principal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.security.component.PasswordEncoderDelegate;
import com.wzkris.principal.domain.AdminInfoDO;
import com.wzkris.principal.domain.AdminToRoleDO;
import com.wzkris.principal.mapper.AdminInfoMapper;
import com.wzkris.principal.mapper.AdminToRoleMapper;
import com.wzkris.principal.service.AdminInfoService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 管理员 业务层处理
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class AdminInfoServiceImpl implements AdminInfoService {

    private final AdminInfoMapper adminInfoMapper;

    private final AdminToRoleMapper adminToRoleMapper;

    private final PasswordEncoderDelegate passwordEncoder;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveAdmin(AdminInfoDO adminInfoDO, List<Long> roleIds) {
        if (adminInfoDO.getPassword() != null && !passwordEncoder.isEncode(adminInfoDO.getPassword())) {
            adminInfoDO.setPassword(passwordEncoder.encode(adminInfoDO.getPassword()));
        }
        boolean success = adminInfoMapper.insert(adminInfoDO) > 0;
        if (success) {
            this.insertAdminRole(adminInfoDO.getAdminId(), roleIds);
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean modifyAdmin(AdminInfoDO adminInfoDO, List<Long> roleIds) {
        if (adminInfoDO.getPassword() != null && !passwordEncoder.isEncode(adminInfoDO.getPassword())) {
            adminInfoDO.setPassword(passwordEncoder.encode(adminInfoDO.getPassword()));
        }
        boolean success = adminInfoMapper.updateById(adminInfoDO) > 0;
        if (success && roleIds != null) {
            adminToRoleMapper.deleteByAdminId(adminInfoDO.getAdminId());
            this.insertAdminRole(adminInfoDO.getAdminId(), roleIds);
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(List<Long> adminIds) {
        boolean success = adminInfoMapper.deleteByIds(adminIds) > 0;
        if (success) {
            adminToRoleMapper.deleteByAdminIds(adminIds);
        }
        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean grantRoles(Long adminId, List<Long> roleIds) {
        adminToRoleMapper.deleteByAdminId(adminId);
        return this.insertAdminRole(adminId, roleIds);
    }

    private boolean insertAdminRole(Long adminId, List<Long> roleIds) {
        if (CollectionUtils.isNotEmpty(roleIds)) {
            List<AdminToRoleDO> list = roleIds.stream()
                    .map(roleId -> new AdminToRoleDO(adminId, roleId))
                    .toList();
            return adminToRoleMapper.insert(list) > 0;
        }
        return false;
    }

    @Override
    public boolean existByUsername(@Nullable Long adminId, String username) {
        LambdaQueryWrapper<AdminInfoDO> lqw = new LambdaQueryWrapper<>(AdminInfoDO.class)
                .eq(AdminInfoDO::getUsername, username)
                .ne(Objects.nonNull(adminId), AdminInfoDO::getAdminId, adminId);
        return adminInfoMapper.exists(lqw);
    }

    @Override
    public boolean existByPhoneNumber(@Nullable Long adminId, String phonenumber) {
        LambdaQueryWrapper<AdminInfoDO> lqw = new LambdaQueryWrapper<>(AdminInfoDO.class)
                .eq(AdminInfoDO::getPhoneNumber, phonenumber)
                .ne(Objects.nonNull(adminId), AdminInfoDO::getAdminId, adminId);
        return adminInfoMapper.exists(lqw);
    }

}
