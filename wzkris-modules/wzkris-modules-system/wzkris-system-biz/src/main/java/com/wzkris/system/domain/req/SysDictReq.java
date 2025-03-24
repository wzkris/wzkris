package com.wzkris.system.domain.req;

import com.wzkris.system.domain.SysDict;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@AutoMappers({@AutoMapper(target = SysDict.class)})
@Schema(description = "字典添加修改参数体")
public class SysDictReq {

    private Long dictId;

    @NotBlank(message = "{desc.dict}" + "{validate.notnull}")
    @Size(min = 2, max = 50, message = "{desc.dict}{desc.key}" + "{validate.size.illegal}")
    @Pattern(regexp = "^[a-z][a-z0-9_]*$", message = "{desc.dict}{validate.illegal}")// 字典类型必须以字母开头，且只能为小写字母，数字，下滑线
    @Schema(description = "字典键")
    private String dictKey;

    @NotBlank(message = "{desc.dict}{desc.name}" + "{validate.notnull}")
    @Size(min = 2, max = 50, message = "{desc.dict}{desc.name}" + "{validate.size.illegal}")
    @Schema(description = "字典名称")
    private String dictName;

    @NotNull(message = "{desc.dict}{desc.value}"+ "{validate.notnull}")
    @Size(min = 1, message = "{desc.dict}{desc.value}"+ "{validate.size.illegal}")
    @Schema(description = "字典键值")
    private SysDict.DictData[] dictValue;

    @Schema(description = "备注")
    private String remark;

}
