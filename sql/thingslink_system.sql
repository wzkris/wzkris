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

 Date: 22/06/2024 13:36:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS thingslink_system;
USE thingslink_system;
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
INSERT INTO `sys_config` VALUES (1, '主框架页-默认皮肤样式名称', 'sys.index.skinName', 'skin-yellow', 'Y', 1713334134616, 1, 20230812105204, NULL);
INSERT INTO `sys_config` VALUES (2, '用户管理-账号初始密码', 'sys.user.initPassword', '123456', 'Y', 1713334134616, 1, 20230531110014, NULL);
INSERT INTO `sys_config` VALUES (3, '主框架页-侧边栏主题', 'sys.index.sideTheme', 'theme-light', 'Y', 1713334134616, 1, 20231201154324, NULL);
INSERT INTO `sys_config` VALUES (4, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false', 'Y', 1713334134616, 1, NULL, NULL);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_code` bigint NOT NULL COMMENT '字典编码',
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
  PRIMARY KEY (`dict_code`) USING BTREE,
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
  `dict_id` bigint NOT NULL COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典类型',
  `create_at` bigint NOT NULL COMMENT '创建时间',
  `create_id` bigint NOT NULL COMMENT '创建者',
  `update_at` bigint NULL DEFAULT NULL COMMENT '更新时间',
  `update_id` bigint NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `uk_dict_type`(`dict_type` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', 1713334134616, 1, 20230531105954, 1);
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
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job`  (
  `job_id` bigint NOT NULL COMMENT '任务ID',
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务组名',
  `invoke_target` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态（0正常 1暂停）',
  `create_at` bigint NOT NULL COMMENT '创建时间',
  `create_id` bigint NOT NULL COMMENT '创建者',
  `update_at` bigint NULL DEFAULT NULL COMMENT '更新时间',
  `update_id` bigint NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`job_id`, `job_name`, `job_group`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务调度表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_job
-- ----------------------------
INSERT INTO `sys_job` VALUES (2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')', '0/15 * * * * ?', '3', '1', '1', 1713334134616, 1, NULL, 1);
INSERT INTO `sys_job` VALUES (3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)', '0/20 * * * * ?', '3', '1', '1', 1713334134616, 1, 20230606111145, 1);

-- ----------------------------
-- Table structure for sys_job_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log`  (
  `job_log_id` bigint NOT NULL,
  `job_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务组名',
  `invoke_target` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用目标字符串',
  `job_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '日志信息',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行状态（0正常 1失败）',
  `exception_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '异常信息',
  `create_at` bigint NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务调度日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_job_log
-- ----------------------------
INSERT INTO `sys_job_log` VALUES (1769648108174245889, '刷新SameToken（每小时）', 'DEFAULT', 'sameTokenTask.refreshToken()', '刷新SameToken（每小时） 总共耗时：5毫秒', '1', 'NoSuchBeanDefinitionException: No bean named \'refreshToken\' available', 1713334134616);
INSERT INTO `sys_job_log` VALUES (1769649128296079362, '刷新SameToken（每小时）', 'DEFAULT', 'sameTokenTask.refreshToken()', '刷新SameToken（每小时） 总共耗时：5毫秒', '1', 'NoSuchBeanDefinitionException: No bean named \'refreshToken\' available', 1713334134616);
INSERT INTO `sys_job_log` VALUES (1769649944801239041, '刷新SameToken（每小时）', 'DEFAULT', 'sameTokenTask.refreshToken()', '刷新SameToken（每小时） 总共耗时：0毫秒', '1', 'NoSuchBeanDefinitionException: No bean named \'refreshToken\' available', 1713334134616);
INSERT INTO `sys_job_log` VALUES (1773530515004674050, '刷新SameToken（每小时）', 'DEFAULT', 'sameTokenTask.refreshToken()', '刷新SameToken（每小时） 总共耗时：4毫秒', '1', 'NoSuchBeanDefinitionException: No bean named \'refreshToken\' available', 1713334134616);
INSERT INTO `sys_job_log` VALUES (1773545614385827841, '刷新SameToken（每小时）', 'DEFAULT', 'sameTokenTask.refreshToken()', '刷新SameToken（每小时） 总共耗时：2毫秒', '1', 'NoSuchBeanDefinitionException: No bean named \'refreshToken\' available', 1713334134616);
INSERT INTO `sys_job_log` VALUES (1773590913007452162, '刷新SameToken（每小时）', 'DEFAULT', 'sameTokenTask.refreshToken()', '刷新SameToken（每小时） 总共耗时：4毫秒', '1', 'NoSuchBeanDefinitionException: No bean named \'refreshToken\' available', 1713334134616);
INSERT INTO `sys_job_log` VALUES (1773606012321497089, '刷新SameToken（每小时）', 'DEFAULT', 'sameTokenTask.refreshToken()', '刷新SameToken（每小时） 总共耗时：2毫秒', '1', 'NoSuchBeanDefinitionException: No bean named \'refreshToken\' available', 1713334134616);
INSERT INTO `sys_job_log` VALUES (1773621111845257217, '刷新SameToken（每小时）', 'DEFAULT', 'sameTokenTask.refreshToken()', '刷新SameToken（每小时） 总共耗时：3毫秒', '1', 'NoSuchBeanDefinitionException: No bean named \'refreshToken\' available', 1713334134616);
INSERT INTO `sys_job_log` VALUES (1773636211503230978, '刷新SameToken（每小时）', 'DEFAULT', 'sameTokenTask.refreshToken()', '刷新SameToken（每小时） 总共耗时：5毫秒', '1', 'NoSuchBeanDefinitionException: No bean named \'refreshToken\' available', 1713334134616);
INSERT INTO `sys_job_log` VALUES (1773877803413639169, '刷新SameToken（每小时）', 'DEFAULT', 'sameTokenTask.refreshToken()', '刷新SameToken（每小时） 总共耗时：5毫秒', '1', 'NoSuchBeanDefinitionException: No bean named \'refreshToken\' available', 1713334134616);
INSERT INTO `sys_job_log` VALUES (1773892902719295490, '刷新SameToken（每小时）', 'DEFAULT', 'sameTokenTask.refreshToken()', '刷新SameToken（每小时） 总共耗时：1毫秒', '1', 'NoSuchBeanDefinitionException: No bean named \'refreshToken\' available', 1713334134616);
INSERT INTO `sys_job_log` VALUES (1773908002213695489, '刷新SameToken（每小时）', 'DEFAULT', 'sameTokenTask.refreshToken()', '刷新SameToken（每小时） 总共耗时：1毫秒', '1', 'NoSuchBeanDefinitionException: No bean named \'refreshToken\' available', 1713334134616);
INSERT INTO `sys_job_log` VALUES (1773923101703901186, '刷新SameToken（每小时）', 'DEFAULT', 'sameTokenTask.refreshToken()', '刷新SameToken（每小时） 总共耗时：1毫秒', '1', 'NoSuchBeanDefinitionException: No bean named \'refreshToken\' available', 1713334134616);
INSERT INTO `sys_job_log` VALUES (1773938201206689794, '刷新SameToken（每小时）', 'DEFAULT', 'sameTokenTask.refreshToken()', '刷新SameToken（每小时） 总共耗时：1毫秒', '1', 'NoSuchBeanDefinitionException: No bean named \'refreshToken\' available', 1713334134616);
INSERT INTO `sys_job_log` VALUES (1773953300776587266, '刷新SameToken（每小时）', 'DEFAULT', 'sameTokenTask.refreshToken()', '刷新SameToken（每小时） 总共耗时：2毫秒', '1', 'NoSuchBeanDefinitionException: No bean named \'refreshToken\' available', 1713334134616);
INSERT INTO `sys_job_log` VALUES (1773968400203878401, '刷新SameToken（每小时）', 'DEFAULT', 'sameTokenTask.refreshToken()', '刷新SameToken（每小时） 总共耗时：1毫秒', '1', 'NoSuchBeanDefinitionException: No bean named \'refreshToken\' available', 1713334134616);
INSERT INTO `sys_job_log` VALUES (1773983500151263234, '刷新SameToken（每小时）', 'DEFAULT', 'sameTokenTask.refreshToken()', '刷新SameToken（每小时） 总共耗时：105毫秒', '1', 'NoSuchBeanDefinitionException: No bean named \'refreshToken\' available', 1713334134616);

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
INSERT INTO `sys_login_log` VALUES (1779028287854153729, 0, 1, '内网IP', '0:0:0:0:0:0:0:1', 'Unknown', NULL, 20240413140610);
INSERT INTO `sys_login_log` VALUES (1779039488944349185, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 20240413145040);
INSERT INTO `sys_login_log` VALUES (1779050294973014017, 0, 1, '内网IP', '0:0:0:0:0:0:0:1', 'Unknown', NULL, 20240413153337);
INSERT INTO `sys_login_log` VALUES (1779051600932478978, 0, 1, '内网IP', '0:0:0:0:0:0:0:1', 'Unknown', NULL, 20240413153849);
INSERT INTO `sys_login_log` VALUES (1779056793908649986, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 20240413155927);
INSERT INTO `sys_login_log` VALUES (1779059212960899073, 0, 1, '内网IP', '0:0:0:0:0:0:0:1', 'Unknown', NULL, 20240413160904);
INSERT INTO `sys_login_log` VALUES (1779068245444894721, 0, 1, '内网IP', '0:0:0:0:0:0:0:1', 'Unknown', NULL, 20240413164457);
INSERT INTO `sys_login_log` VALUES (1779069569137876994, 1774671331416821762, 1774671331412627456, '内网IP', '0:0:0:0:0:0:0:1', 'Unknown', NULL, 20240413165013);
INSERT INTO `sys_login_log` VALUES (1779071944779079682, 1774671331416821762, 1774671331412627456, '内网IP', '0:0:0:0:0:0:0:1', 'Unknown', NULL, 20240413165939);
INSERT INTO `sys_login_log` VALUES (1779768079093039105, 1774671331416821762, 1774671331412627456, '内网IP', '0:0:0:0:0:0:0:1', 'Unknown', NULL, 20240415150549);
INSERT INTO `sys_login_log` VALUES (1779768173334855681, 1774671331416821762, 1774671331412627456, '内网IP', '0:0:0:0:0:0:0:1', 'Unknown', NULL, 20240415150613);
INSERT INTO `sys_login_log` VALUES (1779768345410371585, 1774671331416821762, 1774671331412627456, '内网IP', '0:0:0:0:0:0:0:1', 'Unknown', NULL, 20240415150650);
INSERT INTO `sys_login_log` VALUES (1780477864071372801, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713333976135);
INSERT INTO `sys_login_log` VALUES (1780497925876137986, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713338759425);
INSERT INTO `sys_login_log` VALUES (1780515473006968834, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713342943765);
INSERT INTO `sys_login_log` VALUES (1780834947589402626, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713419111903);
INSERT INTO `sys_login_log` VALUES (1780834980816678913, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713419120378);
INSERT INTO `sys_login_log` VALUES (1780837652743516161, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713419757404);
INSERT INTO `sys_login_log` VALUES (1780840039109554177, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713420326376);
INSERT INTO `sys_login_log` VALUES (1780840341581787138, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713420398484);
INSERT INTO `sys_login_log` VALUES (1780841262365732865, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713420618019);
INSERT INTO `sys_login_log` VALUES (1780841527995199489, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713420681355);
INSERT INTO `sys_login_log` VALUES (1780842512989745153, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713420916185);
INSERT INTO `sys_login_log` VALUES (1780842834894188546, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713420992946);
INSERT INTO `sys_login_log` VALUES (1780843019716194306, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713421037010);
INSERT INTO `sys_login_log` VALUES (1780845917321752577, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713421727850);
INSERT INTO `sys_login_log` VALUES (1780846176164835330, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713421789565);
INSERT INTO `sys_login_log` VALUES (1780847099750576129, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713422009762);
INSERT INTO `sys_login_log` VALUES (1780847444107128834, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713422091863);
INSERT INTO `sys_login_log` VALUES (1780848406997692417, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713422321431);
INSERT INTO `sys_login_log` VALUES (1780848930232922114, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713422446183);
INSERT INTO `sys_login_log` VALUES (1780849544153198593, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713422592555);
INSERT INTO `sys_login_log` VALUES (1780849650617217025, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713422617941);
INSERT INTO `sys_login_log` VALUES (1780849791755546625, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713422651591);
INSERT INTO `sys_login_log` VALUES (1780850131796160514, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713422732661);
INSERT INTO `sys_login_log` VALUES (1780850401619931137, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713422796993);
INSERT INTO `sys_login_log` VALUES (1780850770148257793, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713422884852);
INSERT INTO `sys_login_log` VALUES (1780852628141043714, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713423327826);
INSERT INTO `sys_login_log` VALUES (1780855484139106306, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713424008692);
INSERT INTO `sys_login_log` VALUES (1780857188892995586, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713424415199);
INSERT INTO `sys_login_log` VALUES (1781203155173412865, 0, 1, '内网IP', '0:0:0:0:0:0:0:1', 'Unknown', NULL, 1713506899127);
INSERT INTO `sys_login_log` VALUES (1781203569834889217, 0, 1, '内网IP', '0:0:0:0:0:0:0:1', 'Unknown', NULL, 1713506996190);
INSERT INTO `sys_login_log` VALUES (1781204054650294274, 0, 1, '内网IP', '0:0:0:0:0:0:0:1', 'Unknown', NULL, 1713507094170);
INSERT INTO `sys_login_log` VALUES (1781205911556755457, 0, 1, '内网IP', '0:0:0:0:0:0:0:1', 'Unknown', NULL, 1713507550283);
INSERT INTO `sys_login_log` VALUES (1781225880277389313, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713512317903);
INSERT INTO `sys_login_log` VALUES (1781227165760262145, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713512624570);
INSERT INTO `sys_login_log` VALUES (1781227862144749570, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713512790600);
INSERT INTO `sys_login_log` VALUES (1781228206144786433, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713512872615);
INSERT INTO `sys_login_log` VALUES (1781228299715514370, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713512894923);
INSERT INTO `sys_login_log` VALUES (1781228820731957250, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713513019145);
INSERT INTO `sys_login_log` VALUES (1781230701193629697, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713513467481);
INSERT INTO `sys_login_log` VALUES (1781230890243493890, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713513512555);
INSERT INTO `sys_login_log` VALUES (1781232213504462849, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713513828043);
INSERT INTO `sys_login_log` VALUES (1781232569064001537, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713513912818);
INSERT INTO `sys_login_log` VALUES (1781232891597590529, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713513989714);
INSERT INTO `sys_login_log` VALUES (1781233201661513730, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713514063637);
INSERT INTO `sys_login_log` VALUES (1781233326869876737, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713514093492);
INSERT INTO `sys_login_log` VALUES (1781234543855570945, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713514383645);
INSERT INTO `sys_login_log` VALUES (1781234846797565953, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713514455872);
INSERT INTO `sys_login_log` VALUES (1781235002376884225, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713514492964);
INSERT INTO `sys_login_log` VALUES (1781236217659691009, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713514782710);
INSERT INTO `sys_login_log` VALUES (1781236533083934721, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713514857912);
INSERT INTO `sys_login_log` VALUES (1781236763485442050, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713514912846);
INSERT INTO `sys_login_log` VALUES (1781236842128642049, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713514931597);
INSERT INTO `sys_login_log` VALUES (1781237121699975169, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713514998249);
INSERT INTO `sys_login_log` VALUES (1781237403410403330, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713515065430);
INSERT INTO `sys_login_log` VALUES (1781238577953935361, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713515345447);
INSERT INTO `sys_login_log` VALUES (1781240208971960321, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713515734315);
INSERT INTO `sys_login_log` VALUES (1781240348025720834, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713515767466);
INSERT INTO `sys_login_log` VALUES (1781240646538530817, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713515838636);
INSERT INTO `sys_login_log` VALUES (1781240852285919233, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713515887690);
INSERT INTO `sys_login_log` VALUES (1781241167315898369, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713515962802);
INSERT INTO `sys_login_log` VALUES (1781241299537137666, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713515994326);
INSERT INTO `sys_login_log` VALUES (1781241547525361666, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713516053448);
INSERT INTO `sys_login_log` VALUES (1781242185529331713, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713516205559);
INSERT INTO `sys_login_log` VALUES (1781242757129080834, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713516341843);
INSERT INTO `sys_login_log` VALUES (1782221206790537217, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1713749622037);
INSERT INTO `sys_login_log` VALUES (1783744518771421185, 0, 3, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1714112807797);
INSERT INTO `sys_login_log` VALUES (1783744659632926722, 1774671331416821762, 1774671331412627456, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1714112841874);
INSERT INTO `sys_login_log` VALUES (1783745173766516738, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1714112964450);
INSERT INTO `sys_login_log` VALUES (1783746003102052354, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1714113162183);
INSERT INTO `sys_login_log` VALUES (1783749901934112769, 0, 1, '内网IP', '127.0.0.1', 'Unknown', NULL, 1714114091740);
INSERT INTO `sys_login_log` VALUES (1789899706741727233, 0, 1, '内网IP', '127.0.0.1', 'Unknown', NULL, 1715580318893);
INSERT INTO `sys_login_log` VALUES (1789902740188209154, 0, 1, '内网IP', '127.0.0.1', 'Unknown', NULL, 1715581042645);
INSERT INTO `sys_login_log` VALUES (1789908316062392322, 0, 1, '内网IP', '127.0.0.1', 'Unknown', NULL, 1715582372082);
INSERT INTO `sys_login_log` VALUES (1789909313069424642, 0, 1, '内网IP', '127.0.0.1', 'Unknown', NULL, 1715582609705);
INSERT INTO `sys_login_log` VALUES (1792738268411584514, 0, 1, '内网IP', '127.0.0.1', 'Unknown', NULL, 1716257084474);
INSERT INTO `sys_login_log` VALUES (1793444944714383362, 0, 1, '内网IP', '127.0.0.1', 'Unknown', NULL, 1716425568874);
INSERT INTO `sys_login_log` VALUES (1794965086065528833, 0, 1, '内网IP', '127.0.0.1', 'Unknown', NULL, 1716787998853);
INSERT INTO `sys_login_log` VALUES (1794965730457423873, 0, 3, '内网IP', '127.0.0.1', 'Unknown', NULL, 1716788153556);
INSERT INTO `sys_login_log` VALUES (1794975832941924353, 0, 3, '内网IP', '127.0.0.1', 'Unknown', NULL, 1716790562138);
INSERT INTO `sys_login_log` VALUES (1794978606039629825, 0, 3, '内网IP', '127.0.0.1', 'Unknown', NULL, 1716791222597);
INSERT INTO `sys_login_log` VALUES (1794978666076897281, 0, 1, '内网IP', '127.0.0.1', 'Unknown', NULL, 1716791237602);
INSERT INTO `sys_login_log` VALUES (1794980188051406849, 0, 1, '内网IP', '127.0.0.1', 'Unknown', NULL, 1716791600533);
INSERT INTO `sys_login_log` VALUES (1795326261919977474, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1716874109532);
INSERT INTO `sys_login_log` VALUES (1795330093919997953, 0, 1, '内网IP', '127.0.0.1', 'Unknown', NULL, 1716875024517);
INSERT INTO `sys_login_log` VALUES (1795338319273103361, 0, 1, '内网IP', '127.0.0.1', 'Unknown', NULL, 1716876985573);
INSERT INTO `sys_login_log` VALUES (1795342944097243137, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1716878088241);
INSERT INTO `sys_login_log` VALUES (1797552540547190785, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1717404896216);
INSERT INTO `sys_login_log` VALUES (1797858811028615169, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1717477916884);
INSERT INTO `sys_login_log` VALUES (1798171572744884225, 0, 1, '内网IP', '127.0.0.1', 'Unknown', NULL, 1717552485263);
INSERT INTO `sys_login_log` VALUES (1798186860269932545, 0, 1, '内网IP', '127.0.0.1', 'Unknown', NULL, 1717556130732);
INSERT INTO `sys_login_log` VALUES (1798509425052061698, 0, 1, '内网IP', '127.0.0.1', 'Unknown', NULL, 1717633035587);
INSERT INTO `sys_login_log` VALUES (1798509490659364866, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1717633051833);
INSERT INTO `sys_login_log` VALUES (1798518255743897602, 0, 1, '内网IP', '127.0.0.1', 'Unknown', NULL, 1717635141592);
INSERT INTO `sys_login_log` VALUES (1798539987645075458, 0, 1, '内网IP', '127.0.0.1', 'Unknown', NULL, 1717640322868);
INSERT INTO `sys_login_log` VALUES (1798555446771683330, 0, 1, '内网IP', '127.0.0.1', 'Unknown', NULL, 1717644008628);
INSERT INTO `sys_login_log` VALUES (1798583464265224193, 0, 1, '内网IP', '127.0.0.1', 'Unknown', NULL, 1717650688517);
INSERT INTO `sys_login_log` VALUES (1799357102677512193, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1717835137302);
INSERT INTO `sys_login_log` VALUES (1804048227979984898, 0, 1, '内网IP', '127.0.0.1', 'Unknown', NULL, 1718953589235);
INSERT INTO `sys_login_log` VALUES (1804050878557814786, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1718954221585);
INSERT INTO `sys_login_log` VALUES (1804072701982855169, 0, 1, '内网IP', '127.0.0.1', 'Unknown', NULL, 1718959424017);
INSERT INTO `sys_login_log` VALUES (1804074201412980737, 0, 1, '内网IP', '127.0.0.1', 'Unknown', NULL, 1718959782087);
INSERT INTO `sys_login_log` VALUES (1804077688691544066, 0, 1, '内网IP', '127.0.0.1', 'Unknown', NULL, 1718960613622);
INSERT INTO `sys_login_log` VALUES (1804079332388950017, 0, 1, '内网IP', '127.0.0.1', 'Unknown', NULL, 1718961005556);
INSERT INTO `sys_login_log` VALUES (1804079721217708033, 0, 1, '内网IP', '127.0.0.1', 'Unknown', NULL, 1718961097024);
INSERT INTO `sys_login_log` VALUES (1804080174319980546, 0, 1, '内网IP', '127.0.0.1', 'Unknown', NULL, 1718961205249);
INSERT INTO `sys_login_log` VALUES (1804080646447616002, 0, 1, '内网IP', '127.0.0.1', 'MSEdge', '10.0', 1718961318877);
INSERT INTO `sys_login_log` VALUES (1804324402467844097, 0, 1, '内网IP', '127.0.0.1', 'Unknown', NULL, 1719019434199);

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
INSERT INTO `sys_notice` VALUES (1, '温馨提醒：2018-07-01 若依新版本发布啦', '2', '<p>&lt;</p>', '0', '1', 1, 20221219100609, 1, 20230608134537);
INSERT INTO `sys_notice` VALUES (2, '维护通知：2018-07-01 若依系统凌晨维护', '1', '<p>&lt;</p>', '0', '2', 1, 20221219100609, 1, 20230612134332);

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
INSERT INTO `sys_oper_log` VALUES (1778625396790194177, 0, '操作日志', '3', 'com.thingslink.system.controller.log.SysOperlogController.clean()', 'DELETE', 'admin', '/operlog/clean', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 20240412112514);
INSERT INTO `sys_oper_log` VALUES (1778625872730451970, 0, '部门管理', '2', 'com.thingslink.auth.controller.SysDeptController.edit()', 'PUT', 'admin', '/dept', '127.0.0.1', NULL, '{\"createAt\":\"2024-04-03 12:38:25\",\"createBy\":\"1774671331412627456\",\"updateAt\":\"2024-04-12 11:27:07\",\"updateBy\":\"1\",\"deptId\":\"1775382319191453698\",\"tenantId\":\"1774671331416821762\",\"parentId\":null,\"ancestors\":null,\"deptName\":\"默认租户部门\",\"status\":\"0\",\"deptSort\":0,\"contact\":null,\"email\":null,\"children\":[]}', NULL, '0', NULL, 20240412112707);
INSERT INTO `sys_oper_log` VALUES (1778626232891142146, 0, '部门管理', '2', 'com.thingslink.auth.controller.SysDeptController.edit()', 'PUT', 'admin', '/dept', '127.0.0.1', NULL, NULL, '{\"biz\":0,\"data\":null,\"err_msg\":\"Success\",\"timestamp\":1712892513784}', '0', NULL, 20240412112833);
INSERT INTO `sys_oper_log` VALUES (1780521103470018562, 0, '角色管理', '2', 'com.thingslink.user.controller.SysRoleController.edit()', 'PUT', 'admin', '/role', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1713344286159);
INSERT INTO `sys_oper_log` VALUES (1782221287006601217, 0, '菜单管理', '2', 'com.thingslink.user.controller.SysMenuController.edit()', 'PUT', 'admin', '/menu', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1713749641479);
INSERT INTO `sys_oper_log` VALUES (1782222463068471298, 0, '后台管理', '2', 'com.thingslink.user.controller.SysUserController.edit()', 'PUT', 'admin', '/user', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1713749921870);
INSERT INTO `sys_oper_log` VALUES (1782222568479719425, 0, '后台管理', '2', 'com.thingslink.user.controller.SysUserController.edit()', 'PUT', 'admin', '/user', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1713749947059);
INSERT INTO `sys_oper_log` VALUES (1782222806661660673, 0, '后台管理', '2', 'com.thingslink.user.controller.SysUserController.edit()', 'PUT', 'admin', '/user', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1713750003805);
INSERT INTO `sys_oper_log` VALUES (1783745409394126849, 0, '菜单管理', '2', 'com.thingslink.user.controller.SysMenuController.edit()', 'PUT', 'admin', '/menu', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1714113020593);
INSERT INTO `sys_oper_log` VALUES (1783750711107629057, 0, '字典数据', '2', 'com.thingslink.system.controller.SysDictDataController.edit()', 'PUT', 'admin', '/dict/data', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1714114284631);
INSERT INTO `sys_oper_log` VALUES (1783750723359191041, 0, '字典数据', '2', 'com.thingslink.system.controller.SysDictDataController.edit()', 'PUT', 'admin', '/dict/data', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1714114287585);
INSERT INTO `sys_oper_log` VALUES (1783750777188888577, 0, '字典数据', '2', 'com.thingslink.system.controller.SysDictDataController.edit()', 'PUT', 'admin', '/dict/data', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1714114300420);
INSERT INTO `sys_oper_log` VALUES (1783750790493220865, 0, '字典数据', '2', 'com.thingslink.system.controller.SysDictDataController.edit()', 'PUT', 'admin', '/dict/data', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1714114303592);
INSERT INTO `sys_oper_log` VALUES (1795357936230043650, 0, '角色管理', '2', 'com.thingslink.user.controller.SysRoleController.edit()', 'PUT', 'admin', '/role', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1716881662514);
INSERT INTO `sys_oper_log` VALUES (1795738379936817154, 0, '后台管理', '2', 'com.thingslink.user.controller.SysUserController.edit()', 'PUT', 'admin', '/user', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1716972367418);
INSERT INTO `sys_oper_log` VALUES (1795738418029486081, 0, '后台管理', '2', 'com.thingslink.user.controller.SysUserController.edit()', 'PUT', 'admin', '/user', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1716972376710);
INSERT INTO `sys_oper_log` VALUES (1795738448962478082, 0, '后台管理', '2', 'com.thingslink.user.controller.SysUserController.edit()', 'PUT', 'admin', '/user', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1716972384090);
INSERT INTO `sys_oper_log` VALUES (1804343906774241281, 0, '后台管理', '2', 'com.thingslink.user.controller.SysUserController.edit()', 'POST', 'admin', '/user/edit', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719024084882);
INSERT INTO `sys_oper_log` VALUES (1804346439001395202, 0, '后台管理', '4', 'com.thingslink.user.controller.SysUserController.authRole()', 'POST', 'admin', '/user/authorize_role', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719024688639);
INSERT INTO `sys_oper_log` VALUES (1804346508459069441, 0, '后台管理', '4', 'com.thingslink.user.controller.SysUserController.authRole()', 'POST', 'admin', '/user/authorize_role', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719024705410);
INSERT INTO `sys_oper_log` VALUES (1804346822436278274, 0, '后台管理', '4', 'com.thingslink.user.controller.SysUserController.authRole()', 'POST', 'admin', '/user/authorize_role', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719024780258);
INSERT INTO `sys_oper_log` VALUES (1804348901091397634, 0, '后台管理', '4', 'com.thingslink.user.controller.SysUserController.authRole()', 'POST', 'admin', '/user/authorize_role', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719025275653);
INSERT INTO `sys_oper_log` VALUES (1804351415027191810, 0, '菜单管理', '2', 'com.thingslink.user.controller.SysMenuController.edit()', 'POST', 'admin', '/menu/edit', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719025875011);
INSERT INTO `sys_oper_log` VALUES (1804351457926533122, 0, '岗位管理', '2', 'com.thingslink.user.controller.SysPostController.edit()', 'POST', 'admin', '/post/edit', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719025885445);
INSERT INTO `sys_oper_log` VALUES (1804351469880299521, 0, '岗位管理', '2', 'com.thingslink.user.controller.SysPostController.edit()', 'POST', 'admin', '/post/edit', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719025888299);
INSERT INTO `sys_oper_log` VALUES (1804351487471210497, 0, '岗位管理', '2', 'com.thingslink.user.controller.SysPostController.edit()', 'POST', 'admin', '/post/edit', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719025892494);
INSERT INTO `sys_oper_log` VALUES (1804351523139571714, 0, '角色管理', '2', 'com.thingslink.user.controller.SysRoleController.edit()', 'POST', 'admin', '/role/edit', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719025900999);
INSERT INTO `sys_oper_log` VALUES (1804352359362154497, 0, '角色管理', '4', 'com.thingslink.user.controller.SysRoleController.batchAuth()', 'POST', 'admin', '/role/authorize_user', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719026100366);
INSERT INTO `sys_oper_log` VALUES (1804352640485380098, 0, '角色管理', '4', 'com.thingslink.user.controller.SysRoleController.cancelAuth()', 'POST', 'admin', '/role/authorize/cancel', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719026167391);
INSERT INTO `sys_oper_log` VALUES (1804352811378102273, 0, '角色管理', '4', 'com.thingslink.user.controller.SysRoleController.batchAuth()', 'POST', 'admin', '/role/authorize_user', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719026208143);
INSERT INTO `sys_oper_log` VALUES (1804353114194268161, 0, '角色管理', '4', 'com.thingslink.user.controller.SysRoleController.batchAuth()', 'POST', 'admin', '/role/authorize_user', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719026280333);
INSERT INTO `sys_oper_log` VALUES (1804353151959781378, 0, '角色管理', '4', 'com.thingslink.user.controller.SysRoleController.cancelAuth()', 'POST', 'admin', '/role/authorize/cancel', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719026289339);
INSERT INTO `sys_oper_log` VALUES (1804353169005432833, 0, '角色管理', '4', 'com.thingslink.user.controller.SysRoleController.batchAuth()', 'POST', 'admin', '/role/authorize_user', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719026293398);
INSERT INTO `sys_oper_log` VALUES (1804353358575390722, 0, '角色管理', '4', 'com.thingslink.user.controller.SysRoleController.cancelAuth()', 'POST', 'admin', '/role/authorize/cancel', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719026338599);
INSERT INTO `sys_oper_log` VALUES (1804353365856702465, 0, '角色管理', '4', 'com.thingslink.user.controller.SysRoleController.cancelAuth()', 'POST', 'admin', '/role/authorize/cancel', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719026340336);
INSERT INTO `sys_oper_log` VALUES (1804353371384795138, 0, '角色管理', '4', 'com.thingslink.user.controller.SysRoleController.cancelAuth()', 'POST', 'admin', '/role/authorize/cancel', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719026341652);
INSERT INTO `sys_oper_log` VALUES (1804353383409864705, 0, '角色管理', '4', 'com.thingslink.user.controller.SysRoleController.batchAuth()', 'POST', 'admin', '/role/authorize_user', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719026344522);
INSERT INTO `sys_oper_log` VALUES (1804353760544903170, 0, '角色管理', '4', 'com.thingslink.user.controller.SysRoleController.batchAuth()', 'POST', 'admin', '/role/authorize_user', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719026434433);
INSERT INTO `sys_oper_log` VALUES (1804353879273066497, 0, '角色管理', '4', 'com.thingslink.user.controller.SysRoleController.cancelAuth()', 'POST', 'admin', '/role/authorize/cancel', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719026462743);
INSERT INTO `sys_oper_log` VALUES (1804353884847296514, 0, '角色管理', '4', 'com.thingslink.user.controller.SysRoleController.cancelAuth()', 'POST', 'admin', '/role/authorize/cancel', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719026464074);
INSERT INTO `sys_oper_log` VALUES (1804353893294624769, 0, '角色管理', '4', 'com.thingslink.user.controller.SysRoleController.batchAuth()', 'POST', 'admin', '/role/authorize_user', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719026466088);
INSERT INTO `sys_oper_log` VALUES (1804353946650365953, 0, '角色管理', '4', 'com.thingslink.user.controller.SysRoleController.batchAuth()', 'POST', 'admin', '/role/authorize_user', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719026478808);
INSERT INTO `sys_oper_log` VALUES (1804353999729283074, 0, '角色管理', '4', 'com.thingslink.user.controller.SysRoleController.batchAuth()', 'POST', 'admin', '/role/authorize_user', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719026491463);
INSERT INTO `sys_oper_log` VALUES (1804354070881456130, 0, '角色管理', '4', 'com.thingslink.user.controller.SysRoleController.cancelAuth()', 'POST', 'admin', '/role/authorize/cancel_batch', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719026508441);
INSERT INTO `sys_oper_log` VALUES (1804354091366436865, 0, '角色管理', '4', 'com.thingslink.user.controller.SysRoleController.batchAuth()', 'POST', 'admin', '/role/authorize_user', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719026513311);
INSERT INTO `sys_oper_log` VALUES (1804354388973277186, 0, '角色管理', '4', 'com.thingslink.user.controller.SysRoleController.batchAuth()', 'POST', 'admin', '/role/authorize_user', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719026584267);
INSERT INTO `sys_oper_log` VALUES (1804354471731089410, 0, '角色管理', '4', 'com.thingslink.user.controller.SysRoleController.batchAuth()', 'POST', 'admin', '/role/authorize_user', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719026603995);
INSERT INTO `sys_oper_log` VALUES (1804354513502162946, 0, '角色管理', '4', 'com.thingslink.user.controller.SysRoleController.cancelAuth()', 'POST', 'admin', '/role/authorize/cancel_batch', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719026613958);
INSERT INTO `sys_oper_log` VALUES (1804354552404332545, 0, '角色管理', '4', 'com.thingslink.user.controller.SysRoleController.batchAuth()', 'POST', 'admin', '/role/authorize_user', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719026623231);
INSERT INTO `sys_oper_log` VALUES (1804354788161966081, 0, '角色管理', '4', 'com.thingslink.user.controller.SysRoleController.batchAuth()', 'POST', 'admin', '/role/authorize_user', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719026679439);
INSERT INTO `sys_oper_log` VALUES (1804354826795700225, 0, '角色管理', '4', 'com.thingslink.user.controller.SysRoleController.batchAuth()', 'POST', 'admin', '/role/authorize_user', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719026688651);
INSERT INTO `sys_oper_log` VALUES (1804354841266049025, 0, '角色管理', '4', 'com.thingslink.user.controller.SysRoleController.cancelAuth()', 'POST', 'admin', '/role/authorize/cancel_batch', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719026692102);
INSERT INTO `sys_oper_log` VALUES (1804354900170854402, 0, '角色管理', '4', 'com.thingslink.user.controller.SysRoleController.batchAuth()', 'POST', 'admin', '/role/authorize_user', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719026706144);
INSERT INTO `sys_oper_log` VALUES (1804354961789374465, 0, '角色管理', '4', 'com.thingslink.user.controller.SysRoleController.batchAuth()', 'POST', 'admin', '/role/authorize_user', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719026720836);
INSERT INTO `sys_oper_log` VALUES (1804355037911797762, 0, '角色管理', '4', 'com.thingslink.user.controller.SysRoleController.batchAuth()', 'POST', 'admin', '/role/authorize_user', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719026738984);
INSERT INTO `sys_oper_log` VALUES (1804355476677939201, 0, '角色管理', '4', 'com.thingslink.user.controller.SysRoleController.cancelAuth()', 'POST', 'admin', '/role/authorize/cancel', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719026843595);
INSERT INTO `sys_oper_log` VALUES (1804355506440720385, 0, '角色管理', '4', 'com.thingslink.user.controller.SysRoleController.batchAuth()', 'POST', 'admin', '/role/authorize_user', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719026850697);
INSERT INTO `sys_oper_log` VALUES (1804355545862983681, 0, '角色管理', '4', 'com.thingslink.user.controller.SysRoleController.cancelAuth()', 'POST', 'admin', '/role/authorize/cancel_batch', '127.0.0.1', NULL, NULL, NULL, '0', NULL, 1719026860098);

SET FOREIGN_KEY_CHECKS = 1;
