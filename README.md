<div align="center">

# wzkris-cloud

</div>

## 📖 平台简介

**wzkris-cloud** 是一个基于 **Spring Boot 3** + **JDK 17** 的企业级多租户快速开发平台，
内嵌了一些通用的平台功能

### 系统核心功能

- **用户系统**：支持管理员和移动端用户多端管理
- **认证系统**：基于**Spring-Authorization-Server**实现了OAuth2协议认证授权
- **权限控制**：基于角色的多维度权限管理体系
- **多租户支持**：租户之间完全隔离，管理员可以切换租户
- **日志监控**：完整的操作日志和登录日志记录
- **Excel处理**：基于EasyExcel实现高效的Excel操作
- **接口文档**：使用Swagger自动生成API文档
- **数据脱敏**：敏感信息自动脱敏处理
- **XSS防护**：内置跨站脚本攻击防护
- **定时任务**：xxl-job

## 🏗️ 项目结构

~~~
wzkris     
├── wzkris-auth            // OAuth2认证服务
├── wzkris-bom             // 依赖清单
├── wzkris-common          // 模块控制
│       └── wzkris-common-captcha                      // 验证码模块
│       └── wzkris-common-core                         // 核心模块(工具包)
│       └── wzkris-common-datasource                   // 多数据源
│       └── wzkris-common-excel                        // excel处理
│       └── wzkris-common-loadbanlancer                // rpc均衡负载
│       └── wzkris-common-log                          // 日志记录
│       └── wzkris-common-notifier                     // 通知SDK集成
│       └── wzkris-common-openfeign                    // Rpc调用
│       └── wzkris-common-orm                          // 持久层框架, 租户、分页集成
│       └── wzkris-common-oss                          // 对象存储SDK集成
│       └── wzkris-common-redis                        // 分布式缓存, 轻量级限流
│       └── wzkris-common-seata                        // 分布式事务
│       └── wzkris-common-security                     // 安全模块 OAuth2.1
│       └── wzkris-common-sentinel                     // 限流模块
│       └── wzkris-common-statemachine                 // spring状态机
│       └── wzkris-common-stream                       // spring-stream
│       └── wzkris-common-swagger                      // openAPI3规范
│       └── wzkris-common-thread                       // 动态线程池
│       └── wzkris-common-validator                    // 集成hibernate-validator
│       └── wzkris-common-web                          // web服务依赖
│       └── wzkris-common-weixin-sdk                   // 微信接口支持
├── wzkris-extends         // 扩展模块
│       └── wzkris-monitor-admin                               // 监控中心 
├── wzkris-gateway         // 网关模块 
├── wzkris-modules         // 业务模块
│       └── wzkris-modules-system                              // 系统服务 
│       └── wzkris-modules-user                                // 用户服务 
├──pom.xml                 // 依赖管理
~~~

## 🎯 技术架构

| 分类          | 技术                          | 版本     | 说明           |
|-------------|-----------------------------|--------|--------------|
| **基础框架**    | Spring Boot                 | 3.x.x  | 核心框架         |
| **开发环境**    | JDK                         | 17     | Java开发环境     |
| **Web容器**   | Tomcat                      | -      | 常用Web容器      |
| **权限认证**    | Spring-Authorization-Server | 1.4.2  | Spring官方权限框架 |
| **ORM框架**   | MyBatis-Plus                | 3.5.11 | 持久层框架        |
| **缓存中间件**   | Redisson                    | 3.44.0 | Redis客户端     |
| **数据库连接池**  | HikariCP                    | -      | 高性能连接池       |
| **工具类库**    | Apache-commons              | -      | Apache工具包    |
| **API文档**   | Swagger                     | -      | OpenAPI 3.0  |
| **Excel处理** | EasyExcel                   | -      | 高性能Excel     |
| **任务调度**    | XxlJob                      | -      | 分布式任务调度      |
| **对象映射**    | MapStruct Plus              | -      | 对象转换         |
| **监控管理**    | Spring Boot Admin           | -      | 应用监控         |

## 演示图

|                                                                                            |                                                                                            |
|--------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------|
| ![输入图片说明](https://foruda.gitee.com/images/1744348068350490903/f3deccdb_8354566.png "屏幕截图") | ![输入图片说明](https://foruda.gitee.com/images/1756798438238613539/4086da84_8354566.png "屏幕截图") || ![输入图片说明](https://foruda.gitee.com/images/1756798505818840216/68121aea_8354566.png "屏幕截图") | ![输入图片说明](https://foruda.gitee.com/images/1756798657905378641/f1404d51_8354566.png "屏幕截图") |
| ![输入图片说明](https://foruda.gitee.com/images/1744348256674700279/bd33def1_8354566.png "屏幕截图") | ![输入图片说明](https://foruda.gitee.com/images/1744348272532367001/deb6c1a9_8354566.png "屏幕截图") || ![输入图片说明](https://foruda.gitee.com/images/1744348353122059293/04cdb889_8354566.png "屏幕截图") | ![输入图片说明](https://foruda.gitee.com/images/1744348368296240184/6a796b0f_8354566.png "屏幕截图") |
| ![输入图片说明](https://foruda.gitee.com/images/1744348497996669620/f6215b22_8354566.png "屏幕截图") | ![输入图片说明](https://foruda.gitee.com/images/1744348509244436175/2ecfaaa2_8354566.png "屏幕截图") || ![输入图片说明](https://foruda.gitee.com/images/1744348637638135915/6c4fec2c_8354566.png "屏幕截图") | ![输入图片说明](https://foruda.gitee.com/images/1744348648401632011/eb152dcd_8354566.png "屏幕截图") |
| ![输入图片说明](https://foruda.gitee.com/images/1744348673892216990/052251b5_8354566.png "屏幕截图") | ![输入图片说明](https://foruda.gitee.com/images/1744348688840182604/2d8c05cc_8354566.png "屏幕截图") ||
|                                                                                            |

## 前端项目地址

https://gitee.com/wzkris/wzkris-ui-vben