package com.wzkris.captcha.image.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageCaptchaData {

    private String token;

    private String image;

    private Date expires;

}
