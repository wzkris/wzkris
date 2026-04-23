package com.wzkris.captcha.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageCaptchaInfo {

    private String code;

    private OffsetDateTime expires;

}
