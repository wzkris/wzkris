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

 Date: 14/01/2025 08:42:56
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
INSERT INTO `sys_config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-yellow', 'Y', 1713334134616, 1, 1722905883482, 1);
INSERT INTO `sys_config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 1713334134616, 1, 1719036436632, 1);
INSERT INTO `sys_config` VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-light', 'Y', 1713334134616, 1, 1719036434181, 1);
INSERT INTO `sys_config` VALUES (4, '是否开启注册功能', 'sys.account.registerUser', 'false', 'Y', 1713334134616, 1, 1732351869588, 1);

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

SET FOREIGN_KEY_CHECKS = 1;
