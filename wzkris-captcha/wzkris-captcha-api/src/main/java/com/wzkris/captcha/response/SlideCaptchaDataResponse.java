package com.wzkris.captcha.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlideCaptchaDataResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String token;

    private String backgroundImage;

    private String sliderImage;

    private Date expires;

}
