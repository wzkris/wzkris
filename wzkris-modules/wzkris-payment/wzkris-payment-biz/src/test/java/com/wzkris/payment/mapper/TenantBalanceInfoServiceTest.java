package com.wzkris.payment.mapper;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.payment.domain.TenantBalanceInfoDO;
import com.wzkris.payment.domain.TenantBalanceTransactionLogDO;
import com.wzkris.payment.enums.balance.BalanceRecordTypeEnum;
import com.wzkris.payment.service.TenantBalanceInfoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.UUID;

@DisplayName("租户账户测试用例")
@SpringBootTest
public class TenantBalanceInfoServiceTest {

    @Autowired
    TenantBalanceInfoMapper tenantBalanceInfoMapper;

    @Autowired
    TenantBalanceInfoService tenantBalanceInfoService;

    @Autowired
    TenantBalanceTransactionLogMapper tenantBalanceTransactionLogMapper;

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void test() {
        Long tenantId = this.insert();
        BigDecimal amount = new BigDecimal("100.35");
        this.incryBalance(tenantId, amount);
        this.decryBalance(tenantId, amount);
        this.delete(tenantId);
    }

    String generateId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    Long insert() {
        TenantBalanceInfoDO balance = tenantBalanceInfoService.getOrCreate(IdWorker.getId());
        return balance.getTenantId();
    }

    void incryBalance(Long tenantId, BigDecimal amount) {
        boolean rows = tenantBalanceInfoService.incryBalance(tenantId, amount, generateId(), "1", "");
        Assert.state(rows, "增加余额失败");
        TenantBalanceTransactionLogDO record =
                tenantBalanceTransactionLogMapper.selectOne(Wrappers.lambdaQuery(TenantBalanceTransactionLogDO.class)
                        .eq(TenantBalanceTransactionLogDO::getTenantId, tenantId)
                        .eq(TenantBalanceTransactionLogDO::getRecordType, BalanceRecordTypeEnum.INCOME.getValue()));
        Assert.notNull(record, "增加余额记录失败");
    }

    void decryBalance(Long tenantId, BigDecimal amount) {
        boolean rows = tenantBalanceInfoService.decryBalance(tenantId, amount, generateId(), "0", "");
        Assert.state(rows, "扣减余额失败");
        TenantBalanceTransactionLogDO record =
                tenantBalanceTransactionLogMapper.selectOne(Wrappers.lambdaQuery(TenantBalanceTransactionLogDO.class)
                        .eq(TenantBalanceTransactionLogDO::getTenantId, tenantId)
                        .eq(TenantBalanceTransactionLogDO::getRecordType, BalanceRecordTypeEnum.OUTCOME.getValue()));
        Assert.notNull(record, "扣减余额记录失败");
    }

    void delete(Long tenantId) {
        int rows = tenantBalanceInfoMapper.delete(
                Wrappers.lambdaQuery(TenantBalanceInfoDO.class).eq(TenantBalanceInfoDO::getTenantId, tenantId));
        int logs = tenantBalanceTransactionLogMapper.delete(
                Wrappers.lambdaQuery(TenantBalanceTransactionLogDO.class).eq(TenantBalanceTransactionLogDO::getTenantId, tenantId));
        Assert.state(rows > 0 && logs > 0, "删除失败");
    }

}