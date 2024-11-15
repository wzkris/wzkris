package com.wzkris.user.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "[tenantName] {validate.notnull}")
    private String tenantName;

    @Schema(description = "操作密码")
    @Size(min = 6, max = 6, message = "[operPwd] {validate.size.illegal}")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String operPwd;

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
    private Integer accountLimit;

    @Schema(description = "角色数量（-1不限制）")
    private Integer roleLimit;

    @Schema(description = "岗位数量（-1不限制）")
    private Integer postLimit;

    @Schema(description = "部门数量（-1不限制）")
    private Integer deptLimit;

    @Schema(description = "删除标志")
    private Boolean isDeleted;

    public SysTenant(Long tenantId) {
        this.tenantId = tenantId;
    }

    public static boolean isSuperTenant(Long tenantId) {
        return SecurityConstants.SUPER_TENANT_ID.equals(tenantId);
    }

    @JsonIgnore
    public boolean isSuperTenant() {
        return isSuperTenant(this.tenantId);
    }
}
