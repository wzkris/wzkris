package com.wzkris.auth.domain.vo;

import com.wzkris.common.security.oauth2.domain.model.LoginSyser;
import io.github.linpeilie.annotations.AutoMapper;
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
@AutoMapper(target = LoginSyser.class)
public class LoginUserVO {

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "是否当前租户下的最高管理员")
    private boolean administrator;

    @Schema(description = "权限")
    private Collection<? extends GrantedAuthority> authorities;
}
