package com.wzkris.user.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 钱包记录筛选条件
 */
@Data
public class SysTenantWalletRecordQueryReq {

    private Long tenantId;

    @Schema(description = "记录类型 0-收入 1-支出")
    private String type;

    @Schema(description = "请求参数")
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>(2);
        }
        return params;
    }
}
