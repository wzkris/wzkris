package com.wzkris.auth.domain.req;

import com.wzkris.auth.enums.QrCodeStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QrCodeScanReq {

    @Schema(description = "二维码ID")
    @NotBlank(message = "{invalidParameter.param.invalid}")
    private String qrcodeId;

    @Schema(description = "扫码状态")
    @NotNull(message = "{invalidParameter.param.invalid}")
    private QrCodeStatus status;

}

