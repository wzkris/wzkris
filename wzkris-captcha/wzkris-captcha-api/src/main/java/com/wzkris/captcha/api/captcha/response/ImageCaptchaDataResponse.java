package com.wzkris.captcha.api.captcha.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageCaptchaDataResponse {

    private String token;

    private String image;

    private OffsetDateTime expires;

}
