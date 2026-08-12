package com.wzkris.payment.service.impl;

import com.wzkris.common.orm.plus.ServiceImplPlus;
import com.wzkris.payment.domain.CustomerBalanceInfoDO;
import com.wzkris.payment.domain.CustomerBalanceTransactionLogDO;
import com.wzkris.payment.enums.balance.BalanceRecordTypeEnum;
import com.wzkris.payment.mapper.CustomerBalanceInfoMapper;
import com.wzkris.payment.mapper.CustomerBalanceTransactionLogMapper;
import com.wzkris.payment.service.CustomerBalanceInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CustomerBalanceInfoServiceImpl
        extends ServiceImplPlus<CustomerBalanceInfoMapper, CustomerBalanceInfoDO>
        implements CustomerBalanceInfoService {

    private final CustomerBalanceTransactionLogMapper customerBalanceTransactionLogMapper;

    @Override
    public CustomerBalanceInfoDO getOrCreate(Long customerId) {
        CustomerBalanceInfoDO balance = getOneByObj(CustomerBalanceInfoDO::getCustomerId, customerId);
        if (balance == null) {
            balance = new CustomerBalanceInfoDO(customerId);
            try {
                save(balance);
            } catch (DuplicateKeyException e) {
                // 并发下他线程已建（uk_customer_balance_info_owner），重读
                balance = getOneByObj(CustomerBalanceInfoDO::getCustomerId, customerId);
            }
        }
        return balance;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean incryBalance(Long customerId, BigDecimal amount, String bizNo, String bizType, String remark) {
        amount = amount.abs();
        BigDecimal after = baseMapper.incryBalance(customerId, amount);
        if (after == null) {
            return false;
        }
        CustomerBalanceTransactionLogDO record = new CustomerBalanceTransactionLogDO();
        record.setCustomerId(customerId);
        record.setAmount(amount);
        record.setRecordType(BalanceRecordTypeEnum.INCOME);
        record.setBizNo(bizNo);
        record.setBizType(bizType);
        record.setBeforeBalance(after.subtract(amount));
        record.setAfterBalance(after);
        record.setRemark(remark);
        customerBalanceTransactionLogMapper.insert(record);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean decryBalance(Long customerId, BigDecimal amount, String bizNo, String bizType, String remark) {
        amount = amount.abs();
        BigDecimal after = baseMapper.decryBalance(customerId, amount);
        if (after == null) {
            return false;
        }
        CustomerBalanceTransactionLogDO record = new CustomerBalanceTransactionLogDO();
        record.setCustomerId(customerId);
        record.setAmount(amount);
        record.setRecordType(BalanceRecordTypeEnum.OUTCOME);
        record.setBizNo(bizNo);
        record.setBizType(bizType);
        record.setBeforeBalance(after.add(amount));
        record.setAfterBalance(after);
        record.setRemark(remark);
        customerBalanceTransactionLogMapper.insert(record);
        return true;
    }

}