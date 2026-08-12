package com.wzkris.usercenter.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wzkris.common.orm.model.BaseTenantEntity;
import com.wzkris.usercenter.enums.tenantuser.TenantUserStatusEnum;
import com.wzkris.usercenter.enums.user.GenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * 租户用户对象
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@TableName(schema = "biz", value = "tenant_user")
public class TenantUserDO extends BaseTenantEntity {

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "手机号码")
    private String phoneNumber;

    @Schema(description = "状态值")
    private TenantUserStatusEnum status;

    @Schema(description = "性别")
    private GenderEnum gender;

    @Schema(description = "头像")
    private String avatar;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(description = "密码")
    private String password;

    @Schema(description = "最近登录ip")
    private String loginIp;

    @Schema(description = "最近登录日期")
    private OffsetDateTime loginDate;

    @Schema(description = "额外信息")
    private String remark;

    public TenantUserDO(Long id) {
        this.setId(id);
    }

}

