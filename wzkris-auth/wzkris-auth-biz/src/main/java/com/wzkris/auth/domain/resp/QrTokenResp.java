package com.wzkris.auth.domain.resp;

import com.wzkris.auth.enums.QrCodeStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QrTokenResp {

    @Schema(description = "状态值")
    private String status;

    private String accessToken;

    private String refreshToken;

    private QrTokenResp(String status) {
        this.status = status;
    }

    public static QrTokenResp OVERDUE() {
        return new QrTokenResp(QrCodeStatus.OVERDUE.getValue());
    }

}
