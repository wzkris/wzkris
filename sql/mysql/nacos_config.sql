-- MySQL dump 10.13  Distrib 5.7.44, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: nacos_config
-- ------------------------------------------------------
-- Server version	5.7.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `config_info`
--

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS nacos_config default character set utf8mb4 collate utf8mb4_unicode_ci;
USE nacos_config;
-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `config_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `content` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'content',
  `md5` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` longtext COLLATE utf8mb4_unicode_ci COMMENT 'source user',
  `src_ip` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tenant_id` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户字段',
  `c_desc` text COLLATE utf8mb4_unicode_ci,
  `c_use` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `effect` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `c_schema` longtext COLLATE utf8mb4_unicode_ci,
  `encrypted_data_key` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='config_info';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_info`
--

INSERT INTO `config_info` VALUES (1,'common.yml','COMMON_GROUP','server:\n  # 优雅停机\n  shutdown: graceful\n  # 服务器配置\n  tomcat:\n    # 监控\n    mbeanregistry:\n      enabled: true\n    ## 开启虚拟线程后，此配置无效\n    # accept-count: 100\n    # threads:\n    #   max: 1000\n    #   min-spare: 200\n    # connection-timeout: 10000\n\nspring:\n  threads:\n    virtual:\n      enabled: true\n  ## 开启虚拟线程后，此配置无效\n  # task:\n  #   execution:\n  #     thread-name-prefix: async-threadPool-\n  #     pool:\n  #       core-size: 15\n  #       max-size: 30\n  #       queue-capacity: 1000\n  #       keep-alive: 120s\n  #       allow-core-thread-timeout: false\n  #     shutdown:\n  #       await-termination: true\n  #       await-termination-period: 60s\n  #   scheduling:\n  #     thread-name-prefix: scheduling-threadPool-\n  #     pool:\n  #       size: 10\n  #     shutdown:\n  #       await-termination: true\n  #       await-termination-period: 60s\n  jackson:\n    time-zone: GMT+8\n    # 日期格式化\n    date-format: yyyy-MM-dd HH:mm:ss\n    serialization:\n      # 格式化输出\n      INDENT_OUTPUT: false\n      # 忽略空Bean转json的错误\n      FAIL_ON_EMPTY_BEANS: false\n      # 关闭日期转换成时间戳\n      WRITE_DATES_AS_TIMESTAMPS: false\n    # 设置空如何序列化\n    defaultPropertyInclusion: ALWAYS\n    deserialization:\n      #json中不存在的属性就报错\n      fail_on_unknown_properties: false\n    parser:\n      # 允许使用无引号字段\n      ALLOW_UNQUOTED_FIELD_NAMES: true\n      # 忽略未定义的属性\n      IGNORE_UNDEFINED: true\n      # 忽略json最后的逗号\n      ALLOW_TRAILING_COMMA: true\n      # 允许反斜杠\n      ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER: true\n      # 允许出现特殊字符和转义符\n      ALLOW_UNQUOTED_CONTROL_CHARS: true\n      # 允许出现单引号\n      ALLOW_SINGLE_QUOTES: true\n      # 是否允许使用注释\n      ALLOW_COMMENTS: true\n    mapper:\n      # 使用getter取代setter探测属性，如类中含getName()但不包含name属性与setName()，传输的vo json格式模板中依旧含name属性\n      USE_GETTERS_AS_SETTERS: true\n  mvc:\n    # 关闭DispatcherServlet懒加载\n    servlet:\n      load-on-startup: 0\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common,i18n/local\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \"*\"\n  endpoint:\n    env:\n      access: read_only\n      show-values: always\n    configprops:\n      access: read_only\n      show-values: always\n    beans:\n      access: read_only\n\nmybatis-plus:\n  # 搜索指定包别名\n  typeAliasesPackage: com.wzkris.**.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/*/*.xml\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n  global-config:\n    db-config:\n      logic-delete-field: deleted\n      logic-delete-value: \'true\'\n      logic-not-delete-value: \'false\'','01186ebcb5e117d89d2ac71d4c902aa6','2023-06-19 02:28:00','2026-07-14 19:30:31',NULL,'0:0:0:0:0:0:0:1','','application-prod','公共配置','','','yaml','','');
INSERT INTO `config_info` VALUES (2,'wzkris-gateway.yml','APPLICATION_GROUP','spring:\n  cloud:\n    gateway:\n      server:\n        webmvc:\n          routes:\n            # 认证中心\n            - id: wzkris-auth\n              uri: lb://wzkris-auth\n              predicates:\n                - Path=/wzkris-auth-api/**\n              # 用户中心服务\n            - id: wzkris-user-center\n              uri: lb://wzkris-user-center\n              predicates:\n                - Path=/wzkris-user-center-api/**\n            # 验证码模块\n            - id: wzkris-captcha\n              uri: lb://wzkris-captcha\n              predicates:\n                - Path=/wzkris-captcha-api/**\n\n# 路由策略\nroute-decision:\n  policy: OPEN\n  forceConfig: \n    hintValue: 0.1\n  openConfig: \n    defaultHintValue: \"\"\n\nknife4j:\n  # 聚合swagger文档\n  gateway:\n    enabled: false\n\n# 网关放行\nsecurity:\n  ignores:\n    # 验证码放行\n    - /wzkris-captcha-api/captcha/**\n    # 登录接口\n    - /wzkris-auth-api/login\n    # oauth2接口\n    - /wzkris-auth-api/oauth2/token\n    # 二维码登录\n    - /wzkris-auth-api/qr-code\n    - /wzkris-auth-api/qr-code/poll-status\n    # 回调接口\n    - /wzkris-auth-api/authorization_code_callback\n    - /wzkris-user-center-api/nacos/**\n    # swagger接口放行\n    - /doc.html\n    - /favicon.ico\n    - /webjars/**\n    - /v3/api-docs/**\n    - /*/v3/api-docs/**\n    # 监控端点\n    - /actuator/**','47ea56c303d4ff6ac25ace696a3b60b0','2023-06-19 02:28:00','2026-07-14 19:36:52',NULL,'0:0:0:0:0:0:0:1','','application-prod','','','','yaml','','');
INSERT INTO `config_info` VALUES (3,'wzkris-auth.yml','APPLICATION_GROUP','\n# springdoc配置\nspringdoc:\n  enabled: false\n\n# 打印忽略\ncontroller-log:\n  ignoreUrls: /qr-code/**','d2c993fb5b6d909cf1d1c5cbf30bf40d','2023-06-19 02:28:00','2026-05-09 11:19:21',NULL,'0:0:0:0:0:0:0:1','','application-prod','','','','yaml','','');
INSERT INTO `config_info` VALUES (4,'wzkris-user-center.yml','APPLICATION_GROUP','# spring配置\nspring:\n  # datasource:\n  #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver\n  #   url: jdbc:shardingsphere:classpath:sharding-${spring.profiles.active}.yml\n  datasource:\n    url: jdbc:postgresql://localhost:5432/wzkris_user_center?ssl=false&reWriteBatchedInserts=true&stringtype=unspecified\n    username: root\n    password: root\n    driver-class-name: org.postgresql.Driver\n    hikari:\n      connection-timeout: 30000 \n      maximum-pool-size: 10       \n      minimum-idle: 5             \n      idle-timeout: 600000        \n      pool-name: hikari-pool\n\n# springdoc配置\nspringdoc:\n  enabled: false\n\n# 租户配置\ntenant:\n  includes:\n    - member_info\n    - post_info\n    - tenant_info\n    - tenant_wallet_info\n    - tenant_wallet_record\n    - tenant_wallet_withdrawal_record','3cc7fc173a0f76bd4c9375c7622d37f1','2024-04-16 01:03:03','2026-07-14 19:30:47',NULL,'0:0:0:0:0:0:0:1','','application-prod','','','','yaml','','');
INSERT INTO `config_info` VALUES (5,'wzkris-monitor-admin.yml','APPLICATION_GROUP','# spring\nspring:\n  security:\n    user:\n      name: admin\n      password: admin123\n  boot:\n    admin:\n      ui:\n        title: 服务状态监控\n','dd19c14e3cebc473140e1fc8733a339d','2023-06-19 02:28:00','2023-06-19 02:28:00',NULL,'0:0:0:0:0:0:0:1','','application-prod','',NULL,NULL,'yaml',NULL,'');
INSERT INTO `config_info` VALUES (7,'sentinel-gateway','APPLICATION_GROUP','[\n    {\n        \"resource\": \"wzkris-auth\",\n        \"count\": 2000,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n	{\n        \"resource\": \"wzkris-system\",\n        \"count\": 1000,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n    {\n        \"resource\": \"wzkris-user-center\",\n        \"count\": 1000,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n    {\n        \"resource\": \"wzkris-monitor-admin\",\n        \"count\": 300,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    }\n]','0c01771ac42e7ee302dc5ec79947b677','2023-06-19 02:28:00','2026-02-25 15:08:59',NULL,'0:0:0:0:0:0:0:1','','application-prod','网关限流策略','','','json','','');
INSERT INTO `config_info` VALUES (10,'redis.yml','COMMON_GROUP','spring:\n  redis:\n    redisson:\n      config: |\n        # 集群配置\n        clusterServersConfig: \n          # Redis集群节点地址列表\n          nodeAddresses:\n            - \"redis://127.0.0.1:6379\"\n            - \"redis://127.0.0.1:6380\"\n            - \"redis://127.0.0.1:6381\"\n          # 集群拓扑扫描间隔 ms\n          scanInterval: 5000\n          # 无密码则设置 null\n          password: null\n          # 客户端名称\n          clientName: ${spring.application.name}\n          # 主节点最小连接数\n          masterConnectionMinimumIdleSize: 6\n          # 主节点最大连接数\n          masterConnectionPoolSize: 18\n          # 从节点最小连接数\n          slaveConnectionMinimumIdleSize: 6\n          # 从节点最大连接数\n          slaveConnectionPoolSize: 18\n          # 命令等待超时,单位:毫秒\n          timeout: 3000\n          # 发布和订阅连接池大小\n          subscriptionConnectionPoolSize: 30\n        # 线程池数量\n        threads: 8\n        # Netty线程池数量\n        nettyThreads: 8\n        codec: !<org.redisson.codec.JsonJacksonCodec> {}\n        transportMode: \"NIO\"\n\n\n# spring:\n#   redis:\n#     redisson: \n#       config: |\n#         # 单节点配置\n#         singleServerConfig: \n#           # redis 节点地址\n#           address: \"redis://127.0.0.1:6379\"\n#           # 无密码则设置 null\n#           password: null\n#           # 客户端名称\n#           clientName: ${spring.application.name}\n#           # 最小空闲连接数\n#           connectionMinimumIdleSize: 32\n#           # 连接池大小\n#           connectionPoolSize: 64\n#           # 连接空闲超时,单位:毫秒\n#           idleConnectionTimeout: 10000\n#           # 命令等待超时,单位:毫秒\n#           timeout: 3000\n#           # 发布和订阅连接池大小\n#           subscriptionConnectionPoolSize: 50\n#         # 线程池数量\n#         threads: 8\n#         # Netty线程池数量\n#         nettyThreads: 8\n#         codec: !<org.redisson.codec.JsonJacksonCodec> {}\n#         transportMode: \"NIO\"\n','d19d9cfcd786295407bfb79f4365c633','2025-08-04 15:19:50','2025-08-04 15:19:50',NULL,'0:0:0:0:0:0:0:1','','application-prod',NULL,NULL,NULL,'yaml',NULL,'');
INSERT INTO `config_info` VALUES (12,'common.yml','COMMON_GROUP','server:\n  # 优雅停机\n  shutdown: graceful\n  # 服务器配置\n  tomcat:\n    # 监控\n    mbeanregistry:\n      enabled: true\n    ## 开启虚拟线程后，此配置无效\n    # accept-count: 100\n    # threads:\n    #   max: 1000\n    #   min-spare: 200\n    # connection-timeout: 10000\n\nspring:\n  threads:\n    virtual:\n      enabled: true\n  ## 开启虚拟线程后，此配置无效\n  # task:\n  #   execution:\n  #     thread-name-prefix: async-threadPool-\n  #     pool:\n  #       core-size: 15\n  #       max-size: 30\n  #       queue-capacity: 1000\n  #       keep-alive: 120s\n  #       allow-core-thread-timeout: false\n  #     shutdown:\n  #       await-termination: true\n  #       await-termination-period: 60s\n  #   scheduling:\n  #     thread-name-prefix: scheduling-threadPool-\n  #     pool:\n  #       size: 10\n  #     shutdown:\n  #       await-termination: true\n  #       await-termination-period: 60s\n  jackson:\n    time-zone: GMT+8\n    # 日期格式化\n    date-format: yyyy-MM-dd HH:mm:ss\n    serialization:\n      # 格式化输出\n      INDENT_OUTPUT: false\n      # 忽略空Bean转json的错误\n      FAIL_ON_EMPTY_BEANS: false\n      # 关闭日期转换成时间戳\n      WRITE_DATES_AS_TIMESTAMPS: false\n    # 设置空如何序列化\n    defaultPropertyInclusion: ALWAYS\n    deserialization:\n      #json中不存在的属性就报错\n      fail_on_unknown_properties: false\n    parser:\n      # 允许使用无引号字段\n      ALLOW_UNQUOTED_FIELD_NAMES: true\n      # 忽略未定义的属性\n      IGNORE_UNDEFINED: true\n      # 忽略json最后的逗号\n      ALLOW_TRAILING_COMMA: true\n      # 允许反斜杠\n      ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER: true\n      # 允许出现特殊字符和转义符\n      ALLOW_UNQUOTED_CONTROL_CHARS: true\n      # 允许出现单引号\n      ALLOW_SINGLE_QUOTES: true\n      # 是否允许使用注释\n      ALLOW_COMMENTS: true\n    mapper:\n      # 使用getter取代setter探测属性，如类中含getName()但不包含name属性与setName()，传输的vo json格式模板中依旧含name属性\n      USE_GETTERS_AS_SETTERS: true\n  mvc:\n    # 关闭DispatcherServlet懒加载\n    servlet:\n      load-on-startup: 0\n  messages:\n    # 国际化资源文件路径\n    basename: i18n/common,i18n/local\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \"*\"\n  endpoint:\n    env:\n      access: read_only\n      show-values: always\n    configprops:\n      access: read_only\n      show-values: always\n    beans:\n      access: read_only\n\nmybatis-plus:\n  # 搜索指定包别名\n  typeAliasesPackage: com.wzkris.**.domain\n  # 配置mapper的扫描，找到所有的mapper.xml映射文件\n  mapperLocations: classpath:mapper/*/*.xml\n  configuration:\n    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl\n  global-config:\n    db-config:\n      logic-delete-field: deleted\n      logic-delete-value: \'true\'\n      logic-not-delete-value: \'false\'\n','5cb78c37e32a3f6b70fd61f25d11d8f4','2023-06-19 02:28:00','2026-07-14 18:14:07',NULL,'0:0:0:0:0:0:0:1','','application-dev','公共配置','','','yaml','','');
INSERT INTO `config_info` VALUES (13,'wzkris-gateway.yml','APPLICATION_GROUP','spring:\n  cloud:\n    gateway:\n      server:\n        webmvc:\n          routes:\n            # 认证中心\n            - id: wzkris-auth\n              uri: lb://wzkris-auth\n              predicates:\n                - Path=/wzkris-auth-api/**\n              # 用户中心服务\n            - id: wzkris-user-center\n              uri: lb://wzkris-user-center\n              predicates:\n                - Path=/wzkris-user-center-api/**\n            # 验证码模块\n            - id: wzkris-captcha\n              uri: lb://wzkris-captcha\n              predicates:\n                - Path=/wzkris-captcha-api/**\n\n# 路由策略\nroute-decision:\n  policy: OPEN\n  forceConfig: \n    hintValue: 0.1\n  openConfig: \n    defaultHintValue: \"\"\n\n# knife4j:\n#   # 聚合swagger文档\n#   gateway:\n#     enabled: true\n#     strategy: discover\n#     discover:\n#       version: openapi3\n#       enabled: true\n#     tags-sorter: order\n#     operations-sorter: order\n\n# 网关放行\nsecurity:\n  ignores:\n    # 验证码放行\n    - /wzkris-captcha-api/captcha/**\n    - /wzkris-captcha-api/risk-pass/exchange\n    # 登录接口\n    - /wzkris-auth-api/login\n    # oauth2接口\n    - /wzkris-auth-api/oauth2/token\n    # 二维码登录\n    - /wzkris-auth-api/qr-code\n    - /wzkris-auth-api/qr-code/poll-status\n    # 回调接口\n    - /wzkris-auth-api/authorization_code_callback\n    - /wzkris-user-center-api/nacos/**\n    # swagger接口放行\n    - /doc.html\n    - /favicon.ico\n    - /webjars/**\n    - /v3/api-docs/**\n    - /*/v3/api-docs/**\n    # 监控端点\n    - /actuator/**\n  risk-captcha:\n    enabled: true\n    enforcedPaths:\n      - /wzkris-auth-api/login','e625916bd4ba4897561bcfa14d46ed8d','2023-06-19 02:28:00','2026-07-14 19:36:39',NULL,'0:0:0:0:0:0:0:1','','application-dev','','','','yaml','','');
INSERT INTO `config_info` VALUES (14,'wzkris-auth.yml','APPLICATION_GROUP','\n# springdoc配置\nspringdoc:\n  enabled: true\n  title: 认证模块接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---\n\n# 打印忽略\ncontroller-log:\n  ignoreUrls: /qr-code/**\n\n# jwt-rs256:\n#   previousPublicKey: -----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1cdDeVUgQzB22IcCAHVKij1Csn+tOWIGu2deRxSWQU1CIkrrKyB2zWJ8RX4lp8ssxspnybsuycHmvavNBE7EBVW3bEYIN2ebOFFdOxZPnCC7mBikQBtag3TfmXSO3Mcg/rN4ulqWloDX1Wv3vdAh/eCxYlDNATAAFxEoEdzEe3MapdOygRZWbj9DEfEF1bU3ObxrBV9ExFnPLAUx0CE0MDLhAF3s+qtkcFlpG1h+Q/XDxF8wp53bTqsgFslqFCsJXL8GqFTOnCTPlUybwfa8Mtos9s/djJpm9KZWndrXlDshysnd7bQqG5HGh6Y5AHIfwcUxKFdSMk7jVI472aY4CwIDAQAB-----END PUBLIC KEY-----\n#   previousPrivateKey: -----BEGIN PRIVATE KEY-----MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDVx0N5VSBDMHbYhwIAdUqKPUKyf605Yga7Z15HFJZBTUIiSusrIHbNYnxFfiWnyyzGymfJuy7Jwea9q80ETsQFVbdsRgg3Z5s4UV07Fk+cILuYGKRAG1qDdN+ZdI7cxyD+s3i6WpaWgNfVa/e90CH94LFiUM0BMAAXESgR3MR7cxql07KBFlZuP0MR8QXVtTc5vGsFX0TEWc8sBTHQITQwMuEAXez6q2RwWWkbWH5D9cPEXzCnndtOqyAWyWoUKwlcvwaoVM6cJM+VTJvB9rwy2iz2z92Mmmb0plad2teUOyHKyd3ttCobkcaHpjkAch/BxTEoV1IyTuNUjjvZpjgLAgMBAAECggEBAMgCerqWTm0OduL2zYSoOGlGD5T5p5Q8hpfnimludXX7VpjHB2d+JCjcr/BEqe5nRSloTdqL6qaRZ9SlXFdfaj6jh80haKaNpMf4OAYERc+JQHp485OXBARh4KGuT8t38wLZ32ZbQvDk8wqWzV8lz+e7xbp6ZpNp2Wu7fYXYy2vC+7Zje6qYCYi+JMF6a2ujKEflLI9dDl2fkDyS7P4O2bcdbXCVV6SkaeNCNk5ZAbbRgA5wdCQE/z45cJaciTKwah+pHcN4ytcy8I5zbg410CrA9z5PWAfFxdjQ1EyuvqheDGFbnFDsZJsBP3/7P6/JU3qgep2uM6YYbY8dVpVuf6ECgYEA6xAo1O6U6gW5T7qpZ0CX3MOJVw1aFSTYoa8Rrnyq/ljLGkmNtnp9K+DDGdETAlBemfEc3sntqubIxC0/twQfErFdn0gLnS0gLBbBeYPZUmmAtIyR3dCmwlaJmok+YCTNDgz+RsbFRrS6V9WVREguEpY5NsTGltrmOLZ7WlWmsWcCgYEA6NHGg0PDuTF/MoUgL6ZnzdCtEoN+uV+NAq85C/smngbfFNR+ttFteKzLsbyJydfBFgTS2cnPXyuK/NY6Qa/lXZfxxcfmqv7mreBKc3usSqveXRRbqm6wMz6kSXWk8/2HzywRX9JPNauW13Otyl8l/myC69nAHzVbQey8sy1eab0CgYBb08o/tJxT97x22xLGlUM+KN0ENuEUFXrTXtLneShLiGB/enBz8tHnTDyrXzOv2bm7JagDmJrSAqo3iP20/1UsNkG+saRn2HMTBii60bkaKsDux2NMZfBfRvMmfaryYC4C6SyEda4nev64xWU0cYYeGLVtId36nLUHPrJdjcw6zQKBgQC74zc6DcDxPpGxGqBb9AYHoeVacIYfYY4x4Wi/U4LZux/i9o4AScj3vzNvj4D/REAN3fyvR98zpbc3zkcbZbFLs+iovWdZDfbp0X0j72WeqU79fQVw6H0IDgCVS/y/7xXfymeHFflYjc5gt3lEPT8zMS0C2yrhxLSN4lhynSV5XQKBgA2jWpc40dHIKJaIYgJp1wS4QCP21J06EdYdu3bHARPXP75nm8L6m3nh9iQRWanx1x2Fulgq3rVbX/Re0xT7zVaKaH3iaUwZmnbP+LF0/2exXw+FNlSTfA7tR0eXhYtg6CBapQs9+Wc40wYK93xsROMoGEGrwFzuBiPV7H7N9VBP-----END PRIVATE KEY-----\n#   publicKey: -----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoKs+iVwVQmQT+zXJH28M7DV69TjvRI+/+7/Thflhl2X0vAxgzNKEPlhmHAXpIt38kuWYEJCN1Qke6F8jchYp8mQEZuEMYoMZxxpeClW56/g4xZtOuyyTWtoKRX3fyfe3onKyywcLnBbSisrTiYp0tPtYe4Yg8nd3+FjPu4h1l2eEeeny4J7qagPb1WqVRVD+wjJLK49uMx36GMxTQ0f4RpNhJ7m5rUMlDjuL8Qohh3fXSCOOOZZh59hR/MC2AXgyKu8Uei6uWZjEp64VR/mwxnvh+WzQpm6GsCPB0ElwdtmNzrI/bWmugM3yL7//muAk6u3M5eZRrFCBeZ5aSEp6qQIDAQAB-----END PUBLIC KEY-----\n#   privateKey: -----BEGIN PRIVATE KEY-----MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCgqz6JXBVCZBP7NckfbwzsNXr1OO9Ej7/7v9OF+WGXZfS8DGDM0oQ+WGYcBeki3fyS5ZgQkI3VCR7oXyNyFinyZARm4QxigxnHGl4KVbnr+DjFm067LJNa2gpFfd/J97eicrLLBwucFtKKytOJinS0+1h7hiDyd3f4WM+7iHWXZ4R56fLgnupqA9vVapVFUP7CMksrj24zHfoYzFNDR/hGk2EnubmtQyUOO4vxCiGHd9dII445lmHn2FH8wLYBeDIq7xR6Lq5ZmMSnrhVH+bDGe+H5bNCmboawI8HQSXB22Y3Osj9taa6AzfIvv/+a4CTq7czl5lGsUIF5nlpISnqpAgMBAAECggEAEudzKThDbBdYXoNaka9ARv6X5Ah55og/X4CXg7ju6aBeZk3SiebvFmLSSNPNNYQG9sW62aAYgAtdrpubEQ5YiyOHfQ9XpLOmMc4BaJfjk0cWbvGyFsXM5LCo6ro/vYS+/4cdUFQY4pmw1hG9R/6Zcr3sEESc1cqGvBq+/mK060Zb2XAnCoOB9hZv/9DDxqu2RzX6KxLFLigWjXau2cQkHijW/+rgkKbNcT14Cccf2v9sRKjfDBm8hND3eRYpQqVx4JEfJSE8gvKNMzbN5jMKujLLLi3Zqc8gzhCgHnstPBF4Jceev2+WuXxmvvkQB+vyqNee+f0gbrc+B0n4CBo+fQKBgQDexshyCv27eRaF2dCBw8S+wceHCaaWaD70FwhfbEx63ESQ/tY/x9yknP2cIBDFBNyjLoi2gxUv2CmwyO3uMt1/ZsSWV3Zy6h9ON1ygwk/Ri8U1hhtj+2o5aQYCxYVihp7gRj2djqnGWbmnnCFAONSbbBZ+EMd+Y9qfyWgLqMs6WwKBgQC4oU2CeOs8O/o/XZ014tbAQNC1bGku4HEcMu0CdzxdgDAINU8oo8lMv+TjD0AnjZ6DvhHWt8X/R/jakbCj28tRZGtOr3jd1ydjltzJ+iD13QH9hiyclgIwntCEvZFNV0osvpyFN5zxUC0EYX7XyPazqmOlwuP7bR+SZAUP1zrGSwKBgCcZ/u69bSGttD1UKNvN78eHnazfDcVSkNIpBMgy6w2ZgdNtV0+kld6yy3TO0AK0wsFFlqtqQJrANzkXW2O2u9fzwsecnGa5HzuMTg7NbFMcVEX7vu0CoPdorqxn3OyZMmOtEH2KH9R3xTVdGxz5nJ+XDi+cZFeT0TjAkimRu/IHAoGBAI1CbKkslt5JDVg56bR2z9PcQ1LrlTZWZnQqyoeebsMi9pzxHJV9auCb2SWYX39jrSoi3Ecp6AU/LbkcysczvUWSzT8wpgslOG/L1zV5eIpkA8fTRoKvixFkESneWDOSW6AMXnjWae9/ZlH5vjCfA5HHBeKnUALoJMW+XNkX1VItAoGABaTfRtjO4/uKqYQ1/Y5XVhloiz7Hy80SZrXpxBYYsIEMAjqnbrfYHb9pUToICbEYHorX/rE8rwElbUFw4Gw0/jseS6+WAx5xs7/pF9eirk7LscbTPAmqfTqdwRswNvflioAn8DnDZArSxWOOo1VR/Bz245YcggRlRNFYoLGpFFk=-----END PRIVATE KEY-----','8053b2d35f3bb8ec38efc185134fd7a7','2023-06-19 02:28:00','2026-05-09 11:14:54',NULL,'0:0:0:0:0:0:0:1','','application-dev','','','','yaml','','');
INSERT INTO `config_info` VALUES (15,'wzkris-user-center.yml','APPLICATION_GROUP','# spring配置\nspring:\n  # datasource:\n  #   driver-class-name: org.apache.shardingsphere.driver.ShardingSphereDriver\n  #   url: jdbc:shardingsphere:classpath:sharding-${spring.profiles.active}.yml\n  datasource:\n    url: jdbc:postgresql://localhost:5432/wzkris_user_center?ssl=false&reWriteBatchedInserts=true&stringtype=unspecified\n    username: root\n    password: root\n    driver-class-name: org.postgresql.Driver\n    hikari:\n      connection-timeout: 30000 \n      maximum-pool-size: 10       \n      minimum-idle: 5             \n      idle-timeout: 600000        \n      pool-name: hikari-pool\n\n# springdoc配置\nspringdoc:\n  title: 用户中心接口文档\n  license: Powered By wzkris\n  version: v1.0.0\n  description: ---\n\n# 租户配置\ntenant:\n  includes:\n    - member_info\n    - post_info\n    - tenant_info\n    - tenant_wallet_info\n    - tenant_wallet_record\n    - tenant_wallet_withdrawal_record','16541a57c9198d882920bb1d2f140c7b','2024-04-16 06:36:22','2026-07-14 18:13:58',NULL,'0:0:0:0:0:0:0:1','','application-dev','','','','yaml','','');
INSERT INTO `config_info` VALUES (16,'wzkris-monitor-admin.yml','APPLICATION_GROUP','# spring\nspring:\n  security:\n    user:\n      name: admin\n      password: admin123\n  boot:\n    admin:\n      ui:\n        title: 服务状态监控\n','dd19c14e3cebc473140e1fc8733a339d','2023-06-19 02:28:00','2025-09-29 11:55:07',NULL,'0:0:0:0:0:0:0:1','','application-dev','','','','yaml','','');
INSERT INTO `config_info` VALUES (18,'sentinel-gateway','APPLICATION_GROUP','[\n    {\n        \"resource\": \"wzkris-auth\",\n        \"count\": 2000,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n	{\n        \"resource\": \"wzkris-system\",\n        \"count\": 1000,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n    {\n        \"resource\": \"wzkris-user-center\",\n        \"count\": 1000,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    },\n    {\n        \"resource\": \"wzkris-monitor-admin\",\n        \"count\": 300,\n        \"grade\": 1,\n        \"limitApp\": \"default\",\n        \"strategy\": 0,\n        \"controlBehavior\": 0,\n        \"clusterMode\": false\n    }\n]','0c01771ac42e7ee302dc5ec79947b677','2023-06-19 02:28:00','2026-02-25 15:08:46',NULL,'0:0:0:0:0:0:0:1','','application-dev','网关限流策略','','','json','','');
INSERT INTO `config_info` VALUES (21,'redis.yml','COMMON_GROUP','spring:\n  redis:\n    redisson:\n      config: |\n        # 集群配置\n        clusterServersConfig: \n          # Redis集群节点地址列表\n          nodeAddresses:\n            - \"redis://127.0.0.1:6379\"\n            - \"redis://127.0.0.1:6380\"\n            - \"redis://127.0.0.1:6381\"\n          # 集群拓扑扫描间隔 ms\n          scanInterval: 5000\n          # 无密码则设置 null\n          password: null\n          # 客户端名称\n          clientName: ${spring.application.name}\n          # 主节点最小连接数\n          masterConnectionMinimumIdleSize: 6\n          # 主节点最大连接数\n          masterConnectionPoolSize: 18\n          # 从节点最小连接数\n          slaveConnectionMinimumIdleSize: 6\n          # 从节点最大连接数\n          slaveConnectionPoolSize: 18\n          # 命令等待超时,单位:毫秒\n          timeout: 3000\n          # 发布和订阅连接池大小\n          subscriptionConnectionPoolSize: 30\n        # 线程池数量\n        threads: 8\n        # Netty线程池数量\n        nettyThreads: 8\n        codec: !<org.redisson.codec.JsonJacksonCodec> {}\n        transportMode: \"NIO\"\n\n\n# spring:\n#   redis:\n#     redisson: \n#       config: |\n#         # 单节点配置\n#         singleServerConfig: \n#           # redis 节点地址\n#           address: \"redis://127.0.0.1:6379\"\n#           # 无密码则设置 null\n#           password: null\n#           # 客户端名称\n#           clientName: ${spring.application.name}\n#           # 最小空闲连接数\n#           connectionMinimumIdleSize: 32\n#           # 连接池大小\n#           connectionPoolSize: 64\n#           # 连接空闲超时,单位:毫秒\n#           idleConnectionTimeout: 10000\n#           # 命令等待超时,单位:毫秒\n#           timeout: 3000\n#           # 发布和订阅连接池大小\n#           subscriptionConnectionPoolSize: 50\n#         # 线程池数量\n#         threads: 8\n#         # Netty线程池数量\n#         nettyThreads: 8\n#         codec: !<org.redisson.codec.JsonJacksonCodec> {}\n#         transportMode: \"NIO\"\n','33047a58e1d443074cbc95257bdb4216','2025-08-04 15:12:22','2026-01-15 16:44:10',NULL,'0:0:0:0:0:0:0:1','','application-dev','redis公共配置','','','yaml','','');
INSERT INTO `config_info` VALUES (22,'wzkris-captcha.yml','APPLICATION_GROUP','risk-captcha:\n  passTtlSeconds: 180','a0887872d4f7f6d5aef5923322591441','2026-05-21 11:25:06','2026-05-21 11:27:38',NULL,'0:0:0:0:0:0:0:1','','application-dev','','','','yaml','','');

