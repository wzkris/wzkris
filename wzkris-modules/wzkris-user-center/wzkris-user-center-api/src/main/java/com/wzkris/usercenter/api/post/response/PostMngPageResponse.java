package com.wzkris.usercenter.api.post.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 职位分页列表元素响应（Mng 轨集合元素 -> {域}MngPageResponse）
 *
 * <p>继承 {@link PostMngQueryResponse}，职位分页为单表查询，无跨表展示字段。
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PostMngPageResponse extends PostMngQueryResponse {

}
