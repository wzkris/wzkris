package com.wzkris.principal.mapper;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wzkris.principal.domain.TenantWalletInfoDO;
import com.wzkris.principal.domain.TenantWalletRecordDO;
import com.wzkris.principal.enums.WalletRecordTypeEnum;
import com.wzkris.principal.service.TenantWalletInfoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.UUID;

@DisplayName("租户钱包测试用例")
@SpringBootTest
public class TenantWalletInfoMapperTest {

    @Autowired
    TenantWalletInfoMapper tenantWalletMapper;

    @Autowired
    TenantWalletInfoService tenantWalletInfoService;

    @Autowired
    TenantWalletRecordMapper tenantWalletRecordMapper;

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
        TenantWalletInfoDO wallet = new TenantWalletInfoDO(IdWorker.getId());
        int rows = tenantWalletMapper.insert(wallet);
        Assert.state(rows > 0, "插入失败");
        return wallet.getTenantId();
    }

    void incryBalance(Long tenantId, BigDecimal amount) {
        boolean rows = tenantWalletInfoService.incryBalance(tenantId, amount, generateId(), "1", "");
        Assert.state(rows, "增加余额失败");
        TenantWalletRecordDO record =
                tenantWalletRecordMapper.selectOne(Wrappers.lambdaQuery(TenantWalletRecordDO.class)
                        .eq(TenantWalletRecordDO::getTenantId, tenantId)
                        .eq(TenantWalletRecordDO::getRecordType, WalletRecordTypeEnum.INCOME.getValue()));
        Assert.notNull(record, "增加余额记录失败");
    }

    void decryBalance(Long tenantId, BigDecimal amount) {
        boolean rows = tenantWalletInfoService.decryBalance(tenantId, amount, generateId(), "0", "");
        Assert.state(rows, "扣减余额失败");
        TenantWalletRecordDO record =
                tenantWalletRecordMapper.selectOne(Wrappers.lambdaQuery(TenantWalletRecordDO.class)
                        .eq(TenantWalletRecordDO::getTenantId, tenantId)
                        .eq(TenantWalletRecordDO::getRecordType, WalletRecordTypeEnum.OUTCOME.getValue()));
        Assert.notNull(record, "扣减余额记录失败");
    }

    void delete(Long tenantId) {
        int rows = tenantWalletMapper.deleteById(tenantId);
        int delete = tenantWalletRecordMapper.delete(
                Wrappers.lambdaQuery(TenantWalletRecordDO.class).eq(TenantWalletRecordDO::getTenantId, tenantId));
        Assert.state(rows > 0 && delete > 0, "删除失败");
    }

}
