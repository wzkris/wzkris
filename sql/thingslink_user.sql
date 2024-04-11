/*
 Navicat Premium Data Transfer

 Source Server         : local_mysql8
 Source Server Type    : MySQL
 Source Server Version : 80035
 Source Host           : localhost:3306
 Source Schema         : thingslink_user

 Target Server Type    : MySQL
 Target Server Version : 80035
 File Encoding         : 65001

 Date: 16/04/2024 14:42:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS thingslink_user;
USE thingslink_user;
-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `nickname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `phone_number` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '状态值',
  `gender` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像地址',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
  `login_ip` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录ip',
  `login_date` datetime NULL DEFAULT NULL COMMENT '登录时间',
  `remark` json NULL COMMENT '用户额外信息',
  `create_at` datetime NOT NULL COMMENT '创建时间',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建者',
  `update_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `ad_phoneNumber`(`phone_number` ASC, `is_deleted` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of customer
-- ----------------------------
INSERT INTO `customer` VALUES (11111111, NULL, '15888888888', '0', NULL, NULL, 0, '0:0:0:0:0:0:0:1', '2024-04-13 15:33:48', NULL, '2024-04-10 08:42:11', '1', NULL, NULL);

-- ----------------------------
-- Table structure for oauth2_registered_client
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_registered_client`;
CREATE TABLE `oauth2_registered_client`  (
  `id` bigint NOT NULL,
  `client_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `client_secret` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `scopes` json NOT NULL COMMENT '权限域',
  `authorization_grant_types` json NOT NULL COMMENT '授权类型',
  `redirect_uris` json NOT NULL COMMENT '回调地址',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端状态',
  `auto_approve` tinyint(1) NOT NULL COMMENT '是否自动放行',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_at` datetime NULL DEFAULT NULL,
  `update_at` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_client_id`(`client_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'OAUTH2客户端' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of oauth2_registered_client
-- ----------------------------
INSERT INTO `oauth2_registered_client` VALUES (1, 'server', '$2a$10$jKXqoIKK.w57jS5MKKasH.NQxxpfyUOlvP9yIXYsXelR7dbUGgBn.', '[\"server\"]', '[\"password\", \"sms\", \"refresh_token\", \"client_credentials\"]', '[]', '0', 0, NULL, NULL, NULL, NULL);
INSERT INTO `oauth2_registered_client` VALUES (2, 'messaging-client', '$2a$10$jKXqoIKK.w57jS5MKKasH.NQxxpfyUOlvP9yIXYsXelR7dbUGgBn.', '[\"openid\", \"profile\", \"message.read\", \"message.write\"]', '[\"password\", \"sms\", \"refresh_token\", \"client_credentials\", \"authorization_code\"]', '[\"http://localhost:9000/oauth2/authorization_code_callback\"]', '0', 0, NULL, NULL, '2024-03-08 08:26:33', '2024-03-08 08:26:35');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint NOT NULL COMMENT '部门id',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `parent_id` bigint NOT NULL DEFAULT 0 COMMENT '父部门id',
  `ancestors` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '祖级列表',
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '0代表正常 1代表停用',
  `dept_sort` int NULL DEFAULT NULL COMMENT '显示顺序',
  `contact` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `create_at` datetime NOT NULL COMMENT '创建时间',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建者',
  `update_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`dept_id`) USING BTREE,
  INDEX `ancestors`(`ancestors` ASC) USING BTREE,
  INDEX `parent_id`(`parent_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (100, 0, 0, '0', '最高部门', '0', 0, '15888888888', '', '2022-12-19 10:05:59', '', '2024-04-03 14:54:15', '1');
INSERT INTO `sys_dept` VALUES (101, 0, 114, '0,100,114', 'galaxy', '0', 1, '15888888888', 'ry@qq.com', '2022-12-19 10:05:59', '', '2024-04-03 14:52:28', '1');
INSERT INTO `sys_dept` VALUES (105, 0, 100, '0,100', 'jeecg集团', '0', 3, '15888888888', 'jeecg@qq.com', '2022-12-19 10:05:59', '', '2023-09-19 09:39:31', NULL);
INSERT INTO `sys_dept` VALUES (114, 0, 100, '0,100', 'pig开源联盟', '0', 0, NULL, NULL, '2023-02-09 16:49:08', '', '2024-04-03 14:52:28', '1');
INSERT INTO `sys_dept` VALUES (1775382319191453698, 1774671331416821762, 0, '0', '默认租户部门', '0', 0, NULL, NULL, '2024-04-03 12:38:25', '1774671331412627456', '2024-04-12 11:28:34', '1');
INSERT INTO `sys_dept` VALUES (1775387364419072002, 1774671331416821762, 1775382319191453698, '0,1775382319191453698', '默认租户销售部门', '0', 0, '13566699669', NULL, '2024-04-03 12:58:27', '1774671331412627456', '2024-04-03 13:26:35', '1');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  `menu_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父菜单ID',
  `menu_sort` int NULL DEFAULT NULL COMMENT '显示顺序',
  `path` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由地址',
  `component` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件路径',
  `query` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由参数',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `is_frame` tinyint(1) NOT NULL COMMENT '是否为外链',
  `is_cache` tinyint(1) NOT NULL COMMENT '是否缓存',
  `is_visible` tinyint(1) NOT NULL COMMENT '是否显示',
  `create_at` datetime NOT NULL COMMENT '创建时间',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建者',
  `update_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`menu_id`) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统服务', 0, 100, 'basic', NULL, '', 'M', '0', '', 'system', 0, 0, 1, '2022-12-19 10:06:00', '', '2024-04-03 11:18:23', '1');
INSERT INTO `sys_menu` VALUES (2, '用户及权限服务', 0, 99, 'auth', NULL, '', 'M', '0', '', 'peoples', 0, 0, 1, '2022-12-19 10:06:00', '', '2024-03-18 15:56:28', '1');
INSERT INTO `sys_menu` VALUES (3, '租户管理', 0, 50, 'tenant', NULL, '', 'M', '0', '', 'monitor', 0, 0, 1, '2022-12-19 10:06:00', '', '2024-03-18 16:01:48', '1');
INSERT INTO `sys_menu` VALUES (4, '设备管理', 0, 2, 'equipment', NULL, NULL, 'M', '0', '', 'icon', 0, 0, 1, '2023-06-05 15:54:49', '', '2024-04-02 16:52:13', '1774671331412627456');
INSERT INTO `sys_menu` VALUES (5, '订单服务', 0, 4, 'order', NULL, NULL, 'M', '0', '', 'money', 0, 0, 1, '2023-02-07 13:18:53', '', '2023-08-12 10:46:53', NULL);
INSERT INTO `sys_menu` VALUES (100, '通知公告', 1, 15, 'notice', 'system/notice/index', '', 'C', '0', 'notice:list', 'guide', 0, 0, 1, '2022-12-19 10:06:00', '', '2024-03-18 15:56:46', '1');
INSERT INTO `sys_menu` VALUES (101, '控制台入口', 1, 0, 'controller', NULL, NULL, 'M', '0', '', 'message', 0, 0, 1, '2023-02-09 17:07:34', '', '2024-03-18 15:57:17', '1');
INSERT INTO `sys_menu` VALUES (102, '字典管理', 1, 6, 'dict', 'system/dict/index', '', 'C', '0', 'dict:list', 'dict', 0, 0, 1, '2022-12-19 10:06:00', '', NULL, NULL);
INSERT INTO `sys_menu` VALUES (103, '参数设置', 1, 7, 'config', 'system/config/index', '', 'C', '0', 'config:list', 'edit', 0, 0, 1, '2022-12-19 10:06:00', '', NULL, NULL);
INSERT INTO `sys_menu` VALUES (104, '日志管理', 1, 1, 'log', '', '', 'M', '0', '', 'log', 0, 0, 1, '2022-12-19 10:06:00', '', '2024-03-18 15:57:22', '1');
INSERT INTO `sys_menu` VALUES (105, '定时任务', 1, 20, 'job', 'system/job/index', '', 'C', '0', 'job:list', 'job', 0, 0, 1, '2022-12-19 10:06:00', '', '2024-03-18 16:09:47', '1');
INSERT INTO `sys_menu` VALUES (150, '操作日志', 104, 1, 'operlog', 'system/operlog/index', '', 'C', '0', 'operlog:list', 'form', 0, 0, 1, '2022-12-19 10:06:01', '', NULL, NULL);
INSERT INTO `sys_menu` VALUES (151, '登录日志', 104, 2, 'loginlog', 'system/loginlog/index', '', 'C', '0', 'loginlog:list', 'logininfor', 0, 0, 1, '2022-12-19 10:06:01', '', '2023-08-26 16:32:49', NULL);
INSERT INTO `sys_menu` VALUES (201, '用户管理', 2, 1, 'customer', 'auth/customer/index', '', 'C', '0', 'customer:list', 'user', 0, 0, 1, '2022-12-19 10:06:00', '', '2023-08-26 16:07:16', NULL);
INSERT INTO `sys_menu` VALUES (202, '会员体系', 2, 2, 'vip', 'auth/vip/index', NULL, 'C', '0', 'vip:list', 'build', 0, 0, 1, '2023-04-25 14:06:12', '', '2023-08-29 10:08:48', NULL);
INSERT INTO `sys_menu` VALUES (203, '后台管理', 2, 100, 'user', 'auth/user/index', NULL, 'C', '0', 'user:list', 'people', 0, 0, 1, '2023-01-14 16:54:58', '', '2023-08-26 16:07:26', NULL);
INSERT INTO `sys_menu` VALUES (205, '部门管理', 2, 70, 'dept', 'auth/dept/index', '', 'C', '0', 'dept:list', 'tree', 0, 0, 1, '2022-12-19 10:06:00', '', '2024-03-18 15:37:04', '1');
INSERT INTO `sys_menu` VALUES (206, '角色管理', 2, 99, 'role', 'auth/role/index', '', 'C', '0', 'role:list', 'peoples', 0, 0, 1, '2022-12-19 10:06:00', '', '2023-08-26 16:07:32', NULL);
INSERT INTO `sys_menu` VALUES (207, '菜单管理', 2, 50, 'menu', 'auth/menu/index', '', 'C', '0', 'menu:list', 'tree-table', 0, 0, 1, '2022-12-19 10:06:00', '', '2023-05-31 13:14:34', NULL);
INSERT INTO `sys_menu` VALUES (208, '岗位管理', 2, 8, 'post', 'auth/post/index', '', 'C', '0', 'post:list', 'post', 0, 0, 1, '2022-12-19 10:06:00', '', '2023-08-26 16:07:38', NULL);
INSERT INTO `sys_menu` VALUES (301, '系统接口', 101, 2, 'http://localhost:8080/doc.html', '', '', 'C', '0', 'tool:swagger:list', 'swagger', 0, 0, 1, '2022-12-19 10:06:01', '', '2023-06-07 09:26:47', NULL);
INSERT INTO `sys_menu` VALUES (302, 'Sentinel控制台', 101, 3, 'http://localhost:8718', '', '', 'C', '0', 'monitor:sentinel:list', 'sentinel', 1, 0, 1, '2022-12-19 10:06:00', '', '2023-05-24 14:24:33', NULL);
INSERT INTO `sys_menu` VALUES (303, 'Nacos控制台', 101, 4, 'http://127.0.0.1:8848/nacos', '', '', 'C', '0', 'monitor:nacos:list', 'nacos', 1, 0, 1, '2022-12-19 10:06:00', '', '2023-05-24 14:24:39', NULL);
INSERT INTO `sys_menu` VALUES (304, 'Admin控制台', 101, 5, 'http://localhost:9100/login', '', '', 'C', '0', 'monitor:server:list', 'server', 1, 0, 1, '2022-12-19 10:06:00', '', '2023-05-24 14:24:42', NULL);
INSERT INTO `sys_menu` VALUES (400, '站点管理', 4, 0, 'station', 'equipment/station/index', NULL, 'C', '0', 'station:list', '#', 0, 0, 1, '2023-06-05 15:57:26', '', NULL, NULL);
INSERT INTO `sys_menu` VALUES (401, '设备管理', 4, 1, 'device', 'equipment/device/index', NULL, 'C', '0', 'device:list', '#', 0, 0, 1, '2023-06-05 15:58:17', '', '2023-08-25 08:21:04', NULL);
INSERT INTO `sys_menu` VALUES (500, '订单管理', 5, 0, 'order', 'order/order/index', NULL, 'C', '0', 'order:list', '#', 0, 0, 1, '2023-02-07 13:24:05', '', '2023-08-12 10:47:11', NULL);
INSERT INTO `sys_menu` VALUES (501, '优惠券管理', 5, 1, 'coupon', 'order/coupon/index', NULL, 'C', '0', 'coupon:list', '#', 0, 0, 1, '2023-02-07 13:25:25', '', '2023-05-17 23:42:21', NULL);
INSERT INTO `sys_menu` VALUES (502, '订单投诉', 5, 5, 'feedback', 'order/feedback/index', '', 'C', '0', '', '#', 0, 0, 1, '2023-02-07 13:21:01', '', '2023-08-12 10:47:29', NULL);
INSERT INTO `sys_menu` VALUES (600, '租户管理', 3, 100, 'tenant', 'auth/tenant/index', '', 'C', '0', 'tenant:list', NULL, 0, 0, 1, '2024-03-29 09:50:29', '1', '2024-03-29 09:51:38', '1');
INSERT INTO `sys_menu` VALUES (601, '租户套餐管理', 3, 50, 'package', 'auth/tenant/package/index', NULL, 'C', '0', 'tenantPackage:list', NULL, 0, 0, 1, '2024-03-29 10:03:13', '1', '2024-03-29 10:05:51', '1');
INSERT INTO `sys_menu` VALUES (1000, '用户查询', 201, 1, '', '', '', 'F', '0', 'customer:query', '#', 0, 0, 1, '2022-12-19 10:06:01', '', '2023-06-12 08:53:47', NULL);
INSERT INTO `sys_menu` VALUES (1001, '用户新增', 201, 2, '', '', '', 'F', '0', 'customer:add', '#', 0, 0, 1, '2022-12-19 10:06:01', '', '2023-06-12 08:53:50', NULL);
INSERT INTO `sys_menu` VALUES (1002, '用户修改', 201, 3, '', '', '', 'F', '0', 'customer:edit', '#', 0, 0, 1, '2022-12-19 10:06:01', '', '2023-06-12 08:53:53', NULL);
INSERT INTO `sys_menu` VALUES (1003, '用户删除', 201, 4, '', '', '', 'F', '0', 'customer:remove', '#', 0, 0, 1, '2022-12-19 10:06:01', '', '2023-06-12 08:53:57', NULL);
INSERT INTO `sys_menu` VALUES (1004, '用户导出', 201, 5, '', '', '', 'F', '0', 'customer:export', '#', 0, 0, 1, '2022-12-19 10:06:01', '', '2023-06-12 08:54:00', NULL);
INSERT INTO `sys_menu` VALUES (1006, '重置密码', 203, 7, '', '', '', 'F', '0', 'user:resetPwd', '#', 0, 0, 1, '2022-12-19 10:06:01', '', '2023-06-12 10:51:05', NULL);
INSERT INTO `sys_menu` VALUES (1007, '角色查询', 206, 1, '', '', '', 'F', '0', 'role:query', '#', 0, 0, 1, '2022-12-19 10:06:01', '', '2023-04-19 12:49:50', NULL);
INSERT INTO `sys_menu` VALUES (1008, '角色新增', 206, 2, '', '', '', 'F', '0', 'role:add', '#', 0, 0, 1, '2022-12-19 10:06:01', '', '2023-04-19 12:49:56', NULL);
INSERT INTO `sys_menu` VALUES (1009, '角色修改', 206, 3, '', '', '', 'F', '0', 'role:edit', '#', 0, 0, 1, '2022-12-19 10:06:01', '', '2023-04-19 12:50:01', NULL);
INSERT INTO `sys_menu` VALUES (1010, '角色删除', 206, 4, '', '', '', 'F', '0', 'role:remove', '#', 0, 0, 1, '2022-12-19 10:06:01', '', '2023-04-19 12:50:05', NULL);
INSERT INTO `sys_menu` VALUES (1011, '角色导出', 206, 5, '', '', '', 'F', '0', 'role:export', '#', 0, 0, 1, '2022-12-19 10:06:01', '', '2023-04-19 12:52:19', NULL);
INSERT INTO `sys_menu` VALUES (1012, '菜单查询', 207, 1, '', '', '', 'F', '0', 'menu:query', '#', 0, 0, 1, '2022-12-19 10:06:01', '', '2023-04-22 14:49:44', NULL);
INSERT INTO `sys_menu` VALUES (1013, '菜单新增', 207, 2, '', '', '', 'F', '0', 'menu:add', '#', 0, 0, 1, '2022-12-19 10:06:01', '', '2023-04-22 14:49:49', NULL);
INSERT INTO `sys_menu` VALUES (1014, '菜单修改', 207, 3, '', '', '', 'F', '0', 'menu:edit', '#', 0, 0, 1, '2022-12-19 10:06:01', '', '2023-04-22 14:49:52', NULL);
INSERT INTO `sys_menu` VALUES (1015, '菜单删除', 207, 4, '', '', '', 'F', '0', 'menu:remove', '#', 0, 0, 1, '2022-12-19 10:06:01', '', '2023-04-22 14:49:57', NULL);
INSERT INTO `sys_menu` VALUES (1016, '部门查询', 205, 1, '', '', '', 'F', '0', 'dept:query', '#', 0, 0, 1, '2022-12-19 10:06:01', '', '2023-05-18 05:47:29', NULL);
INSERT INTO `sys_menu` VALUES (1017, '部门新增', 205, 2, '', '', '', 'F', '0', 'dept:add', '#', 0, 0, 1, '2022-12-19 10:06:01', '', '2023-05-18 05:47:26', NULL);
INSERT INTO `sys_menu` VALUES (1018, '部门修改', 205, 3, '', '', '', 'F', '0', 'dept:edit', '#', 0, 0, 1, '2022-12-19 10:06:01', '', '2023-05-18 05:47:23', NULL);
INSERT INTO `sys_menu` VALUES (1019, '部门删除', 205, 4, '', '', '', 'F', '0', 'dept:remove', '#', 0, 0, 1, '2022-12-19 10:06:01', '', '2023-05-18 05:47:17', NULL);
INSERT INTO `sys_menu` VALUES (1020, '岗位查询', 208, 1, '', '', '', 'F', '0', 'post:query', '#', 0, 0, 1, '2022-12-19 10:06:01', '', '2023-04-22 14:53:05', NULL);
INSERT INTO `sys_menu` VALUES (1021, '岗位新增', 208, 2, '', '', '', 'F', '0', 'post:add', '#', 0, 0, 1, '2022-12-19 10:06:01', '', '2023-04-22 14:53:08', NULL);
INSERT INTO `sys_menu` VALUES (1022, '岗位修改', 208, 3, '', '', '', 'F', '0', 'post:edit', '#', 0, 0, 1, '2022-12-19 10:06:01', '', '2023-04-22 14:53:12', NULL);
INSERT INTO `sys_menu` VALUES (1023, '岗位删除', 208, 4, '', '', '', 'F', '0', 'post:remove', '#', 0, 0, 1, '2022-12-19 10:06:02', '', '2023-04-22 14:53:16', NULL);
INSERT INTO `sys_menu` VALUES (1024, '岗位导出', 208, 5, '', '', '', 'F', '0', 'post:export', '#', 0, 0, 1, '2022-12-19 10:06:02', '', '2023-04-22 14:53:20', NULL);
INSERT INTO `sys_menu` VALUES (1025, '字典查询', 102, 1, '#', '', '', 'F', '0', 'dict:query', '#', 0, 0, 1, '2022-12-19 10:06:02', '', NULL, NULL);
INSERT INTO `sys_menu` VALUES (1026, '字典新增', 102, 2, '#', '', '', 'F', '0', 'dict:add', '#', 0, 0, 1, '2022-12-19 10:06:02', '', NULL, NULL);
INSERT INTO `sys_menu` VALUES (1027, '字典修改', 102, 3, '#', '', '', 'F', '0', 'dict:edit', '#', 0, 0, 1, '2022-12-19 10:06:02', '', NULL, NULL);
INSERT INTO `sys_menu` VALUES (1028, '字典删除', 102, 4, '#', '', '', 'F', '0', 'dict:remove', '#', 0, 0, 1, '2022-12-19 10:06:02', '', NULL, NULL);
INSERT INTO `sys_menu` VALUES (1029, '字典导出', 102, 5, '#', '', '', 'F', '0', 'dict:export', '#', 0, 0, 1, '2022-12-19 10:06:02', '', NULL, NULL);
INSERT INTO `sys_menu` VALUES (1030, '参数查询', 103, 1, '#', '', '', 'F', '0', 'config:query', '#', 0, 0, 1, '2022-12-19 10:06:02', '', NULL, NULL);
INSERT INTO `sys_menu` VALUES (1031, '参数新增', 103, 2, '#', '', '', 'F', '0', 'config:add', '#', 0, 0, 1, '2022-12-19 10:06:02', '', NULL, NULL);
INSERT INTO `sys_menu` VALUES (1032, '参数修改', 103, 3, '#', '', '', 'F', '0', 'config:edit', '#', 0, 0, 1, '2022-12-19 10:06:02', '', NULL, NULL);
INSERT INTO `sys_menu` VALUES (1033, '参数删除', 103, 4, '#', '', '', 'F', '0', 'config:remove', '#', 0, 0, 1, '2022-12-19 10:06:02', '', NULL, NULL);
INSERT INTO `sys_menu` VALUES (1034, '参数导出', 103, 5, '#', '', '', 'F', '0', 'config:export', '#', 0, 0, 1, '2022-12-19 10:06:02', '', NULL, NULL);
INSERT INTO `sys_menu` VALUES (1035, '公告查询', 100, 1, '#', '', '', 'F', '0', 'message:notice:query', '#', 0, 0, 1, '2022-12-19 10:06:02', '', '2023-01-14 09:15:00', NULL);
INSERT INTO `sys_menu` VALUES (1036, '公告新增', 100, 2, '#', '', '', 'F', '0', 'message:notice:add', '#', 0, 0, 1, '2022-12-19 10:06:02', '', '2023-01-14 09:15:05', NULL);
INSERT INTO `sys_menu` VALUES (1037, '公告修改', 100, 3, '#', '', '', 'F', '0', 'message:notice:edit', '#', 0, 0, 1, '2022-12-19 10:06:02', '', '2023-01-14 09:15:09', NULL);
INSERT INTO `sys_menu` VALUES (1038, '公告删除', 100, 4, '#', '', '', 'F', '0', 'message:notice:remove', '#', 0, 0, 1, '2022-12-19 10:06:02', '', '2023-01-14 09:15:14', NULL);
INSERT INTO `sys_menu` VALUES (1039, '操作查询', 150, 1, '#', '', '', 'F', '0', 'operlog:query', '#', 0, 0, 1, '2022-12-19 10:06:02', '', '2023-06-12 08:48:33', NULL);
INSERT INTO `sys_menu` VALUES (1040, '操作删除', 150, 2, '#', '', '', 'F', '0', 'operlog:remove', '#', 0, 0, 1, '2022-12-19 10:06:02', '', '2023-06-12 08:48:57', NULL);
INSERT INTO `sys_menu` VALUES (1041, '日志导出', 150, 3, '#', '', '', 'F', '0', 'operlog:export', '#', 0, 0, 1, '2022-12-19 10:06:02', '', NULL, NULL);
INSERT INTO `sys_menu` VALUES (1042, '登录查询', 151, 1, '#', '', '', 'F', '0', 'loginlog:query', '#', 0, 0, 1, '2022-12-19 10:06:02', '', '2023-08-26 16:32:55', NULL);
INSERT INTO `sys_menu` VALUES (1043, '登录删除', 151, 2, '#', '', '', 'F', '0', 'loginlog:remove', '#', 0, 0, 1, '2022-12-19 10:06:02', '', '2023-08-26 16:32:58', NULL);
INSERT INTO `sys_menu` VALUES (1044, '日志导出', 151, 3, '#', '', '', 'F', '0', 'loginlog:export', '#', 0, 0, 1, '2022-12-19 10:06:02', '', '2023-08-26 16:33:01', NULL);
INSERT INTO `sys_menu` VALUES (1045, '账户解锁', 151, 4, '#', '', '', 'F', '0', 'loginlog:unlock', '#', 0, 0, 1, '2022-12-19 10:06:02', '', '2023-08-26 16:33:04', NULL);
INSERT INTO `sys_menu` VALUES (1049, '任务查询', 105, 1, '#', '', '', 'F', '0', 'job:query', '#', 0, 0, 1, '2022-12-19 10:06:02', '', '2023-06-06 10:56:13', NULL);
INSERT INTO `sys_menu` VALUES (1050, '任务新增', 105, 2, '#', '', '', 'F', '0', 'job:add', '#', 0, 0, 1, '2022-12-19 10:06:02', '', '2023-06-06 10:56:16', NULL);
INSERT INTO `sys_menu` VALUES (1051, '任务修改', 105, 3, '#', '', '', 'F', '0', 'job:edit', '#', 0, 0, 1, '2022-12-19 10:06:03', '', '2023-06-06 10:56:21', NULL);
INSERT INTO `sys_menu` VALUES (1052, '任务删除', 105, 4, '#', '', '', 'F', '0', 'job:remove', '#', 0, 0, 1, '2022-12-19 10:06:03', '', '2023-06-06 10:56:24', NULL);
INSERT INTO `sys_menu` VALUES (1053, '状态修改', 105, 5, '#', '', '', 'F', '0', 'job:changeStatus', '#', 0, 0, 1, '2022-12-19 10:06:03', '', '2023-06-12 08:47:19', NULL);
INSERT INTO `sys_menu` VALUES (1054, '任务导出', 105, 6, '#', '', '', 'F', '0', 'job:export', '#', 0, 0, 1, '2022-12-19 10:06:03', '', '2024-03-18 15:47:39', '1');
INSERT INTO `sys_menu` VALUES (1063, '用户修改', 203, 3, '', '', NULL, 'F', '0', 'user:edit', '#', 0, 0, 0, '2023-01-14 08:50:42', '', '2023-05-18 07:56:39', NULL);
INSERT INTO `sys_menu` VALUES (1070, '用户查询', 203, 0, '', '', NULL, 'F', '0', 'user:query', '#', 0, 0, 1, '2023-01-14 16:56:38', '', '2023-06-12 08:35:31', NULL);
INSERT INTO `sys_menu` VALUES (1071, '用户添加', 203, 1, '', '', NULL, 'F', '0', 'user:add', '#', 0, 0, 1, '2023-01-14 16:57:09', '', '2023-04-18 13:17:33', NULL);
INSERT INTO `sys_menu` VALUES (1076, '用户导出', 203, 1, '', '', NULL, 'F', '0', 'user:export', '#', 0, 0, 1, '2023-02-07 13:03:57', '', '2023-04-18 13:17:39', NULL);
INSERT INTO `sys_menu` VALUES (1077, '用户删除', 203, 8, NULL, NULL, NULL, 'F', '0', 'user:remove', NULL, 0, 0, 1, '2024-04-03 16:24:18', '1', '2024-04-03 16:24:18', '1');
INSERT INTO `sys_menu` VALUES (1081, '交易查询', 500, 0, '', NULL, NULL, 'F', '0', 'order:query', '#', 0, 0, 1, '2023-02-07 13:24:27', '', '2023-06-12 08:48:45', NULL);
INSERT INTO `sys_menu` VALUES (1083, '优惠券查询', 501, 0, '', NULL, NULL, 'F', '0', 'coupon:query', '#', 0, 0, 1, '2023-02-07 13:25:39', '', '2023-05-17 23:42:26', NULL);
INSERT INTO `sys_menu` VALUES (1084, '优惠券编辑', 501, 1, '', NULL, NULL, 'F', '0', 'coupon:edit', '#', 0, 0, 1, '2023-02-07 13:26:09', '', '2023-05-17 23:42:31', NULL);
INSERT INTO `sys_menu` VALUES (1085, '优惠券添加', 501, 1, '', NULL, NULL, 'F', '0', 'coupon:add', '#', 0, 0, 1, '2023-02-07 13:26:22', '', '2023-05-17 23:42:36', NULL);
INSERT INTO `sys_menu` VALUES (1086, '优惠券删除', 501, 2, '', NULL, NULL, 'F', '0', 'coupon:remove', '#', 0, 0, 1, '2023-02-07 13:26:42', '', '2023-05-17 23:44:19', NULL);
INSERT INTO `sys_menu` VALUES (1087, '交易删除', 500, 2, '', NULL, NULL, 'F', '0', 'order:remove', '#', 0, 0, 0, '2023-02-09 10:55:16', '', '2023-06-12 08:48:18', NULL);
INSERT INTO `sys_menu` VALUES (1089, '角色权限', 206, 6, '', NULL, NULL, 'F', '0', 'role:auth', '#', 0, 0, 0, '2023-02-13 13:00:15', '', '2023-04-19 12:53:39', NULL);
INSERT INTO `sys_menu` VALUES (1091, '等级查询', 202, 0, '', NULL, NULL, 'F', '0', 'level:query', '#', 0, 0, 0, '2023-04-25 14:11:35', '', '2023-05-18 00:06:39', NULL);
INSERT INTO `sys_menu` VALUES (1092, '等级修改', 202, 1, '', NULL, NULL, 'F', '0', 'level:edit', '#', 0, 0, 0, '2023-04-25 14:12:01', '', '2023-05-18 00:06:44', NULL);
INSERT INTO `sys_menu` VALUES (1093, '等级添加', 202, 2, '', NULL, NULL, 'F', '0', 'level:add', '#', 0, 0, 0, '2023-04-25 14:12:14', '', '2023-05-18 00:06:48', NULL);
INSERT INTO `sys_menu` VALUES (1094, '等级删除', 202, 3, '', NULL, NULL, 'F', '0', 'level:remove', '#', 0, 0, 0, '2023-04-25 14:12:51', '', '2023-05-18 00:06:53', NULL);
INSERT INTO `sys_menu` VALUES (1100, '站点查询', 400, 0, NULL, NULL, NULL, 'F', '0', 'station:query', '#', 0, 0, 1, '2023-06-12 16:21:14', '', '2023-06-12 16:22:41', NULL);
INSERT INTO `sys_menu` VALUES (1101, '站点添加', 400, 1, NULL, NULL, NULL, 'F', '0', 'station:add', '#', 0, 0, 1, '2023-06-12 16:21:40', '', '2023-06-12 16:22:43', NULL);
INSERT INTO `sys_menu` VALUES (1102, '站点修改', 400, 2, NULL, NULL, NULL, 'F', '0', 'station:edit', '#', 0, 0, 1, '2023-06-12 16:21:56', '', '2023-06-12 16:22:45', NULL);
INSERT INTO `sys_menu` VALUES (1103, '站点删除', 400, 3, NULL, NULL, NULL, 'F', '0', 'station:remove', '#', 0, 0, 1, '2023-06-12 16:22:16', '', '2023-06-12 16:22:48', NULL);
INSERT INTO `sys_menu` VALUES (1104, '站点导出', 400, 5, NULL, NULL, NULL, 'F', '0', 'station:export', '#', 0, 0, 1, '2023-06-12 16:22:29', '', '2023-06-12 16:22:51', NULL);
INSERT INTO `sys_menu` VALUES (1105, '设备查询', 401, 0, NULL, NULL, NULL, 'F', '0', 'device:query', '#', 0, 0, 1, '2023-06-12 16:23:26', '', '2023-08-25 08:21:11', NULL);
INSERT INTO `sys_menu` VALUES (1106, '设备添加', 401, 1, NULL, NULL, NULL, 'F', '0', 'device:add', '#', 0, 0, 1, '2023-06-12 16:23:41', '', '2023-08-25 08:21:14', NULL);
INSERT INTO `sys_menu` VALUES (1107, '设备修改', 401, 2, NULL, NULL, NULL, 'F', '0', 'device:edit', '#', 0, 0, 1, '2023-06-12 16:23:53', '', '2023-08-25 08:21:17', NULL);
INSERT INTO `sys_menu` VALUES (1108, '设备删除', 401, 3, NULL, NULL, NULL, 'F', '0', 'device:remove', '#', 0, 0, 1, '2023-06-12 16:24:07', '', '2023-08-25 08:21:20', NULL);
INSERT INTO `sys_menu` VALUES (1109, '设备导出', 401, 4, NULL, NULL, NULL, 'F', '0', 'device:export', '#', 0, 0, 1, '2023-06-12 16:24:19', '', '2023-08-25 08:21:22', NULL);
INSERT INTO `sys_menu` VALUES (1110, '租户详情', 600, 9, NULL, NULL, NULL, 'F', '0', 'tenant:query', NULL, 0, 0, 1, '2024-03-29 10:07:46', '1', '2024-03-29 10:11:39', '1');
INSERT INTO `sys_menu` VALUES (1111, '租户新增', 600, 4, NULL, NULL, NULL, 'F', '0', 'tenant:add', NULL, 0, 0, 1, '2024-03-29 10:08:00', '1', '2024-03-29 10:33:44', '1');
INSERT INTO `sys_menu` VALUES (1112, '租户修改', 600, 2, NULL, NULL, NULL, 'F', '0', 'tenant:edit', NULL, 0, 0, 1, '2024-03-29 10:08:18', '1', '2024-03-29 10:33:50', '1');
INSERT INTO `sys_menu` VALUES (1113, '租户删除', 600, 2, NULL, NULL, NULL, 'F', '0', 'tenant:remove', NULL, 0, 0, 1, '2024-03-29 10:08:33', '1', '2024-03-29 10:33:46', '1');
INSERT INTO `sys_menu` VALUES (1114, '套餐详情', 601, 9, NULL, NULL, NULL, 'F', '0', 'tenantPackage:query', NULL, 0, 0, 1, '2024-03-29 10:09:39', '1', '2024-03-29 10:34:45', '1');
INSERT INTO `sys_menu` VALUES (1115, '套餐新增', 601, 5, NULL, NULL, NULL, 'F', '0', 'tenantPackage:add', NULL, 0, 0, 1, '2024-03-29 10:09:52', '1', '2024-03-29 10:34:48', '1');
INSERT INTO `sys_menu` VALUES (1116, '套餐修改', 601, 4, NULL, NULL, NULL, 'F', '0', 'tenantPackage:edit', NULL, 0, 0, 1, '2024-03-29 10:10:04', '1', '2024-03-29 10:34:51', '1');
INSERT INTO `sys_menu` VALUES (1117, '套餐删除', 601, 2, NULL, NULL, NULL, 'F', '0', 'tenantPackage:remove', NULL, 0, 0, 1, '2024-03-29 10:10:20', '1', '2024-03-29 10:34:54', '1');

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `post_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '岗位编码',
  `post_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位名称',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `post_sort` int NOT NULL COMMENT '显示顺序',
  `create_at` datetime NOT NULL COMMENT '创建时间',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建者',
  `update_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '岗位信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_post
-- ----------------------------
INSERT INTO `sys_post` VALUES (1, 0, 'CEO', '执行总裁', '0', 0, '2023-05-17 00:50:56', '', '2023-06-12 13:48:57', NULL);
INSERT INTO `sys_post` VALUES (4, 0, 'CTO', '首席技术官', '0', 4, '2022-12-19 10:06:00', '', '2024-04-06 13:49:27', '3');
INSERT INTO `sys_post` VALUES (1776484907454525442, 1774671331416821762, NULL, 'CEO', '0', 0, '2024-04-06 13:39:42', '1774671331412627456', '2024-04-06 13:39:42', '1774671331412627456');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `data_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '5' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `role_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色权限字符串',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '状态（0正常，1停用）',
  `role_sort` int NOT NULL COMMENT '显示顺序',
  `is_menu_display` tinyint(1) NULL DEFAULT NULL COMMENT '菜单选项是否关联显示',
  `is_dept_display` tinyint(1) NULL DEFAULT NULL COMMENT '部门选项是否关联显示',
  `create_at` datetime NOT NULL COMMENT '创建时间',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建者',
  `update_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`role_id`) USING BTREE,
  UNIQUE INDEX `uk_role_key`(`role_key` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (2, 0, '1', '开发者', 'DEVOP', '0', 99, 1, 1, '2022-12-19 10:06:00', '', '2024-04-03 16:26:52', '1');
INSERT INTO `sys_role` VALUES (3, 0, '4', '观察者', 'OBSERVER', '0', 97, 1, 1, '2023-02-09 11:04:55', '', '2024-03-30 13:30:13', '1');
INSERT INTO `sys_role` VALUES (4, 0, '4', '员工', 'EMPLOYEE', '0', 5, 1, 1, '2023-02-10 14:58:19', '', '2024-03-30 13:30:15', '1');
INSERT INTO `sys_role` VALUES (1775445330027577345, 1774671331416821762, '5', '默认租户角色', NULL, '0', 0, 1, 1, '2024-04-03 16:48:48', '1774671331412627456', '2024-04-03 16:48:48', '1774671331412627456');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` bigint NOT NULL COMMENT '角色id',
  `dept_id` bigint NOT NULL COMMENT '部门id',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色数据权限关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (2, 1);
INSERT INTO `sys_role_menu` VALUES (2, 2);
INSERT INTO `sys_role_menu` VALUES (2, 3);
INSERT INTO `sys_role_menu` VALUES (2, 4);
INSERT INTO `sys_role_menu` VALUES (2, 5);
INSERT INTO `sys_role_menu` VALUES (2, 100);
INSERT INTO `sys_role_menu` VALUES (2, 101);
INSERT INTO `sys_role_menu` VALUES (2, 102);
INSERT INTO `sys_role_menu` VALUES (2, 103);
INSERT INTO `sys_role_menu` VALUES (2, 104);
INSERT INTO `sys_role_menu` VALUES (2, 105);
INSERT INTO `sys_role_menu` VALUES (2, 150);
INSERT INTO `sys_role_menu` VALUES (2, 151);
INSERT INTO `sys_role_menu` VALUES (2, 201);
INSERT INTO `sys_role_menu` VALUES (2, 202);
INSERT INTO `sys_role_menu` VALUES (2, 203);
INSERT INTO `sys_role_menu` VALUES (2, 205);
INSERT INTO `sys_role_menu` VALUES (2, 206);
INSERT INTO `sys_role_menu` VALUES (2, 207);
INSERT INTO `sys_role_menu` VALUES (2, 208);
INSERT INTO `sys_role_menu` VALUES (2, 301);
INSERT INTO `sys_role_menu` VALUES (2, 302);
INSERT INTO `sys_role_menu` VALUES (2, 303);
INSERT INTO `sys_role_menu` VALUES (2, 304);
INSERT INTO `sys_role_menu` VALUES (2, 400);
INSERT INTO `sys_role_menu` VALUES (2, 401);
INSERT INTO `sys_role_menu` VALUES (2, 500);
INSERT INTO `sys_role_menu` VALUES (2, 501);
INSERT INTO `sys_role_menu` VALUES (2, 502);
INSERT INTO `sys_role_menu` VALUES (2, 600);
INSERT INTO `sys_role_menu` VALUES (2, 601);
INSERT INTO `sys_role_menu` VALUES (2, 1000);
INSERT INTO `sys_role_menu` VALUES (2, 1001);
INSERT INTO `sys_role_menu` VALUES (2, 1002);
INSERT INTO `sys_role_menu` VALUES (2, 1003);
INSERT INTO `sys_role_menu` VALUES (2, 1004);
INSERT INTO `sys_role_menu` VALUES (2, 1006);
INSERT INTO `sys_role_menu` VALUES (2, 1007);
INSERT INTO `sys_role_menu` VALUES (2, 1008);
INSERT INTO `sys_role_menu` VALUES (2, 1009);
INSERT INTO `sys_role_menu` VALUES (2, 1010);
INSERT INTO `sys_role_menu` VALUES (2, 1011);
INSERT INTO `sys_role_menu` VALUES (2, 1012);
INSERT INTO `sys_role_menu` VALUES (2, 1013);
INSERT INTO `sys_role_menu` VALUES (2, 1014);
INSERT INTO `sys_role_menu` VALUES (2, 1015);
INSERT INTO `sys_role_menu` VALUES (2, 1016);
INSERT INTO `sys_role_menu` VALUES (2, 1017);
INSERT INTO `sys_role_menu` VALUES (2, 1018);
INSERT INTO `sys_role_menu` VALUES (2, 1019);
INSERT INTO `sys_role_menu` VALUES (2, 1020);
INSERT INTO `sys_role_menu` VALUES (2, 1021);
INSERT INTO `sys_role_menu` VALUES (2, 1022);
INSERT INTO `sys_role_menu` VALUES (2, 1023);
INSERT INTO `sys_role_menu` VALUES (2, 1024);
INSERT INTO `sys_role_menu` VALUES (2, 1025);
INSERT INTO `sys_role_menu` VALUES (2, 1026);
INSERT INTO `sys_role_menu` VALUES (2, 1027);
INSERT INTO `sys_role_menu` VALUES (2, 1028);
INSERT INTO `sys_role_menu` VALUES (2, 1029);
INSERT INTO `sys_role_menu` VALUES (2, 1030);
INSERT INTO `sys_role_menu` VALUES (2, 1031);
INSERT INTO `sys_role_menu` VALUES (2, 1032);
INSERT INTO `sys_role_menu` VALUES (2, 1033);
INSERT INTO `sys_role_menu` VALUES (2, 1034);
INSERT INTO `sys_role_menu` VALUES (2, 1035);
INSERT INTO `sys_role_menu` VALUES (2, 1036);
INSERT INTO `sys_role_menu` VALUES (2, 1037);
INSERT INTO `sys_role_menu` VALUES (2, 1038);
INSERT INTO `sys_role_menu` VALUES (2, 1039);
INSERT INTO `sys_role_menu` VALUES (2, 1040);
INSERT INTO `sys_role_menu` VALUES (2, 1041);
INSERT INTO `sys_role_menu` VALUES (2, 1042);
INSERT INTO `sys_role_menu` VALUES (2, 1043);
INSERT INTO `sys_role_menu` VALUES (2, 1044);
INSERT INTO `sys_role_menu` VALUES (2, 1045);
INSERT INTO `sys_role_menu` VALUES (2, 1049);
INSERT INTO `sys_role_menu` VALUES (2, 1050);
INSERT INTO `sys_role_menu` VALUES (2, 1051);
INSERT INTO `sys_role_menu` VALUES (2, 1052);
INSERT INTO `sys_role_menu` VALUES (2, 1053);
INSERT INTO `sys_role_menu` VALUES (2, 1054);
INSERT INTO `sys_role_menu` VALUES (2, 1063);
INSERT INTO `sys_role_menu` VALUES (2, 1070);
INSERT INTO `sys_role_menu` VALUES (2, 1071);
INSERT INTO `sys_role_menu` VALUES (2, 1076);
INSERT INTO `sys_role_menu` VALUES (2, 1077);
INSERT INTO `sys_role_menu` VALUES (2, 1081);
INSERT INTO `sys_role_menu` VALUES (2, 1083);
INSERT INTO `sys_role_menu` VALUES (2, 1084);
INSERT INTO `sys_role_menu` VALUES (2, 1085);
INSERT INTO `sys_role_menu` VALUES (2, 1086);
INSERT INTO `sys_role_menu` VALUES (2, 1087);
INSERT INTO `sys_role_menu` VALUES (2, 1089);
INSERT INTO `sys_role_menu` VALUES (2, 1091);
INSERT INTO `sys_role_menu` VALUES (2, 1092);
INSERT INTO `sys_role_menu` VALUES (2, 1093);
INSERT INTO `sys_role_menu` VALUES (2, 1094);
INSERT INTO `sys_role_menu` VALUES (2, 1100);
INSERT INTO `sys_role_menu` VALUES (2, 1101);
INSERT INTO `sys_role_menu` VALUES (2, 1102);
INSERT INTO `sys_role_menu` VALUES (2, 1103);
INSERT INTO `sys_role_menu` VALUES (2, 1104);
INSERT INTO `sys_role_menu` VALUES (2, 1105);
INSERT INTO `sys_role_menu` VALUES (2, 1106);
INSERT INTO `sys_role_menu` VALUES (2, 1107);
INSERT INTO `sys_role_menu` VALUES (2, 1108);
INSERT INTO `sys_role_menu` VALUES (2, 1109);
INSERT INTO `sys_role_menu` VALUES (2, 1110);
INSERT INTO `sys_role_menu` VALUES (2, 1111);
INSERT INTO `sys_role_menu` VALUES (2, 1112);
INSERT INTO `sys_role_menu` VALUES (2, 1113);
INSERT INTO `sys_role_menu` VALUES (2, 1114);
INSERT INTO `sys_role_menu` VALUES (2, 1115);
INSERT INTO `sys_role_menu` VALUES (2, 1116);
INSERT INTO `sys_role_menu` VALUES (2, 1117);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 2);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 4);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 5);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 201);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 202);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 205);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 208);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 400);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 401);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 500);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 501);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 502);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1000);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1001);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1002);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1003);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1004);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1016);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1017);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1018);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1019);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1020);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1021);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1022);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1023);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1024);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1081);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1083);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1084);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1085);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1086);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1087);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1091);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1092);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1093);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1094);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1100);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1101);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1102);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1103);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1104);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1105);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1106);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1107);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1108);
INSERT INTO `sys_role_menu` VALUES (1775445330027577345, 1109);

-- ----------------------------
-- Table structure for sys_tenant
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant`;
CREATE TABLE `sys_tenant`  (
  `tenant_id` bigint NOT NULL COMMENT '租户编号',
  `administrator` bigint NOT NULL COMMENT '管理员ID',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `company_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '企业名称',
  `license_number` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '统一社会信用代码',
  `address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地址',
  `intro` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '企业简介',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '租户状态',
  `domain` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '域名',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `package_id` bigint NOT NULL COMMENT '租户套餐编号',
  `expire_time` datetime NULL DEFAULT NULL COMMENT '过期时间',
  `account_count` int NULL DEFAULT -1 COMMENT '用户数量（-1不限制）',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建者',
  `create_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新者',
  `update_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
  PRIMARY KEY (`tenant_id`) USING BTREE,
  UNIQUE INDEX `uk_administrator`(`administrator` ASC) USING BTREE,
  UNIQUE INDEX `uk_license_number`(`license_number` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_tenant
-- ----------------------------
INSERT INTO `sys_tenant` VALUES (1774671331416821762, 1774671331412627456, NULL, '测试租户', NULL, NULL, NULL, '0', NULL, NULL, 1773625804122202113, NULL, -1, 1, '2024-04-01 13:33:12', 1, '2024-04-01 13:33:12', 0);

-- ----------------------------
-- Table structure for sys_tenant_package
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant_package`;
CREATE TABLE `sys_tenant_package`  (
  `package_id` bigint NOT NULL COMMENT '租户套餐id',
  `package_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '套餐名称',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `menu_ids` json NOT NULL COMMENT '套餐绑定的菜单',
  `is_menu_display` tinyint(1) NULL DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建者',
  `create_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新者',
  `update_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`package_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租户套餐表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_tenant_package
-- ----------------------------
INSERT INTO `sys_tenant_package` VALUES (1773620875265482754, 'c', '0', '[]', 1, NULL, 1, '2024-03-29 15:59:04', 1, '2024-03-29 15:59:04');
INSERT INTO `sys_tenant_package` VALUES (1773625804122202113, '默认套餐', '0', '[2, 203, 1006, 1063, 1076, 1071, 1070, 206, 1089, 1011, 1010, 1009, 1008, 1007, 205, 1019, 1018, 1017, 1016, 208, 1024, 1023, 1022, 1021, 1020, 202, 1094, 1093, 1092, 1091, 201, 1004, 1003, 1002, 1001, 1000, 5, 502, 501, 1086, 1085, 1084, 1083, 500, 1087, 1081, 4, 401, 1109, 1108, 1107, 1106, 1105, 400, 1104, 1103, 1102, 1101, 1100]', 1, NULL, 1, '2024-03-29 16:18:39', 1, '2024-04-03 16:18:14');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint NOT NULL COMMENT '管理员ID',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门ID',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户账号',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `nickname` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `phone_number` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '状态值',
  `gender` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
  `login_ip` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录ip',
  `login_date` datetime NULL DEFAULT NULL COMMENT '登录时间',
  `remark` json NULL COMMENT '用户额外信息',
  `create_at` datetime NOT NULL COMMENT '创建时间',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建者',
  `update_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `ad_username`(`username` ASC, `is_deleted` ASC) USING BTREE,
  UNIQUE INDEX `ad_phoneNumber`(`phone_number` ASC, `is_deleted` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 0, 100, 'admin', 'ry@163.com', 'nick_admin', '15888888888', '0', '0', '/uploadPath/2023/06/10/blob_20230610111344A003.png', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', 0, '0:0:0:0:0:0:0:1', '2024-04-16 14:29:39', NULL, '2022-12-19 10:05:59', '', '2024-04-16 14:29:39', '1');
INSERT INTO `sys_user` VALUES (2, 0, 101, 'ry11', 'ry@qq.com', '若依', '15666666666', '0', '0', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', 0, NULL, NULL, NULL, '2022-12-19 10:05:59', '', '2023-12-15 10:33:40', NULL);
INSERT INTO `sys_user` VALUES (3, 0, 105, 'wzkris', '', 'nick_kris', NULL, '0', '0', NULL, '$2a$10$omhFd0wHbTQeALj2bMkVv.kBTk2.grgWI1gHdeF2TtsHVPO/UwmGm', 0, NULL, NULL, NULL, '2023-02-11 14:46:03', '', '2024-04-06 11:07:28', '1');
INSERT INTO `sys_user` VALUES (1774671331412627456, 1774671331416821762, NULL, 'test', NULL, NULL, NULL, '0', NULL, NULL, '$2a$10$iTXxZxHJ4ieq6yjTrjJjCuWZYWl.gkpIwybRfqQfvZReY6P.Ertcq', 0, '0:0:0:0:0:0:0:1', '2024-04-16 11:11:36', NULL, '2024-04-01 13:33:12', '1', '2024-04-16 11:11:37', '1774671331412627456');
INSERT INTO `sys_user` VALUES (1775426395097997313, 0, 1775382319191453698, '111', NULL, '111', NULL, '0', NULL, NULL, '$2a$10$h8xNUbmvRYmWFqyra0x4Ze.IjbOUbvx4mC3t/ePvK3/1qgd9UhBh2', 1, NULL, NULL, NULL, '2024-04-03 15:33:33', '3', '2024-04-03 16:21:36', '3');

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `user_id` bigint NOT NULL COMMENT '管理员ID',
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户与岗位关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
INSERT INTO `sys_user_post` VALUES (1, 1);
INSERT INTO `sys_user_post` VALUES (3, 4);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint NOT NULL COMMENT '管理员ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户和角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (2, 3);
INSERT INTO `sys_user_role` VALUES (3, 2);
INSERT INTO `sys_user_role` VALUES (1775426395097997313, 4);

SET FOREIGN_KEY_CHECKS = 1;