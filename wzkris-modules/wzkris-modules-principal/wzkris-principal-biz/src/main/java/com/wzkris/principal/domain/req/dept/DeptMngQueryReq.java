package com.wzkris.principal.domain.req.dept;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeptMngQueryReq {

    private Long deptId;

    private Long parentId;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "0代表存在 1代表停用")
    private String status;

    public DeptMngQueryReq(Long deptId) {
        this.deptId = deptId;
    }

    public DeptMngQueryReq(String status) {
        this.status = status;
    }

}
