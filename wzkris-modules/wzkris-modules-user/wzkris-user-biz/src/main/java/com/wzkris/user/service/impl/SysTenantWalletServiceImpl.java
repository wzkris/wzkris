package com.wzkris.user.service.impl;

import com.wzkris.user.constant.UserConstants;
import com.wzkris.user.domain.SysTenantWalletRecord;
import com.wzkris.user.mapper.SysTenantWalletMapper;
import com.wzkris.user.mapper.SysTenantWalletRecordMapper;
import com.wzkris.user.service.SysTenantWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SysTenantWalletServiceImpl implements SysTenantWalletService {

    private final SysTenantWalletMapper sysTenantWalletMapper;
    private final SysTenantWalletRecordMapper sysTenantWalletRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean incryBalance(Long tenantId, BigDecimal amount) {
        amount = amount.abs();
        boolean suc = sysTenantWalletMapper.incryBalance(tenantId, amount) > 0;
        if (suc) {
            SysTenantWalletRecord record = new SysTenantWalletRecord();
            record.setTenantId(tenantId);
            record.setAmount(amount);
            record.setType(UserConstants.WALLET_INCOME);
            record.setPayTime(System.currentTimeMillis());
            sysTenantWalletRecordMapper.insert(record);
        }
        return suc;
    }

    @Override
    public boolean decryBalance(Long tenantId, BigDecimal amount) {
        amount = amount.abs();
        boolean suc = sysTenantWalletMapper.decryBalance(tenantId, amount) > 0;
        if (suc) {
            SysTenantWalletRecord record = new SysTenantWalletRecord();
            record.setTenantId(tenantId);
            record.setAmount(amount);
            record.setType(UserConstants.WALLET_OUTCOME);
            record.setPayTime(System.currentTimeMillis());
            sysTenantWalletRecordMapper.insert(record);
        }
        return suc;
    }
}
