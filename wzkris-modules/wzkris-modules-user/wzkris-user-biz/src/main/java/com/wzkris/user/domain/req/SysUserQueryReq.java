package com.wzkris.user.domain.req;

import com.wzkris.common.web.model.QueryReq;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 前端查询条件
 */
@Data
public class SysUserQueryReq extends QueryReq {

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户邮箱")
    private String email;

    @Schema(description = "手机号码")
    private String phoneNumber;

    @Schema(description = "用户状态")
    private String status;

}
