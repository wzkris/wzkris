/*
 Navicat Premium Data Transfer

 Source Server         : local_mysql8
 Source Server Type    : MySQL
 Source Server Version : 80035
 Source Host           : localhost:3306
 Source Schema         : thingslink_system

 Target Server Type    : MySQL
 Target Server Version : 80035
 File Encoding         : 65001

 Date: 18/03/2024 16:32:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config
-- ----------------------------
CREATE DATABASE IF NOT EXISTS thingslink_system;
USE thingslink_system;
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config`  (
  `config_id` bigint NOT NULL COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数键名',
  `config_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'Y' COMMENT '系统内置（Y是 N否）',
  `create_at` datetime NOT NULL COMMENT '创建时间',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建者',
  `update_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`config_id`) USING BTREE,
  UNIQUE INDEX `uk_config_key`(`config_key` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '参数配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of config
-- ----------------------------
INSERT INTO `config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-yellow', 'Y', '2022-12-19 10:06:08', '', '2023-08-12 10:52:04', NULL);
INSERT INTO `config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', '2022-12-19 10:06:08', '', '2023-05-31 11:00:14', NULL);
INSERT INTO `config` VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-light', 'Y', '2022-12-19 10:06:08', '', '2023-12-01 15:43:24', NULL);
INSERT INTO `config` VALUES (4, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', '2022-12-19 10:06:08', '', NULL, NULL);

-- ----------------------------
-- Table structure for dict_data
-- ----------------------------
DROP TABLE IF EXISTS `dict_data`;
CREATE TABLE `dict_data`  (
  `dict_code` bigint NOT NULL COMMENT '字典编码',
  `dict_sort` int NULL DEFAULT NULL COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态（0正常 1停用）',
  `create_at` datetime NOT NULL COMMENT '创建时间',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建者',
  `update_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`dict_code`) USING BTREE,
  INDEX `idx_dict_type`(`dict_type` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dict_data
-- ----------------------------
INSERT INTO `dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', '2022-12-19 10:06:07', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', '2022-12-19 10:06:07', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', '2022-12-19 10:06:07', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (4, 1, '显示', '1', 'sys_show_hide', '', 'primary', 'Y', '0', '2022-12-19 10:06:07', '', '2023-05-31 11:00:00', NULL);
INSERT INTO `dict_data` VALUES (5, 2, '隐藏', '0', 'sys_show_hide', '', 'danger', 'N', '0', '2022-12-19 10:06:07', '', '2023-05-31 11:00:05', NULL);
INSERT INTO `dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', '2022-12-19 10:06:07', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', '2022-12-19 10:06:07', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', '0', '2022-12-19 10:06:07', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', '0', '2022-12-19 10:06:07', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', '0', '2022-12-19 10:06:07', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', '0', '2022-12-19 10:06:07', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', '2022-12-19 10:06:07', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', '2022-12-19 10:06:07', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', '0', '2022-12-19 10:06:07', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', '0', '2022-12-19 10:06:08', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', '0', '2022-12-19 10:06:08', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', '0', '2022-12-19 10:06:08', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (18, 99, '其他', '0', 'sys_oper_type', '', 'info', 'N', '0', '2022-12-19 10:06:08', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (19, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', '0', '2022-12-19 10:06:08', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (20, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', '0', '2022-12-19 10:06:08', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (21, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', '0', '2022-12-19 10:06:08', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (22, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', '0', '2022-12-19 10:06:08', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (23, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', '0', '2022-12-19 10:06:08', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (24, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', '0', '2022-12-19 10:06:08', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (25, 7, '强退', '7', 'sys_oper_type', '', 'danger', 'N', '0', '2022-12-19 10:06:08', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (27, 9, '清空数据', '9', 'sys_oper_type', '', 'danger', 'N', '0', '2022-12-19 10:06:08', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (28, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', '0', '2022-12-19 10:06:08', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (29, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', '0', '2022-12-19 10:06:08', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (30, 0, '管理员', 'admin', 'pre_dict', NULL, 'default', 'N', '0', '2023-01-13 17:23:51', '', '2023-01-13 17:25:24', NULL);
INSERT INTO `dict_data` VALUES (31, 0, '租户', 'tenant', 'pre_dict', NULL, 'default', 'N', '0', '2023-01-13 17:24:05', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (32, 0, '用户', 'user', 'pre_dict', NULL, 'default', 'N', '0', '2023-01-13 17:24:41', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (33, 0, '在线', 'ONLINE', 'device_status', NULL, 'success', 'N', '0', '2023-02-06 14:53:36', '', '2023-12-01 15:33:59', NULL);
INSERT INTO `dict_data` VALUES (34, 0, '离线', 'OFFLINE', 'device_status', NULL, 'info', 'N', '0', '2023-02-06 14:53:57', '', '2023-12-01 15:34:04', NULL);
INSERT INTO `dict_data` VALUES (35, 1, '故障', 'FAULT', 'device_status', NULL, 'danger', 'N', '0', '2023-02-06 14:54:23', '', '2023-12-01 15:34:21', NULL);
INSERT INTO `dict_data` VALUES (36, 2, '检修', 'FIX', 'device_status', NULL, 'warning', 'N', '0', '2023-02-06 14:54:42', '', '2023-12-01 15:34:26', NULL);
INSERT INTO `dict_data` VALUES (37, 0, '未认证', 'NO', 'pay_certification_status', NULL, 'info', 'N', '0', '2023-02-06 14:59:13', '', '2023-02-06 17:21:27', NULL);
INSERT INTO `dict_data` VALUES (38, 0, '微信支付', 'WX', 'pay_certification_status', NULL, 'success', 'N', '0', '2023-02-06 14:59:28', '', '2023-02-06 17:21:34', NULL);
INSERT INTO `dict_data` VALUES (39, 0, '支付宝', 'ALI', 'pay_certification_status', NULL, 'primary', 'N', '0', '2023-02-06 14:59:41', '', '2023-02-06 17:21:47', NULL);
INSERT INTO `dict_data` VALUES (40, 0, '微信、支付宝认证', 'ALL', 'pay_certification_status', NULL, 'default', 'N', '0', '2023-02-06 15:00:08', '', '2023-02-06 17:22:01', NULL);
INSERT INTO `dict_data` VALUES (41, 0, '全部数据权限', '1', 'data_scope', NULL, 'default', 'N', '0', '2023-02-06 17:17:28', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (42, 0, '自定数据权限', '2', 'data_scope', NULL, 'default', 'N', '0', '2023-02-06 17:17:42', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (43, 0, '本部门数据权限', '3', 'data_scope', NULL, 'default', 'N', '0', '2023-02-06 17:17:56', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (44, 0, '本部门及以下数据权限', '4', 'data_scope', NULL, 'default', 'N', '0', '2023-02-06 17:18:09', '', NULL, NULL);
INSERT INTO `dict_data` VALUES (45, 0, '支付成功', 'SUCCESS', 'charging_status', NULL, 'success', 'N', '0', '2023-02-07 17:12:34', '', '2023-12-01 15:36:01', NULL);
INSERT INTO `dict_data` VALUES (46, 0, '订单关闭', 'CLOSED', 'charging_status', NULL, 'info', 'N', '0', '2023-02-07 17:12:48', '', '2023-12-01 15:36:06', NULL);
INSERT INTO `dict_data` VALUES (47, 0, '充电中', 'CHARGING', 'charging_status', NULL, 'warning', 'N', '0', '2023-02-07 17:13:05', '', '2023-12-01 15:36:41', NULL);
INSERT INTO `dict_data` VALUES (48, 0, '充电结束', 'CHARGE_END', 'charging_status', NULL, 'danger', 'N', '0', '2023-02-07 17:13:18', '', '2023-12-01 15:37:22', NULL);
INSERT INTO `dict_data` VALUES (49, 0, '未支付', 'NOTPAY', 'charging_status', NULL, 'primary', 'N', '0', '2023-02-07 17:13:36', '', '2023-12-01 15:35:56', NULL);
INSERT INTO `dict_data` VALUES (50, 0, '支付异常', 'ERROR', 'charging_status', NULL, 'danger', NULL, '0', '2023-12-01 15:37:45', '', '2023-12-01 15:37:55', NULL);
INSERT INTO `dict_data` VALUES (51, 0, '钱包支付', 'WALLET', 'pay_type', NULL, 'info', 'N', '0', '2023-02-07 17:16:26', '', '2023-12-01 15:40:27', NULL);
INSERT INTO `dict_data` VALUES (52, 0, '微信支付', 'WECHAT', 'pay_type', NULL, 'success', 'N', '0', '2023-02-07 17:16:38', '', '2023-12-01 15:40:33', NULL);
INSERT INTO `dict_data` VALUES (53, 0, '支付宝', 'ZFB', 'pay_type', NULL, 'primary', 'N', '0', '2023-02-07 17:16:48', '', '2023-12-01 15:40:38', NULL);
INSERT INTO `dict_data` VALUES (54, 0, '平台优惠券', '1', 'coupon_type', NULL, 'primary', 'N', '0', '2023-02-08 11:28:28', '', '2023-06-10 09:59:22', NULL);
INSERT INTO `dict_data` VALUES (55, 0, '商户优惠券', '2', 'coupon_type', NULL, 'success', 'N', '0', '2023-02-08 11:28:39', '', '2023-06-10 09:59:28', NULL);
INSERT INTO `dict_data` VALUES (56, 0, '等待审核', '1', 'coupon_status', NULL, 'info', 'N', '0', '2023-02-08 11:29:50', '', '2023-06-10 10:00:21', NULL);
INSERT INTO `dict_data` VALUES (57, 0, '通过审核，可以发放及使用券', '0', 'coupon_status', NULL, 'success', 'N', '0', '2023-02-08 11:29:58', '', '2023-06-10 10:00:25', NULL);
INSERT INTO `dict_data` VALUES (58, 0, '停止发放，券依然可以使用', '2', 'coupon_status', NULL, 'warning', 'N', '0', '2023-02-08 11:30:07', '', '2023-06-10 10:00:29', NULL);
INSERT INTO `dict_data` VALUES (59, 0, '停用，券无法被使用', '3', 'coupon_status', NULL, 'danger', 'N', '0', '2023-02-08 11:30:14', '', '2023-06-10 10:00:32', NULL);
INSERT INTO `dict_data` VALUES (60, 0, '空闲', '0', 'road_status', NULL, 'success', 'N', '0', '2023-02-11 10:46:10', '', '2023-06-06 08:28:12', NULL);
INSERT INTO `dict_data` VALUES (61, 0, '占用', '1', 'road_status', NULL, 'primary', 'N', '0', '2023-02-11 10:46:22', '', '2023-06-06 08:28:51', NULL);

-- ----------------------------
-- Table structure for dict_type
-- ----------------------------
DROP TABLE IF EXISTS `dict_type`;
CREATE TABLE `dict_type`  (
  `dict_id` bigint NOT NULL COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态（0正常 1停用）',
  `create_at` datetime NOT NULL COMMENT '创建时间',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建者',
  `update_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `uk_dict_type`(`dict_type` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dict_type
-- ----------------------------
INSERT INTO `dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', '2022-12-19 10:06:07', '', '2023-05-31 10:59:54', NULL);
INSERT INTO `dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', '2022-12-19 10:06:07', '', NULL, NULL);
INSERT INTO `dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', '2022-12-19 10:06:07', '', NULL, NULL);
INSERT INTO `dict_type` VALUES (4, '任务状态', 'sys_job_status', '0', '2022-12-19 10:06:07', '', NULL, NULL);
INSERT INTO `dict_type` VALUES (5, '任务分组', 'sys_job_group', '0', '2022-12-19 10:06:07', '', NULL, NULL);
INSERT INTO `dict_type` VALUES (6, '系统是否', 'sys_yes_no', '0', '2022-12-19 10:06:07', '', NULL, NULL);
INSERT INTO `dict_type` VALUES (7, '通知类型', 'sys_notice_type', '0', '2022-12-19 10:06:07', '', NULL, NULL);
INSERT INTO `dict_type` VALUES (8, '通知状态', 'sys_notice_status', '0', '2022-12-19 10:06:07', '', NULL, NULL);
INSERT INTO `dict_type` VALUES (9, '操作类型', 'sys_oper_type', '0', '2022-12-19 10:06:07', '', NULL, NULL);
INSERT INTO `dict_type` VALUES (10, '系统状态', 'sys_common_status', '0', '2022-12-19 10:06:07', '', NULL, NULL);
INSERT INTO `dict_type` VALUES (11, '预留字典', 'pre_dict', '0', '2023-01-13 17:23:23', '', '2023-02-13 12:36:49', NULL);
INSERT INTO `dict_type` VALUES (12, '设备状态', 'device_status', '0', '2023-02-06 14:52:33', '', '2023-08-25 08:25:44', NULL);
INSERT INTO `dict_type` VALUES (13, '支付认证状态', 'pay_certification_status', '0', '2023-02-06 14:57:53', '', NULL, NULL);
INSERT INTO `dict_type` VALUES (14, '数据权限', 'data_scope', '0', '2023-02-06 17:17:10', '', '2023-02-09 14:06:23', NULL);
INSERT INTO `dict_type` VALUES (15, '支付状态', 'charging_status', '0', '2023-02-07 17:11:04', '', '2023-12-01 15:39:39', NULL);
INSERT INTO `dict_type` VALUES (16, '支付方式', 'pay_type', '0', '2023-02-07 17:15:58', '', '2023-12-01 15:40:12', NULL);
INSERT INTO `dict_type` VALUES (17, '优惠券类型', 'coupon_type', '0', '2023-02-08 11:28:04', '', NULL, NULL);
INSERT INTO `dict_type` VALUES (18, '优惠券状态', 'coupon_status', '0', '2023-02-08 11:29:31', '', NULL, NULL);
INSERT INTO `dict_type` VALUES (19, '设备通道状态', 'road_status', '0', '2023-02-11 10:45:46', '', '2023-06-10 13:54:04', NULL);

-- ----------------------------
-- Table structure for job
-- ----------------------------
DROP TABLE IF EXISTS `job`;
CREATE TABLE `job`  (
  `job_id` bigint NOT NULL COMMENT '任务ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务组名',
  `invoke_target` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态（0正常 1暂停）',
  `create_at` datetime NOT NULL COMMENT '创建时间',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建者',
  `update_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`job_id`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务调度表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of job
-- ----------------------------
INSERT INTO `job` VALUES (1, '刷新SameToken（每小时）', 'DEFAULT', 'sameTokenTask.refreshToken()', '0 0 * * *  ?', '3', '1', '0', '2022-12-19 10:06:08', '', '2023-06-06 11:11:33', NULL);
INSERT INTO `job` VALUES (2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '0/15 * * * * ?', '3', '1', '1', '2022-12-19 10:06:08', '', NULL, NULL);
INSERT INTO `job` VALUES (3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)', '0/20 * * * * ?', '3', '1', '1', '2022-12-19 10:06:09', '', '2023-06-06 11:11:45', NULL);

-- ----------------------------
-- Table structure for job_log
-- ----------------------------
DROP TABLE IF EXISTS `job_log`;
CREATE TABLE `job_log`  (
  `job_log_id` bigint NOT NULL,
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务组名',
  `invoke_target` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用目标字符串',
  `job_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '日志信息',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行状态（0正常 1失败）',
  `exception_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '异常信息',
  `create_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务调度日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of job_log
-- ----------------------------

-- ----------------------------
-- Table structure for login_log
-- ----------------------------
DROP TABLE IF EXISTS `login_log`;
CREATE TABLE `login_log`  (
  `log_id` bigint NOT NULL,
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '地址',
  `ip_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ip地址',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作系统',
  `login_time` datetime NOT NULL COMMENT '访问时间',
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '后台登录日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of login_log
-- ----------------------------
INSERT INTO `login_log` VALUES (1760128280765878273, 4, '内网IP', '127.0.0.1', 'Unknown', 'Unknown', '2024-02-21 10:24:17');
INSERT INTO `login_log` VALUES (1760129581599903746, 4, '内网IP', '127.0.0.1', 'Unknown', 'Unknown', '2024-02-21 10:29:28');
INSERT INTO `login_log` VALUES (1760129685295681538, 4, '内网IP', '127.0.0.1', 'Unknown', 'Unknown', '2024-02-21 10:29:52');
INSERT INTO `login_log` VALUES (1760130094320013313, 4, '内网IP', '127.0.0.1', 'Unknown', 'Unknown', '2024-02-21 10:31:30');
INSERT INTO `login_log` VALUES (1760130833461235714, 4, '内网IP', '127.0.0.1', 'Unknown', 'Unknown', '2024-02-21 10:34:27');
INSERT INTO `login_log` VALUES (1760134486632910850, 4, '内网IP', '127.0.0.1', 'Unknown', 'Unknown', '2024-02-21 10:48:57');
INSERT INTO `login_log` VALUES (1760134944533467137, 4, '内网IP', '127.0.0.1', 'Unknown', 'Unknown', '2024-02-21 10:50:47');
INSERT INTO `login_log` VALUES (1760135259412451329, 4, '内网IP', '127.0.0.1', 'Unknown', 'Unknown', '2024-02-21 10:52:01');
INSERT INTO `login_log` VALUES (1760139754947170306, 4, '内网IP', '127.0.0.1', 'Unknown', 'Unknown', '2024-02-21 11:09:54');
INSERT INTO `login_log` VALUES (1760141122030227457, 4, '内网IP', '127.0.0.1', 'Unknown', 'Unknown', '2024-02-21 11:15:19');
INSERT INTO `login_log` VALUES (1760142526245457921, 4, '内网IP', '127.0.0.1', 'Unknown', 'Unknown', '2024-02-21 11:20:54');
INSERT INTO `login_log` VALUES (1760186373159759873, 4, '内网IP', '127.0.0.1', 'Unknown', 'Unknown', '2024-02-21 14:14:50');
INSERT INTO `login_log` VALUES (1760186941437620226, 4, '内网IP', '127.0.0.1', 'Unknown', 'Unknown', '2024-02-21 14:17:23');
INSERT INTO `login_log` VALUES (1760187056252497921, 4, '内网IP', '127.0.0.1', 'Unknown', 'Unknown', '2024-02-21 14:17:51');
INSERT INTO `login_log` VALUES (1760187563545178113, 4, '内网IP', '127.0.0.1', 'Unknown', 'Unknown', '2024-02-21 14:19:50');
INSERT INTO `login_log` VALUES (1760190608987369474, 4, '内网IP', '127.0.0.1', 'Unknown', 'Unknown', '2024-02-21 14:31:58');
INSERT INTO `login_log` VALUES (1760198148190064642, 4, '内网IP', '127.0.0.1', 'Unknown', 'Unknown', '2024-02-21 15:01:55');
INSERT INTO `login_log` VALUES (1760570220070219778, 1, '内网IP', '0:0:0:0:0:0:0:1', 'Chrome 12', 'Windows 10', '2024-02-22 15:40:24');
INSERT INTO `login_log` VALUES (1760570252152451074, 1, '内网IP', '0:0:0:0:0:0:0:1', 'Chrome 12', 'Windows 10', '2024-02-22 15:40:32');
INSERT INTO `login_log` VALUES (1760570261900013569, 1, '内网IP', '0:0:0:0:0:0:0:1', 'Chrome 12', 'Windows 10', '2024-02-22 15:40:34');
INSERT INTO `login_log` VALUES (1760570265481949186, 1, '内网IP', '0:0:0:0:0:0:0:1', 'Chrome 12', 'Windows 10', '2024-02-22 15:40:35');
INSERT INTO `login_log` VALUES (1760570603475742721, 1, '内网IP', '0:0:0:0:0:0:0:1', 'Chrome 12', 'Windows 10', '2024-02-22 15:41:56');
INSERT INTO `login_log` VALUES (1760571702635048961, 1, '内网IP', '0:0:0:0:0:0:0:1', 'Chrome 12', 'Windows 10', '2024-02-22 15:46:18');
INSERT INTO `login_log` VALUES (1760571979744325633, 1, '内网IP', '0:0:0:0:0:0:0:1', 'Chrome 12', 'Windows 10', '2024-02-22 15:47:24');
INSERT INTO `login_log` VALUES (1760575307559645186, 1, '内网IP', '127.0.0.1', 'Unknown', 'Unknown', '2024-02-22 16:00:37');
INSERT INTO `login_log` VALUES (1760575531241877506, 1, '内网IP', '127.0.0.1', 'Unknown', 'Unknown', '2024-02-22 16:01:31');
INSERT INTO `login_log` VALUES (1760576283171532802, 1, '内网IP', '127.0.0.1', 'Unknown', 'Unknown', '2024-02-22 16:04:30');
INSERT INTO `login_log` VALUES (1760576552529735681, 4, '内网IP', '127.0.0.1', 'Unknown', 'Unknown', '2024-02-22 16:05:34');
INSERT INTO `login_log` VALUES (1760818602911174658, 4, '内网IP', '127.0.0.1', 'Unknown', 'Unknown', '2024-02-23 08:07:23');
INSERT INTO `login_log` VALUES (1760818787720597505, 4, '内网IP', '127.0.0.1', 'Unknown', 'Unknown', '2024-02-23 08:08:08');
INSERT INTO `login_log` VALUES (1760821168441102338, 4, '内网IP', '127.0.0.1', 'Unknown', 'Unknown', '2024-02-23 08:17:35');
INSERT INTO `login_log` VALUES (1760821699439988738, 4, '内网IP', '127.0.0.1', 'Unknown', 'Unknown', '2024-02-23 08:19:42');
INSERT INTO `login_log` VALUES (1760822746384404482, 4, '内网IP', '127.0.0.1', 'Unknown', 'Unknown', '2024-02-23 08:23:51');
INSERT INTO `login_log` VALUES (1760823221695516674, 4, '内网IP', '127.0.0.1', 'Unknown', 'Unknown', '2024-02-23 08:25:44');
INSERT INTO `login_log` VALUES (1760828363090079746, 1, '内网IP', '127.0.0.1', 'Chrome 12', 'Windows 10', '2024-02-23 08:46:10');

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
  `notice_id` bigint NOT NULL COMMENT '公告ID',
  `notice_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告标题',
  `notice_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '公告内容',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公告状态（0正常 1关闭）',
  `message_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息id',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '创建者',
  `create_at` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  `update_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`notice_id`) USING BTREE,
  UNIQUE INDEX `uk_message_id`(`message_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通知公告表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice` VALUES (1, '温馨提醒：2018-07-01 若依新版本发布啦', '2', '<p>&lt;</p>', '0', '1', 'admin', '2022-12-19 10:06:09', 'admin', '2023-06-08 13:45:37');
INSERT INTO `notice` VALUES (2, '维护通知：2018-07-01 若依系统凌晨维护', '1', '<p>&lt;</p>', '0', '2', 'admin', '2022-12-19 10:06:09', 'admin', '2023-06-12 13:43:32');

-- ----------------------------
-- Table structure for oper_log
-- ----------------------------
DROP TABLE IF EXISTS `oper_log`;
CREATE TABLE `oper_log`  (
  `oper_id` bigint NOT NULL COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模块标题',
  `business_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务类型（0其他 1新增 2修改 3删除）',
  `method` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方式',
  `operator_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作人员',
  `oper_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作地点',
  `oper_param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求参数',
  `json_result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '返回参数',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作状态（0正常 1异常）',
  `error_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '错误消息',
  `oper_time` datetime NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`oper_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '操作日志记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of oper_log
-- ----------------------------
INSERT INTO `oper_log` VALUES (402, '操作日志', '9', 'com.thingslink.system.controller.OperlogController.clean()', 'DELETE', '1', 'admin', '/operlog/clean', '39.108.81.218', NULL, '{ }', '{\r\n  \"code\" : 0,\r\n  \"msg\" : \"SUCCESS\",\r\n  \"current\" : 1686558492202\r\n}', '0', NULL, '2023-06-12 16:28:12');
INSERT INTO `oper_log` VALUES (403, '登录日志', '3', 'com.thingslink.system.controller.LoginLogController.clean()', 'DELETE', '1', 'admin', '/loginLog/clean', '39.108.81.218', NULL, '{ }', '{\r\n  \"code\" : 0,\r\n  \"msg\" : \"SUCCESS\",\r\n  \"current\" : 1686558494817\r\n}', '0', NULL, '2023-06-12 16:28:15');
INSERT INTO `oper_log` VALUES (404, '岗位管理', '1', 'com.thingslink.system.controller.PostController.add()', 'POST', '1', 'admin', '/post', '39.108.81.218', NULL, '{\r\n  \"postCode\" : \"123\",\r\n  \"postName\" : \"13\",\r\n  \"postSort\" : 0,\r\n  \"status\" : \"0\",\r\n  \"flag\" : false\r\n}', NULL, '1', 'nested exception is org.apache.ibatis.exceptions.PersistenceException: \r\n### Error updating database.  Cause: org.apache.dubbo.rpc.RpcException: Failed to invoke the method getSegmentId in the service com.thingslink.idgen.api.RemoteIdGenApi. No provider available for the service com.thingslink.idgen.api.RemoteIdGenApi from registry 127.0.0.1:8848 on the consumer 192.168.0.107 using the dubbo version 3.2.2. Please check if the providers have been started and registered.\r\n### Cause: org.apache.dubbo.rpc.RpcException: Failed to invoke the method getSegmentId in the service com.thingslink.idgen.api.RemoteIdGenApi. No provider available for the service com.thingslink.idgen.api.RemoteIdGenApi from registry 127.0.0.1:8848 on the consumer 192.168.0.107 using the dubbo version 3.2.2. Please check if the providers have been started and registered.', '2023-06-16 00:06:18');
INSERT INTO `oper_log` VALUES (405, '岗位管理', '1', 'com.thingslink.system.controller.PostController.add()', 'POST', '1', 'admin', '/post', '39.108.81.218', NULL, '{\r\n  \"postId\" : 1200,\r\n  \"postCode\" : \"123\",\r\n  \"postName\" : \"13\",\r\n  \"postSort\" : 0,\r\n  \"status\" : \"0\",\r\n  \"flag\" : false\r\n}', '{\r\n  \"code\" : 0,\r\n  \"msg\" : \"SUCCESS\",\r\n  \"current\" : 1686874004685\r\n}', '0', NULL, '2023-06-16 00:06:44');
INSERT INTO `oper_log` VALUES (1695318738326020096, 'thingslink-auth/密码登录', '0', 'com.thingslink.auth.controller.BackLoginController.login()', 'POST', '1', NULL, '/back-login', '127.0.0.1', NULL, '{\r\n  \"userName\" : \"admin\",\r\n  \"password\" : \"admin123\",\r\n  \"uuid\" : \"85\",\r\n  \"code\" : \"48\"\r\n}', NULL, '0', NULL, NULL);
INSERT INTO `oper_log` VALUES (1695318882844958720, 'thingslink-auth/密码登录', '0', 'com.thingslink.auth.controller.BackLoginController.login()', 'POST', '1', NULL, '/back-login', '127.0.0.1', NULL, '{\r\n  \"userName\" : \"admin1\",\r\n  \"password\" : \"admin123\",\r\n  \"uuid\" : \"85\",\r\n  \"code\" : \"48\"\r\n}', NULL, '1', '登录失败，用户不存在/密码错误', NULL);
INSERT INTO `oper_log` VALUES (1695319788755902464, 'thingslink-auth/密码登录', '0', 'com.thingslink.auth.controller.BackLoginController.login()', 'POST', '1', NULL, '/back-login', '127.0.0.1', NULL, '{\r\n  \"userName\" : \"admin1\",\r\n  \"password\" : \"admin123\",\r\n  \"uuid\" : \"85\",\r\n  \"code\" : \"48\"\r\n}', NULL, '1', '登录失败，用户不存在/密码错误', NULL);
INSERT INTO `oper_log` VALUES (1695320777860882432, '密码登录', '0', 'com.thingslink.auth.controller.BackLoginController.login()', 'POST', '1', NULL, '/back-login', '127.0.0.1', NULL, '{\r\n  \"userName\" : \"admin\",\r\n  \"password\" : \"admin123\"\r\n}', NULL, '0', NULL, NULL);
INSERT INTO `oper_log` VALUES (1695321070350671872, '密码登录', '0', 'com.thingslink.auth.controller.BackLoginController.login()', 'POST', '1', NULL, '/back-login', '127.0.0.1', NULL, '{\r\n  \"userName\" : \"wzkris\",\r\n  \"password\" : \"admin123\"\r\n}', NULL, '0', NULL, NULL);
INSERT INTO `oper_log` VALUES (1695321201561083904, '角色管理', '2', 'com.thingslink.system.controller.RoleController.edit()', 'PUT', '1', 'wzkris', '/role', '127.0.0.1', NULL, NULL, NULL, '0', NULL, NULL);
INSERT INTO `oper_log` VALUES (1695321893382176768, '密码登录', '0', 'com.thingslink.auth.controller.BackLoginController.login()', 'POST', '1', NULL, '/back-login', '127.0.0.1', NULL, '{\r\n  \"userName\" : \"admin\",\r\n  \"password\" : \"admin123\"\r\n}', NULL, '0', NULL, NULL);
INSERT INTO `oper_log` VALUES (1695347137249562624, '菜单管理', '2', 'com.thingslink.auth.controller.MenuController.edit()', 'PUT', '1', 'admin', '/menu', '127.0.0.1', NULL, NULL, NULL, '0', NULL, NULL);
INSERT INTO `oper_log` VALUES (1695347163472351232, '菜单管理', '2', 'com.thingslink.auth.controller.MenuController.edit()', 'PUT', '1', 'admin', '/menu', '127.0.0.1', NULL, NULL, NULL, '0', NULL, NULL);
INSERT INTO `oper_log` VALUES (1695347183693090816, '菜单管理', '2', 'com.thingslink.auth.controller.MenuController.edit()', 'PUT', '1', 'admin', '/menu', '127.0.0.1', NULL, NULL, NULL, '0', NULL, NULL);
INSERT INTO `oper_log` VALUES (1695347204597501952, '菜单管理', '2', 'com.thingslink.auth.controller.MenuController.edit()', 'PUT', '1', 'admin', '/menu', '127.0.0.1', NULL, NULL, NULL, '0', NULL, NULL);
INSERT INTO `oper_log` VALUES (1695347216714846208, '菜单管理', '2', 'com.thingslink.auth.controller.MenuController.edit()', 'PUT', '1', 'admin', '/menu', '127.0.0.1', NULL, NULL, NULL, '0', NULL, NULL);
INSERT INTO `oper_log` VALUES (1695347229893349376, '菜单管理', '2', 'com.thingslink.auth.controller.MenuController.edit()', 'PUT', '1', 'admin', '/menu', '127.0.0.1', NULL, NULL, NULL, '0', NULL, NULL);
INSERT INTO `oper_log` VALUES (1695347255658958848, '菜单管理', '2', 'com.thingslink.auth.controller.MenuController.edit()', 'PUT', '1', 'admin', '/menu', '127.0.0.1', NULL, NULL, NULL, '0', NULL, NULL);
INSERT INTO `oper_log` VALUES (1695347770111315968, '后台管理', '2', 'com.thingslink.auth.controller.UserController.edit()', 'PUT', '1', 'wzkris', '/user', '127.0.0.1', NULL, NULL, NULL, '0', NULL, NULL);
INSERT INTO `oper_log` VALUES (1695353166809026560, '菜单管理', '2', 'com.thingslink.auth.controller.MenuController.edit()', 'PUT', '1', 'admin', '/menu', '127.0.0.1', NULL, NULL, NULL, '0', NULL, NULL);
INSERT INTO `oper_log` VALUES (1695353207611215872, '菜单管理', '2', 'com.thingslink.auth.controller.MenuController.edit()', 'PUT', '1', 'admin', '/menu', '127.0.0.1', NULL, NULL, NULL, '0', NULL, NULL);
INSERT INTO `oper_log` VALUES (1695353218675789824, '菜单管理', '2', 'com.thingslink.auth.controller.MenuController.edit()', 'PUT', '1', 'admin', '/menu', '127.0.0.1', NULL, NULL, NULL, '0', NULL, NULL);
INSERT INTO `oper_log` VALUES (1695353230587613184, '菜单管理', '2', 'com.thingslink.auth.controller.MenuController.edit()', 'PUT', '1', 'admin', '/menu', '127.0.0.1', NULL, NULL, NULL, '0', NULL, NULL);
INSERT INTO `oper_log` VALUES (1695353240649748480, '菜单管理', '2', 'com.thingslink.auth.controller.MenuController.edit()', 'PUT', '1', 'admin', '/menu', '127.0.0.1', NULL, NULL, NULL, '0', NULL, NULL);
INSERT INTO `oper_log` VALUES (1695353593218748416, '菜单管理', '2', 'com.thingslink.auth.controller.MenuController.edit()', 'PUT', '1', 'admin', '/menu', '127.0.0.1', NULL, NULL, NULL, '0', NULL, NULL);
INSERT INTO `oper_log` VALUES (1695353618975969280, '菜单管理', '2', 'com.thingslink.auth.controller.MenuController.edit()', 'PUT', '1', 'admin', '/menu', '127.0.0.1', NULL, NULL, NULL, '0', NULL, NULL);
INSERT INTO `oper_log` VALUES (1695353631483383808, '菜单管理', '2', 'com.thingslink.auth.controller.MenuController.edit()', 'PUT', '1', 'admin', '/menu', '127.0.0.1', NULL, NULL, NULL, '0', NULL, NULL);
INSERT INTO `oper_log` VALUES (1695353643005136896, '菜单管理', '2', 'com.thingslink.auth.controller.MenuController.edit()', 'PUT', '1', 'admin', '/menu', '127.0.0.1', NULL, NULL, NULL, '0', NULL, NULL);
INSERT INTO `oper_log` VALUES (1695353654644330496, '菜单管理', '2', 'com.thingslink.auth.controller.MenuController.edit()', 'PUT', '1', 'admin', '/menu', '127.0.0.1', NULL, NULL, NULL, '0', NULL, NULL);
INSERT INTO `oper_log` VALUES (1744989139044659202, '字典类型', NULL, 'com.thingslink.system.controller.DictDataController.remove()', 'DELETE', NULL, 'admin', '/dict/data/26', '127.0.0.1', NULL, NULL, NULL, '0', NULL, NULL);
INSERT INTO `oper_log` VALUES (1749690387652116482, '主体管理', NULL, 'com.thingslink.auth.controller.SubjectController.edit()', 'PUT', NULL, 'admin', '/subject', '127.0.0.1', NULL, NULL, NULL, '0', NULL, NULL);

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `blob_data` longblob NULL COMMENT '存放持久化Trigger对象',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Blob类型的触发器表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_blob_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `calendar_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '日历名称',
  `calendar` longblob NOT NULL COMMENT '存放持久化calendar对象',
  PRIMARY KEY (`sched_name`, `calendar_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '日历信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_calendars
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `cron_expression` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'cron表达式',
  `time_zone_id` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '时区',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Cron类型的触发器表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_cron_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `entry_id` varchar(95) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度器实例id',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `instance_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度器实例名',
  `fired_time` bigint NOT NULL COMMENT '触发的时间',
  `sched_time` bigint NOT NULL COMMENT '定时器制定的时间',
  `priority` int NOT NULL COMMENT '优先级',
  `state` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态',
  `job_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务名称',
  `job_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务组名',
  `is_nonconcurrent` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否并发',
  `requests_recovery` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否接受恢复执行',
  PRIMARY KEY (`sched_name`, `entry_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '已触发的触发器表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_fired_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `job_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务组名',
  `description` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '相关介绍',
  `job_class_name` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '执行任务类名称',
  `is_durable` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否持久化',
  `is_nonconcurrent` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否并发',
  `is_update_data` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否更新数据',
  `requests_recovery` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否接受恢复执行',
  `job_data` longblob NULL COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '任务详细信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `lock_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '悲观锁名称',
  PRIMARY KEY (`sched_name`, `lock_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '存储的悲观锁信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  PRIMARY KEY (`sched_name`, `trigger_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '暂停的触发器表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_paused_trigger_grps
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `instance_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '实例名称',
  `last_checkin_time` bigint NOT NULL COMMENT '上次检查时间',
  `checkin_interval` bigint NOT NULL COMMENT '检查间隔时间',
  PRIMARY KEY (`sched_name`, `instance_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '调度器状态表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `repeat_count` bigint NOT NULL COMMENT '重复的次数统计',
  `repeat_interval` bigint NOT NULL COMMENT '重复的间隔时间',
  `times_triggered` bigint NOT NULL COMMENT '已经触发的次数',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '简单触发器的信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_simple_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `str_prop_1` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'String类型的trigger的第一个参数',
  `str_prop_2` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'String类型的trigger的第二个参数',
  `str_prop_3` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'String类型的trigger的第三个参数',
  `int_prop_1` int NULL DEFAULT NULL COMMENT 'int类型的trigger的第一个参数',
  `int_prop_2` int NULL DEFAULT NULL COMMENT 'int类型的trigger的第二个参数',
  `long_prop_1` bigint NULL DEFAULT NULL COMMENT 'long类型的trigger的第一个参数',
  `long_prop_2` bigint NULL DEFAULT NULL COMMENT 'long类型的trigger的第二个参数',
  `dec_prop_1` decimal(13, 4) NULL DEFAULT NULL COMMENT 'decimal类型的trigger的第一个参数',
  `dec_prop_2` decimal(13, 4) NULL DEFAULT NULL COMMENT 'decimal类型的trigger的第二个参数',
  `bool_prop_1` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Boolean类型的trigger的第一个参数',
  `bool_prop_2` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Boolean类型的trigger的第二个参数',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '同步机制的行锁表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_simprop_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers`  (
  `sched_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '触发器的名字',
  `trigger_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '触发器所属组的名字',
  `job_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_job_details表job_name的外键',
  `job_group` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'qrtz_job_details表job_group的外键',
  `description` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '相关介绍',
  `next_fire_time` bigint NULL DEFAULT NULL COMMENT '上一次触发时间（毫秒）',
  `prev_fire_time` bigint NULL DEFAULT NULL COMMENT '下一次触发时间（默认为-1表示不触发）',
  `priority` int NULL DEFAULT NULL COMMENT '优先级',
  `trigger_state` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '触发器状态',
  `trigger_type` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '触发器的类型',
  `start_time` bigint NOT NULL COMMENT '开始时间',
  `end_time` bigint NULL DEFAULT NULL COMMENT '结束时间',
  `calendar_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '日程表名称',
  `misfire_instr` smallint NULL DEFAULT NULL COMMENT '补偿执行的策略',
  `job_data` longblob NULL COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`, `trigger_name`, `trigger_group`) USING BTREE,
  INDEX `sched_name`(`sched_name` ASC, `job_name` ASC, `job_group` ASC) USING BTREE,
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `qrtz_job_details` (`sched_name`, `job_name`, `job_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '触发器详细信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
