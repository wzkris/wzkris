package com.wzkris.user.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 用户钱包记录表 app_user_wallet_record
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@TableName(schema = "biz_app")
public class AppUserWalletRecord {

    @TableId
    private Long recordId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "金额, 元")
    private BigDecimal amount;

    @Schema(description = "记录类型 0-收入 1-支出")
    private String recordType;

    @Schema(description = "创建时间")
    private Long createAt;

    @Schema(description = "备注")
    private String remark;

}
