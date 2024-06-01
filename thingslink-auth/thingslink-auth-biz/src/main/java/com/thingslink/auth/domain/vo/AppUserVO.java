package com.thingslink.auth.domain.vo;


import com.thingslink.common.core.annotation.Sensitive;
import com.thingslink.common.core.annotation.impl.SensitiveStrategy;
import com.thingslink.common.security.oauth2.model.LoginAppUser;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 登录用户信息VO
 * @date : 2024/4/13 14:13
 */
@Data
@Accessors(chain = true)
@AutoMapper(target = LoginAppUser.class)
public class AppUserVO {

    @Schema(description = "登录id")
    private Long userId;

    @Sensitive(strategy = SensitiveStrategy.PHONE)
    @Schema(description = "手机号密文")
    private String phoneNumber;
}