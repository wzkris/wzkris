<div align="center">

# wzkris-cloud

![version](https://img.shields.io/badge/version-4.1.0-blue.svg)
![JDK](https://img.shields.io/badge/JDK-17+-green.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen.svg)

</div>

## 📖 平台简介

**wzkris-cloud** 是一个基于 **Spring Boot 3** + **JDK 17** 的企业级多租户微服务快速开发平台，
内嵌了丰富的通用功能模块，旨在为企业应用开发提供高效、安全、可扩展的基础架构。

### 系统核心功能

- **多端用户系统**：支持管理员、租户管理员和移动端用户的多端管理
- **OAuth2.1认证系统**：基于**Spring-Authorization-Server**实现完整的OAuth2.1协议认证授权
- **多维度权限控制**：基于角色(RBAC)的精细化权限管理体系，支持菜单、按钮、数据权限
- **完整多租户架构**：租户之间数据完全隔离，支持租户切换和租户管理
- **全面日志监控**：操作日志、登录日志、审计日志的完整记录和查询
- **高效Excel处理**：基于EasyExcel实现的高性能Excel导入导出功能
- **专业接口文档**：集成Knife4j实现OpenAPI 3.0规范的自动API文档生成
- **安全数据保护**：敏感信息自动脱敏处理、XSS防护、SQL注入防护
- **分布式任务调度**：集成xxl-job实现可靠的定时任务管理
- **多种登录方式**：支持账号密码、短信验证码、微信等多种登录方式
- **动态代码执行**：支持动态编译和执行Java代码
- **微服务治理**：基于Spring Cloud和Spring Cloud Alibaba的完整微服务解决方案

## 🏗️ 项目结构

```
wzkris     
├── codestyle              // 代码风格配置文件
├── sql                    // 数据库脚本(MySQL/PostgreSQL)
├── wzkris-auth            // OAuth2认证授权服务
│   ├── wzkris-auth-biz    // 认证业务实现
│   └── wzkris-auth-rmi    // 认证远程接口
├── wzkris-bom             // 依赖版本管理
├── wzkris-common          // 公共模块集合
│   ├── wzkris-common-apikey        // API密钥管理
│   ├── wzkris-common-captcha       // 验证码模块
│   ├── wzkris-common-core          // 核心工具包
│   ├── wzkris-common-excel         // Excel处理
│   ├── wzkris-common-loadbalancer  // 负载均衡
│   ├── wzkris-common-log           // 日志记录
│   ├── wzkris-common-notifier      // 通知SDK集成
│   ├── wzkris-common-openfeign     // 远程调用
│   ├── wzkris-common-orm           // 持久层框架
│   ├── wzkris-common-redis         // Redis缓存
│   ├── wzkris-common-seata         // 分布式事务
│   ├── wzkris-common-security      // 安全模块
│   ├── wzkris-common-sentinel      // 限流熔断
│   ├── wzkris-common-statemachine  // 状态机
│   ├── wzkris-common-swagger       // API文档
│   ├── wzkris-common-thread        // 线程池
│   ├── wzkris-common-validator     // 数据校验
│   ├── wzkris-common-web           // Web基础
│   └── wzkris-common-weixin-sdk    // 微信SDK
├── wzkris-demo            // 示例模块
│   └── wzkris-mq-demo     // 消息队列示例
├── wzkris-extends         // 扩展模块
│   └── wzkris-monitor-admin  // 监控中心
├── wzkris-gateway         // API网关
├── wzkris-modules         // 业务功能模块
│   ├── wzkris-modules-message     // 消息服务
│   └── wzkris-modules-principal   // 主体信息服务
└── pom.xml                // 项目依赖管理
```

## 🌟 项目特点

- **模块化架构**：高度模块化的设计，便于扩展和维护
- **微服务支持**：完整的微服务架构解决方案
- **多租户隔离**：数据、功能、配置全方位的租户隔离
- **安全可靠**：完善的认证授权、数据脱敏、防XSS/SQL注入
- **开发友好**：丰富的工具类和通用组件，提高开发效率
- **性能优化**：缓存策略、异步处理、连接池优化
- **运维便捷**：完善的监控、日志、配置管理

## 🚀 快速开始

### 环境要求

| 环境/工具 | 版本要求 | 说明 | 默认端口 |
|---------|---------|------|---------|
| JDK | 17+ | Java开发环境 | - |
| Maven | 3.6+ | 项目构建工具 | - |
| PostgreSQL | 12+ | 项目主数据库 | 5432 |
| MySQL | 8.0+ | Nacos配置中心数据库 | 3306 |
| Redis | 6.0+ | 缓存和会话管理 | 6379 |
| Nacos | 2.2.0+ | 服务注册发现与配置管理 | 8848 |

### 部署步骤

1. **准备数据库**
   
   **PostgreSQL数据库（主数据库）**
   - 创建PostgreSQL数据库（推荐版本 12+）
   - 执行 `sql/postgresql/wzkris_principal.sql` 初始化主体服务数据库
   - 执行 `sql/postgresql/wzkris_message.sql` 初始化消息服务数据库
   
   **MySQL数据库（Nacos配置中心）**
   - 创建MySQL数据库（推荐版本 8.0+）
   - 执行 `sql/mysql/nacos_config.sql` 初始化Nacos配置中心数据库

2. **配置Nacos**
   - 启动Nacos服务（默认端口 8848）
   - 配置Nacos使用MySQL数据源（指向步骤1创建的nacos_config数据库）
   - 在Nacos控制台导入相关服务配置信息

3. **配置Redis**
   - 启动Redis服务（默认端口 6379）
   - 确保Redis服务可访问，用于缓存和会话管理

4. **构建项目**
   ```bash
   # 编译打包（跳过测试）
   mvn clean package -DskipTests
   ```

5. **启动服务**
   - 按顺序启动各个服务：
     1. Nacos（服务注册与配置中心）
     2. Gateway (8080) - API网关服务
     3. Auth (9000) - 认证授权服务
     4. Principal (8000) - 主体信息服务
     5. Message (5555) - 消息服务
     6. Monitor (9100) - 监控中心（可选）

### 服务端口说明

| 服务名称 | 端口 | 说明 |
|---------|------|------|
| Gateway | 8080 | API网关服务 |
| Auth | 9000 | 认证授权服务 |
| Principal | 8000 | 主体信息服务 |
| Message | 5555 | 消息服务 |
| Monitor | 9100 | 监控中心 |

### 访问方式

- **API网关**: http://localhost:8080
- **API文档**: http://localhost:8080/doc.html (通过网关聚合所有服务的API文档)
- **监控中心**: http://localhost:9100

## 🎯 技术架构

| 分类          | 技术                          | 版本        | 说明                    |
|-------------|-----------------------------|-----------|-----------------------|
| **基础框架**    | Spring Boot                 | 3.4.4     | 核心框架                  |
| **微服务框架**   | Spring Cloud                | 2024.0.1  | 微服务框架                |
| **微服务生态**   | Spring Cloud Alibaba        | 2023.0.3.2 | 微服务生态组件              |
| **开发环境**    | JDK                         | 17        | Java开发环境              |
| **Web容器**   | Tomcat                      | -         | 内嵌Web容器               |
| **API网关**    | Spring Cloud Gateway        | -         | 统一网关入口               |
| **服务注册**    | Nacos                       | 2.2.0+    | 服务注册发现与配置管理          |
| **权限认证**    | Spring-Authorization-Server | -         | OAuth2.1认证框架           |
| **API文档**   | Knife4j                     | 4.5.0     | OpenAPI 3.0文档工具        |
| **对象映射**    | MapStruct Plus              | 1.4.6     | 对象转换增强框架             |
| **工具库**     | Lombok                      | 1.18.28   | 代码简化工具               |
| **网络框架**    | Netty                       | 4.1.110.Final | 高性能网络应用框架         |
| **主数据库**   | PostgreSQL                  | 12+       | 项目主关系型数据库            |
| **配置数据库**  | MySQL                       | 8.0+      | 仅用于Nacos配置中心         |
| **缓存中间件**   | Redis/Redisson              | -         | 分布式缓存                 |
| **任务调度**    | XxlJob                      | 3.2.0     | 分布式任务调度平台            |
| **Excel处理** | EasyExcel                   | 4.0.2     | 高性能Excel处理            |
| **分布式事务**   | Seata                       | -         | 分布式事务框架（Spring Cloud Alibaba集成）|
| **限流熔断**    | Sentinel                    | -         | 流量控制与熔断降级（Spring Cloud Alibaba集成）|
| **监控管理**    | Spring Boot Admin           | -         | 应用监控管理               |
| **远程调用**    | OpenFeign                   | -         | 服务间HTTP调用             |
| **代码质量**    | Checkstyle                  | 3.6.0     | 代码风格检查                |
| **代码格式化**   | Spotless                    | 2.44.4    | 代码自动格式化               |

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

## 📄 许可证

本项目采用 [zlib License](https://opensource.org/licenses/Zlib) 许可证。

## 🤝 贡献指南

欢迎提交Issue和Pull Request！

1. Fork本项目
2. 创建特性分支 (`git checkout -b feature/amazing-feature`)
3. 提交更改 (`git commit -m 'Add some amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 开启Pull Request

## 📞 联系方式

如有问题或建议，请通过以下方式联系我们：

- 项目地址：https://gitee.com/wzkris/wzkris
- 前端项目：https://gitee.com/wzkris/wzkris-ui-vben
- 微信小程序：https://gitee.com/wzkris/wzkris-mini.git