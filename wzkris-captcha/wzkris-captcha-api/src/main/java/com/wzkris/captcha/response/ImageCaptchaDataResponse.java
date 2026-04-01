package com.wzkris.captcha.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageCaptchaDataResponse {

    private String token;

    private String image;

    private Date expires;

}
