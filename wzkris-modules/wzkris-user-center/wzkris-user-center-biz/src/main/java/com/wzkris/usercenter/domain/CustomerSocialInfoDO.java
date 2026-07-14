package com.wzkris.usercenter.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.common.orm.model.BaseEntity;
import com.wzkris.usercenter.enums.social.IdentifierTypeEnum;
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
@TableName(schema = "biz", value = "customer_social_info")
public class CustomerSocialInfoDO extends BaseEntity {

    @TableId
    private Long customerId;

    @Schema(description = "第三方唯一标识")
    private String identifier;

    @Schema(description = "渠道类型")
    private IdentifierTypeEnum identifierType;

}
