package com.wzkris.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "二维码参数")
public class QrCodeIdRequest {

    @Schema(description = "二维码ID")
    @NotBlank(message = "{invalidParameter.param.invalid}")
    private String qrcodeId;

}
