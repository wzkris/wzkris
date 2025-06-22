/*
 Navicat Premium Dump SQL

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 50744 (5.7.44-log)
 Source Host           : localhost:3306
 Source Schema         : nacos_config

 Target Server Type    : MySQL
 Target Server Version : 50744 (5.7.44-log)
 File Encoding         : 65001

 Date: 19/06/2025 08:53:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS nacos_config default character set utf8mb4 collate utf8mb4_unicode_ci;
USE nacos_config;
-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '租户字段',
  `c_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `c_use` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `effect` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `c_schema` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `encrypted_data_key` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfo_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'config_info' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info` VALUES (1, 'application-common.yml', 'APPLICATION_GROUP', 'server:\n  # 优雅停机\n  shutdown: graceful\n\nspring:\n  pid:\n    # 将 PID 写入文件中\n    file: ./${spring.application.name}.pid\n  jackson:\n    time-zone: GMT+8\n    # 日期格式化\n    date-format: yyyy-MM-dd HH:mm:ss\n    serialization:\n      # 格式化输出\n      INDENT_OUTPUT: false\n      # 忽略空Bean转json的错误\n      FAIL_ON_EMPTY_BEANS: false\n      # 关闭日期转换成时间戳\n      WRITE_DATES_AS_TIMESTAMPS: false\n    # 设置空如何序列化\n    defaultPropertyInclusion: ALWAYS\n    deserialization:\n      #json中不存在的属性就报错\n      fail_on_unknown_properties: false\n    parser:\n      # 允许使用无引号字段\n      ALLOW_UNQUOTED_FIELD_NAMES: true\n      # 忽略未定义的属性\n      IGNORE_UNDEFINED: true\n      # 忽略json最后的逗号\n      ALLOW_TRAILING_COMMA: true\n      # 允许反斜杠\n      ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER: true\n      # 允许出现特殊字符和转义符\n      ALLOW_UNQUOTED_CONTROL_CHARS: true\n      # 允许出现单引号\n      ALLOW_SINGLE_QUOTES: true\n      # 是否允许使用注释\n      ALLOW_COMMENTS: true\n    mapper:\n      # 使用getter取代setter探测属性，如类中含getName()但不包含name属性与setName()，传输的vo json格式模板中依旧含name属性\n      USE_GETTERS_AS_SETTERS: true\n  mvc:\n    # mvc找不到对应处理器则抛出异常\n    throw-exception-if-no-handler-found: true\n    # 关闭DispatcherServlet懒加载\n    servlet:\n      load-on-startup: 0\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common\n\n#分布式事务\nseata:\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      ruoyi-system-group: default\n    # 分组和 Seata 服务的映射\n    grouplist:\n      default: 127.0.0.1:8091\n  config:\n    type: nacos\n    nacos:\n      serverAddr: ${spring.cloud.nacos.server-addr}\n      username: ${spring.cloud.nacos.username}\n      password: ${spring.cloud.nacos.password}\n      group: SEATA_GROUP\n      namespace: f06f7669-9836-4413-86cf-9f44ae5c9f30\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: ${spring.cloud.nacos.server-addr}\n      username: ${spring.cloud.nacos.username}\n      password: ${spring.cloud.nacos.password}\n      namespace: f06f7669-9836-4413-86cf-9f44ae5c9f30\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \"*\"\n\n# 默认安全配置\nsecurity:\n  ignores:\n    #swagger接口放行\n    - /v3/api-docs/**\n', '84e2cef7fd8f77184c77c6dd0410d0cc', '2023-06-19 02:28:00', '2024-07-03 05:44:56', 'nacos', '0:0:0:0:0:0:0:1', '', 'application-prod', '公共配置', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (2, 'wzkris-gateway.yml', 'APPLICATION_GROUP', 'spring:\n  cloud:\n    gateway:\n      httpclient:\n        pool:\n          type: FIXED\n          max-connections: 1000\n          acquire-timeout: 80000\n          max-life-time: 30000\n          max-idle-time: 5000\n          connect-timeout: 5000\n          response-timeout: 5000\n          max-request-size: 50MB\n      discovery:\n        locator:\n          lower-case-service-id: true\n          enabled: true\n      routes:\n        # 认证中心\n        - id: wzkris-auth\n          uri: lb://wzkris-auth\n          predicates:\n            - Path=/auth/**\n          filters:\n            - StripPrefix=1\n        # 用户服务\n        - id: wzkris-user\n          uri: lb://wzkris-user\n          predicates:\n            - Path=/ur_biz/**\n          filters:\n            - StripPrefix=1\n            # 黑名单过滤器\n            - name: BlackListUrlFilter\n              args:\n                blacklistUrl:\n                - /nacos/test\n        # 系统模块\n        - id: wzkris-system\n          uri: lb://wzkris-system\n          predicates:\n            - Path=/sys_biz/**\n          filters:\n            - StripPrefix=1\n        # 文件服务\n        - id: wzkris-file\n          uri: lb://wzkris-file\n          predicates:\n            - Path=/fl_biz/**\n          filters:\n            - StripPrefix=1\n        # 监控服务\n        - id: wzkris-monitor\n          uri: lb://wzkris-monitor\n          predicates:\n            - Path=/moniter/**\n          filters:\n            - StripPrefix=1\n\nknife4j:\n  # 聚合swagger文档\n  gateway:\n    enabled: true\n    strategy: discover\n    discover:\n      version: openapi3\n      enabled: true\n      service-config:\n        wzkris-auth:\n          - group-name: 认证服务\n            order: 1\n        wzkris-user:\n          - group-name: 用户服务\n            order: 1\n        wzkris-system:\n          - group-name: 系统服务\n            order: 2\n        wzkris-equipment:\n          - group-name: 设备服务\n            order: 4\n        wzkris-file:\n          - group-name: 文件服务\n            order: 5\n        wzkris-job:\n          - group-name: 定时任务服务\n            order: 6\n    tags-sorter: order\n    operations-sorter: order\n\n# 防止XSS攻击\nxss:\n  enabled: true\n  excludeUrls:\n    - /sys_biz/sys_message/add\n    - /sys_biz/sys_message/edit\n\n# 网关放行\nsecurity:\n  ignores:\n    # 验证码放行\n    - /auth/captcha/**\n    # 登录接口\n    - /auth/login\n    # 登出接口\n    - /auth/logout\n    # oauth2接口\n    - /auth/oauth2/**\n    # 回调接口\n    - /auth/authorization_code_callback\n    # 微信js接口\n    - /ur_biz/wx_req/js_ticket_sign\n    # 用户注册\n    - /ur_biz/app_register/*\n    # swagger接口放行\n    - /*/v3/api-docs/**\n    # 开放接口放行\n    - /*/open/**\n    # 上传图片可访问\n    - /fl_biz/uploadPath/**\n', '8fa111fcab73c4fd01c0a0f6c7b17c1f', '2023-06-19 02:28:00', '2024-05-30 01:20:02', 'nacos', '0:0:0:0:0:0:0:1', '', 'application-prod', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (3, 'wzkris-auth.yml', 'APPLICATION_GROUP', '# spring配置\nspring:\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common,i18n/auth\n  redis:\n    redisson: \n      config: |\n        # 单节点配置\n        singleServerConfig: \n          # redis 节点地址\n          address: \"redis://127.0.0.1:6379\"\n          # 无密码则设置 null\n          password: null\n          # 客户端名称\n          clientName: ${spring.application.name}\n          # 最小空闲连接数\n          connectionMinimumIdleSize: 32\n          # 连接池大小\n          connectionPoolSize: 64\n          # 连接空闲超时,单位:毫秒\n          idleConnectionTimeout: 10000\n          # 命令等待超时,单位:毫秒\n          timeout: 3000\n          # 发布和订阅连接池大小\n          subscriptionConnectionPoolSize: 50\n        # 线程池数量\n        threads: 2\n        # Netty线程池数量\n        nettyThreads: 8\n        codec: !<org.redisson.codec.JsonJacksonCodec> {}\n        transportMode: \"NIO\"\n\n# springdoc配置\nspringdoc:\n  title: 认证模块接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---\n\n# 安全配置\nsecurity:\n  # 不校验白名单\n  ignores:\n    #swagger接口放行\n    - /v3/api-docs/**\n    # 验证码放行\n    - /captcha/**\n    - /oauth2/authorization_code_callback\n', '911c4d2e78b883bafb0dfd3008bbff77', '2023-06-19 02:28:00', '2024-07-03 07:00:15', 'nacos', '0:0:0:0:0:0:0:1', '', 'application-prod', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (4, 'wzkris-user.yml', 'APPLICATION_GROUP', '# spring配置\nspring:\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common,i18n/user\n  # datasource:\n  #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver\n  #   url: jdbc:shardingsphere:classpath:sharding-${spring.profiles.active}.yml\n  datasource:\n    url: jdbc:postgresql://localhost:5432/wzkris_user?ssl=false&reWriteBatchedInserts=true&stringtype=unspecified\n    username: root\n    password: root\n    driver-class-name: org.postgresql.Driver\n    hikari:\n      connection-timeout: 30000 \n      maximum-pool-size: 10       \n      minimum-idle: 5             \n      idle-timeout: 600000        \n      pool-name: hikari-pool\n  redis:\n    redisson: \n      config: |\n        # 单节点配置\n        singleServerConfig: \n          # redis 节点地址\n          address: \"redis://127.0.0.1:6379\"\n          # 无密码则设置 null\n          password: null\n          # 客户端名称\n          clientName: ${spring.application.name}\n          # 最小空闲连接数\n          connectionMinimumIdleSize: 32\n          # 连接池大小\n          connectionPoolSize: 64\n          # 连接空闲超时,单位:毫秒\n          idleConnectionTimeout: 10000\n          # 命令等待超时,单位:毫秒\n          timeout: 3000\n          # 发布和订阅连接池大小\n          subscriptionConnectionPoolSize: 50\n        # 线程池数量\n        threads: 2\n        # Netty线程池数量\n        nettyThreads: 8\n        codec: !<org.redisson.codec.JsonJacksonCodec> {}\n        transportMode: \"NIO\"\n\nmybatis-plus:\n  # 搜索指定包别名\n  typeAliasesPackage: com.wzkris.user.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/user/*.xml\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n\n# springdoc配置\nspringdoc:\n  title: 用户模块接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---\n\n# 安全配置\nsecurity:\n  ignores:\n    #swagger接口放行\n    - /v3/api-docs/**\n    # js签名接口\n    - /wx_req/js_ticket_sign\n\n\n# 租户配置\ntenant:\n  includes:\n    - sys_dept\n    - sys_post\n    - sys_role\n    - sys_tenant\n    - sys_tenant_wallet\n    - sys_tenant_wallet_record\n    - sys_user', '6d17ee5a1a743387131d2965142a6a7a', '2024-04-16 01:03:03', '2024-06-03 08:50:19', 'nacos', '0:0:0:0:0:0:0:1', '', 'application-prod', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (5, 'wzkris-monitor-admin.yml', 'APPLICATION_GROUP', '# spring\nspring:\n  security:\n    user:\n      name: admin\n      password: admin123\n  boot:\n    admin:\n      ui:\n        title: 服务状态监控\n\noauth2-client:\n  url: http://localhost:9000/oauth2/token\n  clientId: server_monitor\n  clientSecret: secret\n  scopes:\n    - monitor', 'dd19c14e3cebc473140e1fc8733a339d', '2023-06-19 02:28:00', '2023-06-19 02:28:00', NULL, '0:0:0:0:0:0:0:1', '', 'application-prod', '', NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (6, 'wzkris-system.yml', 'APPLICATION_GROUP', '# spring配置\nspring:\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common,i18n/system\n  # datasource:\n  #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver\n  #   url: jdbc:shardingsphere:classpath:sharding-${spring.profiles.active}.yml\n  datasource:\n    url: jdbc:postgresql://localhost:5432/wzkris_system?ssl=false&reWriteBatchedInserts=true&stringtype=unspecified\n    username: root\n    password: root\n    driver-class-name: org.postgresql.Driver\n    hikari:\n      connection-timeout: 30000 \n      maximum-pool-size: 10       \n      minimum-idle: 5             \n      idle-timeout: 600000        \n      pool-name: hikari-pool\n  redis:\n    redisson: \n      config: |\n        # 单节点配置\n        singleServerConfig: \n          # redis 节点地址\n          address: \"redis://127.0.0.1:6379\"\n          # 无密码则设置 null\n          password: null\n          # 客户端名称\n          clientName: ${spring.application.name}\n          # 最小空闲连接数\n          connectionMinimumIdleSize: 32\n          # 连接池大小\n          connectionPoolSize: 64\n          # 连接空闲超时,单位:毫秒\n          idleConnectionTimeout: 10000\n          # 命令等待超时,单位:毫秒\n          timeout: 3000\n          # 发布和订阅连接池大小\n          subscriptionConnectionPoolSize: 50\n        # 线程池数量\n        threads: 2\n        # Netty线程池数量\n        nettyThreads: 8\n        codec: !<org.redisson.codec.JsonJacksonCodec> {}\n        transportMode: \"NIO\"\n\nmybatis-plus:\n  # 搜索指定包别名\n  typeAliasesPackage: com.wzkris.**.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/system/*.xml\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n\n# 安全配置\nsecurity:\n  ignores:\n    #swagger接口放行\n    - /v3/api-docs/**\n\n# 租户配置\ntenant:\n  includes:\n    - sys_login_log\n    - sys_oper_log\n\n# springdoc配置\nspringdoc:\n  title: 系统模块接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---\n', '230c032e15ae113a50ae32a4decebb7f', '2023-06-19 02:28:00', '2024-05-30 01:17:55', 'nacos', '0:0:0:0:0:0:0:1', '', 'application-prod', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (7, 'sentinel-gateway', 'APPLICATION_GROUP', '[\n    {\n        \"resource\": \"wzkris-auth\",\n        \"count\": 2000,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n	{\n        \"resource\": \"wzkris-system\",\n        \"count\": 1000,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n    {\n        \"resource\": \"wzkris-user\",\n        \"count\": 1000,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n	{\n        \"resource\": \"wzkris-file\",\n        \"count\": 300,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n    {\n        \"resource\": \"wzkris-moniter\",\n        \"count\": 300,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    }\n]', '2df93928245e7593c3d5c2be233c4f15', '2023-06-19 02:28:00', '2023-12-08 01:03:54', 'nacos', '0:0:0:0:0:0:0:1', '', 'application-prod', '网关限流策略', '', '', 'json', '', '');
INSERT INTO `config_info` VALUES (9, 'wzkris-file.yml', 'APPLICATION_GROUP', 'spring:\n  servlet:\n    multipart:\n      # 根据实际需求作调整\n      # 默认最大上传文件大小为15M，单个文件大小\n      max-file-size: 15728640\n      # 默认最大请求大小为30M，总上传的数据大小\n      max-request-size: 31457280\n\n# 对象配置\noss:\n  use: local\n  # 本地配置\n  local:\n    domain: http://127.0.0.1:9300\n    prefix: /uploadPath\n    path: /home/wzkris\n  # Minio配置\n  minio:\n    url: http://127.0.0.1:9000\n    accessKey: minioadmin\n    secretKey: minioadmin\n  # FastDFS配置\n  fdfs:\n    domain: http://127.0.0.1\n    soTimeout: 3000\n    connectTimeout: 2000\n    trackerList: 127.0.0.1:22122\n', '4ba4b6410304cd769e18ff12a6f0150c', '2023-06-19 02:28:00', '2023-09-15 05:14:26', 'nacos', '115.221.135.11', '', 'application-prod', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (10, 'application-common.yml', 'APPLICATION_GROUP', 'server:\n  # 优雅停机\n  shutdown: graceful\n\nspring:\n  pid:\n    # 将 PID 写入文件中\n    file: ./${spring.application.name}.pid\n  jackson:\n    time-zone: GMT+8\n    # 日期格式化\n    date-format: yyyy-MM-dd HH:mm:ss\n    serialization:\n      # 格式化输出\n      INDENT_OUTPUT: false\n      # 忽略空Bean转json的错误\n      FAIL_ON_EMPTY_BEANS: false\n      # 关闭日期转换成时间戳\n      WRITE_DATES_AS_TIMESTAMPS: false\n    # 设置空如何序列化\n    defaultPropertyInclusion: ALWAYS\n    deserialization:\n      #json中不存在的属性就报错\n      fail_on_unknown_properties: false\n    parser:\n      # 允许使用无引号字段\n      ALLOW_UNQUOTED_FIELD_NAMES: true\n      # 忽略未定义的属性\n      IGNORE_UNDEFINED: true\n      # 忽略json最后的逗号\n      ALLOW_TRAILING_COMMA: true\n      # 允许反斜杠\n      ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER: true\n      # 允许出现特殊字符和转义符\n      ALLOW_UNQUOTED_CONTROL_CHARS: true\n      # 允许出现单引号\n      ALLOW_SINGLE_QUOTES: true\n      # 是否允许使用注释\n      ALLOW_COMMENTS: true\n    mapper:\n      # 使用getter取代setter探测属性，如类中含getName()但不包含name属性与setName()，传输的vo json格式模板中依旧含name属性\n      USE_GETTERS_AS_SETTERS: true\n  mvc:\n    # mvc找不到对应处理器则抛出异常\n    throw-exception-if-no-handler-found: true\n    # 关闭DispatcherServlet懒加载\n    servlet:\n      load-on-startup: 0\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common\n\n#分布式事务\nseata:\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      ruoyi-system-group: default\n    # 分组和 Seata 服务的映射\n    grouplist:\n      default: 127.0.0.1:8091\n  config:\n    type: nacos\n    nacos:\n      serverAddr: ${spring.cloud.nacos.server-addr}\n      username: ${spring.cloud.nacos.username}\n      password: ${spring.cloud.nacos.password}\n      group: SEATA_GROUP\n      namespace: f06f7669-9836-4413-86cf-9f44ae5c9f30\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: ${spring.cloud.nacos.server-addr}\n      username: ${spring.cloud.nacos.username}\n      password: ${spring.cloud.nacos.password}\n      namespace: f06f7669-9836-4413-86cf-9f44ae5c9f30\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \"*\"\n\n# 默认安全配置\nsecurity:\n  ignores:\n    #swagger接口放行\n    - /v3/api-docs/**\n', 'f8e465933a4f89a00e907b233eca3ccc', '2023-06-19 02:28:00', '2025-05-21 17:09:27', 'nacos', '0:0:0:0:0:0:0:1', '', 'application-dev', '公共配置', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (11, 'wzkris-gateway.yml', 'APPLICATION_GROUP', 'spring:\n  cloud:\n    gateway:\n      httpclient:\n        pool:\n          type: FIXED\n          max-connections: 1000\n          acquire-timeout: 80000\n          max-life-time: 30000\n          max-idle-time: 5000\n          connect-timeout: 5000\n          response-timeout: 5000\n          max-request-size: 50MB\n      discovery:\n        locator:\n          lower-case-service-id: true\n          enabled: true\n      routes:\n        # 认证中心\n        - id: wzkris-auth\n          uri: lb://wzkris-auth\n          predicates:\n            - Path=/auth/**\n          filters:\n            - StripPrefix=1\n        # 用户服务\n        - id: wzkris-user\n          uri: lb://wzkris-user\n          predicates:\n            - Path=/ur_biz/**\n          filters:\n            - StripPrefix=1\n            # 黑名单过滤器\n            - name: BlackListUrlFilter\n              args:\n                blacklistUrl:\n                - /nacos/test\n        # 系统模块\n        - id: wzkris-system\n          uri: lb://wzkris-system\n          predicates:\n            - Path=/sys_biz/**\n          filters:\n            - StripPrefix=1\n        # 文件服务\n        - id: wzkris-file\n          uri: lb://wzkris-file\n          predicates:\n            - Path=/fl_biz/**\n          filters:\n            - StripPrefix=1\n        # 监控服务\n        - id: wzkris-monitor\n          uri: lb://wzkris-monitor\n          predicates:\n            - Path=/moniter/**\n          filters:\n            - StripPrefix=1\n\nknife4j:\n  # 聚合swagger文档\n  gateway:\n    enabled: true\n    strategy: discover\n    discover:\n      version: openapi3\n      enabled: true\n      service-config:\n        wzkris-auth:\n          - group-name: 认证服务\n            order: 1\n        wzkris-user:\n          - group-name: 用户服务\n            order: 1\n        wzkris-system:\n          - group-name: 系统服务\n            order: 2\n        wzkris-equipment:\n          - group-name: 设备服务\n            order: 4\n        wzkris-file:\n          - group-name: 文件服务\n            order: 5\n        wzkris-job:\n          - group-name: 定时任务服务\n            order: 6\n    tags-sorter: order\n    operations-sorter: order\n\n# 防止XSS攻击\nxss:\n  enabled: true\n  excludeUrls:\n    - /sys_biz/sys_message/add\n    - /sys_biz/sys_message/edit\n\n# 网关放行\nsecurity:\n  ignores:\n    # 验证码放行\n    - /auth/captcha/**\n    # 登录接口\n    - /auth/login\n    # 登出接口\n    - /auth/logout\n    # oauth2接口\n    - /auth/oauth2/**\n    # 回调接口\n    - /auth/authorization_code_callback\n    # 微信js接口\n    - /ur_biz/wx_req/js_ticket_sign\n    # 用户注册\n    - /ur_biz/app_register/*\n    # swagger接口放行\n    - /*/v3/api-docs/**\n    # 开放接口放行\n    - /*/open/**\n    # 上传图片可访问\n    - /fl_biz/uploadPath/**\n', 'c18cbe7ee7db3c767ba88fca9c1d7ae5', '2023-06-19 02:28:00', '2025-06-18 10:00:11', 'nacos', '0:0:0:0:0:0:0:1', '', 'application-dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (12, 'wzkris-auth.yml', 'APPLICATION_GROUP', '# spring配置\nspring:\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common,i18n/auth\n  redis:\n    redisson: \n      config: |\n        # 单节点配置\n        singleServerConfig: \n          # redis 节点地址\n          address: \"redis://127.0.0.1:6379\"\n          # 无密码则设置 null\n          password: null\n          # 客户端名称\n          clientName: ${spring.application.name}\n          # 最小空闲连接数\n          connectionMinimumIdleSize: 32\n          # 连接池大小\n          connectionPoolSize: 64\n          # 连接空闲超时,单位:毫秒\n          idleConnectionTimeout: 10000\n          # 命令等待超时,单位:毫秒\n          timeout: 3000\n          # 发布和订阅连接池大小\n          subscriptionConnectionPoolSize: 50\n        # 线程池数量\n        threads: 2\n        # Netty线程池数量\n        nettyThreads: 8\n        codec: !<org.redisson.codec.JsonJacksonCodec> {}\n        transportMode: \"NIO\"\n\n# springdoc配置\nspringdoc:\n  title: 认证模块接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---\n\n# 安全配置\nsecurity:\n  # 不校验白名单\n  ignores:\n    #swagger接口放行\n    - /v3/api-docs/**\n    # 验证码放行\n    - /captcha/**\n    - /oauth2/authorization_code_callback\n', '9e5e886c0bbad706a6f729ab3fe44d3e', '2023-06-19 02:28:00', '2025-06-18 17:17:13', 'nacos', '0:0:0:0:0:0:0:1', '', 'application-dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (13, 'wzkris-user.yml', 'APPLICATION_GROUP', '# spring配置\nspring:\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common,i18n/user\n  # datasource:\n  #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver\n  #   url: jdbc:shardingsphere:classpath:sharding-${spring.profiles.active}.yml\n  datasource:\n    url: jdbc:postgresql://localhost:5432/wzkris_user?ssl=false&reWriteBatchedInserts=true&stringtype=unspecified\n    username: root\n    password: root\n    driver-class-name: org.postgresql.Driver\n    hikari:\n      connection-timeout: 30000 \n      maximum-pool-size: 10       \n      minimum-idle: 5             \n      idle-timeout: 600000        \n      pool-name: hikari-pool\n  redis:\n    redisson: \n      config: |\n        # 单节点配置\n        singleServerConfig: \n          # redis 节点地址\n          address: \"redis://127.0.0.1:6379\"\n          # 无密码则设置 null\n          password: null\n          # 客户端名称\n          clientName: ${spring.application.name}\n          # 最小空闲连接数\n          connectionMinimumIdleSize: 32\n          # 连接池大小\n          connectionPoolSize: 64\n          # 连接空闲超时,单位:毫秒\n          idleConnectionTimeout: 10000\n          # 命令等待超时,单位:毫秒\n          timeout: 3000\n          # 发布和订阅连接池大小\n          subscriptionConnectionPoolSize: 50\n        # 线程池数量\n        threads: 2\n        # Netty线程池数量\n        nettyThreads: 8\n        codec: !<org.redisson.codec.JsonJacksonCodec> {}\n        transportMode: \"NIO\"\n\nmybatis-plus:\n  # 搜索指定包别名\n  typeAliasesPackage: com.wzkris.user.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/user/*.xml\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n\n# springdoc配置\nspringdoc:\n  title: 用户模块接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---\n\n# 安全配置\nsecurity:\n  ignores:\n    #swagger接口放行\n    - /v3/api-docs/**\n    # js签名接口\n    - /wx_req/js_ticket_sign\n\n\n# 租户配置\ntenant:\n  includes:\n    - sys_dept\n    - sys_post\n    - sys_role\n    - sys_tenant\n    - sys_tenant_wallet\n    - sys_tenant_wallet_record\n    - sys_user', '093dce05bc5a395ec64fa24837336a1b', '2024-04-16 06:36:22', '2025-05-21 17:08:54', 'nacos', '0:0:0:0:0:0:0:1', '', 'application-dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (14, 'wzkris-monitor-admin.yml', 'APPLICATION_GROUP', '# spring\nspring:\n  security:\n    user:\n      name: admin\n      password: admin123\n  boot:\n    admin:\n      ui:\n        title: 服务状态监控\n\noauth2-client:\n  url: http://localhost:9000/oauth2/token\n  clientId: server_monitor\n  clientSecret: secret\n  scopes:\n    - monitor', 'f1b80368973403f9ee0d8a2e50203772', '2023-06-19 02:28:00', '2025-06-13 11:24:27', 'nacos', '192.168.0.111', '', 'application-dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (15, 'wzkris-system.yml', 'APPLICATION_GROUP', '# spring配置\nspring:\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common,i18n/system\n  # datasource:\n  #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver\n  #   url: jdbc:shardingsphere:classpath:sharding-${spring.profiles.active}.yml\n  datasource:\n    url: jdbc:postgresql://localhost:5432/wzkris_system?ssl=false&reWriteBatchedInserts=true&stringtype=unspecified\n    username: root\n    password: root\n    driver-class-name: org.postgresql.Driver\n    hikari:\n      connection-timeout: 30000 \n      maximum-pool-size: 10       \n      minimum-idle: 5             \n      idle-timeout: 600000        \n      pool-name: hikari-pool\n  redis:\n    redisson: \n      config: |\n        # 单节点配置\n        singleServerConfig: \n          # redis 节点地址\n          address: \"redis://127.0.0.1:6379\"\n          # 无密码则设置 null\n          password: null\n          # 客户端名称\n          clientName: ${spring.application.name}\n          # 最小空闲连接数\n          connectionMinimumIdleSize: 32\n          # 连接池大小\n          connectionPoolSize: 64\n          # 连接空闲超时,单位:毫秒\n          idleConnectionTimeout: 10000\n          # 命令等待超时,单位:毫秒\n          timeout: 3000\n          # 发布和订阅连接池大小\n          subscriptionConnectionPoolSize: 50\n        # 线程池数量\n        threads: 2\n        # Netty线程池数量\n        nettyThreads: 8\n        codec: !<org.redisson.codec.JsonJacksonCodec> {}\n        transportMode: \"NIO\"\n\nmybatis-plus:\n  # 搜索指定包别名\n  typeAliasesPackage: com.wzkris.**.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/system/*.xml\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n\n# 安全配置\nsecurity:\n  ignores:\n    #swagger接口放行\n    - /v3/api-docs/**\n\n# 租户配置\ntenant:\n  includes:\n    - sys_login_log\n    - sys_oper_log\n\n# springdoc配置\nspringdoc:\n  title: 系统模块接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---\n', 'f30ac2213f1c148f5d8a7d7df3f297d0', '2023-06-19 02:28:00', '2025-05-21 17:10:05', 'nacos', '0:0:0:0:0:0:0:1', '', 'application-dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (16, 'sentinel-gateway', 'APPLICATION_GROUP', '[\n    {\n        \"resource\": \"wzkris-auth\",\n        \"count\": 2000,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n	{\n        \"resource\": \"wzkris-system\",\n        \"count\": 1000,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n    {\n        \"resource\": \"wzkris-user\",\n        \"count\": 1000,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n	{\n        \"resource\": \"wzkris-file\",\n        \"count\": 300,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n    {\n        \"resource\": \"wzkris-moniter\",\n        \"count\": 300,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    }\n]', '05989dc889ea53a1f40f56a32eb6bf54', '2023-06-19 02:28:00', '2025-04-16 11:05:50', 'nacos', '0:0:0:0:0:0:0:1', '', 'application-dev', '网关限流策略', '', '', 'json', '', '');
INSERT INTO `config_info` VALUES (18, 'wzkris-file.yml', 'APPLICATION_GROUP', 'spring:\n  servlet:\n    multipart:\n      # 根据实际需求作调整\n      # 默认最大上传文件大小为15M，单个文件大小\n      max-file-size: 15728640\n      # 默认最大请求大小为30M，总上传的数据大小\n      max-request-size: 31457280\n\n# 对象配置\noss:\n  use: local\n  # 本地配置\n  local:\n    domain: http://127.0.0.1:9300\n    prefix: /uploadPath\n    path: /home/wzkris\n  # Minio配置\n  minio:\n    url: http://127.0.0.1:9000\n    accessKey: minioadmin\n    secretKey: minioadmin\n  # FastDFS配置\n  fdfs:\n    domain: http://127.0.0.1\n    soTimeout: 3000\n    connectTimeout: 2000\n    trackerList: 127.0.0.1:22122\n', '2ad31742da254750cdcfe1737f04c141', '2023-06-19 02:28:00', '2025-05-15 10:17:32', 'nacos', '0:0:0:0:0:0:0:1', '', 'application-dev', '', '', '', 'yaml', '', '');

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'datum_id',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `app_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '增加租户字段' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_aggr
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'content',
  `beta_ips` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT 'betaIps',
  `md5` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '租户字段',
  `encrypted_data_key` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfobeta_datagrouptenant`(`data_id`, `group_id`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'config_info_beta' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_beta
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_gray
-- ----------------------------
DROP TABLE IF EXISTS `config_info_gray`;
CREATE TABLE `config_info_gray`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'group_id',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'md5',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'src_user',
  `src_ip` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'src_ip',
  `gmt_create` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'gmt_create',
  `gmt_modified` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'gmt_modified',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'app_name',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT 'tenant_id',
  `gray_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'gray_name',
  `gray_rule` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'gray_rule',
  `encrypted_data_key` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'encrypted_data_key',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfogray_datagrouptenantgray`(`data_id`, `group_id`, `tenant_id`, `gray_name`) USING BTREE,
  INDEX `idx_dataid_gmt_modified`(`data_id`, `gmt_modified`) USING BTREE,
  INDEX `idx_gmt_modified`(`gmt_modified`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'config_info_gray' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_info_gray
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'tenant_id',
  `tag_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfotag_datagrouptenanttag`(`data_id`, `group_id`, `tenant_id`, `tag_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'config_info_tag' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_tag
-- ----------------------------

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation`  (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `tag_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'tenant_id',
  `nid` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`) USING BTREE,
  UNIQUE INDEX `uk_configtagrelation_configidtag`(`id`, `tag_name`, `tag_type`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'config_tag_relation' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_tags_relation
-- ----------------------------

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Group ID，空字符表示整个集群',
  `quota` int(11) NOT NULL COMMENT '配额，0表示使用默认值',
  `usage` int(11) NOT NULL COMMENT '使用量',
  `max_size` int(11) NOT NULL COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(11) NOT NULL COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int(11) NOT NULL COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(11) NOT NULL COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_group_id`(`group_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '集群、各Group容量信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of group_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info`  (
  `id` bigint(20) UNSIGNED NOT NULL,
  `nid` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `group_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `app_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `md5` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `src_user` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `src_ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `op_type` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '租户字段',
  `encrypted_data_key` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '秘钥',
  `publish_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT 'formal' COMMENT 'publish type gray or formal',
  `gray_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'publish type gray or formal',
  `ext_info` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  PRIMARY KEY (`nid`) USING BTREE,
  INDEX `idx_did`(`data_id`) USING BTREE,
  INDEX `idx_gmt_create`(`gmt_create`) USING BTREE,
  INDEX `idx_gmt_modified`(`gmt_modified`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '多租户改造' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of his_config_info
-- ----------------------------

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`  (
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `resource` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `action` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of permissions
-- ----------------------------
INSERT INTO `permissions` VALUES ('prod', 'application-prod:*:*', 'rw');
INSERT INTO `permissions` VALUES ('prod', 'dubbo-prod:*:*', 'rw');
INSERT INTO `permissions` VALUES ('prod', ':*:*', 'rw');
INSERT INTO `permissions` VALUES ('dev', 'application-dev:*:*', 'rw');
INSERT INTO `permissions` VALUES ('dev', 'dubbo-dev:*:*', 'rw');
INSERT INTO `permissions` VALUES ('dev', ':*:*', 'rw');

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  UNIQUE INDEX `uk_username_role`(`username`, `role`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES ('dev', 'dev');
INSERT INTO `roles` VALUES ('nacos', 'ROLE_ADMIN');
INSERT INTO `roles` VALUES ('prod', 'prod');

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Tenant ID',
  `quota` int(11) NOT NULL COMMENT '配额，0表示使用默认值',
  `usage` int(11) NOT NULL COMMENT '使用量',
  `max_size` int(11) NOT NULL COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(11) NOT NULL COMMENT '聚合子配置最大个数',
  `max_aggr_size` int(11) NOT NULL COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(11) NOT NULL COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '租户容量信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tenant_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'tenant_id',
  `tenant_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'tenant_name',
  `tenant_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_info_kptenantid`(`kp`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'tenant_info' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tenant_info
-- ----------------------------
INSERT INTO `tenant_info` VALUES (1, '1', 'application-dev', 'application-dev', '应用开发环境', 'nacos', 1685070991448, 1685071009622);
INSERT INTO `tenant_info` VALUES (2, '1', 'application-prod', 'application-prod', '应用生产环境', 'nacos', 1687141596216, 1687141596216);
INSERT INTO `tenant_info` VALUES (3, '1', 'dubbo-dev', 'dubbo-dev', 'dubbo开发环境', 'nacos', 1739077187594, 1739077187594);
INSERT INTO `tenant_info` VALUES (4, '1', 'dubbo-prod', 'dubbo-prod', 'dubbo生产环境', 'nacos', 1739077200196, 1739077200196);
INSERT INTO `tenant_info` VALUES (5, '2', 'nacos-default-mcp', 'nacos-default-mcp', 'Nacos default AI MCP module.', 'nacos', 1749453112939, 1749453112939);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `enabled` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('dev', '$2a$10$zOHL8QGqN6PNlYRluJpvluKDP16Add3ywu00K2O70klCmSnj8gKzm', 't');
INSERT INTO `users` VALUES ('nacos', '$2a$10$h05FUQ0x5eypjsF02DWyd.2PXBDgXb7GIXNZCjRim6EORDlTLTRLu', 't');
INSERT INTO `users` VALUES ('prod', '$2a$10$AdFVZMyO8R4ZbjIVSlQWBukjx83Buc.x54x1fmwDyLDhT.uWSXi9S', '1');

SET FOREIGN_KEY_CHECKS = 1;
