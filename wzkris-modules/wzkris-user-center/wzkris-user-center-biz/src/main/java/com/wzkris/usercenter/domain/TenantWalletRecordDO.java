package com.wzkris.usercenter.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseTenantEntity;
import com.wzkris.usercenter.enums.wallet.WalletRecordTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 租户钱包记录表
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(schema = "biz", value = "tenant_wallet_record")
public class TenantWalletRecordDO extends BaseTenantEntity {

    @Schema(description = "金额, 元")
    private BigDecimal amount;

    @Schema(description = "记录类型")
    private WalletRecordTypeEnum recordType;

    @Schema(description = "业务类型")
    private String bizType;

    @Schema(description = "业务编号")
    private String bizNo;

    @Schema(description = "备注")
    private String remark;

}
