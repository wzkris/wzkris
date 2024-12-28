package com.wzkris.equipment.domain.req;

import com.wzkris.equipment.domain.ThingsModel;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@AutoMappers({@AutoMapper(target = ThingsModel.class)})
@Schema(description = "筛选条件")
public class ThingsModelQueryReq {

    @Schema(description = "物模型名称")
    private String modelName;

    @Schema(description = "产品id")
    private Long pdtId;

    @Schema(description = "模型类别（1-属性，2-功能，3-事件）")
    private String modelType;
}
