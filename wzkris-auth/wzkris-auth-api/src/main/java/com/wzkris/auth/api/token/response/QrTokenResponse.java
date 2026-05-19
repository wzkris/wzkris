package com.wzkris.auth.api.token.response;

import com.wzkris.auth.enums.QrCodeStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QrTokenResponse {

    @Schema(description = "状态值")
    private String status;

    private String accessToken;

    private String refreshToken;

    private QrTokenResponse(String status) {
        this.status = status;
    }

    public static QrTokenResponse OVERDUE() {
        return new QrTokenResponse(QrCodeStatusEnum.OVERDUE.getValue());
    }

}
