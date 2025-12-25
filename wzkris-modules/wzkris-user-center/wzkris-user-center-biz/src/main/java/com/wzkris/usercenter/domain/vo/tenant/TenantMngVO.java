package com.wzkris.usercenter.domain.vo.tenant;

import com.wzkris.usercenter.domain.TenantInfoDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 租户管理展示层
 *
 * @author wzkris
 */
@Data
public class TenantMngVO extends TenantInfoDO {

    @Schema(description = "套餐名称")
    private String packageName;

    @Schema(description = "余额, 元")
    private BigDecimal balance;

}
