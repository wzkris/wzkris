package com.wzkris.usercenter.request.dept;

import com.wzkris.usercenter.enums.dept.DeptStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeptMngListRequest {

    private Long deptId;

    private Long parentId;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "0代表存在 1代表停用")
    private DeptStatusEnum status;

    public DeptMngListRequest(Long deptId) {
        this.deptId = deptId;
    }

    public DeptMngListRequest(DeptStatusEnum status) {
        this.status = status;
    }

}

