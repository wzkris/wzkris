package com.wzkris.auth.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class KaptchaVO {

    @Schema(description = "是否启用")
    private boolean captchaEnabled;

    @Schema(description = "唯一id")
    private String uuid;

    @Schema(description = "图片base64")
    private String img;

    @Schema(description = "过期时间")
    private int expired;

    public KaptchaVO(boolean captchaEnabled) {
        this.captchaEnabled = captchaEnabled;
    }

}
