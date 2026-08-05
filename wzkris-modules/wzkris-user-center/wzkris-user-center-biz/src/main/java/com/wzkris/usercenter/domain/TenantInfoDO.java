package com.wzkris.usercenter.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wzkris.common.orm.model.BaseEntity;
import com.wzkris.usercenter.enums.tenant.TenantStatusEnum;
import com.wzkris.usercenter.enums.tenant.TenantTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * 租户表
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@TableName(schema = "biz", value = "tenant_info")
public class TenantInfoDO extends BaseEntity {

    @Schema(description = "管理员ID")
    private Long administrator;

    @Schema(description = "租户类型")
    private TenantTypeEnum tenantType;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "租户名称")
    private String tenantName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "操作密码")
    private String operPwd;

    @Schema(description = "租户状态")
    private TenantStatusEnum status;

    @Schema(description = "域名")
    private String domain;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "租户套餐编号")
    private Long packageId;

    @Schema(description = "过期时间")
    private OffsetDateTime expireTime;

    public TenantInfoDO(Long id) {
        this.setId(id);
    }

}

