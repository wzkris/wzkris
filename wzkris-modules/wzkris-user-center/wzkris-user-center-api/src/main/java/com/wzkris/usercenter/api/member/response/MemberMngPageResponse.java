package com.wzkris.usercenter.api.member.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 成员分页列表元素响应（Mng 轨集合元素 -> {域}MngPageResponse）
 *
 * <p>继承 {@link MemberMngQueryResponse}，补充列表展示所需的职位名称跨表字段。
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MemberMngPageResponse extends MemberMngQueryResponse {

    @Schema(description = "职位名称")
    private String postName;

}
