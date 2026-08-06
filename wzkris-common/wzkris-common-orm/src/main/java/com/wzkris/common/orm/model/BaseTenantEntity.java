package com.wzkris.common.orm.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户实体基类
 *
 * <p>租户隔离表继承本类以复用 {@code tenantId} 字段；平台全局表仍直接继承 {@link BaseEntity}。
 * {@code tenantId} 列由 {@code TenantLineInnerInterceptor} 自动注入，无需手动填充。
 *
 * @author wzkris
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseTenantEntity extends BaseEntity {

    /**
     * 租户ID
     */
    private Long tenantId;

}
