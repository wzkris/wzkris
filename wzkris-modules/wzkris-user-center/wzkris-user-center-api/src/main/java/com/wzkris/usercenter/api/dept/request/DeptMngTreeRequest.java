package com.wzkris.usercenter.api.dept.request;

import com.wzkris.usercenter.enums.dept.DeptStatusEnum;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "部门树查询条件")
public class DeptMngTreeRequest {

    @Parameter(description = "部门ID")
    private Long deptId;

    @Parameter(description = "父部门ID")
    private Long parentId;

    @Parameter(description = "部门名称")
    private String deptName;

    @Parameter(description = "0代表存在 1代表停用")
    private DeptStatusEnum status;

}
