package com.wzkris.usercenter.api.tenantpackage.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 租户套餐分页列表元素响应（Mng 轨集合元素 -> {域}MngPageResponse）
 *
 * <p>继承 {@link TenantPackageMngQueryResponse}。分页查询无跨表 JOIN，无额外字段。
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TenantPackageMngPageResponse extends TenantPackageMngQueryResponse {

}
