package com.wzkris.common.web.config;

import jakarta.validation.Validator;
import java.util.Properties;
import org.hibernate.validator.HibernateValidator;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * 校验框架配置类
 *
 * @author Lion Li
 */
public class ValidatorConfig {

    private final MessageSource messageSource;

    public ValidatorConfig(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * 配置校验框架 快速返回模式
     */
    @Bean
    public Validator validator() {
        try (LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean()) {
            // 国际化
            factoryBean.setValidationMessageSource(messageSource);
            // 设置使用 HibernateValidator 校验器
            factoryBean.setProviderClass(HibernateValidator.class);
            Properties properties = new Properties();
            // 设置 快速异常返回
            properties.setProperty("hibernate.validator.fail_fast", "true");
            factoryBean.setValidationProperties(properties);
            // 加载配置
            factoryBean.afterPropertiesSet();
            return factoryBean.getValidator();
        }
    }
}
