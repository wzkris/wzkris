package com.wzkris.common.captcha.config;

import cloud.tianai.captcha.common.constant.CaptchaTypeConstant;
import cloud.tianai.captcha.resource.ResourceStore;
import cloud.tianai.captcha.resource.common.model.dto.Resource;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 验证码配置
 *
 * @author wzkris
 */
@Data
@RefreshScope
public class CaptchaConfig {

    @Autowired
    private ResourceStore resourceStore;

    @PostConstruct
    public void init() {
        // 2. 添加自定义背景图片
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "static/a.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "static/b.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "static/c.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "static/d.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "static/e.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "static/g.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "static/h.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "static/i.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.SLIDER, new Resource("classpath", "static/j.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.ROTATE, new Resource("classpath", "static/48.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.CONCAT, new Resource("classpath", "static/48.jpg", "default"));
        resourceStore.addResource(CaptchaTypeConstant.WORD_IMAGE_CLICK, new Resource("classpath", "static/c.jpg", "default"));
    }

}
