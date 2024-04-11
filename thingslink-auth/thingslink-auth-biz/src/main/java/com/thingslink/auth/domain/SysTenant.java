package com.thingslink.auth.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.thingslink.common.orm.model.BaseEntity;
import com.thingslink.common.security.constants.SecurityConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SysTenant extends BaseEntity {

    @TableId
    private Long tenantId;

    @Schema(description = "管理员ID")
    private Long administrator;

    @Schema(description = "联系电话")
    private String contactPhone;

    @NotNull(message = "[companyName] {validate.notnull}")
    @Size(min = 0, max = 30, message = "[companyName] {validate.size.illegal}")
    @Schema(description = "企业名称")
    private String companyName;

    @Schema(description = "统一社会信用代码")
    private String licenseNumber;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "企业简介")
    private String intro;

    @Schema(description = "租户状态")
    private String status;

    @Schema(description = "域名")
    private String domain;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "租户套餐编号")
    private Long packageId;

    @Schema(description = "过期时间")
    private LocalDateTime expireTime;

    @Schema(description = "用户数量（-1不限制）")
    private Integer accountCount;

    @Schema(description = "删除标志")
    private Boolean isDeleted;

    public static boolean isSuperTenant(Long tenantId) {
        return SecurityConstants.SUPER_TENANT_ID.equals(tenantId);
    }

    public boolean isSuperTenant() {
        return isSuperTenant(this.tenantId);
    }
}
