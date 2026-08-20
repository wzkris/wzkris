package com.wzkris.usercenter.api.dictionary.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 字典分页列表元素响应（Mng 轨集合元素 -> {域}MngPageResponse）
 *
 * <p>继承 {@link DictionaryMngQueryResponse}，无跨表 JOIN 字段，列表元素类型与详情响应保持一致。
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DictionaryMngPageResponse extends DictionaryMngQueryResponse {

}
