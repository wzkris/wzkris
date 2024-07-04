package com.wzkris.user.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.user.domain.SysTenant;
import com.wzkris.user.domain.dto.SysTenantDTO;
import com.wzkris.user.domain.dto.SysUserDTO;
import com.wzkris.user.mapper.SysTenantMapper;
import com.wzkris.user.service.SysTenantService;
import com.wzkris.user.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * 租户套餐层
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class SysTenantServiceImpl implements SysTenantService {
    private final SysTenantMapper sysTenantMapper;
    private final SysUserService sysUserService;
    private final TransactionTemplate transactionTemplate;

    /**
     * 分页查询
     *
     * @param sysTenant 查询条件
     */
    @Override
    public List<SysTenant> listPage(SysTenant sysTenant) {
        // 查询
        LambdaQueryWrapper<SysTenant> lqw = this.buildQueryWrapper(sysTenant);
        return sysTenantMapper.selectList(lqw);
    }

    /**
     * 添加租户
     *
     * @param tenantDTO 参数
     */
    @Override
    public boolean insertTenant(SysTenantDTO tenantDTO) {
        long userId = IdUtil.getSnowflakeNextId();
        tenantDTO.setAdministrator(userId);

        return Boolean.TRUE.equals(transactionTemplate.execute(status -> {
            boolean success = sysTenantMapper.insert(tenantDTO) > 0;
            if (success) {
                SysUserDTO sysUserDTO = new SysUserDTO();
                sysUserDTO.setUserId(userId);
                sysUserDTO.setTenantId(tenantDTO.getTenantId());
                sysUserDTO.setUsername(tenantDTO.getUsername());
                sysUserDTO.setPassword(tenantDTO.getPassword());
                success = sysUserService.insertUser(sysUserDTO);
                // 更新失败则回滚
                if (!success) {
                    status.setRollbackOnly();
                }
            }
            return success;
        }));
    }

    /**
     * 更新租户
     *
     * @param sysTenant 参数
     */
    @Override
    public boolean updateTenant(SysTenant sysTenant) {
        sysTenant.setTenantId(null);
        sysTenant.setAdministrator(null);
        sysTenant.setPackageId(null);
        return sysTenantMapper.updateById(sysTenant) > 0;
    }

    private LambdaQueryWrapper<SysTenant> buildQueryWrapper(SysTenant sysTenant) {
        return new LambdaQueryWrapper<SysTenant>()
                .like(StringUtil.isNotNull(sysTenant.getLicenseNumber()), SysTenant::getLicenseNumber, sysTenant.getLicenseNumber())
                .like(StringUtil.isNotNull(sysTenant.getCompanyName()), SysTenant::getCompanyName, sysTenant.getCompanyName())
                .eq(StringUtil.isNotNull(sysTenant.getStatus()), SysTenant::getStatus, sysTenant.getStatus())
                .eq(StringUtil.isNotNull(sysTenant.getPackageId()), SysTenant::getPackageId, sysTenant.getPackageId())
                .orderByDesc(SysTenant::getTenantId);
    }
}