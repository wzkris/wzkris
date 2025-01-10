/*
 Navicat Premium Data Transfer

 Source Server         : local_mysql8
 Source Server Type    : MySQL
 Source Server Version : 80035
 Source Host           : localhost:3306
 Source Schema         : wzkris_user

 Target Server Type    : MySQL
 Target Server Version : 80035
 File Encoding         : 65001

 Date: 10/01/2025 09:27:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS wzkris_user default character set utf8mb4 collate utf8mb4_unicode_ci;
USE wzkris_user;
-- ----------------------------
-- Table structure for app_user
-- ----------------------------
DROP TABLE IF EXISTS `app_user`;
CREATE TABLE `app_user`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `nickname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `phone_number` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT '状态值',
  `gender` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '2' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像地址',
  `login_ip` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录ip',
  `login_date` bigint NULL DEFAULT NULL COMMENT '登录时间',
  `create_at` bigint NOT NULL COMMENT '创建时间',
  `create_id` bigint NOT NULL COMMENT '创建者',
  `update_at` bigint NULL DEFAULT NULL COMMENT '更新时间',
  `update_id` bigint NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `ad_phoneNumber`(`phone_number` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of app_user
-- ----------------------------
INSERT INTO `app_user` VALUES (1826896461245968384, NULL, '15888888888', '0', '2', NULL, '127.0.0.1', 1732946029937, 1713334134616, 1, NULL, NULL);

-- ----------------------------
-- Table structure for app_user_thirdinfo
-- ----------------------------
DROP TABLE IF EXISTS `app_user_thirdinfo`;
CREATE TABLE `app_user_thirdinfo`  (
  `user_id` bigint NOT NULL,
  `openid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `app_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'appid',
  `channel` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '渠道',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `uk_openid`(`openid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '第三方信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of app_user_thirdinfo
-- ----------------------------

-- ----------------------------
-- Table structure for app_user_wallet
-- ----------------------------
DROP TABLE IF EXISTS `app_user_wallet`;
CREATE TABLE `app_user_wallet`  (
  `user_id` bigint NOT NULL,
  `balance` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '余额, 元',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT '状态',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户钱包' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of app_user_wallet
-- ----------------------------
INSERT INTO `app_user_wallet` VALUES (1826896461245968384, 0.00, '0');

-- ----------------------------
-- Table structure for app_user_wallet_record
-- ----------------------------
DROP TABLE IF EXISTS `app_user_wallet_record`;
CREATE TABLE `app_user_wallet_record`  (
  `record_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `amount` decimal(10, 2) UNSIGNED NOT NULL COMMENT '金额',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '记录类型',
  `pay_time` bigint NOT NULL COMMENT '时间',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`record_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户钱包记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of app_user_wallet_record
-- ----------------------------

-- ----------------------------
-- Table structure for oauth2_client
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_client`;
CREATE TABLE `oauth2_client`  (
  `id` bigint NOT NULL,
  `client_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '客户端名称',
  `client_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `client_secret` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `scopes` json NOT NULL COMMENT '权限域',
  `authorization_grant_types` json NOT NULL COMMENT '授权类型',
  `redirect_uris` json NOT NULL COMMENT '回调地址',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT '客户端状态',
  `auto_approve` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否自动放行',
  `create_at` bigint NOT NULL,
  `create_id` bigint NOT NULL,
  `update_at` bigint NULL DEFAULT NULL,
  `update_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_client_id`(`client_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'OAUTH2客户端' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of oauth2_client
-- ----------------------------
INSERT INTO `oauth2_client` VALUES (1, '系统', 'server', '{bcrypt}$2a$10$hK9Sv9kAvXE00fWtkWxzI.Ns4.5SuQteTJAnsFWXChlOWIUZSFYL2', '[\"openid\"]', '[\"password\", \"sms\", \"refresh_token\"]', '[\"http://localhost:9001/oauth2/authorization_code_callback\"]', '0', 0, 1713334134616, 1, 1736472115713, 1);
INSERT INTO `oauth2_client` VALUES (2, '测试客户端', 'test_cl', '{bcrypt}$2a$10$kLcJ/zFM6vMfmEWhsuYlHOHkpTQm4b6LT5Lvg.XiYx1koSqM7fg2u', '[\"read\", \"write\"]', '[\"urn:ietf:params:oauth:grant-type:device_code\"]', '[]', '0', 0, 1732518072056, 1, 1735955145307, 1);

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint NOT NULL COMMENT '部门id',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `parent_id` bigint NOT NULL DEFAULT 0 COMMENT '父部门id',
  `ancestors` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT '祖级列表',
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '部门名称',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '0代表正常 1代表停用',
  `dept_sort` int NULL DEFAULT NULL COMMENT '显示顺序',
  `contact` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `create_at` bigint NOT NULL COMMENT '创建时间',
  `create_id` bigint NOT NULL COMMENT '创建者',
  `update_at` bigint NULL DEFAULT NULL COMMENT '更新时间',
  `update_id` bigint NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`dept_id`) USING BTREE,
  INDEX `ancestors`(`ancestors` ASC) USING BTREE,
  INDEX `parent_id`(`parent_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (100, 0, 0, '0', '最高部门', '0', 0, '15888888888', '', 1713334134616, 1, 20240403145415, 1);
INSERT INTO `sys_dept` VALUES (105, 0, 114, '0,100,114', 'jeecg集团', '1', 3, '15888888888', 'jeecg@qq.com', 1713334134616, 1, 1735110050181, 1);
INSERT INTO `sys_dept` VALUES (114, 0, 1858433026613760002, '0,100,1858433026613760002', 'pig开源联盟', '0', 0, NULL, NULL, 1713334134616, 1, 1735110050184, 1);
INSERT INTO `sys_dept` VALUES (1775382319191453698, 1774671331416821762, 0, '0', '默认租户部门', '0', 0, NULL, NULL, 1713334134616, 1774671331412627456, 20240412112834, 1);
INSERT INTO `sys_dept` VALUES (1775387364419072002, 1774671331416821762, 1775382319191453698, '0,1775382319191453698', '默认租户销售部门', '0', 0, '13566699669', NULL, 1713334134616, 1774671331412627456, 20240403132635, 1);
INSERT INTO `sys_dept` VALUES (1857293481839091714, 1853719125330489346, 0, '0', '2号', '0', 0, NULL, NULL, 1731648247581, 1, 1731648247581, 1);
INSERT INTO `sys_dept` VALUES (1857293508972044290, 1853719125330489346, 1857293481839091714, '0,1857293481839091714', 'xx', '0', 0, NULL, NULL, 1731648254040, 1, 1731648254040, 1);
INSERT INTO `sys_dept` VALUES (1858433026613760002, 0, 100, '0,100', 'xxx', '0', 0, NULL, NULL, 1731919936222, 1, 1731919936222, 1);

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  `menu_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint NOT NULL COMMENT '父菜单ID',
  `menu_sort` int NOT NULL COMMENT '显示顺序',
  `path` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '#' COMMENT '路由地址',
  `component` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '组件路径',
  `query` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '路由参数',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '#' COMMENT '菜单图标',
  `is_frame` tinyint(1) NOT NULL COMMENT '是否为外链',
  `is_cache` tinyint(1) NOT NULL COMMENT '是否缓存',
  `is_visible` tinyint(1) NOT NULL COMMENT '是否显示',
  `create_at` bigint NOT NULL COMMENT '创建时间',
  `create_id` bigint NOT NULL COMMENT '创建者',
  `update_at` bigint NULL DEFAULT NULL COMMENT '更新时间',
  `update_id` bigint NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`menu_id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 100, 'system', NULL, NULL, 'D', '0', NULL, 'system', 0, 0, 1, 1713334134616, 1, 1732349257371, 1);
INSERT INTO `sys_menu` VALUES (2, '用户权限管理', 0, 99, 'user', NULL, NULL, 'D', '0', NULL, 'peoples', 0, 0, 1, 1713334134616, 1, 1731996088642, 1);
INSERT INTO `sys_menu` VALUES (3, '商户管理', 0, 50, 'tenant', NULL, NULL, 'D', '0', NULL, 'merchant', 0, 0, 1, 1713334134616, 1, 1730344304191, 1);
INSERT INTO `sys_menu` VALUES (4, '设备管理', 0, 10, 'equipment', NULL, NULL, 'D', '0', NULL, 'device', 0, 0, 1, 1713334134616, 1, 1731995658381, 1);
INSERT INTO `sys_menu` VALUES (5, '交易订单', 0, 4, 'trade', NULL, NULL, 'D', '0', NULL, 'order', 0, 0, 1, 1713334134616, 1, 1732672652217, 1);
INSERT INTO `sys_menu` VALUES (100, '系统消息', 1, 15, 'sys_message', 'system/sysmessage/index', NULL, 'M', '0', 'sys_message:list', 'guide', 0, 0, 1, 1713334134616, 1, 1733991014933, 1);
INSERT INTO `sys_menu` VALUES (101, '控制台入口', 1, 0, 'controller', NULL, NULL, 'D', '0', NULL, 'dashboard', 0, 0, 1, 1713334134616, 1, 1730270954332, 1);
INSERT INTO `sys_menu` VALUES (102, '字典管理', 1, 6, 'dict', 'system/dict/index', NULL, 'M', '0', 'dict:list', 'dict', 0, 0, 1, 1713334134616, 1, 1714113020581, NULL);
INSERT INTO `sys_menu` VALUES (103, '参数设置', 1, 7, 'config', 'system/config/index', NULL, 'M', '0', 'config:list', 'edit', 0, 0, 1, 1713334134616, 1, 1714113020581, NULL);
INSERT INTO `sys_menu` VALUES (104, '日志管理', 1, 1, 'log', NULL, NULL, 'D', '0', NULL, 'log', 0, 0, 1, 1713334134616, 1, 1730336344130, 1);
INSERT INTO `sys_menu` VALUES (150, '操作日志', 104, 1, 'operlog', 'system/operlog/index', NULL, 'M', '0', 'operlog:list', 'form', 0, 0, 1, 1713334134616, 1, 1714113020581, NULL);
INSERT INTO `sys_menu` VALUES (151, '登录日志', 104, 2, 'loginlog', 'system/loginlog/index', NULL, 'M', '0', 'loginlog:list', 'logininfor', 0, 0, 1, 1713334134616, 1, 1730336338258, 1);
INSERT INTO `sys_menu` VALUES (201, '顾客管理', 2, 1, 'appuser', 'user/appuser/index', NULL, 'M', '0', 'app_user:list', 'user2', 0, 0, 1, 1713334134616, 1, 1731996271184, 1);
INSERT INTO `sys_menu` VALUES (202, '会员体系', 2, 2, 'vip', 'user/vip/index', NULL, 'M', '0', 'vip:list', 'build', 0, 0, 1, 1713334134616, 1, 1731996267958, 1);
INSERT INTO `sys_menu` VALUES (203, '后台用户', 2, 100, 'sysuser', 'user/sysuser/index', NULL, 'M', '0', 'sys_user:list', 'user', 0, 1, 1, 1713334134616, 1, 1733383530966, 1);
INSERT INTO `sys_menu` VALUES (205, '部门管理', 2, 70, 'dept', 'user/dept/index', NULL, 'M', '0', 'dept:list', 'tree', 0, 0, 1, 1713334134616, 1, 1731996252876, 1);
INSERT INTO `sys_menu` VALUES (206, '角色管理', 2, 99, 'role', 'user/role/index', NULL, 'M', '0', 'sys_role:list', 'role', 0, 0, 1, 1713334134616, 1, 1733984758532, 1);
INSERT INTO `sys_menu` VALUES (207, '菜单管理', 2, 50, 'menu', 'user/menu/index', NULL, 'M', '0', 'menu:list', 'tree-table', 0, 0, 1, 1713334134616, 1, 1731996177723, 1);
INSERT INTO `sys_menu` VALUES (208, '岗位管理', 2, 8, 'post', 'user/post/index', NULL, 'M', '0', 'post:list', 'post', 0, 0, 1, 1713334134616, 1, 1731996261504, 1);
INSERT INTO `sys_menu` VALUES (300, '定时任务', 101, 20, 'http://localhost:9200/xxl-job-admin', NULL, NULL, 'M', '0', 'job:list', 'job', 1, 0, 1, 1713334134616, 1, 1719987477468, 1);
INSERT INTO `sys_menu` VALUES (301, '系统接口', 101, 2, 'http://localhost:8080/doc.html', NULL, NULL, 'M', '0', 'tool:swagger:list', 'swagger', 0, 0, 1, 1713334134616, 1, 1714113020581, NULL);
INSERT INTO `sys_menu` VALUES (302, 'Sentinel控制台', 101, 3, 'http://localhost:8718', NULL, NULL, 'M', '0', 'monitor:sentinel:list', 'sentinel', 1, 0, 1, 1713334134616, 1, 1714113020581, NULL);
INSERT INTO `sys_menu` VALUES (303, 'Nacos控制台', 101, 4, 'http://127.0.0.1:8848/nacos', NULL, NULL, 'M', '0', 'monitor:nacos:list', 'nacos', 1, 0, 1, 1713334134616, 1, 1714113020581, NULL);
INSERT INTO `sys_menu` VALUES (304, '服务监控', 101, 5, 'http://localhost:9100/', NULL, NULL, 'M', '0', 'monitor:server:list', 'server', 1, 0, 1, 1713334134616, 1, 1719987239231, 1);
INSERT INTO `sys_menu` VALUES (400, '站点管理', 4, 10, 'station', 'equipment/station/index', NULL, 'M', '0', 'station:list', 'location', 0, 0, 1, 1713334134616, 1, 1733734162176, 1);
INSERT INTO `sys_menu` VALUES (401, '设备列表', 4, 50, 'device', 'equipment/device/index', NULL, 'M', '0', 'device:list', 'chargePile', 0, 1, 1, 1713334134616, 1, 1733734143112, 1);
INSERT INTO `sys_menu` VALUES (402, '协议管理', 4, 30, 'protocol', 'equipment/protocol/index', NULL, 'M', '0', 'protocol:list', 'protocol', 0, 0, 1, 1732063013229, 1, 1733734147401, 1);
INSERT INTO `sys_menu` VALUES (403, '产品管理', 4, 20, 'product', 'equipment/product/index', NULL, 'M', '0', 'product:list', 'product', 0, 0, 1, 1733298652177, 1, 1733734156104, 1);
INSERT INTO `sys_menu` VALUES (404, '设备地图', 4, 10, 'map', 'equipment/map/index', NULL, 'M', '0', NULL, 'map', 0, 0, 1, 1733706990166, 1, 1733734277275, 1);
INSERT INTO `sys_menu` VALUES (500, '订单管理', 5, 100, 'order', 'trade/order/index', NULL, 'M', '0', 'order:list', 'list', 0, 0, 1, 1713334134616, 1, 1732672962995, 1);
INSERT INTO `sys_menu` VALUES (501, '优惠券管理', 5, 1, 'coupon', 'trade/coupon/index', NULL, 'M', '0', 'coupon:list', 'coupon', 0, 0, 1, 1713334134616, 1, 1732672870665, 1);
INSERT INTO `sys_menu` VALUES (502, '交易投诉', 5, 5, 'feedback', 'trade/feedback/index', NULL, 'M', '0', NULL, 'feedback', 0, 0, 1, 1713334134616, 1, 1732672943651, 1);
INSERT INTO `sys_menu` VALUES (601, '商户信息', 3, 100, 'info', 'user/tenant/index', NULL, 'M', '0', '', 'information', 0, 0, 1, 1713334134616, 1, 1732159304112, 1);
INSERT INTO `sys_menu` VALUES (602, '租户套餐管理', 3, 50, 'package', 'user/tenant/package/index', NULL, 'M', '0', 'tenant_package:list', 'package', 0, 0, 1, 1713334134616, 1, 1732159308804, 1);
INSERT INTO `sys_menu` VALUES (700, '客户端管理', 2, 3, 'client', 'user/client/index', NULL, 'M', '0', 'oauth2_client:list', 'wechat', 0, 0, 1, 1724135157964, 1, 1731996264887, 1);
INSERT INTO `sys_menu` VALUES (1046, '字典查询', 102, 1, '#', NULL, NULL, 'B', '0', 'dict:query', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, NULL);
INSERT INTO `sys_menu` VALUES (1047, '字典新增', 102, 2, '#', NULL, NULL, 'B', '0', 'dict:add', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, NULL);
INSERT INTO `sys_menu` VALUES (1048, '字典修改', 102, 3, '#', NULL, NULL, 'B', '0', 'dict:edit', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, NULL);
INSERT INTO `sys_menu` VALUES (1049, '字典删除', 102, 4, '#', NULL, NULL, 'B', '0', 'dict:remove', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, NULL);
INSERT INTO `sys_menu` VALUES (1050, '字典导出', 102, 5, '#', NULL, NULL, 'B', '0', 'dict:export', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, NULL);
INSERT INTO `sys_menu` VALUES (1051, '参数查询', 103, 1, '#', NULL, NULL, 'B', '0', 'config:query', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, NULL);
INSERT INTO `sys_menu` VALUES (1052, '参数新增', 103, 2, '#', NULL, NULL, 'B', '0', 'config:add', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, NULL);
INSERT INTO `sys_menu` VALUES (1053, '参数修改', 103, 3, '#', NULL, NULL, 'B', '0', 'config:edit', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, NULL);
INSERT INTO `sys_menu` VALUES (1054, '参数删除', 103, 4, '#', NULL, NULL, 'B', '0', 'config:remove', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, NULL);
INSERT INTO `sys_menu` VALUES (1055, '参数导出', 103, 5, '#', NULL, NULL, 'B', '0', 'config:export', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, NULL);
INSERT INTO `sys_menu` VALUES (1056, '公告查询', 100, 1, '#', NULL, NULL, 'B', '0', 'sys_message:query', '#', 0, 0, 1, 1713334134616, 1, 1734314861788, 1);
INSERT INTO `sys_menu` VALUES (1057, '公告新增', 100, 2, '#', NULL, NULL, 'B', '0', 'sys_message:add', '#', 0, 0, 1, 1713334134616, 1, 1734314858988, 1);
INSERT INTO `sys_menu` VALUES (1058, '公告修改', 100, 3, '#', NULL, NULL, 'B', '0', 'sys_message:edit', '#', 0, 0, 1, 1713334134616, 1, 1734314855835, 1);
INSERT INTO `sys_menu` VALUES (1059, '公告删除', 100, 4, '#', NULL, NULL, 'B', '0', 'sys_message:remove', '#', 0, 0, 1, 1713334134616, 1, 1734314852112, 1);
INSERT INTO `sys_menu` VALUES (1060, '发布公告', 100, 10, '#', NULL, NULL, 'B', '0', 'sys_message:publish', '#', 0, 0, 1, 1734314826956, 1, 1734314826956, 1);
INSERT INTO `sys_menu` VALUES (1061, '操作删除', 150, 2, '#', NULL, NULL, 'B', '0', 'operlog:remove', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, NULL);
INSERT INTO `sys_menu` VALUES (1064, '登录删除', 151, 2, '#', NULL, NULL, 'B', '0', 'loginlog:remove', '#', 0, 0, 1, 1713334134616, 1, 1730336330958, 1);
INSERT INTO `sys_menu` VALUES (1125, '余额记录', 601, 3, '#', NULL, NULL, 'B', '0', 'wallet_record:list', '#', 0, 0, 1, 1732598092738, 1, 1733813517885, 2);
INSERT INTO `sys_menu` VALUES (1126, '商户提现', 601, 1, '#', NULL, NULL, 'B', '0', 'tenant:withdrawal', '#', 0, 0, 1, 1732598071539, 1, 1732598191196, 1);
INSERT INTO `sys_menu` VALUES (1127, '商户余额信息', 601, 0, '#', NULL, NULL, 'B', '0', 'tenant:wallet_info', '#', 0, 0, 1, 1732597687278, 1, 1732598171530, 1);
INSERT INTO `sys_menu` VALUES (1128, '租户列表', 601, 10, '#', NULL, NULL, 'B', '0', 'tenant:list', '#', 0, 0, 1, 1730530211713, 1, 1730530211713, 1);
INSERT INTO `sys_menu` VALUES (1129, '重置租户操作密码', 601, 11, '#', NULL, NULL, 'B', '0', 'tenant:edit_operpwd', '#', 0, 0, 1, 1730882615318, 1, 1732598183996, 1);
INSERT INTO `sys_menu` VALUES (1130, '商户基本信息', 601, 20, '#', '', NULL, 'B', '0', 'tenant:getinfo', '#', 0, 0, 1, 1724380930192, 1, 1732597854658, 1);
INSERT INTO `sys_menu` VALUES (1131, '租户详情', 601, 9, '#', NULL, NULL, 'B', '0', 'tenant:query', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, 1);
INSERT INTO `sys_menu` VALUES (1132, '租户新增', 601, 4, '#', NULL, NULL, 'B', '0', 'tenant:add', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, 1);
INSERT INTO `sys_menu` VALUES (1133, '租户修改', 601, 2, '#', NULL, NULL, 'B', '0', 'tenant:edit', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, 1);
INSERT INTO `sys_menu` VALUES (1134, '租户删除', 601, 2, '#', NULL, NULL, 'B', '0', 'tenant:remove', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, 1);
INSERT INTO `sys_menu` VALUES (1135, '套餐详情', 602, 9, '#', NULL, NULL, 'B', '0', 'tenant_package:query', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, 1);
INSERT INTO `sys_menu` VALUES (1136, '套餐新增', 602, 5, '#', NULL, NULL, 'B', '0', 'tenant_package:add', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, 1);
INSERT INTO `sys_menu` VALUES (1137, '套餐修改', 602, 4, '#', NULL, NULL, 'B', '0', 'tenant_package:edit', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, 1);
INSERT INTO `sys_menu` VALUES (1138, '套餐删除', 602, 2, '#', NULL, NULL, 'B', '0', 'tenant_package:remove', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, 1);
INSERT INTO `sys_menu` VALUES (1210, '客户端详情', 700, 1, '#', NULL, NULL, 'B', '0', 'oauth2_client:query', '#', 0, 0, 1, 1724143965889, 1, 1724143965889, 1);
INSERT INTO `sys_menu` VALUES (1211, '客户端修改', 700, 2, '#', NULL, NULL, 'B', '0', 'oauth2_client:edit', '#', 0, 0, 1, 1724143981415, 1, 1724143981415, 1);
INSERT INTO `sys_menu` VALUES (1212, '客户端添加', 700, 3, '#', NULL, NULL, 'B', '0', 'oauth2_client:add', '#', 0, 0, 1, 1724143996446, 1, 1724143996446, 1);
INSERT INTO `sys_menu` VALUES (1213, '客户端删除', 700, 2, '#', NULL, NULL, 'B', '0', 'oauth2_client:remove', '#', 0, 0, 1, 1724144147977, 1, 1724144147977, 1);
INSERT INTO `sys_menu` VALUES (1214, '客户端导出', 700, 4, '#', NULL, NULL, 'B', '0', 'oauth2_client:export', '#', 0, 0, 1, 1724144162238, 1, 1724144162238, 1);
INSERT INTO `sys_menu` VALUES (1215, '修改密钥', 700, 5, '#', NULL, NULL, 'B', '0', 'oauth2_client:edit_secret', '#', 0, 0, 1, 1733186793544, 1, 1733186793544, 1);
INSERT INTO `sys_menu` VALUES (2001, '用户查询', 201, 1, '#', NULL, NULL, 'B', '0', 'app_user:query', '#', 0, 0, 1, 1713334134616, 1, 1724316600041, 1);
INSERT INTO `sys_menu` VALUES (2002, '用户新增', 201, 2, '#', NULL, NULL, 'B', '0', 'app_user:add', '#', 0, 0, 1, 1713334134616, 1, 1724316597178, 1);
INSERT INTO `sys_menu` VALUES (2003, '用户修改', 201, 3, '#', NULL, NULL, 'B', '0', 'app_user:edit', '#', 0, 0, 1, 1713334134616, 1, 1724316594234, 1);
INSERT INTO `sys_menu` VALUES (2004, '用户删除', 201, 4, '#', NULL, NULL, 'B', '0', 'app_user:remove', '#', 0, 0, 1, 1713334134616, 1, 1724316591506, 1);
INSERT INTO `sys_menu` VALUES (2005, '用户导出', 201, 5, '#', NULL, NULL, 'B', '0', 'app_user:export', '#', 0, 0, 1, 1713334134616, 1, 1724316588203, 1);
INSERT INTO `sys_menu` VALUES (2013, '菜单查询', 207, 1, '#', NULL, NULL, 'B', '0', 'menu:query', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, NULL);
INSERT INTO `sys_menu` VALUES (2014, '菜单新增', 207, 2, '#', NULL, NULL, 'B', '0', 'menu:add', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, NULL);
INSERT INTO `sys_menu` VALUES (2015, '菜单修改', 207, 3, '#', NULL, NULL, 'B', '0', 'menu:edit', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, NULL);
INSERT INTO `sys_menu` VALUES (2016, '菜单删除', 207, 4, '#', NULL, NULL, 'B', '0', 'menu:remove', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, NULL);
INSERT INTO `sys_menu` VALUES (2037, '部门查询', 205, 1, '#', NULL, NULL, 'B', '0', 'dept:query', '#', 0, 0, 1, 1713334134616, 1, 1724316974862, 1);
INSERT INTO `sys_menu` VALUES (2038, '部门新增', 205, 2, '#', NULL, NULL, 'B', '0', 'dept:add', '#', 0, 0, 1, 1713334134616, 1, 1724316972264, 1);
INSERT INTO `sys_menu` VALUES (2039, '部门修改', 205, 3, '#', NULL, NULL, 'B', '0', 'dept:edit', '#', 0, 0, 1, 1713334134616, 1, 1724316969623, 1);
INSERT INTO `sys_menu` VALUES (2040, '部门删除', 205, 4, '#', NULL, NULL, 'B', '0', 'dept:remove', '#', 0, 0, 1, 1713334134616, 1, 1724316967034, 1);
INSERT INTO `sys_menu` VALUES (2062, '重置密码', 203, 7, '#', NULL, NULL, 'B', '0', 'sys_user:resetPwd', '#', 0, 0, 1, 1713334134616, 1, 1724316833128, 1);
INSERT INTO `sys_menu` VALUES (2064, '用户修改', 203, 3, '#', NULL, NULL, 'B', '0', 'sys_user:edit', '#', 0, 0, 1, 1713334134616, 1, 1732352143516, 1);
INSERT INTO `sys_menu` VALUES (2071, '用户查询', 203, 0, '#', NULL, NULL, 'B', '0', 'sys_user:query', '#', 0, 0, 1, 1713334134616, 1, 1724316844479, 1);
INSERT INTO `sys_menu` VALUES (2072, '用户添加', 203, 1, '#', NULL, NULL, 'B', '0', 'sys_user:add', '#', 0, 0, 1, 1713334134616, 1, 1724316841529, 1);
INSERT INTO `sys_menu` VALUES (2077, '用户导出', 203, 1, '#', NULL, NULL, 'B', '0', 'sys_user:export', '#', 0, 0, 1, 1713334134616, 1, 1724316838713, 1);
INSERT INTO `sys_menu` VALUES (2078, '用户删除', 203, 8, '#', NULL, NULL, 'B', '0', 'sys_user:remove', '#', 0, 0, 1, 1713334134616, 1, 1724316829679, 1);
INSERT INTO `sys_menu` VALUES (2112, '等级查询', 202, 0, '#', NULL, NULL, 'B', '0', 'level:query', '#', 0, 0, 1, 1713334134616, 1, 1732352184748, 1);
INSERT INTO `sys_menu` VALUES (2113, '等级修改', 202, 1, '#', NULL, NULL, 'B', '0', 'level:edit', '#', 0, 0, 1, 1713334134616, 1, 1732352180655, 1);
INSERT INTO `sys_menu` VALUES (2114, '等级添加', 202, 2, '#', NULL, NULL, 'B', '0', 'level:add', '#', 0, 0, 1, 1713334134616, 1, 1732352175359, 1);
INSERT INTO `sys_menu` VALUES (2115, '等级删除', 202, 3, '#', NULL, NULL, 'B', '0', 'level:remove', '#', 0, 0, 1, 1713334134616, 1, 1732352168566, 1);
INSERT INTO `sys_menu` VALUES (2141, '岗位查询', 208, 1, '#', NULL, NULL, 'B', '0', 'post:query', '#', 0, 0, 1, 1713334134616, 1, 1724317000834, 1);
INSERT INTO `sys_menu` VALUES (2142, '岗位新增', 208, 2, '#', NULL, NULL, 'B', '0', 'post:add', '#', 0, 0, 1, 1713334134616, 1, 1724316998231, 1);
INSERT INTO `sys_menu` VALUES (2143, '岗位修改', 208, 3, '#', NULL, NULL, 'B', '0', 'post:edit', '#', 0, 0, 1, 1713334134616, 1, 1724316995704, 1);
INSERT INTO `sys_menu` VALUES (2144, '岗位删除', 208, 4, '#', NULL, NULL, 'B', '0', 'post:remove', '#', 0, 0, 1, 1713334134616, 1, 1724316993025, 1);
INSERT INTO `sys_menu` VALUES (2145, '岗位导出', 208, 5, '#', NULL, NULL, 'B', '0', 'post:export', '#', 0, 0, 1, 1713334134616, 1, 1724316990336, 1);
INSERT INTO `sys_menu` VALUES (2207, '权限授予', 206, 6, '#', NULL, NULL, 'B', '0', 'sys_role:auth', '#', 0, 0, 1, 1713334134616, 1, 1732352108035, 1);
INSERT INTO `sys_menu` VALUES (2208, '角色查询', 206, 1, '#', NULL, NULL, 'B', '0', 'sys_role:query', '#', 0, 0, 1, 1713334134616, 1, 1724316959450, 1);
INSERT INTO `sys_menu` VALUES (2209, '角色新增', 206, 2, '#', NULL, NULL, 'B', '0', 'sys_role:add', '#', 0, 0, 1, 1713334134616, 1, 1724316956840, 1);
INSERT INTO `sys_menu` VALUES (2210, '角色修改', 206, 3, '#', NULL, NULL, 'B', '0', 'sys_role:edit', '#', 0, 0, 1, 1713334134616, 1, 1724316954128, 1);
INSERT INTO `sys_menu` VALUES (2211, '角色删除', 206, 4, '#', NULL, NULL, 'B', '0', 'sys_role:remove', '#', 0, 0, 1, 1713334134616, 1, 1724316951594, 1);
INSERT INTO `sys_menu` VALUES (3121, '站点查询', 400, 0, '#', NULL, NULL, 'B', '0', 'station:query', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, NULL);
INSERT INTO `sys_menu` VALUES (3122, '站点添加', 400, 1, '#', NULL, NULL, 'B', '0', 'station:add', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, NULL);
INSERT INTO `sys_menu` VALUES (3123, '站点修改', 400, 2, '#', NULL, NULL, 'B', '0', 'station:edit', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, NULL);
INSERT INTO `sys_menu` VALUES (3124, '站点删除', 400, 3, '#', NULL, NULL, 'B', '0', 'station:remove', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, NULL);
INSERT INTO `sys_menu` VALUES (3125, '站点导出', 400, 5, '#', NULL, NULL, 'B', '0', 'station:export', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, NULL);
INSERT INTO `sys_menu` VALUES (3126, '绑定解绑设备', 400, 10, '#', NULL, NULL, 'B', '0', 'station:bind', '#', 0, 0, 1, 1734483472755, 1, 1734483472755, 1);
INSERT INTO `sys_menu` VALUES (3226, '设备查询', 401, 0, '#', NULL, NULL, 'B', '0', 'device:query', '#', 0, 0, 1, 1713334134616, 1, 1733299444542, 1);
INSERT INTO `sys_menu` VALUES (3227, '设备添加', 401, 1, '#', NULL, NULL, 'B', '0', 'device:add', '#', 0, 0, 1, 1713334134616, 1, 1733299441446, 1);
INSERT INTO `sys_menu` VALUES (3228, '设备修改', 401, 2, '#', NULL, NULL, 'B', '0', 'device:edit', '#', 0, 0, 1, 1713334134616, 1, 1733299436836, 1);
INSERT INTO `sys_menu` VALUES (3229, '设备删除', 401, 3, '#', NULL, NULL, 'B', '0', 'device:remove', '#', 0, 0, 1, 1713334134616, 1, 1733299429260, 1);
INSERT INTO `sys_menu` VALUES (3230, '设备导出', 401, 4, '#', NULL, NULL, 'B', '0', 'device:export', '#', 0, 0, 1, 1713334134616, 1, 1733299432300, 1);
INSERT INTO `sys_menu` VALUES (3231, '敏感字段修改', 3228, 10, '#', NULL, NULL, 'F', '0', 'device:field', '#', 0, 0, 1, 1734503404422, 1, 1734509030344, 1);
INSERT INTO `sys_menu` VALUES (3301, '协议详情', 402, 10, '#', NULL, NULL, 'B', '0', 'protocol:query', '#', 0, 0, 1, 1732063192917, 1, 1733299479557, 1);
INSERT INTO `sys_menu` VALUES (3302, '协议添加', 402, 5, '#', NULL, NULL, 'B', '0', 'protocol:add', '#', 0, 0, 1, 1732063208719, 1, 1733299482580, 1);
INSERT INTO `sys_menu` VALUES (3303, '协议修改', 402, 4, '#', NULL, NULL, 'B', '0', 'protocol:edit', '#', 0, 0, 1, 1732063222263, 1, 1733299485501, 1);
INSERT INTO `sys_menu` VALUES (3304, '协议删除', 402, 1, '#', NULL, NULL, 'B', '0', 'protocol:remove', '#', 0, 0, 1, 1732063237210, 1, 1733299488717, 1);
INSERT INTO `sys_menu` VALUES (3401, '产品详情', 403, 10, '#', NULL, NULL, 'B', '0', 'product:query', '#', 0, 0, 1, 1733299234968, 1, 1733299451271, 1);
INSERT INTO `sys_menu` VALUES (3402, '产品添加', 403, 8, '#', NULL, NULL, 'B', '0', 'product:add', '#', 0, 0, 1, 1733299308349, 1, 1733299455452, 1);
INSERT INTO `sys_menu` VALUES (3403, '产品修改', 403, 4, '#', NULL, NULL, 'B', '0', 'product:edit', '#', 0, 0, 1, 1733299326513, 1, 1733299458723, 1);
INSERT INTO `sys_menu` VALUES (3404, '产品删除', 403, 1, '#', NULL, NULL, 'B', '0', 'product:remove', '#', 0, 0, 1, 1733299339587, 1, 1733299462176, 1);
INSERT INTO `sys_menu` VALUES (4102, '交易查询', 500, 0, '#', NULL, NULL, 'B', '0', 'order:query', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, NULL);
INSERT INTO `sys_menu` VALUES (4204, '优惠券查询', 501, 0, '#', NULL, NULL, 'B', '0', 'coupon:query', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, NULL);
INSERT INTO `sys_menu` VALUES (4205, '优惠券编辑', 501, 1, '#', NULL, NULL, 'B', '0', 'coupon:edit', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, NULL);
INSERT INTO `sys_menu` VALUES (4206, '优惠券添加', 501, 1, '#', NULL, NULL, 'B', '0', 'coupon:add', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, NULL);
INSERT INTO `sys_menu` VALUES (4207, '优惠券删除', 501, 2, '#', NULL, NULL, 'B', '0', 'coupon:remove', '#', 0, 0, 1, 1713334134616, 1, 1714113020581, NULL);

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `post_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '岗位编码',
  `post_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '岗位名称',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `post_sort` int NOT NULL COMMENT '显示顺序',
  `create_at` bigint NOT NULL COMMENT '创建时间',
  `create_id` bigint NOT NULL COMMENT '创建者',
  `update_at` bigint NULL DEFAULT NULL COMMENT '更新时间',
  `update_id` bigint NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '岗位信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, 0, 'CEO', '执行总裁', '0', 1, 1713334134616, 1, 1719041786749, 1);
INSERT INTO `sys_post` VALUES (4, 0, 'CTO', '首席技术官', '1', 3, 1713334134616, 1, 1724740002367, 2);
INSERT INTO `sys_post` VALUES (1776484907454525442, 1774671331416821762, NULL, 'CEO', '0', 0, 1713334134616, 1774671331412627456, 20240406133942, 1774671331412627456);
INSERT INTO `sys_post` VALUES (1857295398191411202, 1853719125330489346, NULL, 'xx', '0', 0, 1731648704471, 1853719125066248192, 1731648704471, 1853719125066248192);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `data_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '4' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `role_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '角色名称',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常，1停用）',
  `role_sort` int NOT NULL COMMENT '显示顺序',
  `is_menu_display` tinyint(1) NULL DEFAULT NULL COMMENT '菜单选项是否关联显示',
  `is_dept_display` tinyint(1) NULL DEFAULT NULL COMMENT '部门选项是否关联显示',
  `create_at` bigint NOT NULL COMMENT '创建时间',
  `create_id` bigint NOT NULL COMMENT '创建者',
  `update_at` bigint NULL DEFAULT NULL COMMENT '更新时间',
  `update_id` bigint NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (2, 0, '1', '开发者', '0', 99, 0, 1, 1713334134616, 1, 1735267827187, 1);
INSERT INTO `sys_role` VALUES (3, 0, '4', '观察者', '0', 97, 1, 1, 1713334134616, 1, 1732349385269, 1);
INSERT INTO `sys_role` VALUES (4, 0, '2', '员工', '1', 5, 1, 0, 1713334134616, 1, 1735088705511, 1);
INSERT INTO `sys_role` VALUES (1775445330027577345, 1774671331416821762, '3', '默认租户角色', '0', 0, 1, 1, 1713334134616, 1774671331412627456, 1734318887182, 1);
INSERT INTO `sys_role` VALUES (1858701878891327490, 0, '4', 'xxx', '0', 0, 1, 1, 1731984035599, 2, 1731984035599, 2);

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` bigint NOT NULL COMMENT '角色id',
  `dept_id` bigint NOT NULL COMMENT '部门id',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色数据权限关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO `sys_role_dept` VALUES (4, 105);
INSERT INTO `sys_role_dept` VALUES (4, 114);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (2, 1);
INSERT INTO `sys_role_menu` VALUES (2, 2);
INSERT INTO `sys_role_menu` VALUES (2, 3);
INSERT INTO `sys_role_menu` VALUES (2, 4);
INSERT INTO `sys_role_menu` VALUES (2, 100);
INSERT INTO `sys_role_menu` VALUES (2, 103);
INSERT INTO `sys_role_menu` VALUES (2, 201);
INSERT INTO `sys_role_menu` VALUES (2, 202);
INSERT INTO `sys_role_menu` VALUES (2, 203);
INSERT INTO `sys_role_menu` VALUES (2, 205);
INSERT INTO `sys_role_menu` VALUES (2, 206);
INSERT INTO `sys_role_menu` VALUES (2, 207);
INSERT INTO `sys_role_menu` VALUES (2, 208);
INSERT INTO `sys_role_menu` VALUES (2, 401);
INSERT INTO `sys_role_menu` VALUES (2, 601);
INSERT INTO `sys_role_menu` VALUES (2, 1051);
INSERT INTO `sys_role_menu` VALUES (2, 1052);
INSERT INTO `sys_role_menu` VALUES (2, 1053);
INSERT INTO `sys_role_menu` VALUES (2, 1054);
INSERT INTO `sys_role_menu` VALUES (2, 1055);
INSERT INTO `sys_role_menu` VALUES (2, 1056);
INSERT INTO `sys_role_menu` VALUES (2, 1057);
INSERT INTO `sys_role_menu` VALUES (2, 1058);
INSERT INTO `sys_role_menu` VALUES (2, 1059);
INSERT INTO `sys_role_menu` VALUES (2, 1128);
INSERT INTO `sys_role_menu` VALUES (2, 1130);
INSERT INTO `sys_role_menu` VALUES (2, 1131);
INSERT INTO `sys_role_menu` VALUES (2, 1132);
INSERT INTO `sys_role_menu` VALUES (2, 1133);
INSERT INTO `sys_role_menu` VALUES (2, 1134);
INSERT INTO `sys_role_menu` VALUES (2, 2001);
INSERT INTO `sys_role_menu` VALUES (2, 2013);
INSERT INTO `sys_role_menu` VALUES (2, 2014);
INSERT INTO `sys_role_menu` VALUES (2, 2015);
INSERT INTO `sys_role_menu` VALUES (2, 2016);
INSERT INTO `sys_role_menu` VALUES (2, 2037);
INSERT INTO `sys_role_menu` VALUES (2, 2038);
INSERT INTO `sys_role_menu` VALUES (2, 2039);
INSERT INTO `sys_role_menu` VALUES (2, 2040);
INSERT INTO `sys_role_menu` VALUES (2, 2062);
INSERT INTO `sys_role_menu` VALUES (2, 2064);
INSERT INTO `sys_role_menu` VALUES (2, 2071);
INSERT INTO `sys_role_menu` VALUES (2, 2072);
INSERT INTO `sys_role_menu` VALUES (2, 2077);
INSERT INTO `sys_role_menu` VALUES (2, 2078);
INSERT INTO `sys_role_menu` VALUES (2, 2112);
INSERT INTO `sys_role_menu` VALUES (2, 2113);
INSERT INTO `sys_role_menu` VALUES (2, 2114);
INSERT INTO `sys_role_menu` VALUES (2, 2115);
INSERT INTO `sys_role_menu` VALUES (2, 2141);
INSERT INTO `sys_role_menu` VALUES (2, 2142);
INSERT INTO `sys_role_menu` VALUES (2, 2143);
INSERT INTO `sys_role_menu` VALUES (2, 2144);
INSERT INTO `sys_role_menu` VALUES (2, 2145);
INSERT INTO `sys_role_menu` VALUES (2, 2207);
INSERT INTO `sys_role_menu` VALUES (2, 2208);
INSERT INTO `sys_role_menu` VALUES (2, 2209);
INSERT INTO `sys_role_menu` VALUES (2, 2210);
INSERT INTO `sys_role_menu` VALUES (2, 2211);
INSERT INTO `sys_role_menu` VALUES (2, 3226);
INSERT INTO `sys_role_menu` VALUES (2, 3227);
INSERT INTO `sys_role_menu` VALUES (2, 3228);
INSERT INTO `sys_role_menu` VALUES (2, 3229);
INSERT INTO `sys_role_menu` VALUES (2, 3230);
INSERT INTO `sys_role_menu` VALUES (3, 2);
INSERT INTO `sys_role_menu` VALUES (3, 203);
INSERT INTO `sys_role_menu` VALUES (3, 206);
INSERT INTO `sys_role_menu` VALUES (3, 2062);
INSERT INTO `sys_role_menu` VALUES (3, 2064);
INSERT INTO `sys_role_menu` VALUES (3, 2071);
INSERT INTO `sys_role_menu` VALUES (3, 2072);
INSERT INTO `sys_role_menu` VALUES (3, 2077);
INSERT INTO `sys_role_menu` VALUES (3, 2078);
INSERT INTO `sys_role_menu` VALUES (3, 2207);
INSERT INTO `sys_role_menu` VALUES (3, 2208);
INSERT INTO `sys_role_menu` VALUES (3, 2209);
INSERT INTO `sys_role_menu` VALUES (3, 2210);
INSERT INTO `sys_role_menu` VALUES (3, 2211);
INSERT INTO `sys_role_menu` VALUES (4, 4);
INSERT INTO `sys_role_menu` VALUES (4, 5);
INSERT INTO `sys_role_menu` VALUES (4, 400);
INSERT INTO `sys_role_menu` VALUES (4, 401);
INSERT INTO `sys_role_menu` VALUES (4, 402);
INSERT INTO `sys_role_menu` VALUES (4, 403);
INSERT INTO `sys_role_menu` VALUES (4, 404);
INSERT INTO `sys_role_menu` VALUES (4, 500);
INSERT INTO `sys_role_menu` VALUES (4, 501);
INSERT INTO `sys_role_menu` VALUES (4, 502);
INSERT INTO `sys_role_menu` VALUES (4, 3121);
INSERT INTO `sys_role_menu` VALUES (4, 3122);
INSERT INTO `sys_role_menu` VALUES (4, 3123);
INSERT INTO `sys_role_menu` VALUES (4, 3124);
INSERT INTO `sys_role_menu` VALUES (4, 3125);
INSERT INTO `sys_role_menu` VALUES (4, 3126);
INSERT INTO `sys_role_menu` VALUES (4, 3226);
INSERT INTO `sys_role_menu` VALUES (4, 3227);
INSERT INTO `sys_role_menu` VALUES (4, 3228);
INSERT INTO `sys_role_menu` VALUES (4, 3229);
INSERT INTO `sys_role_menu` VALUES (4, 3230);
INSERT INTO `sys_role_menu` VALUES (4, 3231);
INSERT INTO `sys_role_menu` VALUES (4, 3301);
INSERT INTO `sys_role_menu` VALUES (4, 3302);
INSERT INTO `sys_role_menu` VALUES (4, 3303);
INSERT INTO `sys_role_menu` VALUES (4, 3304);
INSERT INTO `sys_role_menu` VALUES (4, 3401);
INSERT INTO `sys_role_menu` VALUES (4, 3402);
INSERT INTO `sys_role_menu` VALUES (4, 3403);
INSERT INTO `sys_role_menu` VALUES (4, 3404);
INSERT INTO `sys_role_menu` VALUES (4, 4102);
INSERT INTO `sys_role_menu` VALUES (4, 4204);
INSERT INTO `sys_role_menu` VALUES (4, 4205);
INSERT INTO `sys_role_menu` VALUES (4, 4206);
INSERT INTO `sys_role_menu` VALUES (4, 4207);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 2);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 203);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 205);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 208);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 2037);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 2038);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 2039);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 2040);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 2071);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 2141);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 2142);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 2143);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 2144);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 2145);

-- ----------------------------
-- Table structure for sys_tenant
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant`;
CREATE TABLE `sys_tenant`  (
  `tenant_id` bigint NOT NULL COMMENT '租户编号',
  `administrator` bigint NOT NULL COMMENT '管理员ID',
  `tenant_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT '租户类型',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
  `tenant_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '租户名称',
  `oper_pwd` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作密码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT '租户状态',
  `domain` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '域名',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `package_id` bigint NOT NULL COMMENT '租户套餐编号',
  `expire_time` bigint NOT NULL DEFAULT -1 COMMENT '过期时间（-1不限制）',
  `account_limit` smallint NOT NULL DEFAULT 5 COMMENT '账号数量（-1不限制）',
  `role_limit` smallint NOT NULL DEFAULT 5 COMMENT '角色数量（-1不限制）',
  `post_limit` smallint NOT NULL DEFAULT 5 COMMENT '岗位数量（-1不限制）',
  `dept_limit` smallint NOT NULL DEFAULT 5 COMMENT '部门数量（-1不限制）',
  `create_id` bigint NOT NULL COMMENT '创建者',
  `create_at` bigint NOT NULL COMMENT '创建时间',
  `update_id` bigint NULL DEFAULT NULL COMMENT '更新者',
  `update_at` bigint NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`tenant_id`) USING BTREE,
  UNIQUE INDEX `uk_administrator`(`administrator` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '租户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_tenant
-- ----------------------------
INSERT INTO `sys_tenant` VALUES (1774671331416821762, 1774671331412627456, '0', '0000', '测试租户', '{bcrypt}$2a$10$JHoZHN4go8y7T1EEgs3sfOTvfySwCN8bLxVZM.3pB7hk0oNLSmIxS', '0', NULL, NULL, 1773625804122202113, -1, 5, 5, 5, 5, 1, 1713334134616, 1, 1733894016830);
INSERT INTO `sys_tenant` VALUES (1853719125330489346, 1853719125066248192, '0', '00', '租户2', '{bcrypt}$2a$10$PFnKxjjXZuFnwdvLAqrFd.m5lls2JcXWJ2gcMQYphGv0I6K0Fwgrm', '1', NULL, '第二个', 1773620875265482754, 1733896268000, 5, 5, 5, 5, 1, 1730796054571, 1, 1733901461866);

-- ----------------------------
-- Table structure for sys_tenant_package
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant_package`;
CREATE TABLE `sys_tenant_package`  (
  `package_id` bigint NOT NULL COMMENT '租户套餐id',
  `package_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '套餐名称',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `menu_ids` json NOT NULL COMMENT '套餐绑定的菜单',
  `is_menu_display` tinyint(1) NOT NULL DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_id` bigint NOT NULL COMMENT '创建者',
  `create_at` bigint NOT NULL COMMENT '创建时间',
  `update_id` bigint NULL DEFAULT NULL COMMENT '更新者',
  `update_at` bigint NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`package_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '租户套餐表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_tenant_package
-- ----------------------------
INSERT INTO `sys_tenant_package` VALUES (1773620875265482754, 'cc', '1', '[]', 1, NULL, 1, 1713334134616, 1, 1735179990954);
INSERT INTO `sys_tenant_package` VALUES (1773625804122202113, '默认套餐', '0', '[1, 104, 2, 3, 601, 4, 401, 151, 1064, 203, 2078, 2062, 2064, 2077, 2072, 2071, 206, 2207, 2211, 2210, 2209, 2208, 205, 2040, 2039, 2038, 2037, 208, 2145, 2144, 2143, 2142, 2141, 1130, 1125, 1126, 1127, 3228, 3226, 400, 3126, 3125, 3124, 3123, 3122, 3121]', 1, '通用租户套餐', 1, 1713334134616, 1, 1735267380741);

-- ----------------------------
-- Table structure for sys_tenant_wallet
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant_wallet`;
CREATE TABLE `sys_tenant_wallet`  (
  `tenant_id` bigint NOT NULL,
  `balance` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '余额, 元',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT '状态',
  PRIMARY KEY (`tenant_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '租户钱包' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_tenant_wallet
-- ----------------------------
INSERT INTO `sys_tenant_wallet` VALUES (1774671331416821762, 0.00, '0');
INSERT INTO `sys_tenant_wallet` VALUES (1853719125330489346, 0.00, '0');

-- ----------------------------
-- Table structure for sys_tenant_wallet_record
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant_wallet_record`;
CREATE TABLE `sys_tenant_wallet_record`  (
  `record_id` bigint NOT NULL,
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `amount` decimal(10, 2) UNSIGNED NOT NULL COMMENT '金额',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '记录类型',
  `create_at` bigint NOT NULL COMMENT '时间',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`record_id`) USING BTREE,
  INDEX `t_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '租户钱包记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_tenant_wallet_record
-- ----------------------------
INSERT INTO `sys_tenant_wallet_record` VALUES (1, 1774671331416821762, 1.00, '1', 1731996255985, NULL);
INSERT INTO `sys_tenant_wallet_record` VALUES (2, 1853719125330489346, 2.00, '0', 1732006255985, NULL);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint NOT NULL COMMENT '管理员ID',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门ID',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户账号',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `nickname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `phone_number` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT '状态值',
  `gender` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '2' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `login_ip` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '登录ip',
  `login_date` bigint NULL DEFAULT NULL COMMENT '登录时间',
  `remark` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_at` bigint NOT NULL COMMENT '创建时间',
  `create_id` bigint NOT NULL COMMENT '创建者',
  `update_at` bigint NULL DEFAULT NULL COMMENT '更新时间',
  `update_id` bigint NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `ad_username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `ad_phoneNumber`(`phone_number` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 0, 100, 'admin', 'xxxxx@163.com', 'nick_admin', '15888888888', '0', '1', NULL, '{bcrypt}$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '127.0.0.1', 1732947864801, NULL, 1713334134616, 1, 1734156343758, 1);
INSERT INTO `sys_user` VALUES (2, 0, 100, 'wzkris', '111111@1.com', 'nick_kris', NULL, '0', '0', NULL, '{bcrypt}$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '127.0.0.1', 1731992153861, NULL, 1713334134616, 1, 1734578386768, 1);
INSERT INTO `sys_user` VALUES (1774671331412627456, 1774671331416821762, NULL, 'testtt', NULL, NULL, NULL, '0', '2', NULL, '{bcrypt}$2a$10$omhFd0wHbTQeALj2bMkVv.kBTk2.grgWI1gHdeF2TtsHVPO/UwmGm', '127.0.0.1', 1732598359333, NULL, 1713334134616, 1, NULL, NULL);
INSERT INTO `sys_user` VALUES (1853719125066248192, 1853719125330489346, NULL, 'testtt2', NULL, NULL, NULL, '0', '2', NULL, '{bcrypt}$2a$10$v544q0b/1YjPbVQJDRKZrOnXoRxRcR.eyxIUd33TMRNCNXdVh.1Eu', '127.0.0.1', 1731648341626, NULL, 1730796054646, 1, NULL, NULL);
INSERT INTO `sys_user` VALUES (1856251200466030593, 1774671331416821762, 1775382319191453698, '___sub_', NULL, 'xxxxxx', NULL, '0', '2', NULL, '{bcrypt}$2a$10$nRc3b1tgQQCQ/58unpvsHuP9q02lQVJru0JgBR/oePVUcjyc/Pl8a', NULL, NULL, NULL, 1731399748330, 1774671331412627456, 1731399941611, 1774671331412627456);
INSERT INTO `sys_user` VALUES (1856869914760638466, 1774671331416821762, NULL, 'ccccccc', NULL, 'zzzz', NULL, '0', '2', NULL, '{bcrypt}$2a$10$NnSa6nPOcCQkJI8u19EgKuIND0rggfB3qAucSA7mAJwDzTc2uJBEu', NULL, NULL, NULL, 1731547261314, 1, 1731547261314, 1);

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `user_id` bigint NOT NULL COMMENT '管理员ID',
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户与岗位关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES (2, 1);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint NOT NULL COMMENT '管理员ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户和角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (2, 2);
INSERT INTO `sys_user_role` VALUES (1856251200466030593, 1775445330027577345);
INSERT INTO `sys_user_role` VALUES (1856869914760638466, 1775445330027577345);

SET FOREIGN_KEY_CHECKS = 1;
