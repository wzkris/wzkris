package com.wzkris.equipment.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.wzkris.common.orm.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * (Product)实体类
 *
 * @author wzkris
 * @since 2024-12-04 14:40:00
 */
@Data
@NoArgsConstructor
public class Product extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -7774898640593067868L;

    @TableId
    private Long pdtId;

    @Schema(description = "产品名称")
    private String pdtName;

    @Schema(description = "产品唯一标识")
    private String pdtKey;

    @Schema(description = "产品类型 0 直连产品 1 网关产品 2 网关子产品")
    private String pdtType;

    @Schema(description = "协议ID")
    private Long ptcId;

    @Schema(description = "产品描述")
    private String remark;

    public Product(Long pdtId) {
        this.pdtId = pdtId;
    }

}
