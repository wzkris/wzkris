package com.wzkris.user.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 登录用户信息VO
 * @date : 2024/4/13 14:13
 */
@Data
public class SysUserOwnVO {

    @Schema(description = "租户管理员")
    private boolean admin;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "权限")
    private Collection<? extends GrantedAuthority> authorities;
}
