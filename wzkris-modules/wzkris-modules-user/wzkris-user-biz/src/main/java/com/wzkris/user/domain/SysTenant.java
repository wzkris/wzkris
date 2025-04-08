package com.wzkris.user.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wzkris.common.core.constant.SecurityConstants;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 租户表 sys_tenant
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@TableName(schema = "biz_sys")
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

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "操作密码")
    private String operPwd;

    @Schema(description = "租户状态")
    private String status;

    @Schema(description = "域名")
    private String domain;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "租户套餐编号")
    private Long packageId;

    @Schema(description = "过期时间")
    private Date expireTime;

    @Schema(description = "账号数量（-1不限制）")
    private Integer accountLimit;

    @Schema(description = "角色数量（-1不限制）")
    private Integer roleLimit;

    @Schema(description = "岗位数量（-1不限制）")
    private Integer postLimit;

    @Schema(description = "部门数量（-1不限制）")
    private Integer deptLimit;

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
