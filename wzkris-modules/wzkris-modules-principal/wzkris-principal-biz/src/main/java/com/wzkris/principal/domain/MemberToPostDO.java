package com.wzkris.principal.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 租户成员和职位关联表
 *
 * @author wzkris
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberToPostDO {

    @Schema(description = "成员ID")
    private Long memberId;

    @Schema(description = "职位ID")
    private Long postId;

}
