package com.wzkris.auth.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 登录用户信息VO
 * @date : 2024/4/13 14:13
 */
@Data
@Accessors(chain = true)
public class LoginUserVO {

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "是否当前租户下的最高管理员")
    private Boolean isAdmin;

    @Schema(description = "权限")
    private Collection<? extends GrantedAuthority> authorities;
}
