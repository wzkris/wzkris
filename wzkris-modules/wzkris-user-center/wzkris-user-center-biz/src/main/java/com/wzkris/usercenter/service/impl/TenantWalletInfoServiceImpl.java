package com.wzkris.usercenter.service.impl;

import com.wzkris.usercenter.domain.TenantWalletRecordDO;
import com.wzkris.usercenter.enums.WalletRecordTypeEnum;
import com.wzkris.usercenter.mapper.TenantWalletInfoMapper;
import com.wzkris.usercenter.mapper.TenantWalletRecordMapper;
import com.wzkris.usercenter.service.TenantWalletInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TenantWalletInfoServiceImpl implements TenantWalletInfoService {

    private final TenantWalletInfoMapper tenantWalletInfoMapper;

    private final TenantWalletRecordMapper tenantWalletRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean incryBalance(Long tenantId, BigDecimal amount, String bizNo, String bizType, String remark) {
        amount = amount.abs();
        boolean suc = tenantWalletInfoMapper.incryBalance(tenantId, amount) > 0;
        if (suc) {
            TenantWalletRecordDO record = new TenantWalletRecordDO();
            record.setTenantId(tenantId);
            record.setAmount(amount);
            record.setRecordType(WalletRecordTypeEnum.INCOME.getValue());
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
        boolean suc = tenantWalletInfoMapper.decryBalance(tenantId, amount) > 0;
        if (suc) {
            TenantWalletRecordDO record = new TenantWalletRecordDO();
            record.setTenantId(tenantId);
            record.setAmount(amount);
            record.setRecordType(WalletRecordTypeEnum.OUTCOME.getValue());
            record.setBizNo(bizNo);
            record.setBizType(bizType);
            record.setCreateAt(new Date());
            record.setRemark(remark);
            tenantWalletRecordMapper.insert(record);
        }
        return suc;
    }

}
