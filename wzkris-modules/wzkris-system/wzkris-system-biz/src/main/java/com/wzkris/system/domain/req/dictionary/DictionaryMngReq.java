package com.wzkris.system.domain.req.dictionary;

import com.wzkris.system.domain.DictionaryInfoDO;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@AutoMappers({@AutoMapper(target = DictionaryInfoDO.class)})
@Schema(description = "字典添加修改参数体")
public class DictionaryMngReq {

    private Long dictId;

    @NotBlank(message = "{invalidParameter.dictKey.invalid}")
    @Size(min = 5, max = 30, message = "{invalidParameter.dictKey.invalid}")
    @Pattern(regexp = "^[a-z][a-z0-9_]*$", message = "{invalidParameter.dictKey.invalid}")// 字典类型必须以字母开头，且只能为小写字母，数字，下滑线
    @Schema(description = "字典键")
    private String dictKey;

    @NotBlank(message = "{invalidParameter.dictName.invalid}")
    @Size(min = 2, max = 50, message = "{invalidParameter.dictName.invalid}")
    @Schema(description = "字典名称")
    private String dictName;

    @NotNull(message = "{invalidParameter.dictValue.invalid}")
    @Size(min = 1, message = "{invalidParameter.dictValue.invalid}")
    @Schema(description = "字典键值")
    private DictionaryInfoDO.DictData[] dictValue;

    @Schema(description = "备注")
    private String remark;

}
