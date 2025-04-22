package com.wzkris.user.domain.req;

import com.wzkris.common.web.model.QueryReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AppUserWalletRecordQueryReq extends QueryReq {

    @Schema(description = "记录类型 0-收入 1-支出")
    private String recordType;
}
