/*
 Navicat Premium Data Transfer

 Source Server         : local_mysql8
 Source Server Type    : MySQL
 Source Server Version : 80035
 Source Host           : localhost:3306
 Source Schema         : wzkris_system

 Target Server Type    : MySQL
 Target Server Version : 80035
 File Encoding         : 65001

 Date: 14/11/2024 13:34:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS wzkris_system;
USE wzkris_system;
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
INSERT INTO `sys_config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-yellow', 'Y', 1713334134616, 1, 1722905883482, 1);
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
INSERT INTO `sys_dict_data` VALUES (30, 0, '密码模式', 'password', 'authorization_grant_types', NULL, 'primary', 'N', 1713334134616, 1, 1724138711360, 1);
INSERT INTO `sys_dict_data` VALUES (31, 0, '客户端模式', 'client_credentials', 'authorization_grant_types', NULL, 'primary', 'N', 1713334134616, 1, 1724138730253, 1);
INSERT INTO `sys_dict_data` VALUES (32, 0, '授权码模式', 'authorization_code', 'authorization_grant_types', NULL, 'primary', 'N', 1713334134616, 1, 1724138742722, 1);
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
INSERT INTO `sys_dict_data` VALUES (50, 0, '支付异常', 'ERROR', 'charging_status', NULL, 'danger', 'N', 1713334134616, 1, 20231201153755, NULL);
INSERT INTO `sys_dict_data` VALUES (51, 0, '钱包支付', 'WALLET', 'pay_type', NULL, 'info', 'N', 1713334134616, 1, 20231201154027, NULL);
INSERT INTO `sys_dict_data` VALUES (52, 0, '微信支付', 'WECHAT', 'pay_type', NULL, 'success', 'N', 1713334134616, 1, 20231201154033, NULL);
INSERT INTO `sys_dict_data` VALUES (53, 0, '支付宝', 'ZFB', 'pay_type', NULL, 'primary', 'N', 1713334134616, 1, 20231201154038, NULL);
INSERT INTO `sys_dict_data` VALUES (54, 0, '空闲', '0', 'road_status', NULL, 'success', 'N', 1713334134616, 1, 20230606082812, NULL);
INSERT INTO `sys_dict_data` VALUES (55, 0, '占用', '1', 'road_status', NULL, 'primary', 'N', 1713334134616, 1, 20230606082851, NULL);
INSERT INTO `sys_dict_data` VALUES (56, 0, '刷新模式', 'refresh_token', 'authorization_grant_types', NULL, 'primary', 'N', 1724138765564, 1, 1724138765564, 1);
INSERT INTO `sys_dict_data` VALUES (57, 1, '短信模式', 'sms', 'authorization_grant_types', NULL, 'primary', 'N', 1724138776721, 1, 1724139440459, 1);

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
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', 1713334134616, 1, 1724120372348, 1);
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', 1713334134616, 1, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', 1713334134616, 1, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (4, '任务状态', 'sys_job_status', 1713334134616, 1, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (5, '任务分组', 'sys_job_group', 1713334134616, 1, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (6, '系统是否', 'sys_yes_no', 1713334134616, 1, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (7, '通知类型', 'sys_notice_type', 1713334134616, 1, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (8, '通知状态', 'sys_notice_status', 1713334134616, 1, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (9, '操作类型', 'sys_oper_type', 1713334134616, 1, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (10, '系统状态', 'sys_common_status', 1713334134616, 1, NULL, 1);
INSERT INTO `sys_dict_type` VALUES (11, '授权类型', 'authorization_grant_types', 1713334134616, 1, 1724138651758, 1);
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
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录状态（0正常 1异常）',
  `login_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录ip',
  `login_location` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录地址',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作系统',
  `login_time` bigint NOT NULL COMMENT '登录时间',
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '后台登录日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_login_log
-- ----------------------------
INSERT INTO `sys_login_log` VALUES (1854445726222733314, 1774671331416821762, 'testtt', '0', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1730969289714);
INSERT INTO `sys_login_log` VALUES (1854447986348609537, 0, 'admin', '0', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1730969828577);
INSERT INTO `sys_login_log` VALUES (1854678798535737346, 0, 'admin', '0', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1731024857436);
INSERT INTO `sys_login_log` VALUES (1854684753184931841, 1774671331416821762, 'testtt', '0', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1731026278186);
INSERT INTO `sys_login_log` VALUES (1854787487200358402, 1774671331416821762, 'testtt', '0', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1731050771843);
INSERT INTO `sys_login_log` VALUES (1855842211701944321, 0, 'admin', '0', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1731302236931);
INSERT INTO `sys_login_log` VALUES (1855894306534682626, 1774671331416821762, 'testtt', '0', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1731314658178);
INSERT INTO `sys_login_log` VALUES (1856165246250184705, 0, 'admin', '0', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1731379254080);
INSERT INTO `sys_login_log` VALUES (1856248171410956289, 0, '111111', '0', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1731399026063);
INSERT INTO `sys_login_log` VALUES (1856251073160486914, 1774671331416821762, 'testtt', '0', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1731399717964);
INSERT INTO `sys_login_log` VALUES (1856491550472364034, 0, 'admin', '0', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1731457051368);
INSERT INTO `sys_login_log` VALUES (1856561944663384065, 1774671331416821762, 'testtt', '0', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1731473835485);
INSERT INTO `sys_login_log` VALUES (1856860281912356865, 0, 'admin', '0', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1731544963255);
INSERT INTO `sys_login_log` VALUES (1856862215037722626, 0, 'admin', '0', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1731545425013);
INSERT INTO `sys_login_log` VALUES (1856863905556144130, 1774671331416821762, 'testtt', '0', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1731545828601);

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
INSERT INTO `sys_notice` VALUES (2, '维护通知：2018-07-01 若依系统凌晨维护', '1', '&lt;', '0', '2', 1, 20221219100609, 1722905797425, 1);

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `oper_id` bigint NOT NULL COMMENT '日志主键',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `title` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模块标题',
  `sub_title` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '子标题',
  `oper_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作类型（0其他 1新增 2修改 3删除）',
  `method` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方式',
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作人员',
  `oper_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主机地址',
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
INSERT INTO `sys_oper_log` VALUES (1854445788424261634, 0, '操作日志', '', '3', 'com.wzkris.system.controller.SysOperlogController.clean()', 'POST', 'admin', '/operlog/clean', '127.0.0.1', '内网IP', '{}', '{\"biz\":0,\"err_msg\":\"Success\",\"timestamp\":1730969304507}', '0', NULL, 1730969304511);
INSERT INTO `sys_oper_log` VALUES (1854445859651932161, 1774671331416821762, '租户信息', '修改操作密码', '2', 'com.wzkris.user.controller.SysTenantOwnController.editOperPwd()', 'POST', 'testtt', '/tenant/edit_operpwd', '127.0.0.1', '内网IP', '{\"oldPassword\":\"**\",\"newPassword\":\"**\"}', '{\"biz\":1,\"err_msg\":\"密码错误\",\"timestamp\":1730969321530}', '1', '密码错误', 1730969321530);
INSERT INTO `sys_oper_log` VALUES (1855842422847401985, 0, '用户管理', '', '5', 'com.wzkris.user.controller.AppUserController.export()', 'POST', 'admin', '/app_user/export', '127.0.0.1', '内网IP', '{}', NULL, '1', 'Can not find \'Converter\' support class Map.', 1731302288103);
INSERT INTO `sys_oper_log` VALUES (1855842771016577025, 0, '用户管理', '', '5', 'com.wzkris.user.controller.AppUserController.export()', 'POST', 'admin', '/app_user/export', '127.0.0.1', '内网IP', '{}', NULL, '1', 'Can not find \'Converter\' support class Map.', 1731302371122);
INSERT INTO `sys_oper_log` VALUES (1855843434765185025, 0, '用户管理', '', '5', 'com.wzkris.user.controller.AppUserController.export()', 'POST', 'admin', '/app_user/export', '127.0.0.1', '内网IP', '{}', NULL, '1', 'Can not find \'Converter\' support class Map.', 1731302529366);
INSERT INTO `sys_oper_log` VALUES (1855843531909459969, 0, '用户管理', '', '5', 'com.wzkris.user.controller.AppUserController.export()', 'POST', 'admin', '/app_user/export', '127.0.0.1', '内网IP', '{}', NULL, '1', 'Can not find \'Converter\' support class Map.', 1731302552572);
INSERT INTO `sys_oper_log` VALUES (1855843649219948546, 0, '用户管理', '', '5', 'com.wzkris.user.controller.AppUserController.export()', 'POST', 'admin', '/app_user/export', '127.0.0.1', '内网IP', '{}', NULL, '1', 'Can not find \'Converter\' support class Map.', 1731302580481);
INSERT INTO `sys_oper_log` VALUES (1855845446059782146, 0, '用户管理', '', '5', 'com.wzkris.user.controller.AppUserController.export()', 'POST', 'admin', '/app_user/export', '127.0.0.1', '内网IP', '{}', NULL, '1', 'Can not find \'Converter\' support class Map.', 1731303008896);
INSERT INTO `sys_oper_log` VALUES (1855846833766232065, 0, '用户管理', '', '5', 'com.wzkris.user.controller.AppUserController.export()', 'POST', 'admin', '/app_user/export', '127.0.0.1', '内网IP', '{}', NULL, '0', NULL, 1731303339748);
INSERT INTO `sys_oper_log` VALUES (1855849693337841666, 0, '用户管理', '', '5', 'com.wzkris.user.controller.AppUserController.export()', 'POST', 'admin', '/app_user/export', '127.0.0.1', '内网IP', '{}', NULL, '0', NULL, 1731304021532);
INSERT INTO `sys_oper_log` VALUES (1855852079519330306, 0, 'OAuth2客户端管理', '', '5', 'com.wzkris.user.controller.OAuth2ClientController.export()', 'POST', 'admin', '/oauth2_client/export', '127.0.0.1', '内网IP', '{}', NULL, '1', 'Can not find \'Converter\' support class List.', 1731304590437);
INSERT INTO `sys_oper_log` VALUES (1855852187099033601, 0, '岗位管理', '导出岗位数据', '5', 'com.wzkris.user.controller.SysPostController.export()', 'POST', 'admin', '/sys_post/export', '127.0.0.1', '内网IP', '{}', NULL, '0', NULL, 1731304616121);
INSERT INTO `sys_oper_log` VALUES (1855852307966291970, 0, 'OAuth2客户端管理', '', '5', 'com.wzkris.user.controller.OAuth2ClientController.export()', 'POST', 'admin', '/oauth2_client/export', '127.0.0.1', '内网IP', '{}', NULL, '1', 'Can not find \'Converter\' support class List.', 1731304644938);
INSERT INTO `sys_oper_log` VALUES (1855852375205179393, 0, 'OAuth2客户端管理', '', '5', 'com.wzkris.user.controller.OAuth2ClientController.export()', 'POST', 'admin', '/oauth2_client/export', '127.0.0.1', '内网IP', '{}', NULL, '1', 'Can not find \'Converter\' support class List.', 1731304660971);
INSERT INTO `sys_oper_log` VALUES (1855852589320204290, 0, 'OAuth2客户端管理', '', '5', 'com.wzkris.user.controller.OAuth2ClientController.export()', 'POST', 'admin', '/oauth2_client/export', '127.0.0.1', '内网IP', '{}', NULL, '1', 'Can not find \'Converter\' support class List.', 1731304711974);
INSERT INTO `sys_oper_log` VALUES (1855853161574264833, 0, 'OAuth2客户端管理', '', '5', 'com.wzkris.user.controller.OAuth2ClientController.export()', 'POST', 'admin', '/oauth2_client/export', '127.0.0.1', '内网IP', '{}', NULL, '1', 'Can not find \'Converter\' support class List.', 1731304848412);
INSERT INTO `sys_oper_log` VALUES (1855853334329257986, 0, 'OAuth2客户端管理', '', '5', 'com.wzkris.user.controller.OAuth2ClientController.export()', 'POST', 'admin', '/oauth2_client/export', '127.0.0.1', '内网IP', '{}', NULL, '1', 'Can not find \'Converter\' support class List.', 1731304889597);
INSERT INTO `sys_oper_log` VALUES (1855853500637605889, 0, 'OAuth2客户端管理', '', '5', 'com.wzkris.user.controller.OAuth2ClientController.export()', 'POST', 'admin', '/oauth2_client/export', '127.0.0.1', '内网IP', '{}', NULL, '0', NULL, 1731304929249);
INSERT INTO `sys_oper_log` VALUES (1855853632800124929, 0, 'OAuth2客户端管理', '', '5', 'com.wzkris.user.controller.OAuth2ClientController.export()', 'POST', 'admin', '/oauth2_client/export', '127.0.0.1', '内网IP', '{}', NULL, '0', NULL, 1731304960759);
INSERT INTO `sys_oper_log` VALUES (1855853678673227777, 0, 'OAuth2客户端管理', '', '5', 'com.wzkris.user.controller.OAuth2ClientController.export()', 'POST', 'admin', '/oauth2_client/export', '127.0.0.1', '内网IP', '{}', NULL, '0', NULL, 1731304971739);
INSERT INTO `sys_oper_log` VALUES (1855853757349982209, 0, 'OAuth2客户端管理', '', '5', 'com.wzkris.user.controller.OAuth2ClientController.export()', 'POST', 'admin', '/oauth2_client/export', '127.0.0.1', '内网IP', '{}', NULL, '0', NULL, 1731304990495);
INSERT INTO `sys_oper_log` VALUES (1855853909439639553, 0, 'OAuth2客户端管理', '', '5', 'com.wzkris.user.controller.OAuth2ClientController.export()', 'POST', 'admin', '/oauth2_client/export', '127.0.0.1', '内网IP', '{}', NULL, '0', NULL, 1731305026714);
INSERT INTO `sys_oper_log` VALUES (1855854525444485122, 0, 'OAuth2客户端管理', '', '5', 'com.wzkris.user.controller.OAuth2ClientController.export()', 'POST', 'admin', '/oauth2_client/export', '127.0.0.1', '内网IP', '{}', NULL, '1', 'Can not find \'Converter\' support class List.', 1731305173573);
INSERT INTO `sys_oper_log` VALUES (1855860531369046017, 0, 'OAuth2客户端管理', '', '5', 'com.wzkris.user.controller.OAuth2ClientController.export()', 'POST', 'admin', '/oauth2_client/export', '127.0.0.1', '内网IP', '{}', NULL, '1', 'Can not instance custom converter:com.wzkris.user.domain.export.OAuth2ClientExport$ListConverter', 1731306605523);
INSERT INTO `sys_oper_log` VALUES (1855860761124630530, 0, 'OAuth2客户端管理', '', '5', 'com.wzkris.user.controller.OAuth2ClientController.export()', 'POST', 'admin', '/oauth2_client/export', '127.0.0.1', '内网IP', '{}', NULL, '0', NULL, 1731306660291);
INSERT INTO `sys_oper_log` VALUES (1855860776081518594, 0, 'OAuth2客户端管理', '', '5', 'com.wzkris.user.controller.OAuth2ClientController.export()', 'POST', 'admin', '/oauth2_client/export', '127.0.0.1', '内网IP', '{}', NULL, '0', NULL, 1731306663899);
INSERT INTO `sys_oper_log` VALUES (1855860829965742082, 0, 'OAuth2客户端管理', '', '5', 'com.wzkris.user.controller.OAuth2ClientController.export()', 'POST', 'admin', '/oauth2_client/export', '127.0.0.1', '内网IP', '{}', NULL, '0', NULL, 1731306676747);
INSERT INTO `sys_oper_log` VALUES (1855861155863162882, 0, 'OAuth2客户端管理', '', '5', 'com.wzkris.user.controller.OAuth2ClientController.export()', 'POST', 'admin', '/oauth2_client/export', '127.0.0.1', '内网IP', '{}', NULL, '1', 'Convert data:[openid] error, at row:1', 1731306754417);
INSERT INTO `sys_oper_log` VALUES (1855862160709349377, 0, 'OAuth2客户端管理', '', '5', 'com.wzkris.user.controller.OAuth2ClientController.export()', 'POST', 'admin', '/oauth2_client/export', '127.0.0.1', '内网IP', '{}', NULL, '0', NULL, 1731306993989);
INSERT INTO `sys_oper_log` VALUES (1855862280754524162, 0, 'OAuth2客户端管理', '', '5', 'com.wzkris.user.controller.OAuth2ClientController.export()', 'POST', 'admin', '/oauth2_client/export', '127.0.0.1', '内网IP', '{}', NULL, '0', NULL, 1731307022642);
INSERT INTO `sys_oper_log` VALUES (1855862776974241794, 0, 'OAuth2客户端管理', '', '5', 'com.wzkris.user.controller.OAuth2ClientController.export()', 'POST', 'admin', '/oauth2_client/export', '127.0.0.1', '内网IP', '{}', NULL, '0', NULL, 1731307140958);
INSERT INTO `sys_oper_log` VALUES (1855863286326325249, 0, 'OAuth2客户端管理', '', '5', 'com.wzkris.user.controller.OAuth2ClientController.export()', 'POST', 'admin', '/oauth2_client/export', '127.0.0.1', '内网IP', '{}', NULL, '0', NULL, 1731307262351);
INSERT INTO `sys_oper_log` VALUES (1855863829375447041, 0, 'OAuth2客户端管理', '', '5', 'com.wzkris.user.controller.OAuth2ClientController.export()', 'POST', 'admin', '/oauth2_client/export', '127.0.0.1', '内网IP', '{}', NULL, '0', NULL, 1731307391853);
INSERT INTO `sys_oper_log` VALUES (1855863950569861121, 0, 'OAuth2客户端管理', '', '5', 'com.wzkris.user.controller.OAuth2ClientController.export()', 'POST', 'admin', '/oauth2_client/export', '127.0.0.1', '内网IP', '{}', NULL, '0', NULL, 1731307420724);
INSERT INTO `sys_oper_log` VALUES (1855864037354205186, 0, '用户管理', '', '5', 'com.wzkris.user.controller.AppUserController.export()', 'POST', 'admin', '/app_user/export', '127.0.0.1', '内网IP', '{}', NULL, '0', NULL, 1731307441453);
INSERT INTO `sys_oper_log` VALUES (1855865225260789761, 0, '后台管理', '导出用户数据', '5', 'com.wzkris.user.controller.SysUserController.export()', 'POST', 'admin', '/sys_user/export', '127.0.0.1', '内网IP', '{}', NULL, '1', 'cannot find converter from SysUserVO to SysUserExport', 1731307724624);
INSERT INTO `sys_oper_log` VALUES (1855865981686738946, 0, '后台管理', '导出用户数据', '5', 'com.wzkris.user.controller.SysUserController.export()', 'POST', 'admin', '/sys_user/export', '127.0.0.1', '内网IP', '{}', NULL, '0', NULL, 1731307904972);
INSERT INTO `sys_oper_log` VALUES (1855868248045350913, 0, 'OAuth2客户端管理', '导出数据', '5', 'com.wzkris.user.controller.OAuth2ClientController.export()', 'POST', 'admin', '/oauth2_client/export', '127.0.0.1', '内网IP', '{}', NULL, '0', NULL, 1731308445358);
INSERT INTO `sys_oper_log` VALUES (1855878269076467714, 0, '菜单管理', '新增菜单', '1', 'com.wzkris.user.controller.SysMenuController.add()', 'POST', 'admin', '/sys_menu/add', '127.0.0.1', '内网IP', '{\"updateAt\":1731310834327,\"menuName\":\"1\",\"isVisible\":true,\"createAt\":1731310834327,\"parentId\":0,\"path\":\"1\",\"isCache\":false,\"children\":[],\"isFrame\":false,\"menuId\":1855878268094971905,\"menuType\":\"M\",\"menuSort\":1,\"status\":\"0\"}', '{\"biz\":0,\"err_msg\":\"Success\",\"timestamp\":1731310834471}', '0', NULL, 1731310834474);
INSERT INTO `sys_oper_log` VALUES (1855879986681704449, 0, '菜单管理', '新增菜单', '1', 'com.wzkris.user.controller.SysMenuController.add()', 'POST', 'admin', '/sys_menu/add', '127.0.0.1', '内网IP', '{\"updateAt\":1731311244007,\"menuName\":\"11\",\"isVisible\":true,\"createAt\":1731311244007,\"parentId\":0,\"path\":\"1\",\"isCache\":false,\"children\":[],\"isFrame\":false,\"menuId\":1855879986417430529,\"menuType\":\"M\",\"menuSort\":1,\"status\":\"0\"}', '{\"biz\":0,\"err_msg\":\"Success\",\"timestamp\":1731311244018}', '0', NULL, 1731311244021);
INSERT INTO `sys_oper_log` VALUES (1855879995141615617, 0, '菜单管理', '删除菜单', '3', 'com.wzkris.user.controller.SysMenuController.remove()', 'POST', 'admin', '/sys_menu/remove', '127.0.0.1', '内网IP', '1855879986417430529', '{\"biz\":0,\"err_msg\":\"Success\",\"timestamp\":1731311246078}', '0', NULL, 1731311246078);
INSERT INTO `sys_oper_log` VALUES (1856232881461600257, 0, '角色管理', '新增角色', '1', 'com.wzkris.user.controller.SysRoleController.add()', 'POST', 'admin', '/sys_role/add', '127.0.0.1', '内网IP', '{\"isMenuDisplay\":true,\"roleId\":1856232880907972609,\"roleName\":\"11\",\"updateAt\":1731395380608,\"isDeptDisplay\":true,\"deptIds\":[],\"menuIds\":[],\"createAt\":1731395380608,\"roleSort\":0}', '{\"biz\":0,\"err_msg\":\"Success\",\"timestamp\":1731395380623}', '0', NULL, 1731395380624);
INSERT INTO `sys_oper_log` VALUES (1856243838522957825, 0, '角色管理', '批量用户授权', '4', 'com.wzkris.user.controller.SysRoleController.batchAuth()', 'POST', 'admin', '/sys_role/authorize_user', '127.0.0.1', '内网IP', '{\"roleId\":3,\"userIds\":[2,1]}', '{\"biz\":0,\"err_msg\":\"Success\",\"timestamp\":1731397993045}', '0', NULL, 1731397993051);
INSERT INTO `sys_oper_log` VALUES (1856243887298519042, 0, '角色管理', '批量取消授权', '4', 'com.wzkris.user.controller.SysRoleController.cancelAuth()', 'POST', 'admin', '/sys_role/authorize/cancel_batch', '127.0.0.1', '内网IP', '{\"roleId\":3,\"userIds\":[2,1]}', '{\"biz\":0,\"err_msg\":\"Success\",\"timestamp\":1731398004725}', '0', NULL, 1731398004725);
INSERT INTO `sys_oper_log` VALUES (1856243919334612994, 0, '角色管理', '批量用户授权', '4', 'com.wzkris.user.controller.SysRoleController.batchAuth()', 'POST', 'admin', '/sys_role/authorize_user', '127.0.0.1', '内网IP', '{\"roleId\":4,\"userIds\":[2,1]}', '{\"biz\":0,\"err_msg\":\"Success\",\"timestamp\":1731398012365}', '0', NULL, 1731398012365);
INSERT INTO `sys_oper_log` VALUES (1856244031607742466, 0, '角色管理', '批量取消授权', '4', 'com.wzkris.user.controller.SysRoleController.cancelAuth()', 'POST', 'admin', '/sys_role/authorize/cancel_batch', '127.0.0.1', '内网IP', '{\"roleId\":4,\"userIds\":[2,1]}', '{\"biz\":0,\"err_msg\":\"Success\",\"timestamp\":1731398039132}', '0', NULL, 1731398039132);
INSERT INTO `sys_oper_log` VALUES (1856244297648250881, 0, '角色管理', '批量用户授权', '4', 'com.wzkris.user.controller.SysRoleController.batchAuth()', 'POST', 'admin', '/sys_role/authorize_user', '127.0.0.1', '内网IP', '{\"roleId\":4,\"userIds\":[1]}', '{\"biz\":0,\"err_msg\":\"Success\",\"timestamp\":1731398102558}', '0', NULL, 1731398102558);
INSERT INTO `sys_oper_log` VALUES (1856244353721901057, 0, '角色管理', '取消授权', '4', 'com.wzkris.user.controller.SysRoleController.cancelAuth()', 'POST', 'admin', '/sys_role/authorize/cancel', '127.0.0.1', '内网IP', '{\"roleId\":4,\"userId\":1}', '{\"biz\":0,\"err_msg\":\"Success\",\"timestamp\":1731398115930}', '0', NULL, 1731398115930);
INSERT INTO `sys_oper_log` VALUES (1856247730195341314, 0, '后台管理', '导出用户数据', '5', 'com.wzkris.user.controller.SysUserController.export()', 'POST', 'admin', '/sys_user/export', '127.0.0.1', '内网IP', '{\"params\":{}}', NULL, '0', NULL, 1731398920916);
INSERT INTO `sys_oper_log` VALUES (1856247985028669442, 0, '后台管理', '新增用户', '1', 'com.wzkris.user.controller.SysUserController.add()', 'POST', 'admin', '/sys_user/add', '127.0.0.1', '内网IP', '{\"roleIds\":[2],\"postIds\":[],\"deptId\":1775387364419072002,\"nickname\":\"213123\",\"username\":\"testtt\"}', '{\"biz\":1,\"err_msg\":\"修改用户\'testtt\'失败，登录账号已存在\",\"timestamp\":1731398981708}', '1', '修改用户\'testtt\'失败，登录账号已存在', 1731398981708);
INSERT INTO `sys_oper_log` VALUES (1856248022571884546, 0, '后台管理', '新增用户', '1', 'com.wzkris.user.controller.SysUserController.add()', 'POST', 'admin', '/sys_user/add', '127.0.0.1', '内网IP', '{\"roleIds\":[2],\"postIds\":[],\"deptId\":1775387364419072002,\"nickname\":\"213123\",\"updateAt\":1731398990651,\"userId\":1856248022542561282,\"createAt\":1731398990651,\"username\":\"111111\"}', '{\"biz\":0,\"err_msg\":\"Success\",\"timestamp\":1731398990668}', '0', NULL, 1731398990668);
INSERT INTO `sys_oper_log` VALUES (1856249618051571714, 0, '后台管理', '删除用户', '3', 'com.wzkris.user.controller.SysUserController.remove()', 'POST', 'admin', '/sys_user/remove', '127.0.0.1', '内网IP', '[1856248022542561282]', '{\"biz\":0,\"err_msg\":\"Success\",\"timestamp\":1731399370993}', '0', NULL, 1731399370994);
INSERT INTO `sys_oper_log` VALUES (1856249662234370050, 0, '后台管理', '删除用户', '3', 'com.wzkris.user.controller.SysUserController.remove()', 'POST', 'admin', '/sys_user/remove', '127.0.0.1', '内网IP', '[1856248022542561282]', '{\"biz\":1,\"err_msg\":\"Operate Fail\",\"timestamp\":1731399381592}', '1', 'Operate Fail', 1731399381592);
INSERT INTO `sys_oper_log` VALUES (1856249675626782721, 0, '后台管理', '删除用户', '3', 'com.wzkris.user.controller.SysUserController.remove()', 'POST', 'admin', '/sys_user/remove', '127.0.0.1', '内网IP', '[1856248022542561282,1856248022542561282]', NULL, '1', '当前用户没有权限访问数据', 1731399384779);
INSERT INTO `sys_oper_log` VALUES (1856249705813188610, 0, '后台管理', '删除用户', '3', 'com.wzkris.user.controller.SysUserController.remove()', 'POST', 'admin', '/sys_user/remove', '127.0.0.1', '内网IP', '[1856248022542561282,1856248022542561282,1856248022542561282]', NULL, '1', '当前用户没有权限访问数据', 1731399391975);
INSERT INTO `sys_oper_log` VALUES (1856249776474628097, 0, '后台管理', '删除用户', '3', 'com.wzkris.user.controller.SysUserController.remove()', 'POST', 'admin', '/sys_user/remove', '127.0.0.1', '内网IP', '[1856248022542561282]', '{\"biz\":1,\"err_msg\":\"Operate Fail\",\"timestamp\":1731399408824}', '1', 'Operate Fail', 1731399408824);
INSERT INTO `sys_oper_log` VALUES (1856251200734437377, 1774671331416821762, '后台管理', '新增用户', '1', 'com.wzkris.user.controller.SysUserController.add()', 'POST', 'testtt', '/sys_user/add', '127.0.0.1', '内网IP', '{\"roleIds\":[1775445330027577345],\"postIds\":[],\"deptId\":1775382319191453698,\"nickname\":\"1\",\"updateAt\":1731399748330,\"userId\":1856251200466030593,\"createAt\":1731399748330,\"username\":\"11111111\",\"status\":\"0\"}', '{\"biz\":0,\"err_msg\":\"Success\",\"timestamp\":1731399748344}', '0', NULL, 1731399748345);
INSERT INTO `sys_oper_log` VALUES (1856251973161656322, 1774671331416821762, '后台管理', '修改用户', '2', 'com.wzkris.user.controller.SysUserController.edit()', 'POST', 'testtt', '/sys_user/edit', '127.0.0.1', '内网IP', '{\"roleIds\":[1775445330027577345],\"gender\":\"2\",\"isDeleted\":false,\"postIds\":[],\"tenantId\":1774671331416821762,\"deptId\":1775382319191453698,\"nickname\":\"1\",\"updateAt\":1731399932542,\"userId\":1856251200466030593,\"username\":\"1111sub_\",\"status\":\"0\"}', '{\"biz\":0,\"err_msg\":\"Success\",\"timestamp\":1731399932551}', '0', NULL, 1731399932551);
INSERT INTO `sys_oper_log` VALUES (1856251994527440897, 1774671331416821762, '后台管理', '修改用户', '2', 'com.wzkris.user.controller.SysUserController.edit()', 'POST', 'testtt', '/sys_user/edit', '127.0.0.1', '内网IP', '{\"roleIds\":[1775445330027577345],\"gender\":\"2\",\"isDeleted\":false,\"postIds\":[],\"tenantId\":1774671331416821762,\"deptId\":1775382319191453698,\"nickname\":\"1\",\"updateAt\":1731399937644,\"userId\":1856251200466030593,\"username\":\"___sub_\",\"status\":\"0\"}', '{\"biz\":0,\"err_msg\":\"Success\",\"timestamp\":1731399937652}', '0', NULL, 1731399937652);
INSERT INTO `sys_oper_log` VALUES (1856252011195604994, 1774671331416821762, '后台管理', '修改用户', '2', 'com.wzkris.user.controller.SysUserController.edit()', 'POST', 'testtt', '/sys_user/edit', '127.0.0.1', '内网IP', '{\"roleIds\":[1775445330027577345],\"gender\":\"2\",\"isDeleted\":false,\"postIds\":[],\"tenantId\":1774671331416821762,\"deptId\":1775382319191453698,\"nickname\":\"xxxxxx\",\"updateAt\":1731399941611,\"userId\":1856251200466030593,\"username\":\"___sub_\",\"status\":\"0\"}', '{\"biz\":0,\"err_msg\":\"Success\",\"timestamp\":1731399941619}', '0', NULL, 1731399941619);
INSERT INTO `sys_oper_log` VALUES (1856260279515160578, 0, '部门管理', '新增部门', '1', 'com.wzkris.user.controller.SysDeptController.add()', 'POST', 'admin', '/sys_dept/add', '127.0.0.1', '内网IP', '{\"deptSort\":0,\"deptName\":\"11\",\"children\":[],\"deptId\":1856260279276072961,\"updateAt\":1731401912894,\"createAt\":1731401912894,\"email\":\"\",\"status\":\"0\"}', '{\"biz\":0,\"err_msg\":\"Success\",\"timestamp\":1731401912899}', '0', NULL, 1731401912900);
INSERT INTO `sys_oper_log` VALUES (1856260310892748801, 0, '部门管理', '修改部门', '2', 'com.wzkris.user.controller.SysDeptController.edit()', 'POST', 'admin', '/sys_dept/edit', '127.0.0.1', '内网IP', '{\"deptSort\":0,\"deptName\":\"11\",\"children\":[],\"deptId\":1856260279276072961,\"tenantId\":0,\"updateAt\":1731401920411,\"ancestors\":\"0,100,114,105\",\"parentId\":105,\"email\":\"\",\"status\":\"0\"}', '{\"biz\":0,\"err_msg\":\"Success\",\"timestamp\":1731401920418}', '0', NULL, 1731401920418);
INSERT INTO `sys_oper_log` VALUES (1856260428203237378, 0, '部门管理', '删除部门', '3', 'com.wzkris.user.controller.SysDeptController.remove()', 'POST', 'admin', '/sys_dept/remove', '127.0.0.1', '内网IP', '1856260279276072961', '{\"biz\":0,\"err_msg\":\"Success\",\"timestamp\":1731401948386}', '0', NULL, 1731401948386);
INSERT INTO `sys_oper_log` VALUES (1856869915142365186, 0, '后台管理', '新增用户', '1', 'com.wzkris.user.controller.SysUserController.add()', 'POST', 'admin', '/sys_user/add', '127.0.0.1', '内网IP', '{\"roleIds\":[1775445330027577345],\"postIds\":[],\"nickname\":\"zzzz\",\"updateAt\":1731547261314,\"userId\":1856869914760638466,\"createAt\":1731547261314,\"username\":\"ccccccc\",\"status\":\"0\"}', '{\"biz\":0,\"err_msg\":\"Success\",\"timestamp\":1731547261330}', '0', NULL, 1731547261332);

SET FOREIGN_KEY_CHECKS = 1;
