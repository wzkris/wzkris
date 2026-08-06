package com.wzkris.usercenter.api.tenant.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 租户分页列表元素响应（Mng 轨集合元素 -> {域}MngPageResponse）
 *
 * <p>继承 {@link TenantMngQueryResponse}。分页查询 SQL 与详情查询一致，无额外跨表字段。
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TenantMngPageResponse extends TenantMngQueryResponse {

}
