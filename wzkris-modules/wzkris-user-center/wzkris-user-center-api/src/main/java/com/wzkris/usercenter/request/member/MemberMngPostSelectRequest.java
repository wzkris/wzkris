package com.wzkris.usercenter.request.member;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "成员职位选择参数")
public class MemberMngPostSelectRequest {

    @Parameter(description = "成员ID")
    private Long memberId;

    @Parameter(description = "职位名称")
    private String postName;

}
