package com.wzkris.common.web.annotation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
// 表示通过aop框架暴露该代理对象,AopContext能够访问
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
// 指定要扫描的Mapper类的包的路径
@MapperScan("com.wzkris.*.mapper")
// 开启openfeign
@EnableFeignClients(basePackages = "com.wzkris.*.api")
// 包扫描路径
@ComponentScan(basePackages = "com.wzkris.*")
// 自动加载类
//@Import
public @interface EnableCustomConfig {
}
