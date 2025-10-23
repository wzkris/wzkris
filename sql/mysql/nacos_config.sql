/*
 Navicat Premium Dump SQL

 Source Server         : mysql_local
 Source Server Type    : MySQL
 Source Server Version : 50744 (5.7.44)
 Source Host           : localhost:3306
 Source Schema         : nacos_config

 Target Server Type    : MySQL
 Target Server Version : 50744 (5.7.44)
 File Encoding         : 65001

 Date: 23/10/2025 16:33:43
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
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'config_info' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info` VALUES (1, 'common.yml', 'COMMON_GROUP', 'server:\n  # 优雅停机\n  shutdown: graceful\n\nspring:\n  pid:\n    # 将 PID 写入文件中\n    file: ./${spring.application.name}.pid\n  jackson:\n    time-zone: GMT+8\n    # 日期格式化\n    date-format: yyyy-MM-dd HH:mm:ss\n    serialization:\n      # 格式化输出\n      INDENT_OUTPUT: false\n      # 忽略空Bean转json的错误\n      FAIL_ON_EMPTY_BEANS: false\n      # 关闭日期转换成时间戳\n      WRITE_DATES_AS_TIMESTAMPS: false\n    # 设置空如何序列化\n    defaultPropertyInclusion: ALWAYS\n    deserialization:\n      #json中不存在的属性就报错\n      fail_on_unknown_properties: false\n    parser:\n      # 允许使用无引号字段\n      ALLOW_UNQUOTED_FIELD_NAMES: true\n      # 忽略未定义的属性\n      IGNORE_UNDEFINED: true\n      # 忽略json最后的逗号\n      ALLOW_TRAILING_COMMA: true\n      # 允许反斜杠\n      ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER: true\n      # 允许出现特殊字符和转义符\n      ALLOW_UNQUOTED_CONTROL_CHARS: true\n      # 允许出现单引号\n      ALLOW_SINGLE_QUOTES: true\n      # 是否允许使用注释\n      ALLOW_COMMENTS: true\n    mapper:\n      # 使用getter取代setter探测属性，如类中含getName()但不包含name属性与setName()，传输的vo json格式模板中依旧含name属性\n      USE_GETTERS_AS_SETTERS: true\n  mvc:\n    # mvc找不到对应处理器则抛出异常\n    throw-exception-if-no-handler-found: true\n    # 关闭DispatcherServlet懒加载\n    servlet:\n      load-on-startup: 0\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common\n\n#分布式事务\nseata:\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      ruoyi-system-group: default\n    # 分组和 Seata 服务的映射\n    grouplist:\n      default: 127.0.0.1:8091\n  config:\n    type: nacos\n    nacos:\n      serverAddr: ${spring.cloud.nacos.server-addr}\n      username: ${spring.cloud.nacos.username}\n      password: ${spring.cloud.nacos.password}\n      group: SEATA_GROUP\n      namespace: application-dev\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: ${spring.cloud.nacos.server-addr}\n      username: ${spring.cloud.nacos.username}\n      password: ${spring.cloud.nacos.password}\n      namespace: application-dev\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \"*\"\n    env:\n      access: read_only\n      show-values: always\n    configprops:\n      access: read_only\n      show-values: always\n    beans:\n      access: read_only\n      show-values: always\n\nsignature:\n  keys:\n    wzkris-gateway:\n      secret: my-super-secret-key\n    wzkris-principal:\n      secret: my-super-secret-key-0\n    wzkris-auth:\n      secret: my-super-secret-key-1\n    wzkris-message:\n      secret: my-super-secret-key-2\n    wzkris-monitor-admin:\n      secret: my-super-secret-key-3', '84e2cef7fd8f77184c77c6dd0410d0cc', '2023-06-19 02:28:00', '2024-07-03 05:44:56', 'nacos', '0:0:0:0:0:0:0:1', '', 'application-prod', '公共配置', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (2, 'wzkris-gateway.yml', 'APPLICATION_GROUP', 'spring:\n  cloud:\n    gateway:\n      httpclient:\n        pool:\n          type: FIXED\n          max-connections: 1000\n          acquire-timeout: 80000\n          max-life-time: 30000\n          max-idle-time: 5000\n          connect-timeout: 5000\n          response-timeout: 5000\n          max-request-size: 50MB\n      discovery:\n        locator:\n          lower-case-service-id: true\n          enabled: true\n      routes:\n        # 认证中心\n        - id: wzkris-auth\n          uri: lb://wzkris-auth\n          predicates:\n            - Path=/auth-api/**\n          filters:\n            - StripPrefix=1\n        # 业务主体服务\n        - id: wzkris-principal\n          uri: lb://wzkris-principal\n          predicates:\n            - Path=/prin-api/**\n          filters:\n            - StripPrefix=1\n        # 消息模块\n        - id: wzkris-message\n          uri: lb://wzkris-message\n          predicates:\n            - Path=/msg-api/**\n          filters:\n            - StripPrefix=1\nknife4j:\n  # 聚合swagger文档\n  gateway:\n    enabled: true\n    strategy: discover\n    discover:\n      version: openapi3\n      enabled: true\n      service-config:\n        wzkris-auth:\n          - group-name: 认证服务\n            order: 1\n        wzkris-principal:\n          - group-name: 主体服务\n            order: 1\n        wzkris-message:\n          - group-name: 消息服务\n            order: 2\n    tags-sorter: order\n    operations-sorter: order\n\n# 防止XSS攻击\nxss:\n  enabled: true\n  excludeUrls:\n    - /msg-api/announcement-manage/add\n    - /msg-api/announcement-manage/edit\n\n# 网关放行\nsecurity:\n  ignores:\n    # 验证码放行\n    - /auth-api/captcha/**\n    # 登录接口\n    - /auth-api/login\n    # 登出接口\n    - /auth-api/logout\n    # oauth2接口\n    - /auth-api/oauth2/**\n    # 回调接口\n    - /auth-api/authorization_code_callback\n    # swagger接口放行\n    - /*/v3/api-docs/**\n    # 开放接口放行\n    - /*/open/**', '0494cc09f7f03edb2de84c60186f2c35', '2023-06-19 02:28:00', '2025-10-11 13:58:52', NULL, '0:0:0:0:0:0:0:1', '', 'application-prod', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (3, 'wzkris-auth.yml', 'APPLICATION_GROUP', '# spring配置\nspring:\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common,i18n/auth\n\n# springdoc配置\nspringdoc:\n  title: 认证模块接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---\n\n# 动态线程池\ndynamic-threadpool:\n  tomcatExecutor:\n    threadPoolName: tomcatThreadPool\n    corePoolSize: 500\n    maximumPoolSize: 2000\n    keepAliveTime: 60\n    queueCapacity: 1024', '40df3ea19a4f652fc152bf54ac08b29a', '2023-06-19 02:28:00', '2025-08-04 15:20:48', NULL, '0:0:0:0:0:0:0:1', '', 'application-prod', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (4, 'wzkris-principal.yml', 'APPLICATION_GROUP', '# spring配置\nspring:\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common,i18n/principal\n  # datasource:\n  #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver\n  #   url: jdbc:shardingsphere:classpath:sharding-${spring.profiles.active}.yml\n  datasource:\n    url: jdbc:postgresql://localhost:5432/wzkris_principal?ssl=false&reWriteBatchedInserts=true&stringtype=unspecified\n    username: root\n    password: root\n    driver-class-name: org.postgresql.Driver\n    hikari:\n      connection-timeout: 30000 \n      maximum-pool-size: 10       \n      minimum-idle: 5             \n      idle-timeout: 600000        \n      pool-name: hikari-pool\n\nmybatis-plus:\n  # 搜索指定包别名\n  typeAliasesPackage: com.wzkris.principal.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/*/*.xml\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n\n# springdoc配置\nspringdoc:\n  title: 用户模块接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---\n\n# 租户配置\ntenant:\n  includes:\n    - t_staff_info\n    - t_post_info\n    - tenant_info\n    - tenant_wallet_info\n    - tenant_wallet_record\n    - tenant_wallet_withdrawal_record', 'e24059eb405fcdeaf77e2e45d7bd4738', '2024-04-16 01:03:03', '2025-08-04 15:20:57', NULL, '0:0:0:0:0:0:0:1', '', 'application-prod', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (5, 'wzkris-monitor-admin.yml', 'APPLICATION_GROUP', '# spring\nspring:\n  security:\n    user:\n      name: admin\n      password: admin123\n  boot:\n    admin:\n      ui:\n        title: 服务状态监控\n', 'dd19c14e3cebc473140e1fc8733a339d', '2023-06-19 02:28:00', '2023-06-19 02:28:00', NULL, '0:0:0:0:0:0:0:1', '', 'application-prod', '', NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (6, 'wzkris-message.yml', 'APPLICATION_GROUP', '# spring配置\nspring:\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common,i18n/message\n  # datasource:\n  #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver\n  #   url: jdbc:shardingsphere:classpath:sharding-${spring.profiles.active}.yml\n  datasource:\n    url: jdbc:postgresql://localhost:5432/wzkris_message?ssl=false&reWriteBatchedInserts=true&stringtype=unspecified\n    username: root\n    password: root\n    driver-class-name: org.postgresql.Driver\n    hikari:\n      connection-timeout: 30000 \n      maximum-pool-size: 10       \n      minimum-idle: 5             \n      idle-timeout: 600000        \n      pool-name: hikari-pool\n\nmybatis-plus:\n  # 搜索指定包别名\n  typeAliasesPackage: com.wzkris.**.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/*/*.xml\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n\n# 租户配置\ntenant:\n  includes:\n    - staff_login_log\n    - staff_operate_log\n\n# springdoc配置\nspringdoc:\n  title: 消息模块接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---\n', '9ac8b66b4d894677a47369917807616d', '2023-06-19 02:28:00', '2025-08-04 15:21:22', NULL, '0:0:0:0:0:0:0:1', '', 'application-prod', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (7, 'sentinel-gateway', 'APPLICATION_GROUP', '[\n    {\n        \"resource\": \"wzkris-auth\",\n        \"count\": 2000,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n	{\n        \"resource\": \"wzkris-message\",\n        \"count\": 1000,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n    {\n        \"resource\": \"wzkris-principal\",\n        \"count\": 1000,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n    {\n        \"resource\": \"wzkris-moniter\",\n        \"count\": 300,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    }\n]', 'f22df3d1c7b32ea8f54b6d615e53d2c2', '2023-06-19 02:28:00', '2025-10-11 13:51:48', NULL, '0:0:0:0:0:0:0:1', '', 'application-prod', '网关限流策略', '', '', 'json', '', '');
INSERT INTO `config_info` VALUES (10, 'redis.yml', 'COMMON_GROUP', 'spring:\r\n  redis:\r\n    redisson: \r\n      config: |\r\n        # 单节点配置\r\n        singleServerConfig: \r\n          # redis 节点地址\r\n          address: \"redis://127.0.0.1:6379\"\r\n          # 无密码则设置 null\r\n          password: null\r\n          # 客户端名称\r\n          clientName: ${spring.application.name}\r\n          # 最小空闲连接数\r\n          connectionMinimumIdleSize: 32\r\n          # 连接池大小\r\n          connectionPoolSize: 64\r\n          # 连接空闲超时,单位:毫秒\r\n          idleConnectionTimeout: 10000\r\n          # 命令等待超时,单位:毫秒\r\n          timeout: 3000\r\n          # 发布和订阅连接池大小\r\n          subscriptionConnectionPoolSize: 50\r\n        # 线程池数量\r\n        threads: 8\r\n        # Netty线程池数量\r\n        nettyThreads: 8\r\n        codec: !<org.redisson.codec.JsonJacksonCodec> {}\r\n        transportMode: \"NIO\"\r\n', 'd19d9cfcd786295407bfb79f4365c633', '2025-08-04 15:19:50', '2025-08-04 15:19:50', NULL, '0:0:0:0:0:0:0:1', '', 'application-prod', NULL, NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (11, 'thread.yml', 'COMMON_GROUP', 'dynamic-threadpool:\n  tpExecutors:\n  - threadPoolName: t11\n    threadNamePrefix: AsyncThreadPool-\n    corePoolSize: 1\n    maximumPoolSize: 100\n    keepAliveTime: 60', '151e9dfda9bedff83a127267b4e7abf3', '2025-08-11 09:55:20', '2025-08-13 10:04:29', NULL, '0:0:0:0:0:0:0:1', '', 'application-prod', '动态线程池', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (12, 'common.yml', 'COMMON_GROUP', 'server:\n  # 优雅停机\n  shutdown: graceful\n\nspring:\n  pid:\n    # 将 PID 写入文件中\n    file: ./${spring.application.name}.pid\n  jackson:\n    time-zone: GMT+8\n    # 日期格式化\n    date-format: yyyy-MM-dd HH:mm:ss\n    serialization:\n      # 格式化输出\n      INDENT_OUTPUT: false\n      # 忽略空Bean转json的错误\n      FAIL_ON_EMPTY_BEANS: false\n      # 关闭日期转换成时间戳\n      WRITE_DATES_AS_TIMESTAMPS: false\n    # 设置空如何序列化\n    defaultPropertyInclusion: ALWAYS\n    deserialization:\n      #json中不存在的属性就报错\n      fail_on_unknown_properties: false\n    parser:\n      # 允许使用无引号字段\n      ALLOW_UNQUOTED_FIELD_NAMES: true\n      # 忽略未定义的属性\n      IGNORE_UNDEFINED: true\n      # 忽略json最后的逗号\n      ALLOW_TRAILING_COMMA: true\n      # 允许反斜杠\n      ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER: true\n      # 允许出现特殊字符和转义符\n      ALLOW_UNQUOTED_CONTROL_CHARS: true\n      # 允许出现单引号\n      ALLOW_SINGLE_QUOTES: true\n      # 是否允许使用注释\n      ALLOW_COMMENTS: true\n    mapper:\n      # 使用getter取代setter探测属性，如类中含getName()但不包含name属性与setName()，传输的vo json格式模板中依旧含name属性\n      USE_GETTERS_AS_SETTERS: true\n  mvc:\n    # mvc找不到对应处理器则抛出异常\n    throw-exception-if-no-handler-found: true\n    # 关闭DispatcherServlet懒加载\n    servlet:\n      load-on-startup: 0\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common\n\n#分布式事务\nseata:\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      ruoyi-system-group: default\n    # 分组和 Seata 服务的映射\n    grouplist:\n      default: 127.0.0.1:8091\n  config:\n    type: nacos\n    nacos:\n      serverAddr: ${spring.cloud.nacos.server-addr}\n      username: ${spring.cloud.nacos.username}\n      password: ${spring.cloud.nacos.password}\n      group: SEATA_GROUP\n      namespace: application-dev\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: ${spring.cloud.nacos.server-addr}\n      username: ${spring.cloud.nacos.username}\n      password: ${spring.cloud.nacos.password}\n      namespace: application-dev\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \"*\"\n    env:\n      access: read_only\n      show-values: always\n    configprops:\n      access: read_only\n      show-values: always\n    beans:\n      access: read_only\n      show-values: always\n\nsignature:\n  keys:\n    wzkris-gateway:\n      secret: my-super-secret-key\n    wzkris-principal:\n      secret: my-super-secret-key-0\n    wzkris-auth:\n      secret: my-super-secret-key-1\n    wzkris-message:\n      secret: my-super-secret-key-2\n    wzkris-monitor-admin:\n      secret: my-super-secret-key-3', '08af95538faa31380d1f55ad430a383f', '2023-06-19 02:28:00', '2025-10-15 17:15:48', NULL, '0:0:0:0:0:0:0:1', '', 'application-dev', '公共配置', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (13, 'wzkris-gateway.yml', 'APPLICATION_GROUP', 'spring:\n  cloud:\n    gateway:\n      httpclient:\n        pool:\n          type: FIXED\n          max-connections: 1000\n          acquire-timeout: 80000\n          max-life-time: 30000\n          max-idle-time: 5000\n          connect-timeout: 5000\n          response-timeout: 5000\n          max-request-size: 50MB\n      discovery:\n        locator:\n          lower-case-service-id: true\n          enabled: true\n      routes:\n        # 认证中心\n        - id: wzkris-auth\n          uri: lb://wzkris-auth\n          predicates:\n            - Path=/auth-api/**\n          filters:\n            - StripPrefix=1\n        # 业务主体服务\n        - id: wzkris-principal\n          uri: lb://wzkris-principal\n          predicates:\n            - Path=/prin-api/**\n          filters:\n            - StripPrefix=1\n        # 消息模块\n        - id: wzkris-message\n          uri: lb://wzkris-message\n          predicates:\n            - Path=/msg-api/**\n          filters:\n            - StripPrefix=1\nknife4j:\n  # 聚合swagger文档\n  gateway:\n    enabled: true\n    strategy: discover\n    discover:\n      version: openapi3\n      enabled: true\n      service-config:\n        wzkris-auth:\n          - group-name: 认证服务\n            order: 1\n        wzkris-principal:\n          - group-name: 主体服务\n            order: 1\n        wzkris-message:\n          - group-name: 消息服务\n            order: 2\n    tags-sorter: order\n    operations-sorter: order\n\n# 防止XSS攻击\nxss:\n  enabled: true\n  excludeUrls:\n    - /msg-api/announcement-manage/add\n    - /msg-api/announcement-manage/edit\n\n# 网关放行\nsecurity:\n  ignores:\n    # 验证码放行\n    - /auth-api/captcha/**\n    # 登录接口\n    - /auth-api/login\n    # 登出接口\n    - /auth-api/logout\n    # oauth2接口\n    - /auth-api/oauth2/**\n    # 回调接口\n    - /auth-api/authorization_code_callback\n    # swagger接口放行\n    - /*/v3/api-docs/**\n    # 开放接口放行\n    - /*/open/**', '581a78fc437a6d21204bc35d3931d80f', '2023-06-19 02:28:00', '2025-10-13 17:00:19', NULL, '0:0:0:0:0:0:0:1', '', 'application-dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (14, 'wzkris-auth.yml', 'APPLICATION_GROUP', '# spring配置\nspring:\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common,i18n/auth\n\n# springdoc配置\nspringdoc:\n  title: 认证模块接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---\n\n# 动态线程池\ndynamic-threadpool:\n  tomcatExecutor:\n    threadPoolName: tomcatThreadPool\n    corePoolSize: 500\n    maximumPoolSize: 2000\n    keepAliveTime: 60\n    queueCapacity: 1024', '4fe219696471ae53a4dda533573a101c', '2023-06-19 02:28:00', '2025-09-29 10:38:38', NULL, '0:0:0:0:0:0:0:1', '', 'application-dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (15, 'wzkris-principal.yml', 'APPLICATION_GROUP', '# spring配置\nspring:\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common,i18n/principal\n  # datasource:\n  #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver\n  #   url: jdbc:shardingsphere:classpath:sharding-${spring.profiles.active}.yml\n  datasource:\n    url: jdbc:postgresql://localhost:5432/wzkris_principal?ssl=false&reWriteBatchedInserts=true&stringtype=unspecified\n    username: root\n    password: root\n    driver-class-name: org.postgresql.Driver\n    hikari:\n      connection-timeout: 30000 \n      maximum-pool-size: 10       \n      minimum-idle: 5             \n      idle-timeout: 600000        \n      pool-name: hikari-pool\n\nmybatis-plus:\n  # 搜索指定包别名\n  typeAliasesPackage: com.wzkris.principal.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/*/*.xml\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n\n# springdoc配置\nspringdoc:\n  title: 用户模块接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---\n\n# 租户配置\ntenant:\n  includes:\n    - t_staff_info\n    - t_post_info\n    - tenant_info\n    - tenant_wallet_info\n    - tenant_wallet_record\n    - tenant_wallet_withdrawal_record', 'ceb557eecb247e4abcad42e03f3f5637', '2024-04-16 06:36:22', '2025-10-13 17:04:23', NULL, '0:0:0:0:0:0:0:1', '', 'application-dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (16, 'wzkris-monitor-admin.yml', 'APPLICATION_GROUP', '# spring\nspring:\n  security:\n    user:\n      name: admin\n      password: admin123\n  boot:\n    admin:\n      ui:\n        title: 服务状态监控\n', 'dd19c14e3cebc473140e1fc8733a339d', '2023-06-19 02:28:00', '2025-09-29 11:55:07', NULL, '0:0:0:0:0:0:0:1', '', 'application-dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (17, 'wzkris-message.yml', 'APPLICATION_GROUP', '# spring配置\nspring:\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common,i18n/message\n  # datasource:\n  #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver\n  #   url: jdbc:shardingsphere:classpath:sharding-${spring.profiles.active}.yml\n  datasource:\n    url: jdbc:postgresql://localhost:5432/wzkris_message?ssl=false&reWriteBatchedInserts=true&stringtype=unspecified\n    username: root\n    password: root\n    driver-class-name: org.postgresql.Driver\n    hikari:\n      connection-timeout: 30000 \n      maximum-pool-size: 10       \n      minimum-idle: 5             \n      idle-timeout: 600000        \n      pool-name: hikari-pool\n\nmybatis-plus:\n  # 搜索指定包别名\n  typeAliasesPackage: com.wzkris.**.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/*/*.xml\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n\n# 租户配置\ntenant:\n  includes:\n    - staff_login_log\n    - staff_operate_log\n\n# springdoc配置\nspringdoc:\n  title: 消息模块接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---\n', '9621807f89e27a4109c4aba7a728793f', '2023-06-19 02:28:00', '2025-10-14 10:27:19', NULL, '0:0:0:0:0:0:0:1', '', 'application-dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (18, 'sentinel-gateway', 'APPLICATION_GROUP', '[\n    {\n        \"resource\": \"wzkris-auth\",\n        \"count\": 2000,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n	{\n        \"resource\": \"wzkris-message\",\n        \"count\": 1000,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n    {\n        \"resource\": \"wzkris-principal\",\n        \"count\": 1000,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n    {\n        \"resource\": \"wzkris-moniter\",\n        \"count\": 300,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    }\n]', '292c61f0dbd26dd6618e6b6d59a1ead5', '2023-06-19 02:28:00', '2025-10-13 17:07:42', NULL, '0:0:0:0:0:0:0:1', '', 'application-dev', '网关限流策略', '', '', 'json', '', '');
INSERT INTO `config_info` VALUES (21, 'redis.yml', 'COMMON_GROUP', 'spring:\r\n  redis:\r\n    redisson: \r\n      config: |\r\n        # 单节点配置\r\n        singleServerConfig: \r\n          # redis 节点地址\r\n          address: \"redis://127.0.0.1:6379\"\r\n          # 无密码则设置 null\r\n          password: null\r\n          # 客户端名称\r\n          clientName: ${spring.application.name}\r\n          # 最小空闲连接数\r\n          connectionMinimumIdleSize: 32\r\n          # 连接池大小\r\n          connectionPoolSize: 64\r\n          # 连接空闲超时,单位:毫秒\r\n          idleConnectionTimeout: 10000\r\n          # 命令等待超时,单位:毫秒\r\n          timeout: 3000\r\n          # 发布和订阅连接池大小\r\n          subscriptionConnectionPoolSize: 50\r\n        # 线程池数量\r\n        threads: 8\r\n        # Netty线程池数量\r\n        nettyThreads: 8\r\n        codec: !<org.redisson.codec.JsonJacksonCodec> {}\r\n        transportMode: \"NIO\"\r\n', 'd19d9cfcd786295407bfb79f4365c633', '2025-08-04 15:12:22', '2025-10-23 11:15:10', NULL, '0:0:0:0:0:0:0:1', '', 'application-dev', 'redis公共配置', NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (22, 'thread.yml', 'COMMON_GROUP', 'dynamic-threadpool:\n  tpExecutors:\n  - threadPoolName: t11\n    threadNamePrefix: AsyncThreadPool-\n    corePoolSize: 1\n    maximumPoolSize: 100\n    keepAliveTime: 60', '65887fb38e2d3b82f66bcabded041f80', '2025-08-11 09:55:20', '2025-09-26 15:03:25', NULL, '0:0:0:0:0:0:0:1', '', 'application-dev', '动态线程池', '', '', 'yaml', '', '');

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'config_info_gray' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '多租户改造' ROW_FORMAT = DYNAMIC;

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
INSERT INTO `permissions` VALUES ('prod', ':*:*', 'rw');
INSERT INTO `permissions` VALUES ('dev', 'application-dev:*:*', 'rw');
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
