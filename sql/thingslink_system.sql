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

 Date: 03/07/2024 10:13:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS thingslink_system;
USE thingslink_system;
-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `config_id` bigint NOT NULL COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数键名',
  `config_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'Y' COMMENT '系统内置（Y是 N否）',
  `create_at` bigint NOT NULL COMMENT '创建时间',
  `create_id` bigint NOT NULL COMMENT '创建者',
  `update_at` bigint NULL DEFAULT NULL COMMENT '更新时间',
  `update_id` bigint NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`config_id`) USING BTREE,
  UNIQUE INDEX `uk_config_key`(`config_key` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '参数配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-yellow', 'Y', 1713334134616, 1, 1719036432755, 1);
INSERT INTO `sys_config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 1713334134616, 1, 1719036436632, 1);
INSERT INTO `sys_config` VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-light', 'Y', 1713334134616, 1, 1719036434181, 1);
INSERT INTO `sys_config` VALUES (4, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 1713334134616, 1, 1719036435430, 1);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `data_id` bigint NOT NULL COMMENT '字典编码',
  `dict_sort` int NULL DEFAULT NULL COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否默认（Y是 N否）',
  `create_at` bigint NOT NULL COMMENT '创建时间',
  `create_id` bigint NOT NULL COMMENT '创建者',
  `update_at` bigint NULL DEFAULT NULL COMMENT '更新时间',
  `update_id` bigint NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`data_id`) USING BTREE,
  INDEX `idx_dict_type`(`dict_type` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', 1713334134616, 1, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '1', 'sys_show_hide', '', 'primary', 'Y', 1713334134616, 1, 20230531110000, NULL);
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '0', 'sys_show_hide', '', 'danger', 'N', 1713334134616, 1, 20230531110005, NULL);
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', 1713334134616, 1, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', 1713334134616, 1, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', 1713334134616, 1, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', 1713334134616, 1, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (14, 1, '通知', '1', 'sys_notice_type', '', 'warning', 'Y', 1713334134616, 1, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (15, 2, '公告', '2', 'sys_notice_type', '', 'success', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (16, 1, '正常', '0', 'sys_notice_status', '', 'primary', 'Y', 1713334134616, 1, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (17, 2, '关闭', '1', 'sys_notice_status', '', 'danger', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (18, 0, '其他', '0', 'sys_oper_type', '', 'info', 'N', 1713334134616, 1, 20240412103337, 1);
INSERT INTO `sys_dict_data` VALUES (19, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (20, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (21, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (22, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (23, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (24, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (28, 1, '成功', '0', 'sys_common_status', '', 'primary', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (29, 2, '失败', '1', 'sys_common_status', '', 'danger', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (30, 0, '管理员', 'admin', 'pre_dict', NULL, 'default', 'N', 1713334134616, 1, 20240412105133, 1);
INSERT INTO `sys_dict_data` VALUES (31, 0, '租户', 'tenant', 'pre_dict', NULL, 'default', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (32, 0, '用户', 'user', 'pre_dict', NULL, 'default', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (33, 0, '在线', '1', 'device_status', NULL, 'success', 'N', 1713334134616, 1, 1714114284613, 1);
INSERT INTO `sys_dict_data` VALUES (34, 0, '离线', '0', 'device_status', NULL, 'info', 'N', 1713334134616, 1, 1714114287571, 1);
INSERT INTO `sys_dict_data` VALUES (35, 1, '故障', '2', 'device_status', NULL, 'danger', 'N', 1713334134616, 1, 1714114300372, 1);
INSERT INTO `sys_dict_data` VALUES (36, 2, '检修', '3', 'device_status', NULL, 'warning', 'N', 1713334134616, 1, 1714114303578, 1);
INSERT INTO `sys_dict_data` VALUES (37, 0, '未认证', 'NO', 'pay_certification_status', NULL, 'info', 'N', 1713334134616, 1, 20230206172127, NULL);
INSERT INTO `sys_dict_data` VALUES (38, 0, '微信支付', 'WX', 'pay_certification_status', NULL, 'success', 'N', 1713334134616, 1, 20230206172134, NULL);
INSERT INTO `sys_dict_data` VALUES (39, 0, '支付宝', 'ALI', 'pay_certification_status', NULL, 'primary', 'N', 1713334134616, 1, 20230206172147, NULL);
INSERT INTO `sys_dict_data` VALUES (40, 0, '微信、支付宝认证', 'ALL', 'pay_certification_status', NULL, 'default', 'N', 1713334134616, 1, 20230206172201, NULL);
INSERT INTO `sys_dict_data` VALUES (41, 0, '全部数据权限', '1', 'data_scope', NULL, 'default', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (42, 0, '自定数据权限', '2', 'data_scope', NULL, 'default', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (43, 0, '本部门数据权限', '3', 'data_scope', NULL, 'default', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (44, 0, '本部门及以下数据权限', '4', 'data_scope', NULL, 'default', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (45, 0, '支付成功', 'SUCCESS', 'charging_status', NULL, 'success', 'N', 1713334134616, 1, 20231201153601, NULL);
INSERT INTO `sys_dict_data` VALUES (46, 0, '订单关闭', 'CLOSED', 'charging_status', NULL, 'info', 'N', 1713334134616, 1, 20231201153606, NULL);
INSERT INTO `sys_dict_data` VALUES (47, 0, '充电中', 'CHARGING', 'charging_status', NULL, 'warning', 'N', 1713334134616, 1, 20231201153641, NULL);
INSERT INTO `sys_dict_data` VALUES (48, 0, '充电结束', 'CHARGE_END', 'charging_status', NULL, 'danger', 'N', 1713334134616, 1, 20231201153722, NULL);
INSERT INTO `sys_dict_data` VALUES (49, 0, '未支付', 'NOTPAY', 'charging_status', NULL, 'primary', 'N', 1713334134616, 1, 20231201153556, NULL);
INSERT INTO `sys_dict_data` VALUES (50, 0, '支付异常', 'ERROR', 'charging_status', NULL, 'danger', NULL, 1713334134616, 1, 20231201153755, NULL);
INSERT INTO `sys_dict_data` VALUES (51, 0, '钱包支付', 'WALLET', 'pay_type', NULL, 'info', 'N', 1713334134616, 1, 20231201154027, NULL);
INSERT INTO `sys_dict_data` VALUES (52, 0, '微信支付', 'WECHAT', 'pay_type', NULL, 'success', 'N', 1713334134616, 1, 20231201154033, NULL);
INSERT INTO `sys_dict_data` VALUES (53, 0, '支付宝', 'ZFB', 'pay_type', NULL, 'primary', 'N', 1713334134616, 1, 20231201154038, NULL);
INSERT INTO `sys_dict_data` VALUES (54, 0, '空闲', '0', 'road_status', NULL, 'success', 'N', 1713334134616, 1, 20230606082812, NULL);
INSERT INTO `sys_dict_data` VALUES (55, 0, '占用', '1', 'road_status', NULL, 'primary', 'N', 1713334134616, 1, 20230606082851, NULL);

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `type_id` bigint NOT NULL COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典类型',
  `create_at` bigint NOT NULL COMMENT '创建时间',
  `create_id` bigint NOT NULL COMMENT '创建者',
  `update_at` bigint NULL DEFAULT NULL COMMENT '更新时间',
  `update_id` bigint NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`type_id`) USING BTREE,
  UNIQUE INDEX `uk_dict_type`(`dict_type` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', 1713334134616, 1, 1719036473930, 1);
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', 1713334134616, 1, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', 1713334134616, 1, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (4, '任务状态', 'sys_job_status', 1713334134616, 1, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (5, '任务分组', 'sys_job_group', 1713334134616, 1, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', 1713334134616, 1, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (7, '通知类型', 'sys_notice_type', 1713334134616, 1, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (8, '通知状态', 'sys_notice_status', 1713334134616, 1, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (9, '操作类型', 'sys_oper_type', 1713334134616, 1, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (10, '系统状态', 'sys_common_status', 1713334134616, 1, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (11, '预留字典', 'pre_dict', 1713334134616, 1, 20240412105114, 1);
INSERT INTO `sys_dict_type` VALUES (12, '设备状态', 'device_status', 1713334134616, 1, 20230825082544, 1);
INSERT INTO `sys_dict_type` VALUES (13, '支付认证状态', 'pay_certification_status', 1713334134616, 1, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (14, '数据权限', 'data_scope', 1713334134616, 1, 20230209140623, 1);
INSERT INTO `sys_dict_type` VALUES (15, '支付状态', 'charging_status', 1713334134616, 1, 20231201153939, 1);
INSERT INTO `sys_dict_type` VALUES (16, '支付方式', 'pay_type', 1713334134616, 1, 20231201154012, 1);
INSERT INTO `sys_dict_type` VALUES (17, '设备通道状态', 'road_status', 1713334134616, 1, 20230610135404, 1);

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log`  (
  `log_id` bigint NOT NULL,
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户id',
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '地址',
  `ip_addr` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ip地址',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作系统',
  `login_time` bigint NOT NULL COMMENT '访问时间',
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '后台登录日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_login_log
-- ----------------------------
INSERT INTO `sys_login_log` VALUES (1805159404271276034, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1719218514798);
INSERT INTO `sys_login_log` VALUES (1805160816996749313, 0, 1774671331412627456, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1719218851622);
INSERT INTO `sys_login_log` VALUES (1805163558100242434, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1719219505142);
INSERT INTO `sys_login_log` VALUES (1805165831853412353, 0, 1774671331412627456, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1719220047258);
INSERT INTO `sys_login_log` VALUES (1805166100678938626, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1719220111328);
INSERT INTO `sys_login_log` VALUES (1805166251137011714, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1719220147221);

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `notice_id` bigint NOT NULL COMMENT '公告ID',
  `notice_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告标题',
  `notice_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '公告内容',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公告状态（0正常 1关闭）',
  `message_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息id',
  `create_at` bigint NULL DEFAULT NULL COMMENT '创建者',
  `create_id` bigint NULL DEFAULT NULL COMMENT '创建时间',
  `update_at` bigint NULL DEFAULT NULL COMMENT '更新者',
  `update_id` bigint NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`notice_id`) USING BTREE,
  UNIQUE INDEX `uk_message_id`(`message_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通知公告表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES (1, '温馨提醒：2018-07-01 若依新版本发布啦', '2', '&lt;a href=\"https://www.baidu.com\"/&gt;', '0', '1', 1, 20221219100609, 1719034682683, 1);
INSERT INTO `sys_notice` VALUES (2, '维护通知：2018-07-01 若依系统凌晨维护', '1', '&lt;', '0', '2', 1, 20221219100609, 1719034648745, 1);

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `oper_id` bigint NOT NULL COMMENT '日志主键',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模块标题',
  `oper_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作类型（0其他 1新增 2修改 3删除）',
  `method` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方式',
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作人员',
  `oper_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作地点',
  `oper_param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求参数',
  `json_result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '返回参数',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作状态（0正常 1异常）',
  `error_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '错误消息',
  `oper_time` bigint NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`oper_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '操作日志记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------
INSERT INTO `sys_oper_log` VALUES (1805166771369119745, 0, '菜单管理', '2', 'com.thingslink.user.controller.SysMenuController.edit()', 'POST', 'admin', '/sys_menu/edit', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719220271169);

SET FOREIGN_KEY_CHECKS = 1;
