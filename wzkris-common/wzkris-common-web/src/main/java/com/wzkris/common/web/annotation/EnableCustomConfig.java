package com.wzkris.common.web.annotation;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
// 开启定时
@EnableScheduling
// 表示通过aop框架暴露该代理对象,AopContext能够访问
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
// 包扫描路径
@ComponentScan(basePackages = "com.wzkris.*")
// 自动加载类
//@Import
public @interface EnableCustomConfig {

}
