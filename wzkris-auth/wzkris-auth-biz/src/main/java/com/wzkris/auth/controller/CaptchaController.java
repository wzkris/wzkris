package com.wzkris.auth.controller;

import cloud.tianai.captcha.application.ImageCaptchaApplication;
import cloud.tianai.captcha.application.vo.CaptchaResponse;
import cloud.tianai.captcha.application.vo.ImageCaptchaVO;
import cloud.tianai.captcha.common.response.ApiResponse;
import cn.hutool.core.util.RandomUtil;
import com.wzkris.auth.domain.req.SmsCodeReq;
import com.wzkris.common.captcha.model.CheckCaptchaReq;
import com.wzkris.common.captcha.service.CaptchaService;
import com.wzkris.common.core.domain.Result;
import com.wzkris.common.redis.annotation.RateLimit;
import com.wzkris.common.web.model.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 验证码服务
 * @date : 2023/4/17 14:42
 */
@Tag(name = "验证码")
@Slf4j
@Validated
@RestController
@RequestMapping("/captcha")
@RequiredArgsConstructor
public class CaptchaController extends BaseController {

    private final CaptchaService captchaService;

    private final ImageCaptchaApplication application;

    @RateLimit
    @Operation(summary = "生成验证码")
    @PostMapping("/generate")
    public CaptchaResponse<ImageCaptchaVO> genCaptcha(@RequestParam(required = false) String type) {
        // 参数1为具体的验证码类型， 默认支持 SLIDER、ROTATE、WORD_IMAGE_CLICK、CONCAT 等验证码类型，详见： `CaptchaTypeConstant`类
        return application.generateCaptcha(type);
    }

    @Operation(summary = "校验验证码")
    @PostMapping("/check")
    public ApiResponse<?> checkCaptcha(@RequestBody CheckCaptchaReq req) {
        ApiResponse<?> response = application.matching(req.getId(), req.getData());
        return response.isSuccess() ? ApiResponse.ofSuccess(req.getId()) : response;
    }

    @Operation(summary = "短信验证码")
    @PostMapping("/sms_code")
    public Result<Integer> sendSms(@RequestBody @Valid SmsCodeReq req) {
        boolean valid = ((SecondaryVerificationApplication) application).secondaryVerification(req.getCaptchaId());
        if (!valid) {
            return err412("验证码异常");
        }
        captchaService.validateMaxTry(req.getPhone(), 1, 120);
        // TODO 发送短信
        String code = RandomUtil.randomNumbers(6);
        log.info("手机号：{} 验证码是：{}", req.getPhone(), code);
        captchaService.setCaptcha(req.getPhone(), code);
        return ok();
    }

}
