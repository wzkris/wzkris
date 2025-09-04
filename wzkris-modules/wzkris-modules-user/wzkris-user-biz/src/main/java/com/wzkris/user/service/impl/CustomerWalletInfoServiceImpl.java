package com.wzkris.user.service.impl;

import com.wzkris.user.constant.UserConstants;
import com.wzkris.user.domain.CustomerWalletRecordDO;
import com.wzkris.user.mapper.CustomerWalletInfoMapper;
import com.wzkris.user.mapper.CustomerWalletRecordMapper;
import com.wzkris.user.service.CustomerWalletInfoService;
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
    public boolean incryBalance(Long userId, BigDecimal amount) {
        amount = amount.abs();
        boolean suc = customerWalletInfoMapper.incryBalance(userId, amount) > 0;
        if (suc) {
            CustomerWalletRecordDO record = new CustomerWalletRecordDO();
            record.setUserId(userId);
            record.setAmount(amount);
            record.setRecordType(UserConstants.WALLET_INCOME);
            record.setCreateAt(new Date());
            customerWalletRecordMapper.insert(record);
        }
        return suc;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean decryBalance(Long userId, BigDecimal amount) {
        amount = amount.abs();
        boolean suc = customerWalletInfoMapper.decryBalance(userId, amount) > 0;
        if (suc) {
            CustomerWalletRecordDO record = new CustomerWalletRecordDO();
            record.setUserId(userId);
            record.setAmount(amount);
            record.setRecordType(UserConstants.WALLET_OUTCOME);
            record.setCreateAt(new Date());
            customerWalletRecordMapper.insert(record);
        }
        return suc;
    }

}
