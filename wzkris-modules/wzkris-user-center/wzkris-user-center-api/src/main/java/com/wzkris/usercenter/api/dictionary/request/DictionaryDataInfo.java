package com.wzkris.usercenter.api.dictionary.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DictionaryDataInfo {

    @Schema(description = "字典值")
    private String value;

    @Schema(description = "字典标签")
    private String label;

    @Schema(description = "表格回显样式")
    private String tableCls;

}
