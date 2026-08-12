package com.wzkris.usercenter.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.plus.ServiceImplPlus;
import com.wzkris.usercenter.domain.TenantUserToRoleDO;
import com.wzkris.usercenter.domain.TenantRoleDO;
import com.wzkris.usercenter.domain.TenantRoleToMenuDO;
import com.wzkris.usercenter.enums.tenantrole.TenantRoleStatusEnum;
import com.wzkris.usercenter.mapper.TenantUserToRoleMapper;
import com.wzkris.usercenter.mapper.TenantRoleMapper;
import com.wzkris.usercenter.mapper.TenantRoleToMenuMapper;
import com.wzkris.usercenter.response.SelectResponse;
import com.wzkris.usercenter.service.TenantRoleService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TenantRoleServiceImpl
        extends ServiceImplPlus<TenantRoleMapper, TenantRoleDO>
        implements TenantRoleService {

    private final TenantUserToRoleMapper tenantUserToRoleMapper;

    private final TenantRoleToMenuMapper tenantRoleToMenuMapper;

    @Override
    public List<TenantRoleDO> listByTenantUserId(Long tenantUserId) {
        List<Long> tenantRoleIds = tenantUserToRoleMapper.selectObjsByObj(TenantUserToRoleDO::getTenantRoleId, TenantUserToRoleDO::getTenantUserId, tenantUserId);
        if (CollectionUtils.isEmpty(tenantRoleIds)) {
            return Collections.emptyList();
        }
        return this.lambdaQuery()
                .in(TenantRoleDO::getId, tenantRoleIds)
                .eq(TenantRoleDO::getStatus, TenantRoleStatusEnum.ENABLE)
                .list();
    }

    @Override
    public List<SelectResponse> listSelect(String roleName) {
        return baseMapper.selectList(Wrappers.lambdaQuery(TenantRoleDO.class)
                        .select(TenantRoleDO::getId, TenantRoleDO::getRoleName)
                        .eq(TenantRoleDO::getStatus, TenantRoleStatusEnum.ENABLE)
                        .like(StringUtil.isNotBlank(roleName), TenantRoleDO::getRoleName, roleName)
                        .orderByAsc(TenantRoleDO::getId))
                .stream()
                .map(tenantRoleDO -> {
                    SelectResponse SelectResponse = new SelectResponse();
                    SelectResponse.setId(tenantRoleDO.getId());
                    SelectResponse.setLabel(tenantRoleDO.getRoleName());
                    return SelectResponse;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRole(TenantRoleDO role, List<Long> menuIds) {
        boolean success = baseMapper.insert(role) > 0;
        if (success) {
            this.insertRoleMenu(role.getId(), menuIds);
        }
        return success;
    }

    @Override
    public boolean updateRole(TenantRoleDO role, List<Long> menuIds) {
        boolean success = baseMapper.updateById(role) > 0;
        if (success && menuIds != null) {
            tenantRoleToMenuMapper.delete(Wrappers.lambdaQuery(TenantRoleToMenuDO.class)
                    .eq(TenantRoleToMenuDO::getTenantRoleId, role.getId()));
            this.insertRoleMenu(role.getId(), menuIds);
        }
        return success;
    }

    private void insertRoleMenu(Long tenantRoleId, List<Long> menuIds) {
        if (CollectionUtils.isNotEmpty(menuIds)) {
            List<TenantRoleToMenuDO> list = menuIds.stream()
                    .map(menuId -> new TenantRoleToMenuDO(tenantRoleId, menuId))
                    .toList();
            tenantRoleToMenuMapper.insert(list);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeRoles(List<Long> tenantRoleIds) {
        boolean success = baseMapper.deleteByIds(tenantRoleIds) > 0;
        if (success) {
            tenantRoleToMenuMapper.delete(Wrappers.lambdaQuery(TenantRoleToMenuDO.class)
                    .in(TenantRoleToMenuDO::getTenantRoleId, tenantRoleIds));
            tenantUserToRoleMapper.delete(Wrappers.lambdaQuery(TenantUserToRoleDO.class)
                    .in(TenantUserToRoleDO::getTenantRoleId, tenantRoleIds));
        }
        return success;
    }

    @Override
    public boolean existTenantUser(List<Long> tenantRoleIds) {
        tenantRoleIds = tenantRoleIds.stream().filter(Objects::nonNull).toList();
        if (CollectionUtils.isEmpty(tenantRoleIds)) {
            return false;
        }
        return tenantUserToRoleMapper.exists(Wrappers.lambdaQuery(TenantUserToRoleDO.class)
                .in(TenantUserToRoleDO::getTenantRoleId, tenantRoleIds));
    }

}