--
-- Table structure for table `config_info_aggr`
--

DROP TABLE IF EXISTS `config_info_aggr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `config_info_aggr` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'datum_id',
  `content` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `app_name` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tenant_id` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='增加租户字段';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_info_aggr`
--


--
-- Table structure for table `config_info_beta`
--

DROP TABLE IF EXISTS `config_info_beta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `config_info_beta` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'app_name',
  `content` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'content',
  `beta_ips` text COLLATE utf8mb4_unicode_ci COMMENT 'betaIps',
  `md5` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` longtext COLLATE utf8mb4_unicode_ci COMMENT 'source user',
  `src_ip` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户字段',
  `encrypted_data_key` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '秘钥',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`,`group_id`,`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='config_info_beta';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_info_beta`
--


--
-- Table structure for table `config_info_gray`
--

DROP TABLE IF EXISTS `config_info_gray`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `config_info_gray` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `content` longtext NOT NULL COMMENT 'content',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `src_user` text COMMENT 'src_user',
  `src_ip` varchar(100) DEFAULT NULL COMMENT 'src_ip',
  `gmt_create` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'gmt_create',
  `gmt_modified` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'gmt_modified',
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
  `gray_name` varchar(128) NOT NULL COMMENT 'gray_name',
  `gray_rule` text NOT NULL COMMENT 'gray_rule',
  `encrypted_data_key` varchar(256) NOT NULL DEFAULT '' COMMENT 'encrypted_data_key',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_configinfogray_datagrouptenantgray` (`data_id`,`group_id`,`tenant_id`,`gray_name`) USING BTREE,
  KEY `idx_dataid_gmt_modified` (`data_id`,`gmt_modified`) USING BTREE,
  KEY `idx_gmt_modified` (`gmt_modified`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='config_info_gray';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_info_gray`
