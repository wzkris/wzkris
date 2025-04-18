package com.wzkris.common.captcha.model;

import cloud.tianai.captcha.validator.common.model.dto.ImageCaptchaTrack;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 校验验证码参数
 */
@Data
public class CheckCaptchaReq {

    private String id;

    private CustomImageCaptchaTrack data;

    @Data
    static class CustomImageCaptchaTrack extends ImageCaptchaTrack {

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private Date startTime;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private Date stopTime;
    }
}
