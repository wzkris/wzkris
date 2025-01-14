package com.wzkris.system.domain.req;

import com.wzkris.system.domain.GlobalDictType;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@AutoMappers({@AutoMapper(target = GlobalDictType.class)})
@Schema(description = "字典类型添加修改参数体")
public class GlobalDictTypeReq {

    private Long typeId;

    @NotBlank(message = "{desc.dict}{desc.name}" + "{validate.notnull}")
    @Size(min = 2, max = 50, message = "{desc.dict}{desc.name}" + "{validate.size.illegal}")
    @Schema(description = "字典名称")
    private String dictName;

    @NotBlank(message = "{desc.dict}{desc.type}" + "{validate.notnull}")
    @Size(min = 2, max = 50, message = "{desc.dict}{desc.type}" + "{validate.size.illegal}")
    @Pattern(regexp = "^[a-z][a-z0-9_]*$", message = "{desc.dict}{desc.type}{validate.illegal}")// 字典类型必须以字母开头，且只能为小写字母，数字，下滑线
    @Schema(description = "字典类型")
    private String dictType;
}
