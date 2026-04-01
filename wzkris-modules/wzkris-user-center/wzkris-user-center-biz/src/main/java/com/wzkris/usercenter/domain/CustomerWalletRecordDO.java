package com.wzkris.usercenter.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 客户钱包记录表
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@TableName(schema = "biz", value = "customer_wallet_record")
public class CustomerWalletRecordDO {

    @TableId
    private Long recordId;

    @Schema(description = "客户ID")
    private Long customerId;

    @Schema(description = "金额, 元")
    private BigDecimal amount;

    @Schema(description = "记录类型 0-收入 1-支出")
    private String recordType;

    @Schema(description = "创建时间")
    private Date createAt;

    @Schema(description = "备注")
    private String remark;

}
