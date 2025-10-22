package com.wzkris.principal.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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

    private String identifier;

    @Schema(description = "渠道")
    private String identifierType;

}

