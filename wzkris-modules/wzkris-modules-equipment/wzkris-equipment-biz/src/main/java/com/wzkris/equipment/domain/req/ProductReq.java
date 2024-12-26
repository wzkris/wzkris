package com.wzkris.equipment.domain.req;

import com.wzkris.common.core.annotation.EnumsCheck;
import com.wzkris.equipment.constants.ProductConstant;
import com.wzkris.equipment.domain.Product;
import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMappers;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@AutoMappers({@AutoMapper(target = Product.class)})
@Schema(description = "产品添加修改参数体")
public class ProductReq {

    private Long pdtId;

    @Size(min = 3, max = 32, message = "{desc.product}{desc.key}" + "{validate.size.illegal}")
    @Schema(description = "产品名称")
    private String pdtName;

    @Size(min = 16, max = 16, message = "{desc.product}{desc.key}" + "{validate.size.illegal}")
    @Schema(description = "产品唯一标识")
    private String pdtKey;

    @EnumsCheck(values = {ProductConstant.TYPE_NORMAL, ProductConstant.TYPE_GATEWAY, ProductConstant.TYPE_GATEWAY_SUB})
    @Schema(description = "产品类型 0 直连产品 1 网关产品 2 网关子产品")
    private String pdtType;

    @NotNull(message = "{desc.protocol} id" + "{validate.notnull}")
    @Schema(description = "协议ID")
    private Long ptcId;

    @Schema(description = "产品描述")
    private String remark;

}
