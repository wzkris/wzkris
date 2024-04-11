package com.thingslink.auth.domain.dto;

import com.thingslink.auth.domain.SysTenant;
import com.thingslink.common.core.validate.group.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SysTenantDTO extends SysTenant {
    @NotBlank(message = "[username] {validate.notnull}", groups = ValidationGroups.Insert.class)
    @Schema(description = "登录用户名")
    private String username;

    @NotBlank(message = "[password] {validate.notnull}", groups = ValidationGroups.Insert.class)
    @Schema(description = "登录密码")
    private String password;
}
