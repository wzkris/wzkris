package com.wzkris.captcha.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlideCaptchaInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer targetX;

    private Date expires;

}
