package com.wzkris.usercenter.request.member;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 成员授权职位请求体
 */
@Data
@Schema(description = "成员授权职位参数体")
public class MemberMngGrantPostRequest {

    @NotNull(message = "{invalidParameter.id.invalid}")
    @Schema(description = "成员 ID")
    private Long memberId;

    @Schema(description = "职位 ID 列表")
    private List<Long> postIds;

}

