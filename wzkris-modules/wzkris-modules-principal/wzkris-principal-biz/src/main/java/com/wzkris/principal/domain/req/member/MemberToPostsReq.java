package com.wzkris.principal.domain.req.member;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 租户成员对职位 一对多
 */
@Data
public class MemberToPostsReq {

    @NotNull(message = "{invalidParameter.id.invalid}")
    private Long memberId;

    private List<Long> postIds;

}
