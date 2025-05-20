package com.wzkris.user.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 租户钱包记录表 sys_tenant_wallet_record
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@TableName(schema = "biz")
public class SysTenantWalletRecord {

    @TableId
    private Long recordId;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "金额, 元")
    private BigDecimal amount;

    @Schema(description = "记录类型 0-收入 1-支出")
    private String recordType;

    @Schema(description = "业务类型")
    private String bizType;

    @Schema(description = "业务编号")
    private String bizNo;

    @Schema(description = "创建时间")
    private Date createAt;

    @Schema(description = "备注")
    private String remark;

}
