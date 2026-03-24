package com.wzkris.captcha.httpclientimpl.image;

import com.wzkris.captcha.httpclientimpl.image.req.ImageCaptchaReq;
import com.wzkris.captcha.image.service.ImageCaptchaService;
import com.wzkris.common.core.model.Result;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@Validated
@RestController
@RequestMapping("/captcha-image-client")
@RequiredArgsConstructor
public class CaptchaImageClientImpl {

    private final ImageCaptchaService imageCaptchaService;

    @PostMapping("/validate")
    public Result<Boolean> validateImage(ImageCaptchaReq imageCaptchaReq) {
        Boolean b = imageCaptchaService.validateToken(imageCaptchaReq.getToken());
        return Result.ok(b);
    }

}
