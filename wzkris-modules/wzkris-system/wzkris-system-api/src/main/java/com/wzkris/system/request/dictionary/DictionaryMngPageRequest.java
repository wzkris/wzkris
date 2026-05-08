package com.wzkris.system.request.dictionary;

import com.wzkris.common.orm.request.PagingRequest;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "筛选条件")
public class DictionaryMngPageRequest extends PagingRequest {

    @Parameter(description = "字典键")
    private String dictKey;

    @Parameter(description = "字典名称")
    private String dictName;
}
