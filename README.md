<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">wzkris-cloud</h1>
<h4 align="center">基于JDK17的SpringCloudAlibaba + Spring-Authorization-Server的微服务框架，多租户架构</h4>

~~~
wzkris     
├── wzkris-auth            // OAuth2认证服务
├── wzkris-bom             // 依赖清单
├── wzkris-common          // 模块控制
│       └── wzkris-common-captcha                      // 验证码模块
│       └── wzkris-common-core                         // 核心模块(工具包)
│       └── wzkris-common-datasource                   // 多数据源
│       └── wzkris-common-dubbo                        // Rpc调用
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
│       └── wzkris-xxl-job-admin                               // xxl-job控制台 
├── wzkris-gateway         // 网关模块 
├── wzkris-modules         // 业务模块
│       └── wzkris-modules-system                              // 系统服务 
│       └── wzkris-modules-user                                // 用户服务 
├──pom.xml                 // 依赖管理
~~~

## 内置功能

1. 租户管理：租户信息，租户套餐，过期时间，租户钱包。
3. 系统用户：租户下的登录账户信息（管理员属于特殊租户）。
4. 终端管理：OAuth2客户端。
5. 部门管理：配置部门，树结构展现支持数据权限。
6. 岗位管理：配置系统用户所属担任职务。
7. 菜单管理：配置系统菜单，操作权限，按钮权限标识，跳转链接等。
8. 角色管理：角色菜单权限分配、设置角色按部门进行数据范围权限划分。
9. 字典管理：对系统中经常使用的一些较为固定的数据进行维护。
2. 顾客管理：APP端用户信息管理。
10. 参数管理：对系统动态配置常用参数。
11. 系统消息：系统通知公告信息发布维护。
12. 操作日志：系统正常操作日志记录和查询；系统异常信息日志记录和查询。
13. 登录日志：系统登录日志记录查询。
14. 定时任务：xxl-job。
15. 系统接口：根据业务代码自动生成相关的api接口文档。

## 技术架构

- JDK版本：`17+`
- 后端框架：`SpringCloud + SringCloudAlibaba + SpringBoot3`
- 安全框架：`Spring-Authorization-Server`
- 持久层框架：`Mybatis-Plus`
- RPC框架：`dubbo3/openfeign`
- 定时任务：`xxl-job`
- 中间件：`Nacos` `PostgresQL` `Redis`（至少需要这三个组件项目才可以运行）
- 消息队列： 根据需要自行搭配`spring-cloud-stream`

## 演示图



|                                                                                            |                                                                                            |
|--------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------|
| ![输入图片说明](https://foruda.gitee.com/images/1744348068350490903/f3deccdb_8354566.png "屏幕截图")           | ![输入图片说明](https://foruda.gitee.com/images/1744348068417169323/b751865b_8354566.png "屏幕截图") |
| ![输入图片说明](https://foruda.gitee.com/images/1744348256674700279/bd33def1_8354566.png "屏幕截图") | ![输入图片说明](https://foruda.gitee.com/images/1744348272532367001/deb6c1a9_8354566.png "屏幕截图") |
| ![输入图片说明](https://foruda.gitee.com/images/1744348353122059293/04cdb889_8354566.png "屏幕截图") | ![输入图片说明](https://foruda.gitee.com/images/1744348368296240184/6a796b0f_8354566.png "屏幕截图") |
| ![输入图片说明](https://foruda.gitee.com/images/1744348497996669620/f6215b22_8354566.png "屏幕截图") | ![输入图片说明](https://foruda.gitee.com/images/1744348509244436175/2ecfaaa2_8354566.png "屏幕截图") |
| ![输入图片说明](https://foruda.gitee.com/images/1744348637638135915/6c4fec2c_8354566.png "屏幕截图") | ![输入图片说明](https://foruda.gitee.com/images/1744348648401632011/eb152dcd_8354566.png "屏幕截图") |
| ![输入图片说明](https://foruda.gitee.com/images/1744348673892216990/052251b5_8354566.png "屏幕截图") | ![输入图片说明](https://foruda.gitee.com/images/1744348688840182604/2d8c05cc_8354566.png "屏幕截图") |
|  |  |
|  |  |