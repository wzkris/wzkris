package com.wzkris.principal.domain.vo.admin;

import com.wzkris.principal.domain.AdminInfoDO;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 管理员展示层对象
 * @date : 2023/5/26 16:12
 */
@Data
@AutoMapper(target = AdminInfoDO.class)
@NoArgsConstructor
public class AdminManageVO extends AdminInfoDO {

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "部门状态")
    private String deptStatus;

}
