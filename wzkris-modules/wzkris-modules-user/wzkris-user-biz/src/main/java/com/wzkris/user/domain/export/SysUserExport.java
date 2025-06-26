package com.wzkris.user.domain.export;

import com.alibaba.excel.annotation.ExcelProperty;
import com.wzkris.common.core.annotation.PhoneNumber;
import com.wzkris.common.excel.annotation.ExcelDictFormat;
import com.wzkris.common.excel.convert.ExcelDictConvert;
import com.wzkris.user.domain.vo.SysUserVO;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.Data;

@Data
@AutoMappers({@AutoMapper(target = SysUserVO.class)})
public class SysUserExport {

    @ExcelProperty("用户名")
    private String username;

    @ExcelProperty("用户昵称")
    private String nickname;

    @ExcelProperty("用户邮箱")
    private String email;

    @PhoneNumber
    @ExcelProperty("手机号码")
    private String phoneNumber;

    @ExcelProperty(value = "账号状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private String status;

    @ExcelProperty(value = "性别", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=男,1=女,2=未知")
    private String gender;

    @ExcelProperty("部门名称")
    private String deptName;

    @ExcelProperty(value = "部门状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private String deptStatus;

}
