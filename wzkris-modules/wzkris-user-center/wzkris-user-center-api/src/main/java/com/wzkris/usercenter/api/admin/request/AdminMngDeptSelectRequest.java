package com.wzkris.usercenter.api.admin.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "管理员部门选择参数")
public class AdminMngDeptSelectRequest {

    @Parameter(description = "部门名称")
    private String deptName;

}
