package com.wzkris.usercenter.remote.api.customer.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 客户列表元素响应（queryList 返回），字段与 {@link CustomerQueryResponse} 一致，单表查询无跨表展示字段。
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CustomerListResponse extends CustomerQueryResponse {

}
