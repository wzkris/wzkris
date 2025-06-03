package com.wzkris.user.mapper;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.user.constant.UserConstants;
import com.wzkris.user.domain.SysTenantWallet;
import com.wzkris.user.domain.SysTenantWalletRecord;
import com.wzkris.user.service.SysTenantWalletService;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@DisplayName("租户钱包测试用例")
@SpringBootTest
public class SysTenantWalletMapperTest {

    @Autowired
    SysTenantWalletMapper tenantWalletMapper;

    @Autowired
    SysTenantWalletService sysTenantWalletService;

    @Autowired
    SysTenantWalletRecordMapper tenantWalletRecordMapper;

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void test() {
        Long tenantId = this.insert();
        BigDecimal amount = new BigDecimal("100.35");
        this.incryBalance(tenantId, amount);
        this.decryBalance(tenantId, amount);
        this.delete(tenantId);
    }

    Long insert() {
        SysTenantWallet wallet = new SysTenantWallet(IdUtil.getSnowflakeNextId());
        int rows = tenantWalletMapper.insert(wallet);
        Assert.state(rows > 0, "插入失败");
        return wallet.getTenantId();
    }

    void incryBalance(Long tenantId, BigDecimal amount) {
        boolean rows = sysTenantWalletService.incryBalance(tenantId, amount, IdUtil.fastSimpleUUID(), "1", "");
        Assert.state(rows, "增加余额失败");
        SysTenantWalletRecord record =
                tenantWalletRecordMapper.selectOne(Wrappers.lambdaQuery(SysTenantWalletRecord.class)
                        .eq(SysTenantWalletRecord::getTenantId, tenantId)
                        .eq(SysTenantWalletRecord::getRecordType, UserConstants.WALLET_INCOME));
        Assert.notNull(record, "增加余额记录失败");
    }

    void decryBalance(Long tenantId, BigDecimal amount) {
        boolean rows = sysTenantWalletService.decryBalance(tenantId, amount, IdUtil.fastSimpleUUID(), "0", "");
        Assert.state(rows, "扣减余额失败");
        SysTenantWalletRecord record =
                tenantWalletRecordMapper.selectOne(Wrappers.lambdaQuery(SysTenantWalletRecord.class)
                        .eq(SysTenantWalletRecord::getTenantId, tenantId)
                        .eq(SysTenantWalletRecord::getRecordType, UserConstants.WALLET_OUTCOME));
        Assert.notNull(record, "扣减余额记录失败");
    }

    void delete(Long tenantId) {
        int rows = tenantWalletMapper.deleteById(tenantId);
        int delete = tenantWalletRecordMapper.delete(
                Wrappers.lambdaQuery(SysTenantWalletRecord.class).eq(SysTenantWalletRecord::getTenantId, tenantId));
        Assert.state(rows > 0 && delete > 0, "删除失败");
    }
}
