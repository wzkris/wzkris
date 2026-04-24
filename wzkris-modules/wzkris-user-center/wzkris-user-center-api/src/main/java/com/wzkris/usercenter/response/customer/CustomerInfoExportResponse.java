package com.wzkris.usercenter.response.customer;

import com.alibaba.excel.annotation.ExcelProperty;
import com.wzkris.common.excel.annotation.ExcelDictFormat;
import com.wzkris.common.excel.convert.ExcelDictConvert;
import com.wzkris.usercenter.enums.user.GenderEnum;
import lombok.Data;

/**
 * 导出属性
 */
@Data
public class CustomerInfoExportResponse {

    @ExcelProperty(value = "用户昵称")
    private String nickname;

    @ExcelProperty(value = "手机号码")
    private String phoneNumber;

    @ExcelProperty(value = "账号状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private String status;

    @ExcelProperty(value = "性别", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=男,1=女,2=未知")
    private GenderEnum gender;

}
