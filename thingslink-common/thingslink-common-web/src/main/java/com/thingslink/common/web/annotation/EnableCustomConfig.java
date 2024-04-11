package com.thingslink.common.web.annotation;

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
@MapperScan("com.thingslink.*.mapper")
// 开启openfeign
@EnableFeignClients(basePackages = "com.thingslink.*.api")
// 包扫描路径
@ComponentScan(basePackages = "com.thingslink.*")
// 自动加载类
//@Import
public @interface EnableCustomConfig {
}
