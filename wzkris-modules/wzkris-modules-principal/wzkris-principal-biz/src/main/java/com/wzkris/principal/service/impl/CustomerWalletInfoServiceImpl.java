package com.wzkris.principal.service.impl;

import com.wzkris.principal.constant.UserConstants;
import com.wzkris.principal.domain.CustomerWalletRecordDO;
import com.wzkris.principal.mapper.CustomerWalletInfoMapper;
import com.wzkris.principal.mapper.CustomerWalletRecordMapper;
import com.wzkris.principal.service.CustomerWalletInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class CustomerWalletInfoServiceImpl implements CustomerWalletInfoService {

    private final CustomerWalletInfoMapper customerWalletInfoMapper;

    private final CustomerWalletRecordMapper customerWalletRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean incryBalance(Long customerId, BigDecimal amount) {
        amount = amount.abs();
        boolean suc = customerWalletInfoMapper.incryBalance(customerId, amount) > 0;
        if (suc) {
            CustomerWalletRecordDO record = new CustomerWalletRecordDO();
            record.setCustomerId(customerId);
            record.setAmount(amount);
            record.setRecordType(UserConstants.WALLET_INCOME);
            record.setCreateAt(new Date());
            customerWalletRecordMapper.insert(record);
        }
        return suc;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean decryBalance(Long customerId, BigDecimal amount) {
        amount = amount.abs();
        boolean suc = customerWalletInfoMapper.decryBalance(customerId, amount) > 0;
        if (suc) {
            CustomerWalletRecordDO record = new CustomerWalletRecordDO();
            record.setCustomerId(customerId);
            record.setAmount(amount);
            record.setRecordType(UserConstants.WALLET_OUTCOME);
            record.setCreateAt(new Date());
            customerWalletRecordMapper.insert(record);
        }
        return suc;
    }

}
