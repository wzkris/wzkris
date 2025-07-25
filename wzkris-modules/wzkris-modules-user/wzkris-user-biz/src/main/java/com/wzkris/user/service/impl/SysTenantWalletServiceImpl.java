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
import java.util.Date;

@Service
@RequiredArgsConstructor
public class SysTenantWalletServiceImpl implements SysTenantWalletService {

    private final SysTenantWalletMapper tenantWalletMapper;

    private final SysTenantWalletRecordMapper tenantWalletRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean incryBalance(Long tenantId, BigDecimal amount, String bizNo, String bizType, String remark) {
        amount = amount.abs();
        boolean suc = tenantWalletMapper.incryBalance(tenantId, amount) > 0;
        if (suc) {
            SysTenantWalletRecord record = new SysTenantWalletRecord();
            record.setTenantId(tenantId);
            record.setAmount(amount);
            record.setRecordType(UserConstants.WALLET_INCOME);
            record.setBizNo(bizNo);
            record.setBizType(bizType);
            record.setCreateAt(new Date());
            record.setRemark(remark);
            tenantWalletRecordMapper.insert(record);
        }
        return suc;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean decryBalance(Long tenantId, BigDecimal amount, String bizNo, String bizType, String remark) {
        amount = amount.abs();
        boolean suc = tenantWalletMapper.decryBalance(tenantId, amount) > 0;
        if (suc) {
            SysTenantWalletRecord record = new SysTenantWalletRecord();
            record.setTenantId(tenantId);
            record.setAmount(amount);
            record.setRecordType(UserConstants.WALLET_OUTCOME);
            record.setBizNo(bizNo);
            record.setBizType(bizType);
            record.setCreateAt(new Date());
            record.setRemark(remark);
            tenantWalletRecordMapper.insert(record);
        }
        return suc;
    }

}
