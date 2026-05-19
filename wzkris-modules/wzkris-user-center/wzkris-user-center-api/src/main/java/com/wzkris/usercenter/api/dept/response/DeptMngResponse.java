package com.wzkris.usercenter.api.dept.response;

import com.wzkris.usercenter.enums.dept.DeptStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class DeptMngResponse {

    private Long deptId;

    @Schema(description = "父部门ID")
    private Long parentId;

    @Schema(description = "祖级列表")
    private Long[] ancestors;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "0代表存在 1代表停用")
    private DeptStatusEnum status;

    @Schema(description = "显示顺序")
    private Integer deptSort;

    @Schema(description = "联系电话")
    private String contact;

    @Schema(description = "部门邮箱")
    private String email;

    @Schema(description = "子部门")
    private List<DeptMngResponse> children = new ArrayList<>();

}
