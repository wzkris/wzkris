package com.wzkris.principal.domain.vo.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 客户增加数量信息
 * @date : 2025/02/21
 */
@Data
public class CustomerIncryVO {

    @Schema(description = "日期")
    private String dateStr;

    @Schema(description = "数量")
    private int quantity;

    @Schema(description = "增长率")
    private String growthRate;

}
