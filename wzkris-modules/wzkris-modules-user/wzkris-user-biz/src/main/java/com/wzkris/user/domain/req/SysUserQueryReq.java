package com.wzkris.user.domain.req;

import com.wzkris.common.core.annotation.PhoneNumber;
import com.wzkris.common.core.annotation.Xss;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 前端查询条件
 */
@Data
public class SysUserQueryReq {
    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "部门ID")
    private Long deptId;

    @Xss
    @NotBlank(message = "[username] {validate.notnull}")
    @Size(min = 6, max = 20, message = "[username] {validate.size.illegal}")
    @Schema(description = "用户名")
    private String username;

    @Xss
    @Schema(description = "用户昵称")
    private String nickname;

    @Email(message = "[email] {validate.email.illegal}")
    @Size(min = 0, max = 50, message = "[email] {validate.size.illegal}")
    @Schema(description = "用户邮箱")
    private String email;

    @PhoneNumber
    @Schema(description = "手机号码")
    private String phoneNumber;

    @Schema(description = "用户状态")
    private String status;

    @Schema(description = "请求参数")
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>(2);
        }
        return params;
    }
}
