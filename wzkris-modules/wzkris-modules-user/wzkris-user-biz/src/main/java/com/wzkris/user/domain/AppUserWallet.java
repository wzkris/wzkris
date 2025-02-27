package com.wzkris.user.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.wzkris.common.core.constant.CommonConstants;
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
public class AppUserWallet {

    @TableId
    private Long userId;

    @Schema(description = "余额, 元")
    private BigDecimal balance;

    @Schema(description = "状态")
    private String status;

    public AppUserWallet(Long userId) {
        this.userId = userId;
        this.balance = BigDecimal.ZERO;
        this.status = CommonConstants.STATUS_ENABLE;
    }
}
