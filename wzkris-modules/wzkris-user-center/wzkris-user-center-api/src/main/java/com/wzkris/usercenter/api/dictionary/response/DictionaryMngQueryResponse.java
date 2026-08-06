package com.wzkris.usercenter.api.dictionary.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 字典详情响应（Mng 轨单对象 -> {域}MngQueryResponse）
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
public class DictionaryMngQueryResponse {

    private Long id;

    @Schema(description = "字典键")
    private String dictKey;

    @Schema(description = "字典名称")
    private String dictName;

    @Schema(description = "字典键值")
    private DictionaryDataResponse[] dictValue;

    @Schema(description = "备注")
    private String remark;

}
