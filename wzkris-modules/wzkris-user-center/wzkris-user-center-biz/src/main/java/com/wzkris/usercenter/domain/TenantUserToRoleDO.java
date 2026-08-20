package com.wzkris.usercenter.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 租户用户和角色关联表
 *
 * @author wzkris
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName(schema = "biz", value = "tenant_user_to_role", autoResultMap = true)
public class TenantUserToRoleDO extends BaseEntity {

    @Schema(description = "用户ID")
    private Long tenantUserId;

    @Schema(description = "角色ID")
    private Long tenantRoleId;

}
