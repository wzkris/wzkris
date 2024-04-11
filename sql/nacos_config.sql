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

 Date: 16/04/2024 14:41:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
CREATE DATABASE IF NOT EXISTS nacos_config;
USE nacos_config;
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户字段',
  `c_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `c_use` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `effect` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `c_schema` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `encrypted_data_key` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfo_datagrouptenant`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'config_info' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info` VALUES (1, 'application-common.yml', 'thingslink', 'spring:\n  jackson:\n    time-zone: GMT+8\n    # 日期格式化\n    date-format: yyyy-MM-dd HH:mm:ss\n    serialization:\n      # 格式化输出\n      INDENT_OUTPUT: false\n      # 忽略空Bean转json的错误\n      FAIL_ON_EMPTY_BEANS: false\n      # 关闭日期转换成时间戳\n      WRITE_DATES_AS_TIMESTAMPS: false\n    # 设置空如何序列化\n    defaultPropertyInclusion: ALWAYS\n    deserialization:\n      #json中不存在的属性就报错\n      fail_on_unknown_properties: false\n    parser:\n      # 允许使用无引号字段\n      ALLOW_UNQUOTED_FIELD_NAMES: true\n      # 忽略未定义的属性\n      IGNORE_UNDEFINED: true\n      # 忽略json最后的逗号\n      ALLOW_TRAILING_COMMA: true\n      # 允许反斜杠\n      ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER: true\n      # 允许出现特殊字符和转义符\n      ALLOW_UNQUOTED_CONTROL_CHARS: true\n      # 允许出现单引号\n      ALLOW_SINGLE_QUOTES: true\n      # 是否允许使用注释\n      ALLOW_COMMENTS: true\n    mapper:\n      # 使用getter取代setter探测属性，如类中含getName()但不包含name属性与setName()，传输的vo json格式模板中依旧含name属性\n      USE_GETTERS_AS_SETTERS: true\n  mvc:\n    # mvc找不到对应处理器则抛出异常\n    throw-exception-if-no-handler-found: true\n    # 关闭DispatcherServlet懒加载\n    servlet:\n      load-on-startup: 0\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common\n  cloud:\n    openfeign:\n      # 请求使用okhttp\n      okhttp:\n        enabled: true\n      # openfeign超时配置\n      client:\n        config:\n          default:\n            connectTimeout: 10000\n            readTimeout: 10000\n            loggerLevel: basic\n\n# feign启用sentinel\nfeign:\n  sentinel:\n    enabled: true\n\n#分布式事务\nseata:\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      ruoyi-system-group: default\n    # 分组和 Seata 服务的映射\n    grouplist:\n      default: 127.0.0.1:8091\n  config:\n    type: nacos\n    nacos:\n      serverAddr: ${spring.cloud.nacos.server-addr}\n      username: ${spring.cloud.nacos.username}\n      password: ${spring.cloud.nacos.password}\n      group: SEATA_GROUP\n      namespace: f06f7669-9836-4413-86cf-9f44ae5c9f30\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: ${spring.cloud.nacos.server-addr}\n      username: ${spring.cloud.nacos.username}\n      password: ${spring.cloud.nacos.password}\n      namespace: f06f7669-9836-4413-86cf-9f44ae5c9f30\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \"*\"\n\n# 不校验白名单\nsecurity:\n  ip:\n    enable: true\n    ipList:\n      - \n  white:\n    common:\n      urls:\n        #内部调用放行\n        - /inner/**\n        #spring监控服务放行\n        - /actuator/**\n        #swagger接口放行\n        - /v3/api-docs/**\n        - /webjars/**\n        #开放接口放行\n        - /open/**\n        #上传图片可访问\n        - /uploadPath/**\n        #异常处理\n        - /error', '1f86f156ce22c4b74f9fd644f3e6b678', '2023-06-19 02:28:00', '2024-04-16 01:23:46', 'nacos', '0:0:0:0:0:0:0:1', '', 'prod', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (2, 'thingslink-gateway.yml', 'thingslink', 'spring:\n  cloud:\n    gateway:\n      httpclient:\n        pool:\n          type: FIXED\n          max-connections: 1000\n          acquire-timeout: 80000\n          max-life-time: 30000\n          max-idle-time: 5000\n          connect-timeout: 5000\n          response-timeout: 5000\n          max-request-size: 50MB\n      discovery:\n        locator:\n          lower-case-service-id: true\n          enabled: true\n      routes:\n        # 认证中心\n        - id: thingslink-auth\n          uri: lb://thingslink-auth\n          predicates:\n            - Path=/auth/**\n          filters:\n            - CacheRequestFilter\n            - StripPrefix=1\n            # 黑名单过滤器\n            - name: BlackListUrlFilter\n              args:\n                blacklistUrl:\n                - /nacos/test\n        # 用户服务\n        - id: thingslink-user\n          uri: lb://thingslink-user\n          predicates:\n            - Path=/ur/**\n          filters:\n            - StripPrefix=1\n        # 系统模块\n        - id: thingslink-system\n          uri: lb://thingslink-system\n          predicates:\n            - Path=/sys_biz/**\n          filters:\n            - StripPrefix=1\n        # 设备模块\n        - id: thingslink-equipment\n          uri: lb://thingslink-equipment\n          predicates:\n            - Path=/eqp_biz/**\n          filters:\n            - StripPrefix=1\n        # 订单模块\n        - id: thingslink-order\n          uri: lb://thingslink-order\n          predicates:\n            - Path=/ord_biz/**\n          filters:\n            - StripPrefix=1\n        # 文件服务\n        - id: thingslink-file\n          uri: lb://thingslink-file\n          predicates:\n            - Path=/fl_biz/**\n          filters:\n            - StripPrefix=1\n        # 监控服务\n        - id: thingslink-monitor\n          uri: lb://thingslink-monitor\n          predicates:\n            - Path=/moniter/**\n          filters:\n            - StripPrefix=1\n\nknife4j:\n  # 聚合swagger文档\n  gateway:\n    enabled: true\n    strategy: discover\n    discover:\n      version: openapi3\n      enabled: true\n      service-config:\n        thingslink-auth:\n          - group-name: 认证服务\n            order: 1\n        thingslink-user:\n          - group-name: 用户服务\n            order: 1\n        thingslink-system:\n          - group-name: 系统服务\n            order: 2\n        thingslink-order:\n          - group-name: 订单服务\n            order: 3\n        thingslink-equipment:\n          - group-name: 设备服务\n            order: 4\n        thingslink-file:\n          - group-name: 文件服务\n            order: 5\n        thingslink-job:\n          - group-name: 定时任务服务\n            order: 6\n    tags-sorter: order\n    operations-sorter: order\n\n# 防止XSS攻击\nxss:\n  enabled: true\n  excludeUrls:\n    - /system/notice\n', '29ac47ec5200a9e90bedfa8ca6ce4050', '2023-06-19 02:28:00', '2024-04-16 01:38:27', 'nacos', '0:0:0:0:0:0:0:1', '', 'prod', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (3, 'thingslink-auth.yml', 'thingslink', '# spring配置\nspring:\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common,i18n/auth\n\nredisson:\n  # 线程池数量\n  threads: 16\n  # Netty线程池数量\n  nettyThreads: 32\n  # 单节点配置\n  singleServerConfig:\n    # redis 节点地址\n    address: \"redis://127.0.0.1:6379\"\n    # 无密码则设置 null\n    password: \'\'\n    # 客户端名称\n    clientName: ${spring.application.name}\n    # 最小空闲连接数\n    connectionMinimumIdleSize: 32\n    # 连接池大小\n    connectionPoolSize: 64\n    # 连接空闲超时,单位:毫秒\n    idleConnectionTimeout: 10000\n    # 命令等待超时,单位:毫秒\n    timeout: 3000\n    # 发布和订阅连接池大小\n    subscriptionConnectionPoolSize: 50\n\n# springdoc配置\nspringdoc:\n  title: 认证模块接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---\n\n# 安全配置\nsecurity:\n  # 验证码\n  captcha:\n    enabled: false\n    type: math\n  # 不校验白名单\n  white:\n    custom:\n      urls:\n        #验证码放行\n        - /code\n        - /sms_code\n\n# token配置\ntoken-config:\n  accessTokenTimeOut: 1800\n  refreshTokenTimeOut: 86400\n  authorizationCodeTimeOut: 180\n  deviceCodeTimeOut: 180\n', 'bc2022de6cae88df095c9f5a19fe0d1b', '2023-06-19 02:28:00', '2024-04-16 01:23:59', 'nacos', '0:0:0:0:0:0:0:1', '', 'prod', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (4, 'thingslink-user.yml', 'thingslink', '# spring配置\nspring:\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common\n  # datasource:\n  #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver\n  #   url: jdbc:shardingsphere:classpath:sharding-${spring.profiles.active}.yml\n  datasource:\n    url: jdbc:mysql://localhost:3306/thingslink_user?rewriteBatchedStatements=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai\n    username: root\n    password: root\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    hikari:\n      connection-timeout: 30000 \n      maximum-pool-size: 10       \n      minimum-idle: 5             \n      idle-timeout: 600000        \n      pool-name: hikari-pool\n\nmybatis-plus:\n  # 搜索指定包别名\n  typeAliasesPackage: com.thingslink.user.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/user/*.xml\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n  global-config:\n    db-config:\n      # 逻辑删除\n      logic-delete-field: isDeleted \n\nredisson:\n  # 线程池数量\n  threads: 16\n  # Netty线程池数量\n  nettyThreads: 32\n  # 单节点配置\n  singleServerConfig:\n    # redis 节点地址\n    address: \"redis://127.0.0.1:6379\"\n    # 无密码则设置 null\n    password: \'\'\n    # 客户端名称\n    clientName: ${spring.application.name}\n    # 最小空闲连接数\n    connectionMinimumIdleSize: 32\n    # 连接池大小\n    connectionPoolSize: 64\n    # 连接空闲超时,单位:毫秒\n    idleConnectionTimeout: 10000\n    # 命令等待超时,单位:毫秒\n    timeout: 3000\n    # 发布和订阅连接池大小\n    subscriptionConnectionPoolSize: 50\n\n# springdoc配置\nspringdoc:\n  title: 用户模块接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---\n\n# 租户配置\ntenant:\n  includes:\n    - sys_dept\n    - sys_post\n    - sys_role\n    - sys_tenant\n    - sys_user', '06829d0a0358805c93dff01a4e93070c', '2024-04-16 01:03:03', '2024-04-16 06:32:55', 'nacos', '0:0:0:0:0:0:0:1', '', 'prod', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (5, 'thingslink-monitor.yml', 'thingslink', '# spring\nspring:\n  security:\n    user:\n      name: admin\n      password: admin123\n  boot:\n    admin:\n      ui:\n        title: 服务状态监控\n', 'dd19c14e3cebc473140e1fc8733a339d', '2023-06-19 02:28:00', '2023-06-19 02:28:00', NULL, '0:0:0:0:0:0:0:1', '', 'prod', '', NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (6, 'thingslink-system.yml', 'thingslink', '# spring配置\nspring:\n  # datasource:\n  #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver\n  #   url: jdbc:shardingsphere:classpath:sharding-${spring.profiles.active}.yml\n  datasource:\n    url: jdbc:mysql://localhost:3306/thingslink_system?rewriteBatchedStatements=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai\n    username: root\n    password: root\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    hikari:\n      connection-timeout: 30000 \n      maximum-pool-size: 10       \n      minimum-idle: 5             \n      idle-timeout: 600000        \n      pool-name: hikari-pool\n\nredisson:\n  # 线程池数量\n  threads: 16\n  # Netty线程池数量\n  nettyThreads: 32\n  # 单节点配置\n  singleServerConfig:\n    # redis 节点地址\n    address: \"redis://127.0.0.1:6379\"\n    # 无密码则设置 null\n    password: null\n    # 客户端名称\n    clientName: ${spring.application.name}\n    # 最小空闲连接数\n    connectionMinimumIdleSize: 32\n    # 连接池大小\n    connectionPoolSize: 64\n    # 连接空闲超时,单位:毫秒\n    idleConnectionTimeout: 10000\n    # 命令等待超时,单位:毫秒\n    timeout: 3000\n    # 发布和订阅连接池大小\n    subscriptionConnectionPoolSize: 50\n\nmybatis-plus:\n  # 搜索指定包别名\n  typeAliasesPackage: com.thingslink.**.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/system/*.xml\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n\n# 租户配置\ntenant:\n  includes:\n    - sys_login_log\n    - sys_oper_log\n\n# springdoc配置\nspringdoc:\n  title: 系统模块接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---\n', 'a3810e3c537693e04b7384ef5cd00245', '2023-06-19 02:28:00', '2024-04-12 04:46:18', 'nacos', '0:0:0:0:0:0:0:1', '', 'prod', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (7, 'sentinel-thingslink-gateway', 'thingslink', '[\n    {\n        \"resource\": \"thingslink-auth\",\n        \"count\": 2000,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n	{\n        \"resource\": \"thingslink-system\",\n        \"count\": 1000,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n    {\n        \"resource\": \"thingslink-order\",\n        \"count\": 1,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n	{\n        \"resource\": \"thingslink-job\",\n        \"count\": 300,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n    {\n        \"resource\": \"thingslink-moniter\",\n        \"count\": 300,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    }\n]', '2df93928245e7593c3d5c2be233c4f15', '2023-06-19 02:28:00', '2023-12-08 01:03:54', 'nacos', '0:0:0:0:0:0:0:1', '', 'prod', '网关限流策略', '', '', 'json', '', '');
INSERT INTO `config_info` VALUES (8, 'thingslink-equipment.yml', 'thingslink', '# spring配置\nspring:\n  # datasource:\n  #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver\n  #   url: jdbc:shardingsphere:classpath:sharding-${spring.profiles.active}.yml\n  datasource:\n    url: jdbc:mysql://localhost:3306/thingslink_equipment?rewriteBatchedStatements=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai\n    username: root\n    password: root\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    hikari:\n      connection-timeout: 30000 \n      maximum-pool-size: 10       \n      minimum-idle: 5             \n      idle-timeout: 600000        \n      pool-name: hikari-pool\n\nredisson:\n  # 线程池数量\n  threads: 16\n  # Netty线程池数量\n  nettyThreads: 32\n  # 单节点配置\n  singleServerConfig:\n    # redis 节点地址\n    address: \"redis://127.0.0.1:6379\"\n    # 无密码则设置 null\n    password: \'\'\n    # 客户端名称\n    clientName: ${spring.application.name}\n    # 最小空闲连接数\n    connectionMinimumIdleSize: 32\n    # 连接池大小\n    connectionPoolSize: 64\n    # 连接空闲超时,单位:毫秒\n    idleConnectionTimeout: 10000\n    # 命令等待超时,单位:毫秒\n    timeout: 3000\n    # 发布和订阅连接池大小\n    subscriptionConnectionPoolSize: 50\n\nmybatis-plus:\n  # 搜索指定包别名\n  typeAliasesPackage: com.thingslink.**.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/equipment/*.xml\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n#mqtt基本配置\nmqtt:\n  host: tcp://127.0.0.1:1883\n  userName: server_prod\n  password: ${server.port}\n  clientId: server_prod\n  timeout: 5000\n  keepAlive: 300\n  \n# springdoc配置\nspringdoc:\n  title: 设备模块接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---\n', '37ad642c46cfc313dca4ac2887ca20b3', '2023-06-19 02:28:00', '2024-01-08 08:33:56', 'nacos', '0:0:0:0:0:0:0:1', '', 'prod', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (9, 'thingslink-file.yml', 'thingslink', 'spring:\n  servlet:\n    multipart:\n      # 根据实际需求作调整\n      # 默认最大上传文件大小为15M，单个文件大小\n      max-file-size: 15728640\n      # 默认最大请求大小为30M，总上传的数据大小\n      max-request-size: 31457280\n\nredisson:\n  # 线程池数量\n  threads: 16\n  # Netty线程池数量\n  nettyThreads: 32\n  # 单节点配置\n  singleServerConfig:\n    # redis 节点地址\n    address: \"redis://127.0.0.1:6379\"\n    # 无密码则设置 null\n    password: \'\'\n    # 客户端名称\n    clientName: ${spring.application.name}\n    # 最小空闲连接数\n    connectionMinimumIdleSize: 32\n    # 连接池大小\n    connectionPoolSize: 64\n    # 连接空闲超时,单位:毫秒\n    idleConnectionTimeout: 10000\n    # 命令等待超时,单位:毫秒\n    timeout: 3000\n    # 发布和订阅连接池大小\n    subscriptionConnectionPoolSize: 50\n\n# 本地文件上传    \nlocal:\n  domain: http://127.0.0.1:9300\n  path: /home/thingslink\n  prefix: /uploadPath\n\n# FastDFS配置\nfdfs:\n  domain: http://8.129.231.12\n  soTimeout: 3000\n  connectTimeout: 2000\n  trackerList: 8.129.231.12:22122\n\n# Minio配置\nminio:\n  url: http://8.129.231.12:9000\n  accessKey: minioadmin\n  secretKey: minioadmin\n  bucketName: test', '4ba4b6410304cd769e18ff12a6f0150c', '2023-06-19 02:28:00', '2023-09-15 05:14:26', 'nacos', '115.221.135.11', '', 'prod', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (10, 'thingslink-order.yml', 'thingslink', '# spring配置\nspring:\n  # datasource:\n  #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver\n  #   url: jdbc:shardingsphere:classpath:sharding-${spring.profiles.active}.yml\n  datasource:\n    url: jdbc:mysql://localhost:3306/thingslink_order?rewriteBatchedStatements=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai\n    username: root\n    password: root\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    hikari:\n      connection-timeout: 30000 \n      maximum-pool-size: 10       \n      minimum-idle: 5             \n      idle-timeout: 600000        \n      pool-name: hikari-pool\n\nredisson:\n  # 线程池数量\n  threads: 16\n  # Netty线程池数量\n  nettyThreads: 32\n  # 单节点配置\n  singleServerConfig:\n    # redis 节点地址\n    address: \"redis://127.0.0.1:6379\"\n    # 无密码则设置 null\n    password: \'\'\n    # 客户端名称\n    clientName: ${spring.application.name}\n    # 最小空闲连接数\n    connectionMinimumIdleSize: 32\n    # 连接池大小\n    connectionPoolSize: 64\n    # 连接空闲超时,单位:毫秒\n    idleConnectionTimeout: 10000\n    # 命令等待超时,单位:毫秒\n    timeout: 3000\n    # 发布和订阅连接池大小\n    subscriptionConnectionPoolSize: 50\n\nmybatis-plus:\n  # 搜索指定包别名\n  typeAliasesPackage: com.thingslink.**.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/order/*.xml\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n# springdoc配置\nspringdoc:\n  title: 订单模块接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---', 'b97a7dae64eabc75b662c98ec231496f', '2023-08-07 05:20:04', '2024-01-08 08:34:20', 'nacos', '0:0:0:0:0:0:0:1', '', 'prod', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (13, 'application-common.yml', 'thingslink', 'spring:\n  jackson:\n    time-zone: GMT+8\n    # 日期格式化\n    date-format: yyyy-MM-dd HH:mm:ss\n    serialization:\n      # 格式化输出\n      INDENT_OUTPUT: false\n      # 忽略空Bean转json的错误\n      FAIL_ON_EMPTY_BEANS: false\n      # 关闭日期转换成时间戳\n      WRITE_DATES_AS_TIMESTAMPS: false\n    # 设置空如何序列化\n    defaultPropertyInclusion: ALWAYS\n    deserialization:\n      #json中不存在的属性就报错\n      fail_on_unknown_properties: false\n    parser:\n      # 允许使用无引号字段\n      ALLOW_UNQUOTED_FIELD_NAMES: true\n      # 忽略未定义的属性\n      IGNORE_UNDEFINED: true\n      # 忽略json最后的逗号\n      ALLOW_TRAILING_COMMA: true\n      # 允许反斜杠\n      ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER: true\n      # 允许出现特殊字符和转义符\n      ALLOW_UNQUOTED_CONTROL_CHARS: true\n      # 允许出现单引号\n      ALLOW_SINGLE_QUOTES: true\n      # 是否允许使用注释\n      ALLOW_COMMENTS: true\n    mapper:\n      # 使用getter取代setter探测属性，如类中含getName()但不包含name属性与setName()，传输的vo json格式模板中依旧含name属性\n      USE_GETTERS_AS_SETTERS: true\n  mvc:\n    # mvc找不到对应处理器则抛出异常\n    throw-exception-if-no-handler-found: true\n    # 关闭DispatcherServlet懒加载\n    servlet:\n      load-on-startup: 0\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common\n  cloud:\n    openfeign:\n      # 请求使用okhttp\n      okhttp:\n        enabled: true\n      # openfeign超时配置\n      client:\n        config:\n          default:\n            connectTimeout: 10000\n            readTimeout: 10000\n            loggerLevel: basic\n\n# feign启用sentinel\nfeign:\n  sentinel:\n    enabled: true\n\n#分布式事务\nseata:\n  enabled: false\n  # Seata 应用编号，默认为 ${spring.application.name}\n  application-id: ${spring.application.name}\n  # Seata 事务组编号，用于 TC 集群名\n  tx-service-group: ${spring.application.name}-group\n  # 关闭自动代理\n  enable-auto-data-source-proxy: false\n  # 服务配置项\n  service:\n    # 虚拟组和分组的映射\n    vgroup-mapping:\n      ruoyi-system-group: default\n    # 分组和 Seata 服务的映射\n    grouplist:\n      default: 127.0.0.1:8091\n  config:\n    type: nacos\n    nacos:\n      serverAddr: ${spring.cloud.nacos.server-addr}\n      username: ${spring.cloud.nacos.username}\n      password: ${spring.cloud.nacos.password}\n      group: SEATA_GROUP\n      namespace: f06f7669-9836-4413-86cf-9f44ae5c9f30\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: ${spring.cloud.nacos.server-addr}\n      username: ${spring.cloud.nacos.username}\n      password: ${spring.cloud.nacos.password}\n      namespace: f06f7669-9836-4413-86cf-9f44ae5c9f30\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \"*\"\n\n# 不校验白名单\nsecurity:\n  ip:\n    enable: true\n    ipList:\n      - \n  white:\n    common:\n      urls:\n        #内部调用放行\n        - /inner/**\n        #spring监控服务放行\n        - /actuator/**\n        #swagger接口放行\n        - /v3/api-docs/**\n        - /webjars/**\n        #开放接口放行\n        - /open/**\n        #上传图片可访问\n        - /uploadPath/**\n        #异常处理\n        - /error', 'f4bdb4fb5677f3ae801540f1cd6d636a', '2023-06-19 02:28:00', '2023-09-15 07:57:16', 'dev', '115.221.135.11', '', 'dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (14, 'thingslink-gateway.yml', 'thingslink', 'spring:\n  cloud:\n    gateway:\n      httpclient:\n        pool:\n          type: FIXED\n          max-connections: 1000\n          acquire-timeout: 80000\n          max-life-time: 30000\n          max-idle-time: 5000\n          connect-timeout: 5000\n          response-timeout: 5000\n          max-request-size: 50MB\n      discovery:\n        locator:\n          lower-case-service-id: true\n          enabled: true\n      routes:\n        # 认证中心\n        - id: thingslink-auth\n          uri: lb://thingslink-auth\n          predicates:\n            - Path=/auth/**\n          filters:\n            - CacheRequestFilter\n            - StripPrefix=1\n            # 黑名单过滤器\n            - name: BlackListUrlFilter\n              args:\n                blacklistUrl:\n                - /nacos/test\n        # 用户服务\n        - id: thingslink-user\n          uri: lb://thingslink-user\n          predicates:\n            - Path=/ur/**\n          filters:\n            - StripPrefix=1\n        # 系统模块\n        - id: thingslink-system\n          uri: lb://thingslink-system\n          predicates:\n            - Path=/sys_biz/**\n          filters:\n            - StripPrefix=1\n        # 设备模块\n        - id: thingslink-equipment\n          uri: lb://thingslink-equipment\n          predicates:\n            - Path=/eqp_biz/**\n          filters:\n            - StripPrefix=1\n        # 订单模块\n        - id: thingslink-order\n          uri: lb://thingslink-order\n          predicates:\n            - Path=/ord_biz/**\n          filters:\n            - StripPrefix=1\n        # 文件服务\n        - id: thingslink-file\n          uri: lb://thingslink-file\n          predicates:\n            - Path=/fl_biz/**\n          filters:\n            - StripPrefix=1\n        # 监控服务\n        - id: thingslink-monitor\n          uri: lb://thingslink-monitor\n          predicates:\n            - Path=/moniter/**\n          filters:\n            - StripPrefix=1\n\nknife4j:\n  # 聚合swagger文档\n  gateway:\n    enabled: true\n    strategy: discover\n    discover:\n      version: openapi3\n      enabled: true\n      service-config:\n        thingslink-auth:\n          - group-name: 认证服务\n            order: 1\n        thingslink-user:\n          - group-name: 用户服务\n            order: 1\n        thingslink-system:\n          - group-name: 系统服务\n            order: 2\n        thingslink-order:\n          - group-name: 订单服务\n            order: 3\n        thingslink-equipment:\n          - group-name: 设备服务\n            order: 4\n        thingslink-file:\n          - group-name: 文件服务\n            order: 5\n        thingslink-job:\n          - group-name: 定时任务服务\n            order: 6\n    tags-sorter: order\n    operations-sorter: order\n\n# 防止XSS攻击\nxss:\n  enabled: true\n  excludeUrls:\n    - /system/notice\n', '5a8589c1c5257c37681af46cbe03aace', '2023-06-19 02:28:00', '2023-09-18 02:45:41', 'dev', '115.221.128.151', '', 'dev', '网关配置', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (15, 'thingslink-auth.yml', 'thingslink', '# spring配置\nspring:\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common,i18n/auth\n\nredisson:\n  # 线程池数量\n  threads: 16\n  # Netty线程池数量\n  nettyThreads: 32\n  # 单节点配置\n  singleServerConfig:\n    # redis 节点地址\n    address: \"redis://127.0.0.1:6379\"\n    # 无密码则设置 null\n    password: \'\'\n    # 客户端名称\n    clientName: ${spring.application.name}\n    # 最小空闲连接数\n    connectionMinimumIdleSize: 32\n    # 连接池大小\n    connectionPoolSize: 64\n    # 连接空闲超时,单位:毫秒\n    idleConnectionTimeout: 10000\n    # 命令等待超时,单位:毫秒\n    timeout: 3000\n    # 发布和订阅连接池大小\n    subscriptionConnectionPoolSize: 50\n\n# springdoc配置\nspringdoc:\n  title: 认证模块接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---\n\n# 安全配置\nsecurity:\n  # 验证码\n  captcha:\n    enabled: false\n    type: math\n  # 不校验白名单\n  white:\n    custom:\n      urls:\n        #验证码放行\n        - /code\n        - /sms_code\n\n# token配置\ntoken-config:\n  accessTokenTimeOut: 1800\n  refreshTokenTimeOut: 86400\n  authorizationCodeTimeOut: 180\n  deviceCodeTimeOut: 180\n', '6980eee2c51880c67b1781e09dc3d154', '2023-06-19 02:28:00', '2023-09-14 08:32:09', 'dev', '115.221.135.11', '', 'dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (16, 'thingslink-user.yml', 'thingslink', '# spring配置\nspring:\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common\n  # datasource:\n  #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver\n  #   url: jdbc:shardingsphere:classpath:sharding-${spring.profiles.active}.yml\n  datasource:\n    url: jdbc:mysql://localhost:3306/thingslink_user?rewriteBatchedStatements=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai\n    username: root\n    password: root\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    hikari:\n      connection-timeout: 30000 \n      maximum-pool-size: 10       \n      minimum-idle: 5             \n      idle-timeout: 600000        \n      pool-name: hikari-pool\n\nmybatis-plus:\n  # 搜索指定包别名\n  typeAliasesPackage: com.thingslink.user.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/user/*.xml\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n  global-config:\n    db-config:\n      # 逻辑删除\n      logic-delete-field: isDeleted \n\nredisson:\n  # 线程池数量\n  threads: 16\n  # Netty线程池数量\n  nettyThreads: 32\n  # 单节点配置\n  singleServerConfig:\n    # redis 节点地址\n    address: \"redis://127.0.0.1:6379\"\n    # 无密码则设置 null\n    password: \'\'\n    # 客户端名称\n    clientName: ${spring.application.name}\n    # 最小空闲连接数\n    connectionMinimumIdleSize: 32\n    # 连接池大小\n    connectionPoolSize: 64\n    # 连接空闲超时,单位:毫秒\n    idleConnectionTimeout: 10000\n    # 命令等待超时,单位:毫秒\n    timeout: 3000\n    # 发布和订阅连接池大小\n    subscriptionConnectionPoolSize: 50\n\n# springdoc配置\nspringdoc:\n  title: 用户模块接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---\n\n# 租户配置\ntenant:\n  includes:\n    - sys_dept\n    - sys_post\n    - sys_role\n    - sys_tenant\n    - sys_user', '06829d0a0358805c93dff01a4e93070c', '2024-04-16 06:36:22', '2024-04-16 06:36:22', NULL, '0:0:0:0:0:0:0:1', '', 'dev', '', NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (17, 'thingslink-monitor.yml', 'thingslink', '# spring\nspring:\n  security:\n    user:\n      name: admin\n      password: admin123\n  boot:\n    admin:\n      ui:\n        title: 服务状态监控\n', 'dd19c14e3cebc473140e1fc8733a339d', '2023-06-19 02:28:00', '2023-06-19 02:28:00', NULL, '0:0:0:0:0:0:0:1', '', 'dev', '', NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (18, 'thingslink-system.yml', 'thingslink', '# spring配置\nspring:\n  # datasource:\n  #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver\n  #   url: jdbc:shardingsphere:classpath:sharding-${spring.profiles.active}.yml\n  datasource:\n    url: jdbc:mysql://localhost:3306/thingslink_system?rewriteBatchedStatements=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai\n    username: root\n    password: root\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    hikari:\n      connection-timeout: 30000 \n      maximum-pool-size: 10       \n      minimum-idle: 5             \n      idle-timeout: 600000        \n      pool-name: hikari-pool\n\nredisson:\n  # 线程池数量\n  threads: 16\n  # Netty线程池数量\n  nettyThreads: 32\n  # 单节点配置\n  singleServerConfig:\n    # redis 节点地址\n    address: \"redis://127.0.0.1:6379\"\n    # 无密码则设置 null\n    password: null\n    # 客户端名称\n    clientName: ${spring.application.name}\n    # 最小空闲连接数\n    connectionMinimumIdleSize: 32\n    # 连接池大小\n    connectionPoolSize: 64\n    # 连接空闲超时,单位:毫秒\n    idleConnectionTimeout: 10000\n    # 命令等待超时,单位:毫秒\n    timeout: 3000\n    # 发布和订阅连接池大小\n    subscriptionConnectionPoolSize: 50\n\nmybatis-plus:\n  # 搜索指定包别名\n  typeAliasesPackage: com.thingslink.**.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/system/*.xml\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n\n# 租户配置\ntenant:\n  includes:\n    - sys_login_log\n    - sys_oper_log\n\n# springdoc配置\nspringdoc:\n  title: 系统模块接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---\n', '1c4d28f92f25084e73d069217b12ac8b', '2023-06-19 02:28:00', '2023-09-15 05:18:32', 'dev', '115.221.135.11', '', 'dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (19, 'sentinel-thingslink-gateway', 'thingslink', '[\n    {\n        \"resource\": \"thingslink-auth\",\n        \"count\": 2000,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n	{\n        \"resource\": \"thingslink-system\",\n        \"count\": 1000,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n    {\n        \"resource\": \"thingslink-order\",\n        \"count\": 100,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n	{\n        \"resource\": \"thingslink-job\",\n        \"count\": 300,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n    {\n        \"resource\": \"thingslink-moniter\",\n        \"count\": 300,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    }\n]', '6ef92909e96b2927bc6fb87e174bea51', '2023-06-19 02:28:00', '2023-09-16 02:56:35', 'dev', '115.221.135.11', '', 'dev', '网关限流策略', '', '', 'json', '', '');
INSERT INTO `config_info` VALUES (20, 'thingslink-equipment.yml', 'thingslink', '# spring配置\nspring:\n  # datasource:\n  #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver\n  #   url: jdbc:shardingsphere:classpath:sharding-${spring.profiles.active}.yml\n  datasource:\n    url: jdbc:mysql://localhost:3306/thingslink_equipment?rewriteBatchedStatements=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai\n    username: root\n    password: root\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    hikari:\n      connection-timeout: 30000 \n      maximum-pool-size: 10       \n      minimum-idle: 5             \n      idle-timeout: 600000        \n      pool-name: hikari-pool\n\nredisson:\n  # 线程池数量\n  threads: 16\n  # Netty线程池数量\n  nettyThreads: 32\n  # 单节点配置\n  singleServerConfig:\n    # redis 节点地址\n    address: \"redis://127.0.0.1:6379\"\n    # 无密码则设置 null\n    password: \'\'\n    # 客户端名称\n    clientName: ${spring.application.name}\n    # 最小空闲连接数\n    connectionMinimumIdleSize: 32\n    # 连接池大小\n    connectionPoolSize: 64\n    # 连接空闲超时,单位:毫秒\n    idleConnectionTimeout: 10000\n    # 命令等待超时,单位:毫秒\n    timeout: 3000\n    # 发布和订阅连接池大小\n    subscriptionConnectionPoolSize: 50\n\nmybatis-plus:\n  # 搜索指定包别名\n  typeAliasesPackage: com.thingslink.**.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/equipment/*.xml\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n#mqtt基本配置\nmqtt:\n  host: tcp://127.0.0.1:1883\n  userName: server_prod\n  password: ${server.port}\n  clientId: server_prod\n  timeout: 5000\n  keepAlive: 300\n  \n# springdoc配置\nspringdoc:\n  title: 设备模块接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---\n', '9812ae3ea4f2192e52c7ae811cbf0994', '2023-06-19 02:28:00', '2023-09-15 05:18:44', 'dev', '115.221.135.11', '', 'dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (21, 'thingslink-file.yml', 'thingslink', 'spring:\n  servlet:\n    multipart:\n      # 根据实际需求作调整\n      # 默认最大上传文件大小为15M，单个文件大小\n      max-file-size: 15728640\n      # 默认最大请求大小为30M，总上传的数据大小\n      max-request-size: 31457280\n\nredisson:\n  # 线程池数量\n  threads: 16\n  # Netty线程池数量\n  nettyThreads: 32\n  # 单节点配置\n  singleServerConfig:\n    # redis 节点地址\n    address: \"redis://127.0.0.1:6379\"\n    # 无密码则设置 null\n    password: \'\'\n    # 客户端名称\n    clientName: ${spring.application.name}\n    # 最小空闲连接数\n    connectionMinimumIdleSize: 32\n    # 连接池大小\n    connectionPoolSize: 64\n    # 连接空闲超时,单位:毫秒\n    idleConnectionTimeout: 10000\n    # 命令等待超时,单位:毫秒\n    timeout: 3000\n    # 发布和订阅连接池大小\n    subscriptionConnectionPoolSize: 50\n\n# 本地文件上传    \nlocal:\n  domain: http://127.0.0.1:9300\n  path: /home/thingslink\n  prefix: /uploadPath\n\n# FastDFS配置\nfdfs:\n  domain: http://8.129.231.12\n  soTimeout: 3000\n  connectTimeout: 2000\n  trackerList: 8.129.231.12:22122\n\n# Minio配置\nminio:\n  url: http://8.129.231.12:9000\n  accessKey: minioadmin\n  secretKey: minioadmin\n  bucketName: test', '5b2bc4a29f9bdcdc1e415a55213917a0', '2023-06-19 02:28:00', '2023-09-18 05:19:49', 'dev', '115.221.128.151', '', 'dev', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` VALUES (22, 'thingslink-order.yml', 'thingslink', '# spring配置\nspring:\n  # datasource:\n  #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver\n  #   url: jdbc:shardingsphere:classpath:sharding-${spring.profiles.active}.yml\n  datasource:\n    url: jdbc:mysql://localhost:3306/thingslink_order?rewriteBatchedStatements=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&serverTimezone=Asia/Shanghai\n    username: root\n    password: root\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    hikari:\n      connection-timeout: 30000 \n      maximum-pool-size: 10       \n      minimum-idle: 5             \n      idle-timeout: 600000        \n      pool-name: hikari-pool\n\nredisson:\n  # 线程池数量\n  threads: 16\n  # Netty线程池数量\n  nettyThreads: 32\n  # 单节点配置\n  singleServerConfig:\n    # redis 节点地址\n    address: \"redis://127.0.0.1:6379\"\n    # 无密码则设置 null\n    password: \'\'\n    # 客户端名称\n    clientName: ${spring.application.name}\n    # 最小空闲连接数\n    connectionMinimumIdleSize: 32\n    # 连接池大小\n    connectionPoolSize: 64\n    # 连接空闲超时,单位:毫秒\n    idleConnectionTimeout: 10000\n    # 命令等待超时,单位:毫秒\n    timeout: 3000\n    # 发布和订阅连接池大小\n    subscriptionConnectionPoolSize: 50\n\nmybatis-plus:\n  # 搜索指定包别名\n  typeAliasesPackage: com.thingslink.**.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/order/*.xml\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n# springdoc配置\nspringdoc:\n  title: 订单模块接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---', 'dd9f3539f5fb47f9dc3d48aad1e456b1', '2023-08-07 05:20:04', '2023-08-25 00:46:58', 'nacos', '115.221.135.245', '', 'dev', '', '', '', 'yaml', '', '');

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'datum_id',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `app_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '增加租户字段' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_aggr
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'content',
  `beta_ips` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'betaIps',
  `md5` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户字段',
  `encrypted_data_key` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfobeta_datagrouptenant`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'config_info_beta' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_beta
