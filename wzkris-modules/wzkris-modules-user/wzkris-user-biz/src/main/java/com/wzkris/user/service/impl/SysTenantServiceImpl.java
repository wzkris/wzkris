package com.wzkris.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.utils.DynamicTenantUtil;
import com.wzkris.user.domain.*;
import com.wzkris.user.domain.dto.SysTenantDTO;
import com.wzkris.user.domain.dto.SysUserDTO;
import com.wzkris.user.mapper.*;
import com.wzkris.user.service.SysTenantService;
import com.wzkris.user.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 租户层
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class SysTenantServiceImpl implements SysTenantService {
    private final SysTenantMapper tenantMapper;
    private final SysTenantWalletMapper tenantWalletMapper;
    private final SysTenantWalletRecordMapper tenantWalletRecordMapper;
    private final SysUserMapper userMapper;
    private final SysUserService userService;
    private final SysRoleMapper roleMapper;
    private final SysDeptMapper deptMapper;
    private final SysPostMapper postMapper;
    private final SysRoleDeptMapper roleDeptMapper;
    private final SysRoleMenuMapper roleMenuMapper;
    private final SysUserRoleMapper userRoleMapper;
    private final SysUserPostMapper userPostMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<SysTenant> list(SysTenant sysTenant) {
        LambdaQueryWrapper<SysTenant> lqw = this.buildQueryWrapper(sysTenant);
        return tenantMapper.selectList(lqw);
    }

    private LambdaQueryWrapper<SysTenant> buildQueryWrapper(SysTenant sysTenant) {
        return new LambdaQueryWrapper<SysTenant>()
                .like(StringUtil.isNotNull(sysTenant.getTenantName()), SysTenant::getTenantName, sysTenant.getTenantName())
                .eq(StringUtil.isNotNull(sysTenant.getStatus()), SysTenant::getStatus, sysTenant.getStatus())
                .eq(StringUtil.isNotNull(sysTenant.getPackageId()), SysTenant::getPackageId, sysTenant.getPackageId())
                .orderByDesc(SysTenant::getTenantId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertTenant(SysTenantDTO tenantDTO) {
        long userId = IdUtil.getSnowflakeNextId();

        tenantDTO.setAdministrator(userId);
        tenantDTO.setOperPwd(passwordEncoder.encode(tenantDTO.getOperPwd()));
        tenantMapper.insert(tenantDTO);
        SysTenantWallet wallet = new SysTenantWallet(tenantDTO.getTenantId());
        tenantWalletMapper.insert(wallet);
        SysUserDTO sysUserDTO = new SysUserDTO();
        sysUserDTO.setUserId(userId);
        sysUserDTO.setTenantId(tenantDTO.getTenantId());
        sysUserDTO.setUsername(tenantDTO.getUsername());
        sysUserDTO.setPassword(tenantDTO.getPassword());
        userService.insertUser(sysUserDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void hardDeleteByIds(List<Long> tenantIds) {
        tenantMapper.hardDeleteByIds(tenantIds);
        tenantWalletMapper.deleteByIds(tenantIds);
        LambdaQueryWrapper<SysTenantWalletRecord> recordw = Wrappers.lambdaQuery(SysTenantWalletRecord.class)
                .in(SysTenantWalletRecord::getTenantId, tenantIds);
        tenantWalletRecordMapper.delete(recordw);

        LambdaQueryWrapper<SysUser> userw = Wrappers.lambdaQuery(SysUser.class)
                .select(SysUser::getUserId)
                .in(SysUser::getTenantId, tenantIds);
        List<Long> userIds = userMapper.selectList(userw).stream().map(SysUser::getUserId).toList();
        if (CollUtil.isNotEmpty(userIds)) {
            userMapper.hardDeleteByIds(userIds);
            userRoleMapper.deleteByUserIds(userIds);
            userPostMapper.deleteByUserIds(userIds);
        }
        LambdaQueryWrapper<SysRole> rolew = Wrappers.lambdaQuery(SysRole.class)
                .select(SysRole::getRoleId)
                .in(SysRole::getTenantId, tenantIds);
        List<Long> roleIds = roleMapper.selectList(rolew).stream().map(SysRole::getRoleId).toList();
        if (CollUtil.isNotEmpty(roleIds)) {
            roleMapper.deleteByIds(roleIds);
            roleDeptMapper.deleteByRoleIds(roleIds);
            roleMenuMapper.deleteByRoleIds(roleIds);
        }
        LambdaQueryWrapper<SysDept> deptw = Wrappers.lambdaQuery(SysDept.class)
                .in(SysDept::getTenantId, tenantIds);
        deptMapper.delete(deptw);
        LambdaQueryWrapper<SysPost> postw = Wrappers.lambdaQuery(SysPost.class)
                .in(SysPost::getTenantId, tenantIds);
        postMapper.delete(postw);
    }

    @Override
    public boolean checkAccountLimit(Long tenantId) {
        if (SysTenant.isSuperTenant(tenantId)) {
            return true;
        }
        return DynamicTenantUtil.ignore(() -> {
            SysTenant tenant = tenantMapper.selectById(tenantId);
            if (tenant.getAccountLimit() == -1) {
                return true;
            }
            Long count = userMapper.selectCount(Wrappers.lambdaQuery(SysUser.class).eq(SysUser::getTenantId, tenantId));
            return tenant.getAccountLimit() - count > 0;
        });
    }

    @Override
    public boolean checkRoleLimit(Long tenantId) {
        if (SysTenant.isSuperTenant(tenantId)) {
            return true;
        }
        return DynamicTenantUtil.ignore(() -> {
            SysTenant tenant = tenantMapper.selectById(tenantId);
            if (tenant.getRoleLimit() == -1) {
                return true;
            }
            Long count = roleMapper.selectCount(Wrappers.lambdaQuery(SysRole.class).eq(SysRole::getTenantId, tenantId));
            return tenant.getRoleLimit() - count > 0;
        });
    }

    @Override
    public boolean checkPostLimit(Long tenantId) {
        if (SysTenant.isSuperTenant(tenantId)) {
            return true;
        }
        return DynamicTenantUtil.ignore(() -> {
            SysTenant tenant = tenantMapper.selectById(tenantId);
            if (tenant.getPostLimit() == -1) {
                return true;
            }
            Long count = postMapper.selectCount(Wrappers.lambdaQuery(SysPost.class).eq(SysPost::getTenantId, tenantId));
            return tenant.getPostLimit() - count > 0;
        });
    }

    @Override
    public boolean checkDeptLimit(Long tenantId) {
        if (SysTenant.isSuperTenant(tenantId)) {
            return true;
        }
        return DynamicTenantUtil.ignore(() -> {
            SysTenant tenant = tenantMapper.selectById(tenantId);
            if (tenant.getDeptLimit() == -1) {
                return true;
            }
            Long count = deptMapper.selectCount(Wrappers.lambdaQuery(SysDept.class).eq(SysDept::getTenantId, tenantId));
            return tenant.getDeptLimit() - count > 0;
        });
    }

}
