package com.wzkris.common.validator;

import com.wzkris.common.core.utils.JsonUtil;
import com.wzkris.common.validator.domain.TestModel;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.HibernateValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Properties;
import java.util.Set;

@Slf4j
@DisplayName("参数验证器-集成测试")
public class ValidatorIntegrationTest {

    private final TestModel model;

    private final Validator validator;

    public ValidatorIntegrationTest() {
        this.model = new TestModel();
        model.setIdcard("33032619311001304");
        model.setPhonenumber("1783610000");
        model.setSsAddr("浙江省丽水市中央大道001号");
        model.setSsEmail("test@gmail.com");
        model.setSsPhone("17836100000");
        model.setSsBankcard("36494943131313215615");
        try (LocalValidatorFactoryBean factoryBean = new LocalValidatorFactoryBean()) {
            // 设置使用 HibernateValidator 校验器
            factoryBean.setProviderClass(HibernateValidator.class);
            Properties properties = new Properties();
            // 设置 全部异常校验
            properties.setProperty("hibernate.validator.fail_fast", "false");
            factoryBean.setValidationProperties(properties);
            // 加载配置
            factoryBean.afterPropertiesSet();
            validator = factoryBean.getValidator();
        }
    }

    @Test
    public void test() {
        String ssString = JsonUtil.toJsonString(model);
        TestModel ssModel = JsonUtil.parseObject(ssString, TestModel.class);
        assert ssModel.getSsAddr().contains("***");
        assert ssModel.getSsEmail().contains("***");
        assert ssModel.getSsPhone().contains("***");
        assert ssModel.getSsBankcard().contains("***");
        Set<ConstraintViolation<TestModel>> validate =
                validator.validate(model);
        assert validate.size() == 2;
    }

}
