package com.wzkris.system.domain.req;

import com.wzkris.common.core.annotation.EnumsCheck;
import com.wzkris.common.core.constant.CommonConstants;
import com.wzkris.system.domain.GlobalDictData;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@AutoMappers({@AutoMapper(target = GlobalDictData.class)})
@Schema(description = "字典数据添加修改参数体")
public class GlobalDictDataReq {

    private Long dataId;

    @Schema(description = "排序")
    private Long dictSort;

    @NotBlank(message = "{desc.dict}{desc.label}" + "{validate.notnull}")
    @Size(min = 1, max = 50, message = "{desc.dict}{desc.label}" + "{validate.size.illegal}")
    @Schema(description = "标签")
    private String dictLabel;

    @NotBlank(message = "{desc.dict}{desc.value}" + "{validate.notnull}")
    @Size(min = 1, max = 50, message = "{desc.dict}{desc.value}" + "{validate.size.illegal}")
    @Schema(description = "键值")
    private String dictValue;

    @NotBlank(message = "{desc.dict}{desc.type}" + "{validate.notnull}")
    @Size(min = 1, max = 50, message = "{desc.dict}{desc.type}" + "{validate.size.illegal}")
    @Schema(description = "字典类型")
    private String dictType;

    @Max(value = 100)
    @Schema(description = "样式属性（其他样式扩展）")
    private String cssClass;

    @Schema(description = "表格字典样式")
    private String listClass;

    @EnumsCheck(values = {CommonConstants.YES, CommonConstants.NO})
    @Schema(description = "是否默认（Y是 N否")
    private String isDefault;
}
