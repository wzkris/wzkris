package com.wzkris.system.response.dictionary;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DictionaryInfoResponse {

    private Long dictId;

    @Schema(description = "字典键")
    private String dictKey;

    @Schema(description = "字典名称")
    private String dictName;

    @Schema(description = "字典键值")
    private DictionaryDataResponse[] dictValue;

    @Schema(description = "备注")
    private String remark;

}
