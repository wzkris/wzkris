package com.wzkris.captcha.api.captcha.response;

import com.wzkris.captcha.enums.CaptchaTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 统一派发响应：{@code phase} 区分签发/换证，{@code data} 为各形态原有返回体（与旧接口一致，便于前端适配）。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaDispatchResponse implements Serializable {

    public static final String PHASE_ISSUE = "issue";

    public static final String PHASE_REDEEM = "redeem";

    @Serial
    private static final long serialVersionUID = 1L;

    private CaptchaTypeEnum type;

    private String phase;

    private Object data;

    public static CaptchaDispatchResponse of(CaptchaTypeEnum type, String phase, Object data) {
        return new CaptchaDispatchResponse(type, phase, data);
    }

}
