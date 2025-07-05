package com.wzkris.common.orm.config;

import com.wzkris.common.core.utils.StringUtil;
import com.wzkris.common.orm.annotation.DataScope;
import com.wzkris.common.orm.utils.DataScopeUtil;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;

/**
 * 启动时扫描所有mapper是否标记注解
 * 将mappedStatementId作为key存起来
 *
 * @author wzkris
 */
public class DataScopeConfig implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof MapperFactoryBean) {
            Class<?> mapperInterface = ((MapperFactoryBean<?>) bean).getMapperInterface();
            scanMapperAnnotations(mapperInterface);
        }
        return bean;
    }

    private void scanMapperAnnotations(Class<?> mapperInterface) {
        // 检查接口注解
        boolean interfacePresent = mapperInterface.isAnnotationPresent(DataScope.class);
        DataScope dataScope = mapperInterface.getAnnotation(DataScope.class);

        // 检查方法注解
        for (Method method : mapperInterface.getMethods()) {
            if (method.isAnnotationPresent(DataScope.class)) {
                DataScopeUtil.setDataScope(getMethodName(mapperInterface, method), method.getAnnotation(DataScope.class));
            } else {
                if (interfacePresent) {
                    DataScopeUtil.setDataScope(getMethodName(mapperInterface, method), dataScope);
                }
            }
        }
    }

    private String getMethodName(Class<?> mapperInterface, Method method) {
        return mapperInterface.getName() + StringUtil.DOT + method.getName();
    }

}