-- ----------------------------

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'tenant_id',
  `tag_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_configinfotag_datagrouptenanttag`(`data_id` ASC, `group_id` ASC, `tenant_id` ASC, `tag_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'config_info_tag' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_info_tag
-- ----------------------------

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation`  (
  `id` bigint NOT NULL COMMENT 'id',
  `tag_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'tenant_id',
  `nid` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`) USING BTREE,
  UNIQUE INDEX `uk_configtagrelation_configidtag`(`id` ASC, `tag_name` ASC, `tag_type` ASC) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'config_tag_relation' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config_tags_relation
-- ----------------------------

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Group ID，空字符表示整个集群',
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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '集群、各Group容量信息表' ROW_FORMAT = DYNAMIC;

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
  `data_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `group_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `app_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `md5` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `src_user` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `src_ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `op_type` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户字段',
  `encrypted_data_key` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`nid`) USING BTREE,
  INDEX `idx_did`(`data_id` ASC) USING BTREE,
  INDEX `idx_gmt_create`(`gmt_create` ASC) USING BTREE,
  INDEX `idx_gmt_modified`(`gmt_modified` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '多租户改造' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of his_config_info
-- ----------------------------

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`  (
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `resource` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `action` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of permissions
-- ----------------------------
INSERT INTO `permissions` VALUES ('prod', 'prod:*:*', 'rw');
INSERT INTO `permissions` VALUES ('dev', 'dev:*:*', 'rw');

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  UNIQUE INDEX `uk_username_role`(`username` ASC, `role` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

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
  `tenant_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'Tenant ID',
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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租户容量信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tenant_capacity
-- ----------------------------

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'tenant_id',
  `tenant_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'tenant_name',
  `tenant_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_info_kptenantid`(`kp` ASC, `tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'tenant_info' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tenant_info
-- ----------------------------
INSERT INTO `tenant_info` VALUES (1, '1', 'dev', 'dev', '开发环境', 'nacos', 1685070991448, 1685071009622);
INSERT INTO `tenant_info` VALUES (2, '1', 'prod', 'prod', '生产环境', 'nacos', 1687141596216, 1687141596216);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `password` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `enabled` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('dev', '$2a$10$0rQC3/xH3czsnLp2qiLJg.Wwi4KMvnVp33eVlRvkOaWd4ITw56Oau', 't');
INSERT INTO `users` VALUES ('nacos', '$2a$10$h05FUQ0x5eypjsF02DWyd.2PXBDgXb7GIXNZCjRim6EORDlTLTRLu', 't');
INSERT INTO `users` VALUES ('prod', '$2a$10$LdUnGcqhQD6DI2OE/WewG.vV5yJjg1hrMA2mhxW/yXEXkTnvdEufm', '1');

SET FOREIGN_KEY_CHECKS = 1;
