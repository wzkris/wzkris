package com.wzkris.usercenter.api.customer.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 客户分页列表元素响应（Mng 轨集合元素 -> {域}MngPageResponse）
 *
 * <p>继承 {@link CustomerMngQueryResponse}。分页查询无跨表 JOIN，无额外字段。
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CustomerMngPageResponse extends CustomerMngQueryResponse {

}
