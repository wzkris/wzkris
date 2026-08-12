package com.wzkris.payment.service.impl;

import com.wzkris.common.orm.plus.ServiceImplPlus;
import com.wzkris.payment.domain.TenantBalanceInfoDO;
import com.wzkris.payment.domain.TenantBalanceTransactionLogDO;
import com.wzkris.payment.enums.balance.BalanceRecordTypeEnum;
import com.wzkris.payment.mapper.TenantBalanceInfoMapper;
import com.wzkris.payment.mapper.TenantBalanceTransactionLogMapper;
import com.wzkris.payment.service.TenantBalanceInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TenantBalanceInfoServiceImpl
        extends ServiceImplPlus<TenantBalanceInfoMapper, TenantBalanceInfoDO>
        implements TenantBalanceInfoService {

    private final TenantBalanceTransactionLogMapper tenantBalanceTransactionLogMapper;

    @Override
    public TenantBalanceInfoDO getOrCreate(Long tenantId) {
        TenantBalanceInfoDO balance = getOneByObj(TenantBalanceInfoDO::getTenantId, tenantId);
        if (balance == null) {
            balance = new TenantBalanceInfoDO(tenantId);
            try {
                save(balance);
            } catch (DuplicateKeyException e) {
                // 并发下他线程已建（uk_tenant_balance_info_owner），重读
                balance = getOneByObj(TenantBalanceInfoDO::getTenantId, tenantId);
            }
        }
        return balance;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean incryBalance(Long tenantId, BigDecimal amount, String bizNo, String bizType, String remark) {
        amount = amount.abs();
        BigDecimal after = baseMapper.incryBalance(tenantId, amount);
        if (after == null) {
            return false;
        }
        TenantBalanceTransactionLogDO record = new TenantBalanceTransactionLogDO();
        record.setTenantId(tenantId);
        record.setAmount(amount);
        record.setRecordType(BalanceRecordTypeEnum.INCOME);
        record.setBizNo(bizNo);
        record.setBizType(bizType);
        record.setBeforeBalance(after.subtract(amount));
        record.setAfterBalance(after);
        record.setRemark(remark);
        tenantBalanceTransactionLogMapper.insert(record);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean decryBalance(Long tenantId, BigDecimal amount, String bizNo, String bizType, String remark) {
        amount = amount.abs();
        BigDecimal after = baseMapper.decryBalance(tenantId, amount);
        if (after == null) {
            return false;
        }
        TenantBalanceTransactionLogDO record = new TenantBalanceTransactionLogDO();
        record.setTenantId(tenantId);
        record.setAmount(amount);
        record.setRecordType(BalanceRecordTypeEnum.OUTCOME);
        record.setBizNo(bizNo);
        record.setBizType(bizType);
        record.setBeforeBalance(after.add(amount));
        record.setAfterBalance(after);
        record.setRemark(remark);
        tenantBalanceTransactionLogMapper.insert(record);
        return true;
    }

}