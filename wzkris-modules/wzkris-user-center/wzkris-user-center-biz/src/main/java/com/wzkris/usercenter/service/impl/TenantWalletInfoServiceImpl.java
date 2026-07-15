package com.wzkris.usercenter.service.impl;

import com.wzkris.common.orm.plus.ServiceImplPlus;
import com.wzkris.usercenter.domain.TenantWalletInfoDO;
import com.wzkris.usercenter.domain.TenantWalletRecordDO;
import com.wzkris.usercenter.enums.wallet.WalletRecordTypeEnum;
import com.wzkris.usercenter.mapper.TenantWalletInfoMapper;
import com.wzkris.usercenter.mapper.TenantWalletRecordMapper;
import com.wzkris.usercenter.service.TenantWalletInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class TenantWalletInfoServiceImpl
        extends ServiceImplPlus<TenantWalletInfoMapper, TenantWalletInfoDO>
        implements TenantWalletInfoService {

    private final TenantWalletRecordMapper tenantWalletRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean incryBalance(Long tenantId, BigDecimal amount, String bizNo, String bizType, String remark) {
        amount = amount.abs();
        boolean suc = baseMapper.incryBalance(tenantId, amount) > 0;
        if (suc) {
            TenantWalletRecordDO record = new TenantWalletRecordDO();
            record.setTenantId(tenantId);
            record.setAmount(amount);
            record.setRecordType(WalletRecordTypeEnum.INCOME);
            record.setBizNo(bizNo);
            record.setBizType(bizType);
            record.setCreateAt(OffsetDateTime.now());
            record.setRemark(remark);
            tenantWalletRecordMapper.insert(record);
        }
        return suc;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean decryBalance(Long tenantId, BigDecimal amount, String bizNo, String bizType, String remark) {
        amount = amount.abs();
        boolean suc = baseMapper.decryBalance(tenantId, amount) > 0;
        if (suc) {
            TenantWalletRecordDO record = new TenantWalletRecordDO();
            record.setTenantId(tenantId);
            record.setAmount(amount);
            record.setRecordType(WalletRecordTypeEnum.OUTCOME);
            record.setBizNo(bizNo);
            record.setBizType(bizType);
            record.setCreateAt(OffsetDateTime.now());
            record.setRemark(remark);
            tenantWalletRecordMapper.insert(record);
        }
        return suc;
    }

}
