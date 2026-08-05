package com.wzkris.usercenter.api.member.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "成员授权职位参数体")
public class MemberMngGrantPostRequest {

    @NotNull(message = "{invalidParameter.id.invalid}")
    @Schema(description = "成员 ID")
    private Long id;

    @Schema(description = "职位 ID 列表")
    private List<Long> postIds;

}

