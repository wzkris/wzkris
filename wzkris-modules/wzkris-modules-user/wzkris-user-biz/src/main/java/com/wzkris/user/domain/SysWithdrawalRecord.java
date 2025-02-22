package com.wzkris.user.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 系统提现记录 sys_user_role
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysWithdrawalRecord {

    @TableId
    private Long withdrawalId;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "状态 '0'处理中 '1'成功 '2'失败")
    private String status;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "第三方请求参数")
    private String requestParams;

    @Schema(description = "金额")
    private BigDecimal amount;

    @Schema(description = "错误信息")
    private String errorMsg;

    @Schema(description = "创建者")
    private Long creatorId;

    @Schema(description = "创建时间")
    private Long createAt;

    @Schema(description = "完成时间")
    private Long completeAt;

    @Schema(description = "备注")
    private String remark;
}
