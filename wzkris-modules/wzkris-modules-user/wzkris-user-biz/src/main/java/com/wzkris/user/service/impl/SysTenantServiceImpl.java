package com.wzkris.user.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.user.domain.SysTenant;
import com.wzkris.user.domain.SysTenantWallet;
import com.wzkris.user.domain.dto.SysTenantDTO;
import com.wzkris.user.domain.dto.SysUserDTO;
import com.wzkris.user.mapper.SysTenantMapper;
import com.wzkris.user.mapper.SysTenantWalletMapper;
import com.wzkris.user.service.SysTenantService;
import com.wzkris.user.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * 租户层
 *
 * @author wzkris
 */
@Service
@RequiredArgsConstructor
public class SysTenantServiceImpl implements SysTenantService {
    private final SysTenantMapper sysTenantMapper;
    private final SysTenantWalletMapper sysTenantWalletMapper;
    private final SysUserService sysUserService;
    private final TransactionTemplate transactionTemplate;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<SysTenant> listPage(SysTenant sysTenant) {
        // 查询
        LambdaQueryWrapper<SysTenant> lqw = this.buildQueryWrapper(sysTenant);
        return sysTenantMapper.selectList(lqw);
    }

    @Override
    public boolean insertTenant(SysTenantDTO tenantDTO) {
        long userId = IdUtil.getSnowflakeNextId();
        tenantDTO.setAdministrator(userId);

        return Boolean.TRUE.equals(transactionTemplate.execute(status -> {
            sysTenantMapper.insert(tenantDTO);
            SysTenantWallet wallet = new SysTenantWallet();
            wallet.setTenantId(tenantDTO.getTenantId());
            wallet.setPassword(passwordEncoder.encode("123456"));
            sysTenantWalletMapper.insert(wallet);
            SysUserDTO sysUserDTO = new SysUserDTO();
            sysUserDTO.setUserId(userId);
            sysUserDTO.setTenantId(tenantDTO.getTenantId());
            sysUserDTO.setUsername(tenantDTO.getUsername());
            sysUserDTO.setPassword(tenantDTO.getPassword());
            return sysUserService.insertUser(sysUserDTO);
        }));
    }

    @Override
    public boolean updateTenant(SysTenant sysTenant) {
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
