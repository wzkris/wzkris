package com.wzkris.captcha.request;

import com.wzkris.captcha.enums.CaptchaTypeEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * 验证码统一请求体：{@code POST /captcha}，{@code type} 指定形态；{@code captcha} 仅在换证阶段携带且非空。
 * <p>
 * 签发：{@code {"type":"CHALLENGE"}}<br>
 * 换证：{@code {"type":"CHALLENGE","captcha":{"token":"...","solutions":[1,2]}}}（{@code type} 与 {@link com.wzkris.captcha.enums.CaptchaTypeEnum} 的 value 忽略大小写一致）
 */
@Data
public class CaptchaDispatchRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "invalidParameter.captcha.type")
    private CaptchaTypeEnum type;

    /**
     * 各形态换证参数，结构与原有 /redeem 请求体字段一致（见各 Captcha*Request）。
     */
    private Map<String, Object> captcha;

}
