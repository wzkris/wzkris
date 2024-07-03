package com.wzkris.user.domain.vo;

import com.wzkris.user.domain.SysUser;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 管理员展示层对象
 * @date : 2023/5/26 16:12
 */
@Data
@AutoMapper(target = SysUser.class)
@Accessors(chain = true)
@NoArgsConstructor
public class SysUserVO extends SysUser {

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "部门状态")
    private String deptStatus;
}
