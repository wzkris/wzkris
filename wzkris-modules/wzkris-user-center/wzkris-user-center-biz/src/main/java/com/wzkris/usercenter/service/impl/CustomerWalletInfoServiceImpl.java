package com.wzkris.usercenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wzkris.usercenter.domain.CustomerWalletInfoDO;
import com.wzkris.usercenter.domain.CustomerWalletRecordDO;
import com.wzkris.usercenter.enums.wallet.WalletRecordTypeEnum;
import com.wzkris.usercenter.mapper.CustomerWalletInfoMapper;
import com.wzkris.usercenter.mapper.CustomerWalletRecordMapper;
import com.wzkris.usercenter.service.CustomerWalletInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class CustomerWalletInfoServiceImpl
        extends ServiceImpl<CustomerWalletInfoMapper, CustomerWalletInfoDO>
        implements CustomerWalletInfoService {

    private final CustomerWalletRecordMapper customerWalletRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean incryBalance(Long customerId, BigDecimal amount) {
        amount = amount.abs();
        boolean suc = baseMapper.incryBalance(customerId, amount) > 0;
        if (suc) {
            CustomerWalletRecordDO record = new CustomerWalletRecordDO();
            record.setCustomerId(customerId);
            record.setAmount(amount);
            record.setRecordType(WalletRecordTypeEnum.INCOME);
            record.setCreateAt(OffsetDateTime.now());
            customerWalletRecordMapper.insert(record);
        }
        return suc;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean decryBalance(Long customerId, BigDecimal amount) {
        amount = amount.abs();
        boolean suc = baseMapper.decryBalance(customerId, amount) > 0;
        if (suc) {
            CustomerWalletRecordDO record = new CustomerWalletRecordDO();
            record.setCustomerId(customerId);
            record.setAmount(amount);
            record.setRecordType(WalletRecordTypeEnum.OUTCOME);
            record.setCreateAt(OffsetDateTime.now());
            customerWalletRecordMapper.insert(record);
        }
        return suc;
    }

}
