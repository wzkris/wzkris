package com.wzkris.user.domain.export;

import com.alibaba.excel.annotation.ExcelProperty;
import com.wzkris.common.excel.annotation.ExcelDictFormat;
import com.wzkris.common.excel.convert.ExcelDictConvert;
import com.wzkris.user.domain.SysPost;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.Data;

@Data
@AutoMappers({@AutoMapper(target = SysPost.class)})
public class SysPostExport {

    @ExcelProperty("岗位编码")
    private String postCode;

    @ExcelProperty("岗位名称")
    private String postName;

    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private String status;

}
