package com.wzkris.user.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户和角色关联 biz.tenant_wallet_withdrawal_record
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserToRoleDO {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "角色ID")
    private Long roleId;

}
