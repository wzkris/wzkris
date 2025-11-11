package com.wzkris.principal.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wzkris.principal.enums.IdentifierType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 客户第三方信息
 *
 * @author wzkris
 */
@Data
@TableName(schema = "biz", value = "customer_social_info")
public class CustomerSocialInfoDO {

    @TableId
    private Long customerId;

    @Schema(description = "第三方唯一标识")
    private String identifier;

    /**
     * {@link IdentifierType}
     */
    @Schema(description = "渠道类型")
    private String identifierType;

}

