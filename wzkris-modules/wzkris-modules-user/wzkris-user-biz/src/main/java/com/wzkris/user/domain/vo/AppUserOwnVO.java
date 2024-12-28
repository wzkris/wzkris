package com.wzkris.user.domain.vo;


import com.wzkris.common.core.annotation.Sensitive;
import com.wzkris.common.core.annotation.impl.SensitiveStrategy;
import com.wzkris.common.security.oauth2.domain.model.ClientUser;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author : wzkris
 * @version : V1.0.1
 * @description : 登录用户信息VO
 * @date : 2024/4/13 14:13
 */
@Data
@AutoMapper(target = ClientUser.class)
public class AppUserOwnVO {

    @Schema(description = "头像")
    private String avatar;

    @Sensitive(strategy = SensitiveStrategy.PHONE)
    @Schema(description = "手机号密文")
    private String phoneNumber;
}
