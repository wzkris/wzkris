package com.wzkris.user.domain.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SysDeptQueryReq {

    private Long deptId;

    private Long parentId;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "0代表存在 1代表停用")
    private String status;

    public SysDeptQueryReq(Long deptId) {
        this.deptId = deptId;
    }

    public SysDeptQueryReq(String status) {
        this.status = status;
    }

}
