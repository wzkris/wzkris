package com.wzkris.auth.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class KaptchaDTO {
    @Schema(description = "是否启用")
    private boolean captchaEnabled;

    @Schema(description = "唯一id")
    private String uuid;

    @Schema(description = "图片base64")
    private String img;

    public KaptchaDTO(boolean captchaEnabled) {
        this.captchaEnabled = captchaEnabled;
    }

}
