package com.wzkris.user.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 租户表 sys_tenant
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
public class SysTenant extends BaseEntity {

    @TableId
    private Long tenantId;

    @Schema(description = "管理员ID")
    private Long administrator;

    @Schema(description = "租户类型 0-个人 1-企业")
    private String tenantType;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "租户名称")
    private String tenantName;

    @Schema(description = "租户状态")
    private String status;

    @Schema(description = "域名")
    private String domain;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "租户套餐编号")
    private Long packageId;

    @Schema(description = "过期时间（-1不限制）")
    private Long expireTime;

    @Schema(description = "账号数量（-1不限制）")
    private Short accountLimit;

    @Schema(description = "删除标志")
    private Boolean isDeleted;

    public static boolean isSuperTenant(Long tenantId) {
        return SecurityConstants.SUPER_TENANT_ID.equals(tenantId);
    }

    @JsonIgnore
    public boolean isSuperTenant() {
        return isSuperTenant(this.tenantId);
    }
}
