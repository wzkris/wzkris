package com.wzkris.user.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.utils.DynamicTenantUtil;
import com.wzkris.common.security.oauth2.service.PasswordEncoderDelegate;
import com.wzkris.user.domain.*;
import com.wzkris.user.domain.vo.SelectVO;
import com.wzkris.user.mapper.*;
import com.wzkris.user.service.SysRoleService;
import com.wzkris.user.service.SysTenantService;
import com.wzkris.user.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 租户层
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class SysTenantServiceImpl implements SysTenantService {

    private final SysUserMapper userMapper;

    private final SysUserService userService;

    private final SysRoleMapper roleMapper;

    private final SysRoleService roleService;

    private final SysDeptMapper deptMapper;

    private final SysPostMapper postMapper;

    private final PasswordEncoderDelegate passwordEncoder;

    private final SysTenantMapper tenantMapper;

    private final SysTenantWalletMapper tenantWalletMapper;

    private final SysTenantWalletRecordMapper tenantWalletRecordMapper;

    @Override
    public List<SelectVO> listSelect(String tenantName) {
        LambdaQueryWrapper<SysTenant> lqw = new LambdaQueryWrapper<SysTenant>()
                .select(SysTenant::getTenantId, SysTenant::getTenantName)
                .like(StringUtil.isNotBlank(tenantName), SysTenant::getTenantName, tenantName);
        return tenantMapper.selectList(lqw).stream().map(SelectVO::new).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertTenant(SysTenant tenant, String username, String password) {
        if (!passwordEncoder.isEncode(tenant.getOperPwd())) {
            tenant.setOperPwd(passwordEncoder.encode(tenant.getOperPwd()));
        }
        long userId = IdUtil.getSnowflakeNextId();
        tenant.setAdministrator(userId);
        tenantMapper.insert(tenant);

        SysTenantWallet wallet = new SysTenantWallet(tenant.getTenantId());
        tenantWalletMapper.insert(wallet);

        SysUser user = new SysUser();
        user.setUserId(userId);
        user.setTenantId(tenant.getTenantId());
        user.setUsername(username);
        user.setPassword(password);
        return userService.insertUser(user, null, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<Long> tenantIds) {
        tenantMapper.deleteByIds(tenantIds);
        tenantWalletMapper.deleteByIds(tenantIds);
        LambdaQueryWrapper<SysTenantWalletRecord> recordw = Wrappers.lambdaQuery(SysTenantWalletRecord.class)
                .in(SysTenantWalletRecord::getTenantId, tenantIds);
        tenantWalletRecordMapper.delete(recordw);

        LambdaQueryWrapper<SysUser> userw = Wrappers.lambdaQuery(SysUser.class)
                .select(SysUser::getUserId)
                .in(SysUser::getTenantId, tenantIds);
        List<Long> userIds = userMapper.selectList(userw).stream().map(SysUser::getUserId).toList();
        if (CollUtil.isNotEmpty(userIds)) {
            userService.deleteByIds(userIds);
        }
        LambdaQueryWrapper<SysRole> rolew = Wrappers.lambdaQuery(SysRole.class)
                .select(SysRole::getRoleId)
                .in(SysRole::getTenantId, tenantIds);
        List<Long> roleIds = roleMapper.selectList(rolew).stream().map(SysRole::getRoleId).toList();
        if (CollUtil.isNotEmpty(roleIds)) {
            roleService.deleteByIds(roleIds);
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

    @Override
    public boolean checkAdministrator(List<Long> userIds) {
        return DynamicTenantUtil.ignore(() -> {
            LambdaQueryWrapper<SysTenant> lqw = Wrappers.lambdaQuery(SysTenant.class)
                    .in(SysTenant::getAdministrator, userIds);
            return tenantMapper.exists(lqw);
        });
    }

}
