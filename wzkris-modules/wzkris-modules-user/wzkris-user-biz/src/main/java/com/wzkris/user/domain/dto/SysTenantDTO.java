package com.wzkris.user.domain.dto;

import com.wzkris.common.core.annotation.group.ValidationGroups;
import com.wzkris.user.domain.SysTenant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 租户传输层
 */
@Data
@NoArgsConstructor
public class SysTenantDTO extends SysTenant {
    @NotBlank(message = "[username] {validate.notnull}", groups = ValidationGroups.Insert.class)
    @Schema(description = "登录用户名")
    private String username;

    @NotBlank(message = "[password] {validate.notnull}", groups = ValidationGroups.Insert.class)
    @Schema(description = "登录密码")
    private String password;
}
