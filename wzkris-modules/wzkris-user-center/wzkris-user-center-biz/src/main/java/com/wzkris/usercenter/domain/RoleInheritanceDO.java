package com.wzkris.usercenter.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 角色继承关系关联表 role_inheritance
 *
 * @author wzkris
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(schema = "biz", value = "role_inheritance", autoResultMap = true)
public class RoleInheritanceDO extends BaseEntity {

    @Schema(description = "角色 ID")
    private Long roleId;

    @Schema(description = "子角色 ID")
    private Long childId;

}
