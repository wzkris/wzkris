package com.wzkris.user.domain.export.customer;

import com.alibaba.excel.annotation.ExcelProperty;
import com.wzkris.common.excel.annotation.ExcelDictFormat;
import com.wzkris.common.excel.convert.ExcelDictConvert;
import com.wzkris.user.domain.CustomerInfoDO;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.Data;

/**
 * 导出属性
 */
@Data
@AutoMappers({@AutoMapper(target = CustomerInfoDO.class)})
public class CustomerInfoExport {

    @ExcelProperty(value = "用户昵称")
    private String nickname;

    @ExcelProperty(value = "手机号码")
    private String phoneNumber;

    @ExcelProperty(value = "账号状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private String status;

    @ExcelProperty(value = "性别", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=男,1=女,2=未知")
    private String gender;

}
