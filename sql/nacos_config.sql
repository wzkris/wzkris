/*
 Navicat Premium Data Transfer

 Source Server         : local_mysql8
 Source Server Type    : MySQL
 Source Server Version : 80035
 Source Host           : localhost:3306
 Source Schema         : nacos_config

 Target Server Type    : MySQL
 Target Server Version : 80035
 File Encoding         : 65001

 Date: 21/01/2025 09:07:52
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
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
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
  UNIQUE INDEX `uk_configinfo_datagrouptenant`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'config_info' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info` VALUES (1, 'application-common.yml', 'wzkris', 'server:\n  # 优雅停机\n  shutdown: graceful\n\nspring:\n  pid:\n    # 将 PID 写入文件中\n    file: ./${spring.application.name}.pid\n  jackson:\n    time-zone: GMT+8\n    # 日期格式化\n    date-format: yyyy-MM-dd HH:mm:ss\n    serialization:\n      # 格式化输出\n      INDENT_OUTPUT: false\n      # 忽略空Bean转json的错误\n      FAIL_ON_EMPTY_BEANS: false\n      # 关闭日期转换成时间戳\n      WRITE_DATES_AS_TIMESTAMPS: false\n    # 设置空如何序列化\n    defaultPropertyInclusion: ALWAYS\n    deserialization:\n      #json中不存在的属性就报错\n      fail_on_unknown_properties: false\n    parser:\n      # 允许使用无引号字段\n      ALLOW_UNQUOTED_FIELD_NAMES: true\n      # 忽略未定义的属性\n      IGNORE_UNDEFINED: true\n      # 忽略json最后的逗号\n      ALLOW_TRAILING_COMMA: true\n      # 允许反斜杠\n      ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER: true\n      # 允许出现特殊字符和转义符\n      ALLOW_UNQUOTED_CONTROL_CHARS: true\n      # 允许出现单引号\n      ALLOW_SINGLE_QUOTES: true\n      # 是否允许使用注释\n      ALLOW_COMMENTS: true\n    mapper:\n      # 使用getter取代setter探测属性，如类中含getName()但不包含name属性与setName()，传输的vo json格式模板中依旧含name属性\n      USE_GETTERS_AS_SETTERS: true\n  mvc:\n    # mvc找不到对应处理器则抛出异常\n    throw-exception-if-no-handler-found: true\n    # 关闭DispatcherServlet懒加载\n    servlet:\n      load-on-startup: 0\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common\n  cloud:\n    openfeign:\n      # 请求使用okhttp\n      okhttp:\n        enabled: true\n      # openfeign超时配置\n      client:\n        config:\n          default:\n            connectTimeout: 10000\n            readTimeout: 10000\n            loggerLevel: basic\n\n# feign启用sentinel\nfeign:\n  sentinel:\n    enabled: true\n\n#分布式事务\nseata:\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      ruoyi-system-group: default\n    # 分组和 Seata 服务的映射\n    grouplist:\n      default: 127.0.0.1:8091\n  config:\n    type: nacos\n    nacos:\n      serverAddr: ${spring.cloud.nacos.server-addr}\n      username: ${spring.cloud.nacos.username}\n      password: ${spring.cloud.nacos.password}\n      group: SEATA_GROUP\n      namespace: f06f7669-9836-4413-86cf-9f44ae5c9f30\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: ${spring.cloud.nacos.server-addr}\n      username: ${spring.cloud.nacos.username}\n      password: ${spring.cloud.nacos.password}\n      namespace: f06f7669-9836-4413-86cf-9f44ae5c9f30\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \"*\"\n\nsecurity:\n  feign:\n    identityKey: 8edeb038-38e7-11ef-9ec3-00155d106729\n    identityValue: aa2160ee-38e7-11ef-817b-fa163ecd525d\n  # 不校验白名单\n  ignore:\n    commons:\n      - /inner/noauth/**\n      #spring监控服务放行\n      - /actuator/**\n      #swagger接口放行\n      - /v3/api-docs/**\n      - /webjars/**\n      #开放接口放行\n      - /open/**\n      #上传图片可访问\n      - /uploadPath/**\n      #异常处理\n      - /error', '84e2cef7fd8f77184c77c6dd0410d0cc', '2023-06-19 02:28:00', '2024-07-03 05:44:56', 'nacos', '0:0:0:0:0:0:0:1', '', 'wzkris-prod', '公共配置', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (2, 'wzkris-gateway.yml', 'wzkris', 'spring:\n  cloud:\n    gateway:\n      httpclient:\n        pool:\n          type: FIXED\n          max-connections: 1000\n          acquire-timeout: 80000\n          max-life-time: 30000\n          max-idle-time: 5000\n          connect-timeout: 5000\n          response-timeout: 5000\n          max-request-size: 50MB\n      discovery:\n        locator:\n          lower-case-service-id: true\n          enabled: true\n      routes:\n        # 认证中心\n        - id: wzkris-auth\n          uri: lb://wzkris-auth\n          predicates:\n            - Path=/auth/**\n          filters:\n            - StripPrefix=1\n        # 用户服务\n        - id: wzkris-user\n          uri: lb://wzkris-user\n          predicates:\n            - Path=/ur_biz/**\n          filters:\n            - StripPrefix=1\n            # 黑名单过滤器\n            - name: BlackListUrlFilter\n              args:\n                blacklistUrl:\n                - /nacos/test\n        # 系统模块\n        - id: wzkris-system\n          uri: lb://wzkris-system\n          predicates:\n            - Path=/sys_biz/**\n          filters:\n            - StripPrefix=1\n        # 设备模块\n        - id: wzkris-equipment\n          uri: lb://wzkris-equipment\n          predicates:\n            - Path=/eqp_biz/**\n          filters:\n            - StripPrefix=1\n        # 文件服务\n        - id: wzkris-file\n          uri: lb://wzkris-file\n          predicates:\n            - Path=/fl_biz/**\n          filters:\n            - StripPrefix=1\n        # 监控服务\n        - id: wzkris-monitor\n          uri: lb://wzkris-monitor\n          predicates:\n            - Path=/moniter/**\n          filters:\n            - StripPrefix=1\n\nknife4j:\n  # 聚合swagger文档\n  gateway:\n    enabled: true\n    strategy: discover\n    discover:\n      version: openapi3\n      enabled: true\n      service-config:\n        wzkris-auth:\n          - group-name: 认证服务\n            order: 1\n        wzkris-user:\n          - group-name: 用户服务\n            order: 1\n        wzkris-system:\n          - group-name: 系统服务\n            order: 2\n        wzkris-equipment:\n          - group-name: 设备服务\n            order: 4\n        wzkris-file:\n          - group-name: 文件服务\n            order: 5\n        wzkris-job:\n          - group-name: 定时任务服务\n            order: 6\n    tags-sorter: order\n    operations-sorter: order\n\n# 防止XSS攻击\nxss:\n  enabled: true\n  excludeUrls:\n    - /sys_biz/sys_message/add\n    - /sys_biz/sys_message/edit\n\n# 网关放行\nsecurity:\n  ignores:\n    # 验证码放行\n    - /auth/code\n    - /auth/sms_code\n    # 认证接口\n    - /auth/oauth2/**\n    # 回调接口\n    - /auth/authorization_code_callback\n    # 微信js接口\n    - /ur_biz/wx_req/js_ticket_sign\n    # 用户注册\n    - /ur_biz/app_register/*\n    # swagger接口放行\n    - /*/v3/api-docs/**\n    # 开放接口放行\n    - /*/open/**\n    # 上传图片可访问\n    - /fl_biz/uploadPath/**\n', '8fa111fcab73c4fd01c0a0f6c7b17c1f', '2023-06-19 02:28:00', '2024-05-30 01:20:02', 'nacos', '0:0:0:0:0:0:0:1', '', 'wzkris-prod', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (3, 'wzkris-auth.yml', 'wzkris', '# spring配置\nspring:\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common,i18n/auth\n  redis:\n    redisson: \n      config: |\n        # 单节点配置\n        singleServerConfig: \n          # redis 节点地址\n          address: \"redis://127.0.0.1:6379\"\n          # 无密码则设置 null\n          password: null\n          # 客户端名称\n          clientName: ${spring.application.name}\n          # 最小空闲连接数\n          connectionMinimumIdleSize: 32\n          # 连接池大小\n          connectionPoolSize: 64\n          # 连接空闲超时,单位:毫秒\n          idleConnectionTimeout: 10000\n          # 命令等待超时,单位:毫秒\n          timeout: 3000\n          # 发布和订阅连接池大小\n          subscriptionConnectionPoolSize: 50\n        # 线程池数量\n        threads: 2\n        # Netty线程池数量\n        nettyThreads: 8\n        codec: !<org.redisson.codec.JsonJacksonCodec> {}\n        transportMode: \"NIO\"\n\n# springdoc配置\nspringdoc:\n  title: 认证模块接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---\n\n# 安全配置\nsecurity:\n  # 验证码\n  captcha:\n    enabled: true\n    type: math\n  # 不校验白名单\n  ignore:\n    customs:\n      # 验证码放行\n      - /code\n      - /sms_code\n      - /authorization_code_callback\n\n# token配置\ntoken-config:\n  accessTokenTimeOut: 1800\n  refreshTokenTimeOut: 86400\n  authorizationCodeTimeOut: 1800\n  deviceCodeTimeOut: 1800\n', '911c4d2e78b883bafb0dfd3008bbff77', '2023-06-19 02:28:00', '2024-07-03 07:00:15', 'nacos', '0:0:0:0:0:0:0:1', '', 'wzkris-prod', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (4, 'wzkris-user.yml', 'wzkris', '# spring配置\nspring:\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common,i18n/user\n  # datasource:\n  #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver\n  #   url: jdbc:shardingsphere:classpath:sharding-${spring.profiles.active}.yml\n  datasource:\n    url: jdbc:mysql://localhost:3306/wzkris_user?rewriteBatchedStatements=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai\n    username: root\n    password: root\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    hikari:\n      connection-timeout: 30000 \n      maximum-pool-size: 10       \n      minimum-idle: 5             \n      idle-timeout: 600000        \n      pool-name: hikari-pool\n  redis:\n    redisson: \n      config: |\n        # 单节点配置\n        singleServerConfig: \n          # redis 节点地址\n          address: \"redis://127.0.0.1:6379\"\n          # 无密码则设置 null\n          password: null\n          # 客户端名称\n          clientName: ${spring.application.name}\n          # 最小空闲连接数\n          connectionMinimumIdleSize: 32\n          # 连接池大小\n          connectionPoolSize: 64\n          # 连接空闲超时,单位:毫秒\n          idleConnectionTimeout: 10000\n          # 命令等待超时,单位:毫秒\n          timeout: 3000\n          # 发布和订阅连接池大小\n          subscriptionConnectionPoolSize: 50\n        # 线程池数量\n        threads: 2\n        # Netty线程池数量\n        nettyThreads: 8\n        codec: !<org.redisson.codec.JsonJacksonCodec> {}\n        transportMode: \"NIO\"\n\nmybatis-plus:\n  # 搜索指定包别名\n  typeAliasesPackage: com.wzkris.user.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/user/*.xml\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n  global-config:\n    db-config:\n      # 逻辑删除\n      logic-delete-field: isDeleted \n\n# springdoc配置\nspringdoc:\n  title: 用户模块接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---\n\n# 安全配置\nsecurity:\n  # 验证码\n  captcha:\n    enabled: false\n    type: math\n  # 不校验白名单\n  ignore:\n    customs:\n      # js签名接口\n      - /wx_req/js_ticket_sign\n\n\n# 租户配置\ntenant:\n  includes:\n    - sys_dept\n    - sys_post\n    - sys_role\n    - sys_tenant\n    - sys_tenant_wallet\n    - sys_tenant_wallet_record\n    - sys_user', '6d17ee5a1a743387131d2965142a6a7a', '2024-04-16 01:03:03', '2024-06-03 08:50:19', 'nacos', '0:0:0:0:0:0:0:1', '', 'wzkris-prod', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (5, 'wzkris-monitor-admin.yml', 'wzkris', '# spring\nspring:\n  security:\n    user:\n      name: admin\n      password: admin123\n  boot:\n    admin:\n      ui:\n        title: 服务状态监控\n', 'dd19c14e3cebc473140e1fc8733a339d', '2023-06-19 02:28:00', '2023-06-19 02:28:00', NULL, '0:0:0:0:0:0:0:1', '', 'wzkris-prod', '', NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (6, 'wzkris-system.yml', 'wzkris', '# spring配置\nspring:\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common,i18n/system\n  # datasource:\n  #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver\n  #   url: jdbc:shardingsphere:classpath:sharding-${spring.profiles.active}.yml\n  datasource:\n    url: jdbc:mysql://localhost:3306/wzkris_system?rewriteBatchedStatements=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai\n    username: root\n    password: root\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    hikari:\n      connection-timeout: 30000 \n      maximum-pool-size: 10       \n      minimum-idle: 5             \n      idle-timeout: 600000        \n      pool-name: hikari-pool\n  redis:\n    redisson: \n      config: |\n        # 单节点配置\n        singleServerConfig: \n          # redis 节点地址\n          address: \"redis://127.0.0.1:6379\"\n          # 无密码则设置 null\n          password: null\n          # 客户端名称\n          clientName: ${spring.application.name}\n          # 最小空闲连接数\n          connectionMinimumIdleSize: 32\n          # 连接池大小\n          connectionPoolSize: 64\n          # 连接空闲超时,单位:毫秒\n          idleConnectionTimeout: 10000\n          # 命令等待超时,单位:毫秒\n          timeout: 3000\n          # 发布和订阅连接池大小\n          subscriptionConnectionPoolSize: 50\n        # 线程池数量\n        threads: 2\n        # Netty线程池数量\n        nettyThreads: 8\n        codec: !<org.redisson.codec.JsonJacksonCodec> {}\n        transportMode: \"NIO\"\n\nmybatis-plus:\n  # 搜索指定包别名\n  typeAliasesPackage: com.wzkris.**.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/system/*.xml\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n\n# 租户配置\ntenant:\n  includes:\n    - sys_login_log\n    - sys_oper_log\n\n# springdoc配置\nspringdoc:\n  title: 系统模块接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---\n', '230c032e15ae113a50ae32a4decebb7f', '2023-06-19 02:28:00', '2024-05-30 01:17:55', 'nacos', '0:0:0:0:0:0:0:1', '', 'wzkris-prod', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (7, 'sentinel-gateway', 'wzkris', '[\n    {\n        \"resource\": \"wzkris-auth\",\n        \"count\": 2000,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n	{\n        \"resource\": \"wzkris-system\",\n        \"count\": 1000,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n    {\n        \"resource\": \"wzkris-user\",\n        \"count\": 100,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n    {\n        \"resource\": \"wzkris-equipment\",\n        \"count\": 300,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n	{\n        \"resource\": \"wzkris-file\",\n        \"count\": 300,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n    {\n        \"resource\": \"wzkris-moniter\",\n        \"count\": 300,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    }\n]', '2df93928245e7593c3d5c2be233c4f15', '2023-06-19 02:28:00', '2023-12-08 01:03:54', 'nacos', '0:0:0:0:0:0:0:1', '', 'wzkris-prod', '网关限流策略', '', '', 'json', '', '');
INSERT INTO `config_info` VALUES (8, 'wzkris-equipment.yml', 'wzkris', '# spring配置\nspring:\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common,i18n/equipment\n  # datasource:\n  #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver\n  #   url: jdbc:shardingsphere:classpath:sharding-${spring.profiles.active}.yml\n  datasource:\n    url: jdbc:mysql://localhost:3306/wzkris_equipment?rewriteBatchedStatements=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai\n    username: root\n    password: root\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    hikari:\n      connection-timeout: 30000 \n      maximum-pool-size: 10       \n      minimum-idle: 5             \n      idle-timeout: 600000        \n      pool-name: hikari-pool\n  redis:\n    redisson: \n      config: |\n        # 单节点配置\n        singleServerConfig: \n          # redis 节点地址\n          address: \"redis://127.0.0.1:6379\"\n          # 无密码则设置 null\n          password: null\n          # 客户端名称\n          clientName: ${spring.application.name}\n          # 最小空闲连接数\n          connectionMinimumIdleSize: 32\n          # 连接池大小\n          connectionPoolSize: 64\n          # 连接空闲超时,单位:毫秒\n          idleConnectionTimeout: 10000\n          # 命令等待超时,单位:毫秒\n          timeout: 3000\n          # 发布和订阅连接池大小\n          subscriptionConnectionPoolSize: 50\n        # 线程池数量\n        threads: 2\n        # Netty线程池数量\n        nettyThreads: 8\n        codec: !<org.redisson.codec.JsonJacksonCodec> {}\n        transportMode: \"NIO\"\n\nmybatis-plus:\n  # 搜索指定包别名\n  typeAliasesPackage: com.wzkris.**.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/equipment/*.xml\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n\n#mqtt基本配置\nmqtt:\n  enable: false\n  settings: \n    car:\n      clientNum: 3\n      host: tcp://127.0.0.1:1883\n      userName: server_prod\n      password: ${server.port}\n      clientId: server_prod\n      automaticReconnect: true\n      maxReconnectDelay: 60000\n      cleanSession: false\n      maxInFlight: 1000\n      connectTimeout: 15\n      keepAlive: 180\n    example:\n      clientNum: 2\n      host: tcp://127.0.0.1:1883\n      userName: server_prod\n      password: ${server.port}\n      clientId: server_prod\n      automaticReconnect: true\n      maxReconnectDelay: 60000\n      cleanSession: true\n      maxInFlight: 100\n      connectTimeout: 15\n      keepAlive: 10\n\n# springdoc配置\nspringdoc:\n  title: 设备模块接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---\n\n# 不校验白名单\nsecurity:\n  white:\n    custom:\n      urls:\n        # mqtt验证接口\n        - /emqx/authorize\n\n# 租户配置\ntenant:\n  includes:\n    - device\n    - station', '7bcbb731f013ab576a05280d5555a495', '2023-06-19 02:28:00', '2024-05-30 01:19:00', 'nacos', '0:0:0:0:0:0:0:1', '', 'wzkris-prod', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (9, 'wzkris-file.yml', 'wzkris', 'spring:\n  servlet:\n    multipart:\n      # 根据实际需求作调整\n      # 默认最大上传文件大小为15M，单个文件大小\n      max-file-size: 15728640\n      # 默认最大请求大小为30M，总上传的数据大小\n      max-request-size: 31457280\n\n# 本地文件上传    \nlocal:\n  domain: http://127.0.0.1:9300\n  path: /home/wzkris\n  prefix: /uploadPath\n\n# FastDFS配置\nfdfs:\n  domain: http://8.129.231.12\n  soTimeout: 3000\n  connectTimeout: 2000\n  trackerList: 8.129.231.12:22122\n\n# Minio配置\nminio:\n  url: http://8.129.231.12:9000\n  accessKey: minioadmin\n  secretKey: minioadmin\n  bucketName: test', '4ba4b6410304cd769e18ff12a6f0150c', '2023-06-19 02:28:00', '2023-09-15 05:14:26', 'nacos', '115.221.135.11', '', 'wzkris-prod', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (10, 'application-common.yml', 'wzkris', 'server:\n  # 优雅停机\n  shutdown: graceful\n\nspring:\n  pid:\n    # 将 PID 写入文件中\n    file: ./${spring.application.name}.pid\n  jackson:\n    time-zone: GMT+8\n    # 日期格式化\n    date-format: yyyy-MM-dd HH:mm:ss\n    serialization:\n      # 格式化输出\n      INDENT_OUTPUT: false\n      # 忽略空Bean转json的错误\n      FAIL_ON_EMPTY_BEANS: false\n      # 关闭日期转换成时间戳\n      WRITE_DATES_AS_TIMESTAMPS: false\n    # 设置空如何序列化\n    defaultPropertyInclusion: ALWAYS\n    deserialization:\n      #json中不存在的属性就报错\n      fail_on_unknown_properties: false\n    parser:\n      # 允许使用无引号字段\n      ALLOW_UNQUOTED_FIELD_NAMES: true\n      # 忽略未定义的属性\n      IGNORE_UNDEFINED: true\n      # 忽略json最后的逗号\n      ALLOW_TRAILING_COMMA: true\n      # 允许反斜杠\n      ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER: true\n      # 允许出现特殊字符和转义符\n      ALLOW_UNQUOTED_CONTROL_CHARS: true\n      # 允许出现单引号\n      ALLOW_SINGLE_QUOTES: true\n      # 是否允许使用注释\n      ALLOW_COMMENTS: true\n    mapper:\n      # 使用getter取代setter探测属性，如类中含getName()但不包含name属性与setName()，传输的vo json格式模板中依旧含name属性\n      USE_GETTERS_AS_SETTERS: true\n  mvc:\n    # mvc找不到对应处理器则抛出异常\n    throw-exception-if-no-handler-found: true\n    # 关闭DispatcherServlet懒加载\n    servlet:\n      load-on-startup: 0\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common\n  cloud:\n    openfeign:\n      # 请求使用okhttp\n      okhttp:\n        enabled: true\n      # openfeign超时配置\n      client:\n        config:\n          default:\n            connectTimeout: 10000\n            readTimeout: 10000\n            loggerLevel: basic\n\n# feign启用sentinel\nfeign:\n  sentinel:\n    enabled: true\n\n#分布式事务\nseata:\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      ruoyi-system-group: default\n    # 分组和 Seata 服务的映射\n    grouplist:\n      default: 127.0.0.1:8091\n  config:\n    type: nacos\n    nacos:\n      serverAddr: ${spring.cloud.nacos.server-addr}\n      username: ${spring.cloud.nacos.username}\n      password: ${spring.cloud.nacos.password}\n      group: SEATA_GROUP\n      namespace: f06f7669-9836-4413-86cf-9f44ae5c9f30\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: ${spring.cloud.nacos.server-addr}\n      username: ${spring.cloud.nacos.username}\n      password: ${spring.cloud.nacos.password}\n      namespace: f06f7669-9836-4413-86cf-9f44ae5c9f30\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \"*\"\n\nsecurity:\n  feign:\n    identityKey: 8edeb038-38e7-11ef-9ec3-00155d106729\n    identityValue: aa2160ee-38e7-11ef-817b-fa163ecd525d\n  # 不校验白名单\n  ignore:\n    commons:\n      - /inner/noauth/**\n      #spring监控服务放行\n      - /actuator/**\n      #swagger接口放行\n      - /v3/api-docs/**\n      - /webjars/**\n      #开放接口放行\n      - /open/**\n      #上传图片可访问\n      - /uploadPath/**\n      #异常处理\n      - /error', 'b9f635ab1af0afb04d86b07aae584ed3', '2023-06-19 02:28:00', '2024-09-29 05:21:15', 'nacos', '0:0:0:0:0:0:0:1', '', 'wzkris-dev', '公共配置', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (11, 'wzkris-gateway.yml', 'wzkris', 'spring:\n  cloud:\n    gateway:\n      httpclient:\n        pool:\n          type: FIXED\n          max-connections: 1000\n          acquire-timeout: 80000\n          max-life-time: 30000\n          max-idle-time: 5000\n          connect-timeout: 5000\n          response-timeout: 5000\n          max-request-size: 50MB\n      discovery:\n        locator:\n          lower-case-service-id: true\n          enabled: true\n      routes:\n        # 认证中心\n        - id: wzkris-auth\n          uri: lb://wzkris-auth\n          predicates:\n            - Path=/auth/**\n          filters:\n            - StripPrefix=1\n        # 用户服务\n        - id: wzkris-user\n          uri: lb://wzkris-user\n          predicates:\n            - Path=/ur_biz/**\n          filters:\n            - StripPrefix=1\n            # 黑名单过滤器\n            - name: BlackListUrlFilter\n              args:\n                blacklistUrl:\n                - /nacos/test\n        # 系统模块\n        - id: wzkris-system\n          uri: lb://wzkris-system\n          predicates:\n            - Path=/sys_biz/**\n          filters:\n            - StripPrefix=1\n        # 设备模块\n        - id: wzkris-equipment\n          uri: lb://wzkris-equipment\n          predicates:\n            - Path=/eqp_biz/**\n          filters:\n            - StripPrefix=1\n        # 文件服务\n        - id: wzkris-file\n          uri: lb://wzkris-file\n          predicates:\n            - Path=/fl_biz/**\n          filters:\n            - StripPrefix=1\n        # 监控服务\n        - id: wzkris-monitor\n          uri: lb://wzkris-monitor\n          predicates:\n            - Path=/moniter/**\n          filters:\n            - StripPrefix=1\n\nknife4j:\n  # 聚合swagger文档\n  gateway:\n    enabled: true\n    strategy: discover\n    discover:\n      version: openapi3\n      enabled: true\n      service-config:\n        wzkris-auth:\n          - group-name: 认证服务\n            order: 1\n        wzkris-user:\n          - group-name: 用户服务\n            order: 1\n        wzkris-system:\n          - group-name: 系统服务\n            order: 2\n        wzkris-equipment:\n          - group-name: 设备服务\n            order: 4\n        wzkris-file:\n          - group-name: 文件服务\n            order: 5\n        wzkris-job:\n          - group-name: 定时任务服务\n            order: 6\n    tags-sorter: order\n    operations-sorter: order\n\n# 防止XSS攻击\nxss:\n  enabled: true\n  excludeUrls:\n    - /sys_biz/sys_message/add\n    - /sys_biz/sys_message/edit\n\n# 网关放行\nsecurity:\n  ignores:\n    # 验证码放行\n    - /auth/code\n    - /auth/sms_code\n    # 认证接口\n    - /auth/oauth2/**\n    # 回调接口\n    - /auth/authorization_code_callback\n    # 微信js接口\n    - /ur_biz/wx_req/js_ticket_sign\n    # 用户注册\n    - /ur_biz/app_register/*\n    # swagger接口放行\n    - /*/v3/api-docs/**\n    # 开放接口放行\n    - /*/open/**\n    # 上传图片可访问\n    - /fl_biz/uploadPath/**\n', 'ff833ffb322967f4fec27ee6bdda4355', '2023-06-19 02:28:00', '2025-01-20 15:10:20', 'nacos', '0:0:0:0:0:0:0:1', '', 'wzkris-dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (12, 'wzkris-auth.yml', 'wzkris', '# spring配置\nspring:\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common,i18n/auth\n  redis:\n    redisson: \n      config: |\n        # 单节点配置\n        singleServerConfig: \n          # redis 节点地址\n          address: \"redis://127.0.0.1:6379\"\n          # 无密码则设置 null\n          password: null\n          # 客户端名称\n          clientName: ${spring.application.name}\n          # 最小空闲连接数\n          connectionMinimumIdleSize: 32\n          # 连接池大小\n          connectionPoolSize: 64\n          # 连接空闲超时,单位:毫秒\n          idleConnectionTimeout: 10000\n          # 命令等待超时,单位:毫秒\n          timeout: 3000\n          # 发布和订阅连接池大小\n          subscriptionConnectionPoolSize: 50\n        # 线程池数量\n        threads: 2\n        # Netty线程池数量\n        nettyThreads: 8\n        codec: !<org.redisson.codec.JsonJacksonCodec> {}\n        transportMode: \"NIO\"\n\n# springdoc配置\nspringdoc:\n  title: 认证模块接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---\n\n# 安全配置\nsecurity:\n  # 验证码\n  captcha:\n    enabled: true\n    type: math\n  # 不校验白名单\n  ignore:\n    customs:\n      # 验证码放行\n      - /code\n      - /sms_code\n      - /authorization_code_callback\n\n# token配置\ntoken-config:\n  accessTokenTimeOut: 1800\n  refreshTokenTimeOut: 86400\n  authorizationCodeTimeOut: 1800\n  deviceCodeTimeOut: 1800\n', 'ecc3aaaaed33bedf8f25e674f6eaf933', '2023-06-19 02:28:00', '2024-11-27 01:08:51', 'nacos', '0:0:0:0:0:0:0:1', '', 'wzkris-dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (13, 'wzkris-user.yml', 'wzkris', '# spring配置\nspring:\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common,i18n/user\n  # datasource:\n  #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver\n  #   url: jdbc:shardingsphere:classpath:sharding-${spring.profiles.active}.yml\n  datasource:\n    url: jdbc:mysql://localhost:3306/wzkris_user?rewriteBatchedStatements=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai\n    username: root\n    password: root\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    hikari:\n      connection-timeout: 30000 \n      maximum-pool-size: 10       \n      minimum-idle: 5             \n      idle-timeout: 600000        \n      pool-name: hikari-pool\n  redis:\n    redisson: \n      config: |\n        # 单节点配置\n        singleServerConfig: \n          # redis 节点地址\n          address: \"redis://127.0.0.1:6379\"\n          # 无密码则设置 null\n          password: null\n          # 客户端名称\n          clientName: ${spring.application.name}\n          # 最小空闲连接数\n          connectionMinimumIdleSize: 32\n          # 连接池大小\n          connectionPoolSize: 64\n          # 连接空闲超时,单位:毫秒\n          idleConnectionTimeout: 10000\n          # 命令等待超时,单位:毫秒\n          timeout: 3000\n          # 发布和订阅连接池大小\n          subscriptionConnectionPoolSize: 50\n        # 线程池数量\n        threads: 2\n        # Netty线程池数量\n        nettyThreads: 8\n        codec: !<org.redisson.codec.JsonJacksonCodec> {}\n        transportMode: \"NIO\"\n\nmybatis-plus:\n  # 搜索指定包别名\n  typeAliasesPackage: com.wzkris.user.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/user/*.xml\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n  global-config:\n    db-config:\n      # 逻辑删除\n      logic-delete-field: isDeleted \n\n# springdoc配置\nspringdoc:\n  title: 用户模块接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---\n\n# 安全配置\nsecurity:\n  # 验证码\n  captcha:\n    enabled: false\n    type: math\n  # 不校验白名单\n  ignore:\n    customs:\n      # js签名接口\n      - /wx_req/js_ticket_sign\n\n\n# 租户配置\ntenant:\n  includes:\n    - sys_dept\n    - sys_post\n    - sys_role\n    - sys_tenant\n    - sys_tenant_wallet\n    - sys_tenant_wallet_record\n    - sys_user', '2b77bc851345800d99142851ac3e1803', '2024-04-16 06:36:22', '2025-01-21 09:06:31', 'nacos', '0:0:0:0:0:0:0:1', '', 'wzkris-dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (14, 'wzkris-monitor-admin.yml', 'wzkris', '# spring\nspring:\n  security:\n    user:\n      name: admin\n      password: admin123\n  boot:\n    admin:\n      ui:\n        title: 服务状态监控\n', 'dd19c14e3cebc473140e1fc8733a339d', '2023-06-19 02:28:00', '2024-07-03 05:51:40', 'nacos', '0:0:0:0:0:0:0:1', '', 'wzkris-dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (15, 'wzkris-system.yml', 'wzkris', '# spring配置\nspring:\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common,i18n/system\n  # datasource:\n  #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver\n  #   url: jdbc:shardingsphere:classpath:sharding-${spring.profiles.active}.yml\n  datasource:\n    url: jdbc:mysql://localhost:3306/wzkris_system?rewriteBatchedStatements=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai\n    username: root\n    password: root\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    hikari:\n      connection-timeout: 30000 \n      maximum-pool-size: 10       \n      minimum-idle: 5             \n      idle-timeout: 600000        \n      pool-name: hikari-pool\n  redis:\n    redisson: \n      config: |\n        # 单节点配置\n        singleServerConfig: \n          # redis 节点地址\n          address: \"redis://127.0.0.1:6379\"\n          # 无密码则设置 null\n          password: null\n          # 客户端名称\n          clientName: ${spring.application.name}\n          # 最小空闲连接数\n          connectionMinimumIdleSize: 32\n          # 连接池大小\n          connectionPoolSize: 64\n          # 连接空闲超时,单位:毫秒\n          idleConnectionTimeout: 10000\n          # 命令等待超时,单位:毫秒\n          timeout: 3000\n          # 发布和订阅连接池大小\n          subscriptionConnectionPoolSize: 50\n        # 线程池数量\n        threads: 2\n        # Netty线程池数量\n        nettyThreads: 8\n        codec: !<org.redisson.codec.JsonJacksonCodec> {}\n        transportMode: \"NIO\"\n\nmybatis-plus:\n  # 搜索指定包别名\n  typeAliasesPackage: com.wzkris.**.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/system/*.xml\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n\n# 租户配置\ntenant:\n  includes:\n    - sys_login_log\n    - sys_oper_log\n\n# springdoc配置\nspringdoc:\n  title: 系统模块接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---\n', '78a5fd538f50dd245efd29e836738051', '2023-06-19 02:28:00', '2024-12-26 14:14:18', 'nacos', '0:0:0:0:0:0:0:1', '', 'wzkris-dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (16, 'sentinel-gateway', 'wzkris', '[\n    {\n        \"resource\": \"wzkris-auth\",\n        \"count\": 2000,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n	{\n        \"resource\": \"wzkris-system\",\n        \"count\": 1000,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n    {\n        \"resource\": \"wzkris-user\",\n        \"count\": 100,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n    {\n        \"resource\": \"wzkris-equipment\",\n        \"count\": 300,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n	{\n        \"resource\": \"wzkris-file\",\n        \"count\": 300,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n    {\n        \"resource\": \"wzkris-moniter\",\n        \"count\": 300,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    }\n]', 'ded7e36ad0b962ff6bf6753c8c2660d2', '2023-06-19 02:28:00', '2024-12-03 08:39:55', 'nacos', '0:0:0:0:0:0:0:1', '', 'wzkris-dev', '网关限流策略', '', '', 'json', '', '');
INSERT INTO `config_info` VALUES (17, 'wzkris-equipment.yml', 'wzkris', '# spring配置\nspring:\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common,i18n/equipment\n  # datasource:\n  #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver\n  #   url: jdbc:shardingsphere:classpath:sharding-${spring.profiles.active}.yml\n  datasource:\n    url: jdbc:mysql://localhost:3306/wzkris_equipment?rewriteBatchedStatements=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai\n    username: root\n    password: root\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    hikari:\n      connection-timeout: 30000 \n      maximum-pool-size: 10       \n      minimum-idle: 5             \n      idle-timeout: 600000        \n      pool-name: hikari-pool\n  redis:\n    redisson: \n      config: |\n        # 单节点配置\n        singleServerConfig: \n          # redis 节点地址\n          address: \"redis://127.0.0.1:6379\"\n          # 无密码则设置 null\n          password: null\n          # 客户端名称\n          clientName: ${spring.application.name}\n          # 最小空闲连接数\n          connectionMinimumIdleSize: 32\n          # 连接池大小\n          connectionPoolSize: 64\n          # 连接空闲超时,单位:毫秒\n          idleConnectionTimeout: 10000\n          # 命令等待超时,单位:毫秒\n          timeout: 3000\n          # 发布和订阅连接池大小\n          subscriptionConnectionPoolSize: 50\n        # 线程池数量\n        threads: 2\n        # Netty线程池数量\n        nettyThreads: 8\n        codec: !<org.redisson.codec.JsonJacksonCodec> {}\n        transportMode: \"NIO\"\n\nmybatis-plus:\n  # 搜索指定包别名\n  typeAliasesPackage: com.wzkris.**.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/equipment/*.xml\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n\n#mqtt基本配置\nmqtt:\n  enable: false\n  settings: \n    car:\n      clientNum: 3\n      host: tcp://127.0.0.1:1883\n      userName: server_prod\n      password: ${server.port}\n      clientId: server_prod\n      automaticReconnect: true\n      maxReconnectDelay: 60000\n      cleanSession: false\n      maxInFlight: 1000\n      connectTimeout: 15\n      keepAlive: 180\n    example:\n      clientNum: 2\n      host: tcp://127.0.0.1:1883\n      userName: server_prod\n      password: ${server.port}\n      clientId: server_prod\n      automaticReconnect: true\n      maxReconnectDelay: 60000\n      cleanSession: true\n      maxInFlight: 100\n      connectTimeout: 15\n      keepAlive: 10\n\n# springdoc配置\nspringdoc:\n  title: 设备模块接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---\n\n# 不校验白名单\nsecurity:\n  white:\n    custom:\n      urls:\n        # mqtt验证接口\n        - /emqx/authorize\n\n# 租户配置\ntenant:\n  includes:\n    - device\n    - station', '5229ac9e34058c4a6545e634b43b9e84', '2023-06-19 02:28:00', '2024-12-26 15:08:18', 'nacos', '0:0:0:0:0:0:0:1', '', 'wzkris-dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (18, 'wzkris-file.yml', 'wzkris', 'spring:\n  servlet:\n    multipart:\n      # 根据实际需求作调整\n      # 默认最大上传文件大小为15M，单个文件大小\n      max-file-size: 15728640\n      # 默认最大请求大小为30M，总上传的数据大小\n      max-request-size: 31457280\n\n# 本地文件上传    \nlocal:\n  domain: http://127.0.0.1:9300\n  path: /home/wzkris\n  prefix: /uploadPath\n\n# FastDFS配置\nfdfs:\n  domain: http://8.129.231.12\n  soTimeout: 3000\n  connectTimeout: 2000\n  trackerList: 8.129.231.12:22122\n\n# Minio配置\nminio:\n  url: http://8.129.231.12:9000\n  accessKey: minioadmin\n  secretKey: minioadmin\n  bucketName: test', '7202dda5e7f4ef419049f053e5461a2e', '2023-06-19 02:28:00', '2024-07-18 09:07:53', 'nacos', '0:0:0:0:0:0:0:1', '', 'wzkris-dev', '', '', '', 'yaml', '', '');

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
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
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
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
  UNIQUE INDEX `uk_configinfobeta_datagrouptenant`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'config_info_beta' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_beta
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
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
  UNIQUE INDEX `uk_configinfotag_datagrouptenanttag`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC, `tag_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'config_info_tag' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_tag
-- ----------------------------

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation`  (
  `id` bigint NOT NULL COMMENT 'id',
  `tag_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'tenant_id',
  `nid` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`) USING BTREE,
  UNIQUE INDEX `uk_configtagrelation_configidtag`(`id` ASC, `tag_name` ASC, `tag_type` ASC) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'config_tag_relation' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_tags_relation
-- ----------------------------

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Group ID，空字符表示整个集群',
  `quota` int NOT NULL COMMENT '配额，0表示使用默认值',
  `usage` int NOT NULL COMMENT '使用量',
  `max_size` int NOT NULL COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int NOT NULL COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int NOT NULL COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int NOT NULL COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_group_id`(`group_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '集群、各Group容量信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of group_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info`  (
  `id` bigint UNSIGNED NOT NULL,
  `nid` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
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
  PRIMARY KEY (`nid`) USING BTREE,
  INDEX `idx_did`(`data_id` ASC) USING BTREE,
  INDEX `idx_gmt_create`(`gmt_create` ASC) USING BTREE,
  INDEX `idx_gmt_modified`(`gmt_modified` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '多租户改造' ROW_FORMAT = DYNAMIC;

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
INSERT INTO `permissions` VALUES ('prod', 'wzkris-prod:*:*', 'rw');
INSERT INTO `permissions` VALUES ('dev', 'wzkris-dev:*:*', 'rw');

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  UNIQUE INDEX `uk_username_role`(`username` ASC, `role` ASC) USING BTREE
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
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Tenant ID',
  `quota` int NOT NULL COMMENT '配额，0表示使用默认值',
  `usage` int NOT NULL COMMENT '使用量',
  `max_size` int NOT NULL COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int NOT NULL COMMENT '聚合子配置最大个数',
  `max_aggr_size` int NOT NULL COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int NOT NULL COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '租户容量信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tenant_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'tenant_id',
  `tenant_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'tenant_name',
  `tenant_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_info_kptenantid`(`kp` ASC, `tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'tenant_info' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tenant_info
-- ----------------------------
INSERT INTO `tenant_info` VALUES (1, '1', 'wzkris-dev', 'wzkris-dev', '开发环境', 'nacos', 1685070991448, 1685071009622);
INSERT INTO `tenant_info` VALUES (2, '1', 'wzkris-prod', 'wzkris-prod', '生产环境', 'nacos', 1687141596216, 1687141596216);

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
INSERT INTO `users` VALUES ('dev', '$2a$10$0rQC3/xH3czsnLp2qiLJg.Wwi4KMvnVp33eVlRvkOaWd4ITw56Oau', 't');
INSERT INTO `users` VALUES ('nacos', '$2a$10$h05FUQ0x5eypjsF02DWyd.2PXBDgXb7GIXNZCjRim6EORDlTLTRLu', 't');
INSERT INTO `users` VALUES ('prod', '$2a$10$LdUnGcqhQD6DI2OE/WewG.vV5yJjg1hrMA2mhxW/yXEXkTnvdEufm', '1');

SET FOREIGN_KEY_CHECKS = 1;
