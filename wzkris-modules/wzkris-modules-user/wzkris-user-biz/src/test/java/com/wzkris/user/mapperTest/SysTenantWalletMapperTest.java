package com.wzkris.user.mapperTest;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.common.core.utils.I18nUtil;
import com.wzkris.user.constant.UserConstants;
import com.wzkris.user.domain.SysTenantWallet;
import com.wzkris.user.domain.SysTenantWalletRecord;
import com.wzkris.user.mapper.SysTenantWalletMapper;
import com.wzkris.user.mapper.SysTenantWalletRecordMapper;
import com.wzkris.user.service.SysTenantWalletService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import java.math.BigDecimal;

@DisplayName("租户钱包测试用例")
@SpringBootTest
public class SysTenantWalletMapperTest {

    static String password = "123456";

    @Autowired
    SysTenantWalletMapper tenantWalletMapper;

    @Autowired
    SysTenantWalletService sysTenantWalletService;

    @Autowired
    SysTenantWalletRecordMapper tenantWalletRecordMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void test2() {
        String message = I18nUtil.messageRegex("{desc.username}{desc.or}{desc.pwd}{desc.error}");
        System.out.println(message);
    }

    @Test
    public void test() {
        Long tenantId = this.insert();
        BigDecimal amount = new BigDecimal("100.35");
        this.incryBalance(tenantId, amount);
        this.decryBalance(tenantId, amount);
        this.validatePassword(tenantId);
        this.delete(tenantId);
    }

    Long insert() {
        SysTenantWallet wallet = new SysTenantWallet();
        wallet.setTenantId(IdUtil.getSnowflakeNextId());
        int rows = tenantWalletMapper.insert(wallet);
        Assert.state(rows > 0, "插入失败");
        return wallet.getTenantId();
    }

    void incryBalance(Long tenantId, BigDecimal amount) {
        boolean rows = sysTenantWalletService.incryBalance(tenantId, amount);
        Assert.state(rows, "增加余额失败");
        SysTenantWalletRecord record = tenantWalletRecordMapper.selectOne(Wrappers.lambdaQuery(SysTenantWalletRecord.class)
                .eq(SysTenantWalletRecord::getTenantId, tenantId)
                .eq(SysTenantWalletRecord::getType, UserConstants.WALLET_INCOME));
        Assert.notNull(record, "增加余额记录失败");
    }

    void decryBalance(Long tenantId, BigDecimal amount) {
        boolean rows = sysTenantWalletService.decryBalance(tenantId, amount);
        Assert.state(rows, "扣减余额失败");
        SysTenantWalletRecord record = tenantWalletRecordMapper.selectOne(Wrappers.lambdaQuery(SysTenantWalletRecord.class)
                .eq(SysTenantWalletRecord::getTenantId, tenantId)
                .eq(SysTenantWalletRecord::getType, UserConstants.WALLET_OUTCOME));
        Assert.notNull(record, "扣减余额记录失败");
    }

    void validatePassword(Long tenantId) {
        SysTenantWallet wallet = tenantWalletMapper.selectById(tenantId);
    }

    void delete(Long tenantId) {
        int rows = tenantWalletMapper.deleteById(tenantId);
        int delete = tenantWalletRecordMapper.delete(Wrappers.lambdaQuery(SysTenantWalletRecord.class)
                .eq(SysTenantWalletRecord::getTenantId, tenantId));
        Assert.state(rows > 0 && delete > 0, "删除失败");
    }
}
