package com.wzkris.user.domain.export;

import com.alibaba.excel.annotation.ExcelProperty;
import com.wzkris.common.excel.annotation.ExcelDictFormat;
import com.wzkris.common.excel.convert.ExcelDictConvert;
import com.wzkris.common.excel.convert.ExcelListConverter;
import com.wzkris.user.domain.OAuth2Client;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import lombok.Data;

import java.util.List;

@Data
@AutoMappers({@AutoMapper(target = OAuth2Client.class)})
public class OAuth2ClientExport {

    @ExcelProperty("客户端")
    private String clientName;

    @ExcelProperty("客户端ID")
    private String clientId;

    @ExcelProperty(value = "权限域", converter = ExcelListConverter.class)
    private List<String> scopes;

    @ExcelProperty(value = "授权类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(
            readConverterExp =
                    "password=密码模式,sms=短信模式,refresh_token=刷新模式,authorization_code=授权码模式,client_credentials=客户端模式")
    private List<String> authorizationGrantTypes;

    @ExcelProperty(value = "回调地址", converter = ExcelListConverter.class)
    private List<String> redirectUris;

    @ExcelProperty(value = "客户端状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private String status;

    @ExcelProperty(value = "放行配置", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "false=自动,true=手动")
    private Boolean autoApprove;

}
