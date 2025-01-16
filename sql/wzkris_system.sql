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

 Date: 15/01/2025 10:43:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


CREATE DATABASE IF NOT EXISTS wzkris_system default character set utf8mb4 collate utf8mb4_unicode_ci;
USE wzkris_system;
-- ----------------------------
-- Table structure for global_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `global_dict_data`;
CREATE TABLE `global_dict_data`  (
  `data_id` bigint NOT NULL COMMENT '字典编码',
  `dict_sort` int NOT NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典标签',
  `dict_value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典键值',
  `dict_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `create_at` bigint NOT NULL COMMENT '创建时间',
  `create_id` bigint NOT NULL COMMENT '创建者',
  `update_at` bigint NULL DEFAULT NULL COMMENT '更新时间',
  `update_id` bigint NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`data_id`) USING BTREE,
  INDEX `idx_dict_type`(`dict_type` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '字典数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of global_dict_data
-- ----------------------------
INSERT INTO `global_dict_data` VALUES (1, 1, '男', '0', 'user_sex', '', '', 'Y', 1713334134616, 1, NULL, NULL);
INSERT INTO `global_dict_data` VALUES (2, 2, '女', '1', 'user_sex', '', '', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `global_dict_data` VALUES (3, 3, '未知', '2', 'user_sex', '', '', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `global_dict_data` VALUES (4, 1, '显示', 'true', 'menu_visible', '', 'primary', 'Y', 1713334134616, 1, 1732346171150, 1);
INSERT INTO `global_dict_data` VALUES (5, 2, '隐藏', 'false', 'menu_visible', '', 'danger', 'N', 1713334134616, 1, 1732346176502, 1);
INSERT INTO `global_dict_data` VALUES (6, 1, '正常', '0', 'common_disable', '', 'primary', 'Y', 1713334134616, 1, NULL, NULL);
INSERT INTO `global_dict_data` VALUES (7, 2, '停用', '1', 'common_disable', '', 'danger', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `global_dict_data` VALUES (8, 1, '正常', '0', 'sys_job_status', '', 'primary', 'Y', 1713334134616, 1, NULL, NULL);
INSERT INTO `global_dict_data` VALUES (9, 2, '暂停', '1', 'sys_job_status', '', 'danger', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `global_dict_data` VALUES (10, 1, '默认', 'DEFAULT', 'sys_job_group', '', '', 'Y', 1713334134616, 1, NULL, NULL);
INSERT INTO `global_dict_data` VALUES (11, 2, '系统', 'SYSTEM', 'sys_job_group', '', '', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `global_dict_data` VALUES (12, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', 1713334134616, 1, NULL, NULL);
INSERT INTO `global_dict_data` VALUES (13, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `global_dict_data` VALUES (14, 1, '系统公告', '1', 'msg_type', '', 'primary', 'Y', 1713334134616, 1, 1734312560477, 1);
INSERT INTO `global_dict_data` VALUES (15, 2, 'APP公告', '2', 'msg_type', '', 'success', 'N', 1713334134616, 1, 1734312567234, 1);
INSERT INTO `global_dict_data` VALUES (16, 1, '草稿', '0', 'msg_status', '', 'info', 'Y', 1713334134616, 1, 1733991582786, 1);
INSERT INTO `global_dict_data` VALUES (17, 2, '关闭', '1', 'msg_status', '', 'danger', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `global_dict_data` VALUES (18, 0, '已发布', '2', 'msg_status', NULL, 'primary', 'N', 1733991577087, 1, 1734316199926, 1);
INSERT INTO `global_dict_data` VALUES (19, 0, '其他', '0', 'sys_oper_type', '', 'info', 'N', 1713334134616, 1, 20240412103337, 1);
INSERT INTO `global_dict_data` VALUES (20, 1, '新增', '1', 'sys_oper_type', '', 'info', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `global_dict_data` VALUES (21, 2, '修改', '2', 'sys_oper_type', '', 'info', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `global_dict_data` VALUES (22, 3, '删除', '3', 'sys_oper_type', '', 'danger', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `global_dict_data` VALUES (23, 4, '授权', '4', 'sys_oper_type', '', 'primary', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `global_dict_data` VALUES (24, 5, '导出', '5', 'sys_oper_type', '', 'warning', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `global_dict_data` VALUES (25, 6, '导入', '6', 'sys_oper_type', '', 'warning', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `global_dict_data` VALUES (29, 1, '成功', '0', 'sys_oper_status', '', 'primary', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `global_dict_data` VALUES (30, 2, '失败', '1', 'sys_oper_status', '', 'danger', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `global_dict_data` VALUES (31, 0, '密码模式', 'password', 'authorization_grant_types', NULL, 'primary', 'N', 1713334134616, 1, 1724138711360, 1);
INSERT INTO `global_dict_data` VALUES (32, 0, '客户端模式', 'client_credentials', 'authorization_grant_types', NULL, 'primary', 'N', 1713334134616, 1, 1724138730253, 1);
INSERT INTO `global_dict_data` VALUES (33, 0, '授权码模式', 'authorization_code', 'authorization_grant_types', NULL, 'primary', 'N', 1713334134616, 1, 1724138742722, 1);
INSERT INTO `global_dict_data` VALUES (34, 0, '在线', 'true', 'online_status', NULL, 'success', 'N', 1713334134616, 1, 1733714896206, 1);
INSERT INTO `global_dict_data` VALUES (35, 0, '离线', 'false', 'online_status', NULL, 'info', 'N', 1713334134616, 1, 1733714903414, 1);
INSERT INTO `global_dict_data` VALUES (38, 0, '未认证', 'NO', 'pay_certification_status', NULL, 'info', 'N', 1713334134616, 1, 20230206172127, NULL);
INSERT INTO `global_dict_data` VALUES (39, 0, '微信支付', 'WX', 'pay_certification_status', NULL, 'success', 'N', 1713334134616, 1, 20230206172134, NULL);
INSERT INTO `global_dict_data` VALUES (40, 0, '支付宝', 'ALI', 'pay_certification_status', NULL, 'primary', 'N', 1713334134616, 1, 20230206172147, NULL);
INSERT INTO `global_dict_data` VALUES (42, 0, '全部数据权限', '1', 'data_scope', NULL, 'default', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `global_dict_data` VALUES (43, 0, '自定数据权限', '2', 'data_scope', NULL, 'default', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `global_dict_data` VALUES (44, 0, '本部门数据权限', '3', 'data_scope', NULL, 'default', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `global_dict_data` VALUES (45, 0, '本部门及以下数据权限', '4', 'data_scope', NULL, 'default', 'N', 1713334134616, 1, NULL, NULL);
INSERT INTO `global_dict_data` VALUES (46, 0, '支付成功', 'SUCCESS', 'pay_status', NULL, 'success', 'N', 1713334134616, 1, 20231201153601, NULL);
INSERT INTO `global_dict_data` VALUES (47, 0, '订单关闭', 'CLOSED', 'pay_status', NULL, 'info', 'N', 1713334134616, 1, 20231201153606, NULL);
INSERT INTO `global_dict_data` VALUES (50, 0, '未支付', 'NOTPAY', 'pay_status', NULL, 'primary', 'N', 1713334134616, 1, 20231201153556, NULL);
INSERT INTO `global_dict_data` VALUES (51, 0, '支付异常', 'ERROR', 'pay_status', NULL, 'danger', 'N', 1713334134616, 1, 20231201153755, NULL);
INSERT INTO `global_dict_data` VALUES (52, 0, '钱包支付', 'WALLET', 'pay_type', NULL, 'info', 'Y', 1713334134616, 1, 20231201154027, NULL);
INSERT INTO `global_dict_data` VALUES (53, 0, '微信支付', 'WECHAT', 'pay_type', NULL, 'success', 'N', 1713334134616, 1, 20231201154033, NULL);
INSERT INTO `global_dict_data` VALUES (54, 0, '支付宝', 'ZFB', 'pay_type', NULL, 'primary', 'N', 1713334134616, 1, 20231201154038, NULL);
INSERT INTO `global_dict_data` VALUES (55, 0, 'mqtt', 'MQTT', 'protocol_type', NULL, 'primary', 'N', 1713334134616, 1, 1732083154010, 1);
INSERT INTO `global_dict_data` VALUES (56, 0, 'http', 'HTTP', 'protocol_type', NULL, 'primary', 'N', 1713334134616, 1, 1732083038284, 1);
INSERT INTO `global_dict_data` VALUES (57, 0, '刷新模式', 'refresh_token', 'authorization_grant_types', NULL, 'primary', 'N', 1724138765564, 1, 1724138765564, 1);
INSERT INTO `global_dict_data` VALUES (58, 1, '短信模式', 'sms', 'authorization_grant_types', NULL, 'primary', 'N', 1724138776721, 1, 1724139440459, 1);
INSERT INTO `global_dict_data` VALUES (59, 0, 'java', 'java', 'protocol_language', NULL, 'primary', 'Y', 1732083132495, 1, 1732083487053, 1);
INSERT INTO `global_dict_data` VALUES (60, 0, 'lua', 'lua', 'protocol_language', NULL, 'primary', 'N', 1732083138284, 1, 1732083489674, 1);
INSERT INTO `global_dict_data` VALUES (61, 0, '目录', 'D', 'menu_type', NULL, 'info', 'N', 1732346595006, 1, 1734502769620, 1);
INSERT INTO `global_dict_data` VALUES (62, 0, '菜单', 'M', 'menu_type', NULL, 'primary', 'N', 1732346618297, 1, 1734502774651, 1);
INSERT INTO `global_dict_data` VALUES (63, 0, '按钮', 'B', 'menu_type', NULL, 'danger', 'N', 1732346631024, 1, 1734502780059, 1);
INSERT INTO `global_dict_data` VALUES (64, 0, '字段', 'F', 'menu_type', NULL, 'warning', 'N', 1734502837728, 1, 1734502837728, 1);
INSERT INTO `global_dict_data` VALUES (65, 0, '收入', '0', 'wallet_record_type', NULL, 'primary', 'N', 1732523552276, 1, 1732523552276, 1);
INSERT INTO `global_dict_data` VALUES (66, 0, '支出', '1', 'wallet_record_type', NULL, 'danger', 'N', 1732523560627, 1, 1732523560627, 1);
INSERT INTO `global_dict_data` VALUES (67, 0, '属性上报', '1', 'command_type', NULL, 'primary', 'N', 1734654628030, 1, 1734654628030, 1);
INSERT INTO `global_dict_data` VALUES (68, 0, '功能调用', '2', 'command_type', NULL, 'danger', 'N', 1734654633496, 1, 1734654704876, 1);
INSERT INTO `global_dict_data` VALUES (69, 0, '事件上报', '3', 'command_type', NULL, 'primary', 'N', 1734654641481, 1, 1734654641481, 1);
INSERT INTO `global_dict_data` VALUES (70, 0, '设备升级', '4', 'command_type', NULL, 'warning', 'N', 1734654665369, 1, 1734654665369, 1);
INSERT INTO `global_dict_data` VALUES (71, 0, '设备上线', '5', 'command_type', NULL, 'success', 'N', 1734654677241, 1, 1734654677241, 1);
INSERT INTO `global_dict_data` VALUES (72, 0, '设备离线', '6', 'command_type', NULL, 'info', 'N', 1734654685085, 1, 1734654685085, 1);
INSERT INTO `global_dict_data` VALUES (73, 0, '直连产品', '0', 'product_type', NULL, 'default', 'N', 1734771047695, 1, 1734771047695, 1);
INSERT INTO `global_dict_data` VALUES (74, 0, '网关产品', '1', 'product_type', NULL, 'default', 'N', 1734771055660, 1, 1734771055660, 1);
INSERT INTO `global_dict_data` VALUES (75, 0, '网关子产品', '2', 'product_type', NULL, 'default', 'N', 1734771061676, 1, 1734771061676, 1);
INSERT INTO `global_dict_data` VALUES (76, 0, '属性', '1', 'things_model_type', NULL, 'default', 'N', 1735004717937, 1, 1735004717937, 1);
INSERT INTO `global_dict_data` VALUES (77, 0, '服务', '2', 'things_model_type', NULL, 'default', 'N', 1735004799852, 1, 1735028386611, 1);
INSERT INTO `global_dict_data` VALUES (78, 0, '事件', '3', 'things_model_type', NULL, 'default', 'N', 1735004811706, 1, 1735004811706, 1);
INSERT INTO `global_dict_data` VALUES (79, 0, '整形', 'int32', 'things_model_datatype', NULL, 'default', 'N', 1735007269709, 1, 1735007269709, 1);
INSERT INTO `global_dict_data` VALUES (80, 0, '小数', 'decimal', 'things_model_datatype', NULL, 'default', 'N', 1735007348398, 1, 1735007348398, 1);
INSERT INTO `global_dict_data` VALUES (81, 0, '布尔', 'boolean', 'things_model_datatype', NULL, 'default', 'N', 1735007461440, 1, 1735007461440, 1);
INSERT INTO `global_dict_data` VALUES (82, 0, '枚举', 'enum', 'things_model_datatype', NULL, 'default', 'N', 1735007474501, 1, 1735007474501, 1);
INSERT INTO `global_dict_data` VALUES (83, 0, '字符串', 'string', 'things_model_datatype', NULL, 'default', 'N', 1735007483437, 1, 1735007483437, 1);
INSERT INTO `global_dict_data` VALUES (84, 0, '数组', 'array', 'things_model_datatype', NULL, 'default', 'N', 1735007491547, 1, 1735007491547, 1);
INSERT INTO `global_dict_data` VALUES (85, 0, '结构体', 'struct', 'things_model_datatype', NULL, 'default', 'N', 1735007508886, 1, 1735007508886, 1);
INSERT INTO `global_dict_data` VALUES (86, 0, '设备码模式', 'urn:ietf:params:oauth:grant-type:device_code', 'authorization_grant_types', NULL, 'primary', 'N', 1735954486738, 1, 1735954494338, 1);
INSERT INTO `global_dict_data` VALUES (87, 0, 'token交换模式', 'urn:ietf:params:oauth:grant-type:token-exchange', 'authorization_grant_types', NULL, 'primary', 'N', 1735954518903, 1, 1735954522611, 1);

-- ----------------------------
-- Table structure for global_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `global_dict_type`;
CREATE TABLE `global_dict_type`  (
  `type_id` bigint NOT NULL COMMENT '字典主键',
  `dict_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典名称',
  `dict_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '字典类型',
  `create_at` bigint NOT NULL COMMENT '创建时间',
  `create_id` bigint NOT NULL COMMENT '创建者',
  `update_at` bigint NULL DEFAULT NULL COMMENT '更新时间',
  `update_id` bigint NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`type_id`) USING BTREE,
  UNIQUE INDEX `uk_dict_type`(`dict_type` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of global_dict_type
-- ----------------------------
INSERT INTO `global_dict_type` VALUES (1, '用户性别', 'user_sex', 1713334134616, 1, 1732084160647, 1);
INSERT INTO `global_dict_type` VALUES (2, '菜单可见状态', 'menu_visible', 1713334134616, 1, 1732084210201, 1);
INSERT INTO `global_dict_type` VALUES (3, '菜单类型', 'menu_type', 1732346523788, 1, 1732346523788, 1);
INSERT INTO `global_dict_type` VALUES (4, '是否禁用', 'common_disable', 1713334134616, 1, 1732084443416, 1);
INSERT INTO `global_dict_type` VALUES (5, '任务状态', 'sys_job_status', 1713334134616, 1, NULL, 1);
INSERT INTO `global_dict_type` VALUES (6, '任务分组', 'sys_job_group', 1713334134616, 1, NULL, 1);
INSERT INTO `global_dict_type` VALUES (7, '系统是否', 'sys_yes_no', 1713334134616, 1, NULL, 1);
INSERT INTO `global_dict_type` VALUES (8, '消息类型', 'msg_type', 1713334134616, 1, 1734316213003, 1);
INSERT INTO `global_dict_type` VALUES (9, '消息状态', 'msg_status', 1713334134616, 1, 1734316217169, 1);
INSERT INTO `global_dict_type` VALUES (10, '操作类型', 'sys_oper_type', 1713334134616, 1, 1732083989997, 1);
INSERT INTO `global_dict_type` VALUES (11, '操作状态', 'sys_oper_status', 1713334134616, 1, 1732083226583, 1);
INSERT INTO `global_dict_type` VALUES (12, '授权类型', 'authorization_grant_types', 1713334134616, 1, 1724138651758, 1);
INSERT INTO `global_dict_type` VALUES (13, '设备连接状态', 'online_status', 1713334134616, 1, 1733714888104, 1);
INSERT INTO `global_dict_type` VALUES (14, '支付认证状态', 'pay_certification_status', 1713334134616, 1, NULL, 1);
INSERT INTO `global_dict_type` VALUES (15, '数据权限', 'data_scope', 1713334134616, 1, 1732084039645, 1);
INSERT INTO `global_dict_type` VALUES (16, '支付状态', 'pay_status', 1713334134616, 1, 1732082605137, 1);
INSERT INTO `global_dict_type` VALUES (17, '支付方式', 'pay_type', 1713334134616, 1, 1732084044525, 1);
INSERT INTO `global_dict_type` VALUES (18, '协议类型', 'protocol_type', 1713334134616, 1, 1732083004677, 1);
INSERT INTO `global_dict_type` VALUES (19, '协议语言', 'protocol_language', 1732083121999, 1, 1732083121999, 1);
INSERT INTO `global_dict_type` VALUES (20, '钱包记录类型', 'wallet_record_type', 1732523540360, 1, 1732523540360, 1);
INSERT INTO `global_dict_type` VALUES (21, '设备指令类型', 'command_type', 1734654585314, 1, 1734771173275, 1);
INSERT INTO `global_dict_type` VALUES (22, '产品类型', 'product_type', 1734771009279, 1, 1734771009279, 1);
INSERT INTO `global_dict_type` VALUES (23, '物模型类型', 'things_model_type', 1735004709344, 1, 1735004709344, 1);
INSERT INTO `global_dict_type` VALUES (24, '物模型数据类型', 'things_model_datatype', 1735007240157, 1, 1735007240157, 1);

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `config_id` bigint NOT NULL COMMENT '参数主键',
  `config_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '参数名称',
  `config_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '参数键名',
  `config_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'Y' COMMENT '系统内置（Y是 N否）',
  `create_at` bigint NOT NULL COMMENT '创建时间',
  `create_id` bigint NOT NULL COMMENT '创建者',
  `update_at` bigint NULL DEFAULT NULL COMMENT '更新时间',
  `update_id` bigint NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`config_id`) USING BTREE,
  UNIQUE INDEX `uk_config_key`(`config_key` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '参数配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 1713334134616, 1, 1719036436632, 1);

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log`  (
  `log_id` bigint NOT NULL,
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `grant_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '授权类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '登录状态（0正常 1异常）',
  `error_msg` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '失败信息',
  `login_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '登录ip',
  `login_location` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '登录地址',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '浏览器类型',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作系统',
  `login_time` bigint NOT NULL COMMENT '登录时间',
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '后台登录日志' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_login_log
-- ----------------------------
INSERT INTO `sys_login_log` VALUES (1878725689120645122, 0, 1, 'admin', 'password', '0', '', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1736758083971);
INSERT INTO `sys_login_log` VALUES (1878726101143904257, 0, 1, 'admin', 'password', '1', '密码错误', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1736758182145);
INSERT INTO `sys_login_log` VALUES (1878965120473382914, 0, 1, 'admin', 'password', '0', '', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1736815168193);
INSERT INTO `sys_login_log` VALUES (1879068177995546626, 0, 1879065533604380673, 'aaaaaa', 'password', '0', '', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1736839739588);
INSERT INTO `sys_login_log` VALUES (1879068390428655617, 0, 1, 'admin', 'password', '0', '', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1736839790319);
INSERT INTO `sys_login_log` VALUES (1879069035697160193, 0, 1879065533604380673, 'aaaaaa', 'password', '1', '登录失败，用户名或密码错误', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1736839944181);
INSERT INTO `sys_login_log` VALUES (1879069062951747585, 0, 1879065533604380673, 'aaaaaa', 'password', '1', '登录失败，用户名或密码错误', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1736839950663);
INSERT INTO `sys_login_log` VALUES (1879069239200595970, 0, 1879065533604380673, 'aaaaaa', 'password', '1', '登录失败，用户名或密码错误', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1736839992696);
INSERT INTO `sys_login_log` VALUES (1879069272524341250, 0, 1879065533604380673, 'aaaaaa', 'password', '1', '登录失败，用户名或密码错误', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1736840000642);
INSERT INTO `sys_login_log` VALUES (1879069621209415681, 0, 1879065533604380673, 'aaaaaa', 'password', '1', '登录失败，用户名或密码错误', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1736840083772);
INSERT INTO `sys_login_log` VALUES (1879069711890268162, 0, 1879065533604380673, 'aaaaaa', 'password', '1', '账号被禁用，请联系管理员', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1736840105393);
INSERT INTO `sys_login_log` VALUES (1879069743938945025, 0, 1, 'admin', 'password', '0', '', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1736840113042);
INSERT INTO `sys_login_log` VALUES (1879069960931262465, 0, 1879065533604380673, 'aaaaaa', 'password', '0', '', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1736840164756);
INSERT INTO `sys_login_log` VALUES (1879072794057785345, 1774671331416821762, 1774671331412627456, 'testtt', 'password', '0', '', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1736840840189);
INSERT INTO `sys_login_log` VALUES (1879078669031981058, 0, 1, 'admin', 'password', '0', '', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1736842240932);
INSERT INTO `sys_login_log` VALUES (1879079137745453058, 1774671331416821762, 1774671331412627456, 'testtt', 'password', '0', '', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1736842352694);
INSERT INTO `sys_login_log` VALUES (1879080785192566785, 1774671331416821762, 1856251200466030593, '___sub_', 'password', '1', '登录失败，用户名或密码错误', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1736842745479);
INSERT INTO `sys_login_log` VALUES (1879080802523430913, 1774671331416821762, 1856251200466030593, '___sub_', 'password', '1', '登录失败，用户名或密码错误', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1736842749596);
INSERT INTO `sys_login_log` VALUES (1879080862518755329, 1774671331416821762, 1856251200466030593, '___sub_', 'password', '0', '', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1736842763911);
INSERT INTO `sys_login_log` VALUES (1879081108212695041, 1774671331416821762, 1856251200466030593, '___sub_', 'password', '0', '', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1736842822493);
INSERT INTO `sys_login_log` VALUES (1879335021226659841, 0, 1, 'admin', 'password', '0', '', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1736903357848);
INSERT INTO `sys_login_log` VALUES (1879335080223739905, 1774671331416821762, 1774671331412627456, 'testtt', 'password', '0', '', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1736903374125);
INSERT INTO `sys_login_log` VALUES (1879343620673933313, 1774671331416821762, 1856251200466030593, '___sub_', 'password', '0', '', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1736905410334);
INSERT INTO `sys_login_log` VALUES (1879343860781060098, 1774671331416821762, 1774671331412627456, 'testtt', 'password', '0', '', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1736905467579);
INSERT INTO `sys_login_log` VALUES (1879344051051466753, 1774671331416821762, 1856251200466030593, '___sub_', 'password', '0', '', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1736905512944);
INSERT INTO `sys_login_log` VALUES (1879345755956350977, 1774671331416821762, 1856251200466030593, '___sub_', 'password', '0', '', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1736905919430);
INSERT INTO `sys_login_log` VALUES (1879347169319358466, 0, 2, 'wzkris', 'password', '0', '', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1736906256387);
INSERT INTO `sys_login_log` VALUES (1879350439844679681, 0, 1, 'admin', 'password', '0', '', '127.0.0.1', '内网IP', 'MSEdge', 'Windows 10 or Windows Server 2016', 1736907036158);

-- ----------------------------
-- Table structure for sys_message
-- ----------------------------
DROP TABLE IF EXISTS `sys_message`;
CREATE TABLE `sys_message`  (
  `msg_id` bigint NOT NULL COMMENT '消息ID',
  `title` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息标题',
  `msg_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息类型（1系统公告 2APP公告）',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息内容',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT '消息状态（0草稿 1关闭 2已发送）',
  `create_at` bigint NOT NULL COMMENT '创建者',
  `create_id` bigint NOT NULL COMMENT '创建时间',
  `update_at` bigint NULL DEFAULT NULL COMMENT '更新者',
  `update_id` bigint NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`msg_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统消息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_message
-- ----------------------------
INSERT INTO `sys_message` VALUES (1867761637258874881, '测试标题', '2', '测试内容xxxx', '2', 1734144050316, 1, 1734144050316, 1);
INSERT INTO `sys_message` VALUES (1867761817215488002, '测试标题5', '2', '内容aauuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuqqqq', '2', 1734144093219, 1, 1734144093219, 1);
INSERT INTO `sys_message` VALUES (1867761817215488005, '测试通知7', '1', '测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测测', '2', 1734144094219, 1, 1734144093219, 1);
INSERT INTO `sys_message` VALUES (1868480718916243457, '冲钱充钱', '1', '<h1>充充充充充充充充充充充充充充充充充充充充充充充充充充充充充</h1>', '2', 1734315492740, 1, 1735376394791, 1);

-- ----------------------------
-- Table structure for sys_notify
-- ----------------------------
DROP TABLE IF EXISTS `sys_notify`;
CREATE TABLE `sys_notify`  (
  `notify_id` bigint NOT NULL,
  `notify_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '通知类型（0系统通知 1设备告警）',
  `title` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标题',
  `content` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '通知内容',
  `create_at` bigint NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`notify_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统通知表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_notify
-- ----------------------------
INSERT INTO `sys_notify` VALUES (1854445788424261634, '0', '测试通知', '1111', 1730969304511);
INSERT INTO `sys_notify` VALUES (1873884393595662338, '0', '重要通知', 'zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz', 1735603829184);
INSERT INTO `sys_notify` VALUES (1873884472104644610, '0', '重要通知', 'zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz', 1735603847897);
INSERT INTO `sys_notify` VALUES (1873884863462567938, '0', '重要通知', 'zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz', 1735603941202);
INSERT INTO `sys_notify` VALUES (1879056739268706306, '0', '租户创建成功', '租户：1111创建成功，登录账号：1111，临时登录密码：36770974，临时操作密码：676482', 1736837012452);
INSERT INTO `sys_notify` VALUES (1879065533864361986, '0', '账号创建成功', '用户：aaaaaa创建成功，临时登录密码：66485484', 1736839109279);
INSERT INTO `sys_notify` VALUES (1879343474221420545, '0', '系统用户创建成功', '用户账号：xxxxxx创建成功，临时登录密码：60043090', 1736905375411);

-- ----------------------------
-- Table structure for sys_notify_send
-- ----------------------------
DROP TABLE IF EXISTS `sys_notify_send`;
CREATE TABLE `sys_notify_send`  (
  `notify_id` bigint NOT NULL COMMENT '系统消息ID',
  `user_id` bigint NOT NULL COMMENT '接收用户ID',
  `read_state` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '0' COMMENT '已读1 未读0',
  PRIMARY KEY (`notify_id`, `user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '通知发送表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_notify_send
-- ----------------------------
INSERT INTO `sys_notify_send` VALUES (1854445788424261634, 1, '0');
INSERT INTO `sys_notify_send` VALUES (1873884393595662338, 1, '0');
INSERT INTO `sys_notify_send` VALUES (1873884472104644610, 1, '1');
INSERT INTO `sys_notify_send` VALUES (1873884863462567938, 1, '1');
INSERT INTO `sys_notify_send` VALUES (1879056739268706306, 1, '0');
INSERT INTO `sys_notify_send` VALUES (1879065533864361986, 1, '1');
INSERT INTO `sys_notify_send` VALUES (1879343474221420545, 1774671331412627456, '0');

-- ----------------------------
-- Table structure for sys_oper_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log`  (
  `oper_id` bigint NOT NULL COMMENT '日志主键',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `title` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '模块标题',
  `sub_title` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '子标题',
  `oper_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作类型（0其他 1新增 2修改 3删除）',
  `method` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '请求方式',
  `user_id` bigint NOT NULL COMMENT '操作人员ID',
  `oper_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作人员',
  `oper_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作地点',
  `oper_param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '请求参数',
  `json_result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '返回参数',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作状态（0正常 1异常）',
  `error_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '错误消息',
  `oper_time` bigint NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`oper_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '操作日志记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_oper_log
-- ----------------------------
INSERT INTO `sys_oper_log` VALUES (1877963169153585154, 0, '后台管理', '修改用户', '2', 'com.wzkris.user.controller.SysUserController.edit()', 'POST', 1, 'admin', '/sys_user/edit', '127.0.0.1', '内网IP', '{\"roleIds\":[2],\"gender\":\"0\",\"postIds\":[1],\"deptId\":100,\"nickname\":\"nick_kris\",\"userId\":2,\"email\":\"111111@1.com\",\"username\":\"wzkris\",\"status\":\"0\"}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736576284910}', '0', NULL, 1736576284910);
INSERT INTO `sys_oper_log` VALUES (1878984731398914049, 0, '角色管理', '批量用户授权', '4', 'com.wzkris.user.controller.SysRoleController.batchAuth()', 'POST', 1, 'admin', '/sys_role/authorize_user', '127.0.0.1', '内网IP', '{\"roleId\":1858701878891327490,\"userIds\":[2]}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736819844423}', '0', NULL, 1736819844427);
INSERT INTO `sys_oper_log` VALUES (1878984767071469570, 0, '角色管理', '取消授权', '4', 'com.wzkris.user.controller.SysRoleController.cancelAuth()', 'POST', 1, 'admin', '/sys_role/authorize/cancel', '127.0.0.1', '内网IP', '{\"roleId\":1858701878891327490,\"userId\":2}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736819852971}', '0', NULL, 1736819852972);
INSERT INTO `sys_oper_log` VALUES (1878985433231802369, 0, '角色管理', '新增角色', '1', 'com.wzkris.user.controller.SysRoleController.add()', 'POST', 1, 'admin', '/sys_role/add', '127.0.0.1', '内网IP', '{\"isMenuDisplay\":true,\"roleName\":\"123123\",\"isDeptDisplay\":true,\"deptIds\":[],\"menuIds\":[],\"roleSort\":0}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736820011800}', '0', NULL, 1736820011800);
INSERT INTO `sys_oper_log` VALUES (1878985443369435138, 0, '角色管理', '新增角色', '1', 'com.wzkris.user.controller.SysRoleController.add()', 'POST', 1, 'admin', '/sys_role/add', '127.0.0.1', '内网IP', '{\"isMenuDisplay\":true,\"roleName\":\"123\",\"isDeptDisplay\":true,\"deptIds\":[],\"menuIds\":[],\"roleSort\":0}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736820014217}', '0', NULL, 1736820014217);
INSERT INTO `sys_oper_log` VALUES (1878986055477772290, 0, '角色管理', '删除角色', '3', 'com.wzkris.user.controller.SysRoleController.remove()', 'POST', 1, 'admin', '/sys_role/remove', '127.0.0.1', '内网IP', '[1878985433164722178]', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736820102512}', '0', NULL, 1736820102512);
INSERT INTO `sys_oper_log` VALUES (1878986238873714689, 0, '角色管理', '删除角色', '3', 'com.wzkris.user.controller.SysRoleController.remove()', 'POST', 1, 'admin', '/sys_role/remove', '127.0.0.1', '内网IP', '[1878985443293966338]', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736820198461}', '0', NULL, 1736820198467);
INSERT INTO `sys_oper_log` VALUES (1878986473595355138, 0, '岗位管理', '新增岗位', '1', 'com.wzkris.user.controller.SysPostController.add()', 'POST', 1, 'admin', '/sys_post/add', '127.0.0.1', '内网IP', '{\"postSort\":0,\"postName\":\"111\",\"status\":\"0\"}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736820259836}', '0', NULL, 1736820259836);
INSERT INTO `sys_oper_log` VALUES (1878986484710260737, 0, '岗位管理', '新增岗位', '1', 'com.wzkris.user.controller.SysPostController.add()', 'POST', 1, 'admin', '/sys_post/add', '127.0.0.1', '内网IP', '{\"postSort\":0,\"postName\":\"1111\",\"status\":\"0\"}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736820262492}', '0', NULL, 1736820262492);
INSERT INTO `sys_oper_log` VALUES (1878986516897349634, 0, '岗位管理', '删除岗位', '3', 'com.wzkris.user.controller.SysPostController.remove()', 'POST', 1, 'admin', '/sys_post/remove', '127.0.0.1', '内网IP', '[1878986473524035585,1878986484630552578]', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736820266892}', '0', NULL, 1736820266892);
INSERT INTO `sys_oper_log` VALUES (1878986736771153922, 0, '部门管理', '修改部门', '2', 'com.wzkris.user.controller.SysDeptController.edit()', 'POST', 1, 'admin', '/sys_dept/edit', '127.0.0.1', '内网IP', '{\"deptSort\":0,\"deptName\":\"最高部门\",\"contact\":\"15888888888\",\"deptId\":100,\"tenantId\":0,\"ancestors\":\"0\",\"parentId\":0,\"email\":\"\",\"status\":\"0\"}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736820295452}', '0', NULL, 1736820295452);
INSERT INTO `sys_oper_log` VALUES (1878988058882883586, 0, '部门管理', '修改部门', '2', 'com.wzkris.user.controller.SysDeptController.edit()', 'POST', 1, 'admin', '/sys_dept/edit', '127.0.0.1', '内网IP', '{\"deptSort\":0,\"deptName\":\"最高部门\",\"contact\":\"15888888888\",\"deptId\":100,\"tenantId\":0,\"ancestors\":\"0\",\"parentId\":0,\"email\":\"\",\"status\":\"0\"}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736820637760}', '0', NULL, 1736820637761);
INSERT INTO `sys_oper_log` VALUES (1878988117913518082, 0, '岗位管理', '新增岗位', '1', 'com.wzkris.user.controller.SysPostController.add()', 'POST', 1, 'admin', '/sys_post/add', '127.0.0.1', '内网IP', '{\"postSort\":0,\"postName\":\"111\",\"status\":\"0\"}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736820651861}', '0', NULL, 1736820651861);
INSERT INTO `sys_oper_log` VALUES (1878988129095532545, 0, '岗位管理', '新增岗位', '1', 'com.wzkris.user.controller.SysPostController.add()', 'POST', 1, 'admin', '/sys_post/add', '127.0.0.1', '内网IP', '{\"postSort\":0,\"postName\":\"111\",\"status\":\"0\"}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736820654544}', '0', NULL, 1736820654544);
INSERT INTO `sys_oper_log` VALUES (1878988143423275010, 0, '岗位管理', '删除岗位', '3', 'com.wzkris.user.controller.SysPostController.remove()', 'POST', 1, 'admin', '/sys_post/remove', '127.0.0.1', '内网IP', '{}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736820657961}', '0', NULL, 1736820657961);
INSERT INTO `sys_oper_log` VALUES (1878988205008240641, 0, '岗位管理', '新增岗位', '1', 'com.wzkris.user.controller.SysPostController.add()', 'POST', 1, 'admin', '/sys_post/add', '127.0.0.1', '内网IP', '{\"postSort\":0,\"postName\":\"11\",\"status\":\"0\"}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736820672637}', '0', NULL, 1736820672637);
INSERT INTO `sys_oper_log` VALUES (1878988213870804993, 0, '岗位管理', '新增岗位', '1', 'com.wzkris.user.controller.SysPostController.add()', 'POST', 1, 'admin', '/sys_post/add', '127.0.0.1', '内网IP', '{\"postSort\":0,\"postName\":\"11\",\"status\":\"0\"}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736820674758}', '0', NULL, 1736820674758);
INSERT INTO `sys_oper_log` VALUES (1878988812167299073, 0, '岗位管理', '删除岗位', '3', 'com.wzkris.user.controller.SysPostController.remove()', 'POST', 1, 'admin', '/sys_post/remove', '127.0.0.1', '内网IP', '[1878988204932780033,1878988213807927297]', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736820817345}', '0', NULL, 1736820817350);
INSERT INTO `sys_oper_log` VALUES (1878991661873905666, 0, '菜单管理', '修改菜单', '2', 'com.wzkris.user.controller.SysMenuController.edit()', 'POST', 1, 'admin', '/sys_menu/edit', '127.0.0.1', '内网IP', '{\"path\":\"#\",\"isCache\":false,\"isFrame\":false,\"icon\":\"#\",\"menuId\":1129,\"menuName\":\"重置租户操作密码\",\"menuType\":\"B\",\"perms\":\"tenant:reset_operpwd\",\"isVisible\":true,\"parentId\":601,\"menuSort\":11,\"status\":\"0\"}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736821496833}', '0', NULL, 1736821496833);
INSERT INTO `sys_oper_log` VALUES (1878992217501745153, 0, '租户管理', '重置操作密码', '2', 'com.wzkris.user.controller.SysTenantController.resetOperPwd()', 'POST', 1, 'admin', '/sys_tenant/reset_operpwd', '127.0.0.1', '内网IP', '', '{\"code\":1,\"message\":\"操作密码必须为6位数字\",\"timestamp\":1736821629256}', '1', '操作密码必须为6位数字', 1736821629261);
INSERT INTO `sys_oper_log` VALUES (1878992269699858434, 0, '租户管理', '重置操作密码', '2', 'com.wzkris.user.controller.SysTenantController.resetOperPwd()', 'POST', 1, 'admin', '/sys_tenant/reset_operpwd', '127.0.0.1', '内网IP', '', '{\"code\":1,\"message\":\"操作密码必须为6位数字\",\"timestamp\":1736821641735}', '1', '操作密码必须为6位数字', 1736821641735);
INSERT INTO `sys_oper_log` VALUES (1878995390715019265, 0, '租户管理', '重置操作密码', '2', 'com.wzkris.user.controller.SysTenantController.resetOperPwd()', 'POST', 1, 'admin', '/sys_tenant/reset_operpwd', '127.0.0.1', '内网IP', '{\"password\":\"**\",\"id\":1853719125330489346}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736822385846}', '0', NULL, 1736822385847);
INSERT INTO `sys_oper_log` VALUES (1878996024373690369, 0, '租户管理', '重置操作密码', '2', 'com.wzkris.user.controller.SysTenantController.resetOperPwd()', 'POST', 1, 'admin', '/sys_tenant/reset_operpwd', '127.0.0.1', '内网IP', '{\"password\":\"**\",\"id\":1853719125330489346}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736822536925}', '0', NULL, 1736822536925);
INSERT INTO `sys_oper_log` VALUES (1878996100881989633, 0, '租户管理', '重置操作密码', '2', 'com.wzkris.user.controller.SysTenantController.resetOperPwd()', 'POST', 1, 'admin', '/sys_tenant/reset_operpwd', '127.0.0.1', '内网IP', '{\"password\":\"**\",\"id\":1853719125330489346}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736822555162}', '0', NULL, 1736822555164);
INSERT INTO `sys_oper_log` VALUES (1878996785455316994, 0, '租户管理', '重置操作密码', '2', 'com.wzkris.user.controller.SysTenantController.resetOperPwd()', 'POST', 1, 'admin', '/sys_tenant/reset_operpwd', '127.0.0.1', '内网IP', '{\"password\":\"**\",\"id\":1853719125330489346}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736822718377}', '0', NULL, 1736822718377);
INSERT INTO `sys_oper_log` VALUES (1878996887423041538, 0, '租户管理', '重置操作密码', '2', 'com.wzkris.user.controller.SysTenantController.resetOperPwd()', 'POST', 1, 'admin', '/sys_tenant/reset_operpwd', '127.0.0.1', '内网IP', '{\"password\":\"**\",\"id\":1853719125330489346}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736822742689}', '0', NULL, 1736822742689);
INSERT INTO `sys_oper_log` VALUES (1878997168202334210, 0, '后台管理', '重置密码', '2', 'com.wzkris.user.controller.SysUserController.resetPwd()', 'POST', 1, 'admin', '/sys_user/reset_password', '127.0.0.1', '内网IP', '{\"password\":\"**\",\"id\":1856251200466030593}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736822809630}', '0', NULL, 1736822809630);
INSERT INTO `sys_oper_log` VALUES (1879034323901497346, 0, '后台管理', '修改用户', '2', 'com.wzkris.user.controller.SysUserController.edit()', 'POST', 1, 'admin', '/sys_user/edit', '127.0.0.1', '内网IP', '{\"roleIds\":[1775445330027577345],\"gender\":\"2\",\"postIds\":[],\"deptId\":1775382319191453698,\"nickname\":\"xxxxxx\",\"userId\":1856251200466030593,\"username\":\"___sub_\",\"status\":\"0\"}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736831668211}', '0', NULL, 1736831668211);
INSERT INTO `sys_oper_log` VALUES (1879056027897970690, 0, '租户管理', '修改租户', '2', 'com.wzkris.user.controller.SysTenantController.edit()', 'POST', 1, 'admin', '/sys_tenant/edit', '127.0.0.1', '内网IP', '{\"tenantType\":\"0\",\"expireTime\":1733896268000,\"accountLimit\":5,\"roleLimit\":5,\"tenantName\":\"租户2\",\"tenantId\":1853719125330489346,\"packageId\":1773620875265482754,\"deptLimit\":5,\"postLimit\":5,\"remark\":\"第二个\",\"contactPhone\":\"00\",\"status\":\"1\"}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736836842790}', '0', NULL, 1736836842791);
INSERT INTO `sys_oper_log` VALUES (1879056738295627778, 0, '租户管理', '新增租户', '1', 'com.wzkris.user.controller.SysTenantController.add()', 'POST', 1, 'admin', '/sys_tenant/add', '127.0.0.1', '内网IP', '{\"expireTime\":-1,\"tenantName\":\"1111\",\"packageId\":1773625804122202113,\"contactPhone\":\"1111\",\"status\":\"0\",\"username\":\"1111\"}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736837012258}', '0', NULL, 1736837012258);
INSERT INTO `sys_oper_log` VALUES (1879059894253535234, 0, '租户管理', '新增租户', '1', 'com.wzkris.user.controller.SysTenantController.add()', 'POST', 1, 'admin', '/sys_tenant/add', '127.0.0.1', '内网IP', '{\"expireTime\":-1,\"tenantName\":\"222\",\"packageId\":1773625804122202113,\"status\":\"0\",\"username\":\"222\"}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736837764652}', '0', NULL, 1736837764652);
INSERT INTO `sys_oper_log` VALUES (1879065533864361985, 0, '后台管理', '新增用户', '1', 'com.wzkris.user.controller.SysUserController.add()', 'POST', 1, 'admin', '/sys_user/add', '127.0.0.1', '内网IP', '{\"roleIds\":[],\"postIds\":[],\"username\":\"aaaaaa\"}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736839109236}', '0', NULL, 1736839109237);
INSERT INTO `sys_oper_log` VALUES (1879065889595867138, 0, '系统账户', '', '2', 'com.wzkris.user.controller.SysUserOwnController.editPwd()', 'POST', 1, 'admin', '/user/account/edit_password', '127.0.0.1', '内网IP', '{\"oldPassword\":\"**\",\"newPassword\":\"**\"}', '{\"code\":1,\"message\":\"修改密码失败，旧密码错误\",\"timestamp\":1736839194092}', '1', '修改密码失败，旧密码错误', 1736839194092);
INSERT INTO `sys_oper_log` VALUES (1879065918331043841, 0, '系统账户', '', '2', 'com.wzkris.user.controller.SysUserOwnController.editPwd()', 'POST', 1, 'admin', '/user/account/edit_password', '127.0.0.1', '内网IP', '{\"oldPassword\":\"**\",\"newPassword\":\"**\"}', '{\"code\":1,\"message\":\"新密码不能包含空格\",\"timestamp\":1736839200939}', '1', '新密码不能包含空格', 1736839200939);
INSERT INTO `sys_oper_log` VALUES (1879065927751450625, 0, '系统账户', '', '2', 'com.wzkris.user.controller.SysUserOwnController.editPwd()', 'POST', 1, 'admin', '/user/account/edit_password', '127.0.0.1', '内网IP', '{\"oldPassword\":\"**\",\"newPassword\":\"**\"}', '{\"code\":1,\"message\":\"新密码不能包含空格\",\"timestamp\":1736839203185}', '1', '新密码不能包含空格', 1736839203185);
INSERT INTO `sys_oper_log` VALUES (1879068282488242178, 0, '系统账户', '', '2', 'com.wzkris.user.controller.SysUserOwnController.editPwd()', 'POST', 1879065533604380673, 'aaaaaa', '/user/account/edit_password', '127.0.0.1', '内网IP', '{\"oldPassword\":\"**\",\"newPassword\":\"**\"}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736839764608}', '0', NULL, 1736839764608);
INSERT INTO `sys_oper_log` VALUES (1879068914691489794, 0, '租户管理', '删除租户', '3', 'com.wzkris.user.controller.SysTenantController.remove()', 'POST', 1, 'admin', '/sys_tenant/remove', '127.0.0.1', '内网IP', '[1879056737943396354]', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736839915326}', '0', NULL, 1736839915327);
INSERT INTO `sys_oper_log` VALUES (1879068924007038977, 0, '租户管理', '删除租户', '3', 'com.wzkris.user.controller.SysTenantController.remove()', 'POST', 1, 'admin', '/sys_tenant/remove', '127.0.0.1', '内网IP', '[1879059893762899969]', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736839917546}', '0', NULL, 1736839917547);
INSERT INTO `sys_oper_log` VALUES (1879068982572105730, 0, '后台管理', '状态修改', '2', 'com.wzkris.user.controller.SysUserController.editStatus()', 'POST', 1, 'admin', '/sys_user/edit_status', '127.0.0.1', '内网IP', '{\"id\":1879065533604380673,\"status\":\"1\"}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736839931499}', '0', NULL, 1736839931499);
INSERT INTO `sys_oper_log` VALUES (1879069852865019905, 0, '后台管理', '状态修改', '2', 'com.wzkris.user.controller.SysUserController.editStatus()', 'POST', 1, 'admin', '/sys_user/edit_status', '127.0.0.1', '内网IP', '{\"id\":1879065533604380673,\"status\":\"0\"}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736840139009}', '0', NULL, 1736840139009);
INSERT INTO `sys_oper_log` VALUES (1879070021035638785, 0, '系统账户', '', '2', 'com.wzkris.user.controller.SysUserOwnController.editPwd()', 'POST', 1879065533604380673, 'aaaaaa', '/user/account/edit_password', '127.0.0.1', '内网IP', '{\"oldPassword\":\"**\",\"newPassword\":\"**\"}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736840179112}', '0', NULL, 1736840179112);
INSERT INTO `sys_oper_log` VALUES (1879070403480666113, 0, '系统账户', '', '2', 'com.wzkris.user.controller.SysUserOwnController.editPwd()', 'POST', 1879065533604380673, 'aaaaaa', '/user/account/edit_password', '127.0.0.1', '内网IP', '{\"oldPassword\":\"**\",\"newPassword\":\"**\"}', '{\"code\":1,\"message\":\"修改密码失败，旧密码错误\",\"timestamp\":1736840270280}', '1', '修改密码失败，旧密码错误', 1736840270280);
INSERT INTO `sys_oper_log` VALUES (1879070436183654402, 0, '系统账户', '', '2', 'com.wzkris.user.controller.SysUserOwnController.editPwd()', 'POST', 1879065533604380673, 'aaaaaa', '/user/account/edit_password', '127.0.0.1', '内网IP', '{\"oldPassword\":\"**\",\"newPassword\":\"**\"}', '{\"code\":1,\"message\":\"修改密码失败，旧密码错误\",\"timestamp\":1736840278084}', '1', '修改密码失败，旧密码错误', 1736840278084);
INSERT INTO `sys_oper_log` VALUES (1879070511110701058, 0, '系统账户', '', '2', 'com.wzkris.user.controller.SysUserOwnController.editPwd()', 'POST', 1879065533604380673, 'aaaaaa', '/user/account/edit_password', '127.0.0.1', '内网IP', '{\"oldPassword\":\"**\",\"newPassword\":\"**\"}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736840295943}', '0', NULL, 1736840295943);
INSERT INTO `sys_oper_log` VALUES (1879070972651913217, 0, '系统账户', '', '2', 'com.wzkris.user.controller.SysUserOwnController.editPwd()', 'POST', 1879065533604380673, 'aaaaaa', '/user/account/edit_password', '127.0.0.1', '内网IP', '{\"oldPassword\":\"**\",\"newPassword\":\"**\"}', '{\"code\":1,\"message\":\"新密码长度至少要8位且不能包含空格\",\"timestamp\":1736840405927}', '1', '新密码长度至少要8位且不能包含空格', 1736840405932);
INSERT INTO `sys_oper_log` VALUES (1879070986094657538, 0, '系统账户', '', '2', 'com.wzkris.user.controller.SysUserOwnController.editPwd()', 'POST', 1879065533604380673, 'aaaaaa', '/user/account/edit_password', '127.0.0.1', '内网IP', '{\"oldPassword\":\"**\",\"newPassword\":\"**\"}', '{\"code\":1,\"message\":\"修改密码失败，旧密码错误\",\"timestamp\":1736840409192}', '1', '修改密码失败，旧密码错误', 1736840409192);
INSERT INTO `sys_oper_log` VALUES (1879071004079833090, 0, '系统账户', '', '2', 'com.wzkris.user.controller.SysUserOwnController.editPwd()', 'POST', 1879065533604380673, 'aaaaaa', '/user/account/edit_password', '127.0.0.1', '内网IP', '{\"oldPassword\":\"**\",\"newPassword\":\"**\"}', '{\"code\":1,\"message\":\"修改密码失败，旧密码错误\",\"timestamp\":1736840413480}', '1', '修改密码失败，旧密码错误', 1736840413480);
INSERT INTO `sys_oper_log` VALUES (1879071213765672962, 0, '系统账户', '', '2', 'com.wzkris.user.controller.SysUserOwnController.editPwd()', 'POST', 1879065533604380673, 'aaaaaa', '/user/account/edit_password', '127.0.0.1', '内网IP', '{\"oldPassword\":\"**\",\"newPassword\":\"**\"}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736840463468}', '0', NULL, 1736840463468);
INSERT INTO `sys_oper_log` VALUES (1879071247554985986, 0, '系统账户', '', '2', 'com.wzkris.user.controller.SysUserOwnController.editPwd()', 'POST', 1879065533604380673, 'aaaaaa', '/user/account/edit_password', '127.0.0.1', '内网IP', '{\"oldPassword\":\"**\",\"newPassword\":\"**\"}', '{\"code\":1,\"message\":\"新密码长度至少要8位且不能包含空格\",\"timestamp\":1736840471512}', '1', '新密码长度至少要8位且不能包含空格', 1736840471512);
INSERT INTO `sys_oper_log` VALUES (1879071259856879618, 0, '系统账户', '', '2', 'com.wzkris.user.controller.SysUserOwnController.editPwd()', 'POST', 1879065533604380673, 'aaaaaa', '/user/account/edit_password', '127.0.0.1', '内网IP', '{\"oldPassword\":\"**\",\"newPassword\":\"**\"}', '{\"code\":1,\"message\":\"修改密码失败，旧密码错误\",\"timestamp\":1736840474445}', '1', '修改密码失败，旧密码错误', 1736840474445);
INSERT INTO `sys_oper_log` VALUES (1879071282564841473, 0, '系统账户', '', '2', 'com.wzkris.user.controller.SysUserOwnController.editPwd()', 'POST', 1879065533604380673, 'aaaaaa', '/user/account/edit_password', '127.0.0.1', '内网IP', '{\"oldPassword\":\"**\",\"newPassword\":\"**\"}', '{\"code\":1,\"message\":\"新密码不能与旧密码相同\",\"timestamp\":1736840479874}', '1', '新密码不能与旧密码相同', 1736840479874);
INSERT INTO `sys_oper_log` VALUES (1879071303955791873, 0, '系统账户', '', '2', 'com.wzkris.user.controller.SysUserOwnController.editPwd()', 'POST', 1879065533604380673, 'aaaaaa', '/user/account/edit_password', '127.0.0.1', '内网IP', '{\"oldPassword\":\"**\",\"newPassword\":\"**\"}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736840484973}', '0', NULL, 1736840484973);
INSERT INTO `sys_oper_log` VALUES (1879072047161294849, 0, '系统账户', '', '2', 'com.wzkris.user.controller.SysUserOwnController.editPwd()', 'POST', 1879065533604380673, 'aaaaaa', '/user/account/edit_password', '127.0.0.1', '内网IP', '{\"oldPassword\":\"**\",\"newPassword\":\"**\"}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736840662170}', '0', NULL, 1736840662170);
INSERT INTO `sys_oper_log` VALUES (1879072068766154754, 0, '系统账户', '', '2', 'com.wzkris.user.controller.SysUserOwnController.editPwd()', 'POST', 1879065533604380673, 'aaaaaa', '/user/account/edit_password', '127.0.0.1', '内网IP', '{\"oldPassword\":\"**\",\"newPassword\":\"**\"}', '{\"code\":1,\"message\":\"修改密码失败，旧密码错误\",\"timestamp\":1736840667321}', '1', '修改密码失败，旧密码错误', 1736840667321);
INSERT INTO `sys_oper_log` VALUES (1879072586729144322, 0, '系统账户', '', '2', 'com.wzkris.user.controller.SysUserOwnController.editPwd()', 'POST', 1879065533604380673, 'aaaaaa', '/user/account/edit_password', '127.0.0.1', '内网IP', '{\"oldPassword\":\"**\",\"newPassword\":\"**\"}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736840790809}', '0', NULL, 1736840790809);
INSERT INTO `sys_oper_log` VALUES (1879079105369620481, 0, '租户管理', '重置操作密码', '2', 'com.wzkris.user.controller.SysTenantController.resetOperPwd()', 'POST', 1, 'admin', '/sys_tenant/reset_operpwd', '127.0.0.1', '内网IP', '{\"password\":\"**\",\"id\":1774671331416821762}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736842344927}', '0', NULL, 1736842344927);
INSERT INTO `sys_oper_log` VALUES (1879080054771302401, 1774671331416821762, '租户信息', '修改操作密码', '2', 'com.wzkris.user.controller.SysTenantOwnController.editOperPwd()', 'POST', 1774671331412627456, 'testtt', '/tenant/edit_operpwd', '127.0.0.1', '内网IP', '{\"oldPassword\":\"**\",\"newPassword\":\"**\"}', '{\"code\":1,\"message\":\"修改密码失败，旧密码错误\",\"timestamp\":1736842571270}', '1', '修改密码失败，旧密码错误', 1736842571277);
INSERT INTO `sys_oper_log` VALUES (1879080076506185730, 1774671331416821762, '租户信息', '修改操作密码', '2', 'com.wzkris.user.controller.SysTenantOwnController.editOperPwd()', 'POST', 1774671331412627456, 'testtt', '/tenant/edit_operpwd', '127.0.0.1', '内网IP', '{\"oldPassword\":\"**\",\"newPassword\":\"**\"}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736842576515}', '0', NULL, 1736842576515);
INSERT INTO `sys_oper_log` VALUES (1879080317959684097, 1774671331416821762, '后台管理', '删除用户', '3', 'com.wzkris.user.controller.SysUserController.remove()', 'POST', 1774671331412627456, 'testtt', '/sys_user/remove', '127.0.0.1', '内网IP', '[1856869914760638466]', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736842634082}', '0', NULL, 1736842634082);
INSERT INTO `sys_oper_log` VALUES (1879080410657996801, 1774671331416821762, '后台管理', '重置密码', '2', 'com.wzkris.user.controller.SysUserController.resetPwd()', 'POST', 1774671331412627456, 'testtt', '/sys_user/reset_password', '127.0.0.1', '内网IP', '{\"password\":\"**\",\"id\":1774671331412627456}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736842656183}', '0', NULL, 1736842656183);
INSERT INTO `sys_oper_log` VALUES (1879339960753623041, 1774671331416821762, '后台管理', '修改用户', '2', 'com.wzkris.user.controller.SysUserController.edit()', 'POST', 1774671331412627456, 'testtt', '/sys_user/edit', '127.0.0.1', '内网IP', '{\"roleIds\":[1775445330027577345],\"gender\":\"2\",\"postIds\":[],\"deptId\":1775382319191453698,\"nickname\":\"xxxxxx\",\"userId\":1856251200466030593,\"username\":\"___sub_\",\"status\":\"0\"}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736904537669}', '0', NULL, 1736904537669);
INSERT INTO `sys_oper_log` VALUES (1879343474041065473, 1774671331416821762, '后台管理', '新增用户', '1', 'com.wzkris.user.controller.SysUserController.add()', 'POST', 1774671331412627456, 'testtt', '/sys_user/add', '127.0.0.1', '内网IP', '{\"deptId\":1775387364419072002,\"username\":\"xxxxxx\"}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736905375350}', '0', NULL, 1736905375352);
INSERT INTO `sys_oper_log` VALUES (1879343575140569090, 1774671331416821762, '后台管理', '重置密码', '2', 'com.wzkris.user.controller.SysUserController.resetPwd()', 'POST', 1774671331412627456, 'testtt', '/sys_user/reset_password', '127.0.0.1', '内网IP', '{\"password\":\"**\",\"id\":1856251200466030593}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736905399480}', '0', NULL, 1736905399480);
INSERT INTO `sys_oper_log` VALUES (1879343713959448578, 1774671331416821762, '部门管理', '修改部门', '2', 'com.wzkris.user.controller.SysDeptController.edit()', 'POST', 1856251200466030593, '___sub_', '/sys_dept/edit', '127.0.0.1', '内网IP', '{\"deptSort\":0,\"deptName\":\"默认租户部门\",\"deptId\":1775382319191453698,\"tenantId\":1774671331416821762,\"ancestors\":\"0\",\"parentId\":0,\"status\":\"0\"}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736905432576}', '0', NULL, 1736905432576);
INSERT INTO `sys_oper_log` VALUES (1879343944780386306, 1774671331416821762, '角色管理', '修改角色', '2', 'com.wzkris.user.controller.SysRoleController.edit()', 'POST', 1774671331412627456, 'testtt', '/sys_role/edit', '127.0.0.1', '内网IP', '{\"isMenuDisplay\":true,\"roleId\":1775445330027577345,\"roleName\":\"默认租户角色\",\"isDeptDisplay\":true,\"deptIds\":[],\"dataScope\":\"4\",\"menuIds\":[2,203,2071,205,2040,2039,2038,2037,208,2145,2144,2143,2142,2141],\"status\":\"0\",\"roleSort\":0}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736905487608}', '0', NULL, 1736905487608);
INSERT INTO `sys_oper_log` VALUES (1879345698922205186, 1774671331416821762, '角色管理', '修改角色', '2', 'com.wzkris.user.controller.SysRoleController.edit()', 'POST', 1774671331412627456, 'testtt', '/sys_role/edit', '127.0.0.1', '内网IP', '{\"isMenuDisplay\":true,\"roleId\":1775445330027577345,\"roleName\":\"默认租户角色\",\"isDeptDisplay\":true,\"deptIds\":[],\"dataScope\":\"4\",\"menuIds\":[2,203,206,2071,2207,2210,2209,2208,205,2040,2039,2038,2037,208,2145,2144,2143,2142,2141],\"status\":\"0\",\"roleSort\":0}', '{\"code\":0,\"message\":\"Success\",\"timestamp\":1736905905786}', '0', NULL, 1736905905788);
INSERT INTO `sys_oper_log` VALUES (1879347297866387458, 0, '角色管理', '批量用户授权', '4', 'com.wzkris.user.controller.SysRoleController.batchAuth()', 'POST', 2, 'wzkris', '/sys_role/authorize_user', '127.0.0.1', '内网IP', '{\"roleId\":4,\"userIds\":[2]}', NULL, '1', '无此角色数据访问权限', 1736906287001);
INSERT INTO `sys_oper_log` VALUES (1879349193607909377, 0, '后台管理', '状态修改', '2', 'com.wzkris.user.controller.SysRoleController.editStatus()', 'POST', 2, 'wzkris', '/sys_role/edit_status', '127.0.0.1', '内网IP', '{\"id\":4,\"status\":\"0\"}', NULL, '1', '无此角色数据访问权限', 1736906738979);
INSERT INTO `sys_oper_log` VALUES (1879350263432253442, 0, '后台管理', '状态修改', '2', 'com.wzkris.user.controller.SysRoleController.editStatus()', 'POST', 2, 'wzkris', '/sys_role/edit_status', '127.0.0.1', '内网IP', '{\"id\":4,\"status\":\"0\"}', NULL, '1', '无此角色数据访问权限', 1736906994094);

SET FOREIGN_KEY_CHECKS = 1;
