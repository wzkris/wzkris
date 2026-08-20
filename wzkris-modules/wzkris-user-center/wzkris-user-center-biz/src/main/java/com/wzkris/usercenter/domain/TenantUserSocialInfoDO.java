package com.wzkris.usercenter.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import com.wzkris.usercenter.enums.social.SocialTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 客户第三方信息
 *
 * @author wzkris
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(schema = "biz", value = "tenant_user_social_info")
public class TenantUserSocialInfoDO extends BaseEntity {

    @Schema(description = "租户用户ID")
    private Long tenantUserId;

    @Schema(description = "三方平台用户唯一标识")
    private String socialUid;

    @Schema(description = "渠道类型")
    private SocialTypeEnum socialType;

    @Schema(description = "渠道应用标识(小程序/公众号appid)")
    private String appid;

}
