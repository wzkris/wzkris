package com.wzkris.auth.domain.vo;

import com.wzkris.auth.enums.QrCodeStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QrTokenVO {

    @Schema(description = "状态值")
    private String status;

    private String accessToken;

    private String refreshToken;

    private QrTokenVO(String status) {
        this.status = status;
    }

    public static QrTokenVO OVERDUE() {
        return new QrTokenVO(QrCodeStatusEnum.OVERDUE.getValue());
    }

}
