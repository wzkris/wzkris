package com.wzkris.auth.domain.resp;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Collection;
import lombok.Data;

/**
 * 用户信息
 *
 * @author wzkris
 */
@Data
public class SysUserinfo {

    @Schema(description = "是否租户管理员")
    private boolean admin;

    @Schema(description = "是否超级租户")
    private boolean superTenant;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "权限")
    private Collection<String> authorities;
}
