package com.wzkris.usercenter.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 角色和部门关联 role_to_dept
 *
 * @author wzkris
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(schema = "biz", value = "role_to_dept", autoResultMap = true)
public class RoleToDeptDO extends BaseEntity {

    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "部门ID")
    private Long deptId;

}