--


--
-- Table structure for table `config_info_tag`
--

DROP TABLE IF EXISTS `config_info_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `config_info_tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'tenant_id',
  `tag_id` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'app_name',
  `content` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'content',
  `md5` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` longtext COLLATE utf8mb4_unicode_ci COMMENT 'source user',
  `src_ip` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`,`group_id`,`tenant_id`,`tag_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='config_info_tag';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_info_tag`
--


--
-- Table structure for table `config_tags_relation`
--

DROP TABLE IF EXISTS `config_tags_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `config_tags_relation` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `tag_name` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'tenant_id',
  `nid` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`) USING BTREE,
  UNIQUE KEY `uk_configtagrelation_configidtag` (`id`,`tag_name`,`tag_type`) USING BTREE,
  KEY `idx_tenant_id` (`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='config_tag_relation';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_tags_relation`
--


--
-- Table structure for table `group_capacity`
--

DROP TABLE IF EXISTS `group_capacity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `group_capacity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Group ID，空字符表示整个集群',
  `quota` int(11) NOT NULL COMMENT '配额，0表示使用默认值',
  `usage` int(11) NOT NULL COMMENT '使用量',
  `max_size` int(11) NOT NULL COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(11) NOT NULL COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int(11) NOT NULL COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(11) NOT NULL COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_group_id` (`group_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='集群、各Group容量信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_capacity`
--


--
-- Table structure for table `his_config_info`
--

DROP TABLE IF EXISTS `his_config_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `his_config_info` (
  `id` bigint(20) unsigned NOT NULL,
  `nid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `group_id` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL,
  `app_name` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'app_name',
  `content` longtext COLLATE utf8mb4_unicode_ci NOT NULL,
  `md5` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `src_user` longtext COLLATE utf8mb4_unicode_ci,
  `src_ip` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `op_type` char(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tenant_id` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户字段',
  `encrypted_data_key` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '秘钥',
  `publish_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT 'formal' COMMENT 'publish type gray or formal',
  `gray_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'publish type gray or formal',
  `ext_info` longtext COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`nid`) USING BTREE,
  KEY `idx_did` (`data_id`) USING BTREE,
  KEY `idx_gmt_create` (`gmt_create`) USING BTREE,
  KEY `idx_gmt_modified` (`gmt_modified`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='多租户改造';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `his_config_info`
--

-- ----------------------------
-- Records of his_config_info
-- ----------------------------

--
-- Table structure for table `permissions`
--

DROP TABLE IF EXISTS `permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permissions` (
  `role` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `resource` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `action` varchar(8) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permissions`
--

INSERT INTO `permissions` VALUES ('prod','application-prod:*:*','rw');
INSERT INTO `permissions` VALUES ('prod',':*:*','rw');
INSERT INTO `permissions` VALUES ('dev','application-dev:*:*','rw');
INSERT INTO `permissions` VALUES ('dev',':*:*','rw');

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roles` (
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  UNIQUE KEY `uk_username_role` (`username`,`role`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

INSERT INTO `roles` VALUES ('dev','dev');
INSERT INTO `roles` VALUES ('nacos','ROLE_ADMIN');
INSERT INTO `roles` VALUES ('prod','prod');

--
-- Table structure for table `tenant_capacity`
--

DROP TABLE IF EXISTS `tenant_capacity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tenant_capacity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Tenant ID',
  `quota` int(11) NOT NULL COMMENT '配额，0表示使用默认值',
  `usage` int(11) NOT NULL COMMENT '使用量',
  `max_size` int(11) NOT NULL COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(11) NOT NULL COMMENT '聚合子配置最大个数',
  `max_aggr_size` int(11) NOT NULL COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(11) NOT NULL COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_tenant_id` (`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='租户容量信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tenant_capacity`
--


--
-- Table structure for table `tenant_info`
--

DROP TABLE IF EXISTS `tenant_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tenant_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'tenant_id',
  `tenant_name` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'tenant_name',
  `tenant_desc` text COLLATE utf8mb4_unicode_ci COMMENT 'tenant_desc',
  `create_source` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`,`tenant_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='tenant_info';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tenant_info`
--

INSERT INTO `tenant_info` VALUES (1,'1','application-dev','application-dev','应用开发环境','nacos',1685070991448,1685071009622);
INSERT INTO `tenant_info` VALUES (2,'1','application-prod','application-prod','应用生产环境','nacos',1687141596216,1687141596216);
INSERT INTO `tenant_info` VALUES (5,'2','nacos-default-mcp','nacos-default-mcp','Nacos default AI MCP module.','nacos',1749453112939,1749453112939);

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `enabled` varchar(5) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

INSERT INTO `users` VALUES ('dev','$2a$10$zOHL8QGqN6PNlYRluJpvluKDP16Add3ywu00K2O70klCmSnj8gKzm','t');
INSERT INTO `users` VALUES ('nacos','$2a$10$h05FUQ0x5eypjsF02DWyd.2PXBDgXb7GIXNZCjRim6EORDlTLTRLu','t');
INSERT INTO `users` VALUES ('prod','$2a$10$AdFVZMyO8R4ZbjIVSlQWBukjx83Buc.x54x1fmwDyLDhT.uWSXi9S','1');
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-16 15:47:35
