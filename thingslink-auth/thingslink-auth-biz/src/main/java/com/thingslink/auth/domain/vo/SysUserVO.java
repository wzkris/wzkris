package com.thingslink.auth.domain.vo;

import com.thingslink.auth.domain.SysUser;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 管理员展示层对象
 * @date : 2023/5/26 16:12
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = SysUser.class)
public class SysUserVO extends SysUser {

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "部门状态")
    private String deptStatus;
}
