package com.wzkris.user.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 用户钱包表 app_user_wallet
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@TableName(schema = "biz_app")
public class AppUserWallet {

    @TableId
    private Long userId;

    @Schema(description = "余额, 元")
    private BigDecimal balance;

    @Schema(description = "状态")
    private String status;

    public AppUserWallet(Long userId) {
        this.userId = userId;
    }
}
