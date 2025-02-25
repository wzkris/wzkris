package com.wzkris.user.service.impl;

import cn.hutool.core.date.DateUtil;
import com.wzkris.user.constant.UserConstants;
import com.wzkris.user.domain.AppUserWalletRecord;
import com.wzkris.user.mapper.AppUserWalletMapper;
import com.wzkris.user.mapper.AppUserWalletRecordMapper;
import com.wzkris.user.service.AppUserWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AppUserWalletServiceImpl implements AppUserWalletService {

    private final AppUserWalletMapper appUserWalletMapper;

    private final AppUserWalletRecordMapper appUserWalletRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean incryBalance(Long userId, BigDecimal amount) {
        amount = amount.abs();
        boolean suc = appUserWalletMapper.incryBalance(userId, amount) > 0;
        if (suc) {
            AppUserWalletRecord record = new AppUserWalletRecord();
            record.setUserId(userId);
            record.setAmount(amount);
            record.setRecordType(UserConstants.WALLET_INCOME);
            record.setCreateAt(DateUtil.date());
            appUserWalletRecordMapper.insert(record);
        }
        return suc;
    }

    @Override
    public boolean decryBalance(Long userId, BigDecimal amount) {
        amount = amount.abs();
        boolean suc = appUserWalletMapper.decryBalance(userId, amount) > 0;
        if (suc) {
            AppUserWalletRecord record = new AppUserWalletRecord();
            record.setUserId(userId);
            record.setAmount(amount);
            record.setRecordType(UserConstants.WALLET_OUTCOME);
            record.setCreateAt(DateUtil.date());
            appUserWalletRecordMapper.insert(record);
        }
        return suc;
    }
}
