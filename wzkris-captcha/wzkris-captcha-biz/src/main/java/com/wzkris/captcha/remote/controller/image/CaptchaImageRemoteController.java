package com.wzkris.captcha.remote.controller.image;

import com.wzkris.captcha.remote.controller.image.request.ImageCaptchaRequest;
import com.wzkris.captcha.service.ImageCaptchaService;
import com.wzkris.common.core.model.Result;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@Validated
@RestController
@RequestMapping("/captcha-image-remote")
@RequiredArgsConstructor
public class CaptchaImageRemoteController {

    private final ImageCaptchaService imageCaptchaService;

    @PostMapping("/validate")
    public Result<Boolean> validateImage(@RequestBody ImageCaptchaRequest request) {
        Boolean b = imageCaptchaService.validateToken(request.getToken());
        return Result.ok(b);
    }

}



