/*
 Navicat Premium Data Transfer

 Source Server         : local_mysql8
 Source Server Type    : MySQL
 Source Server Version : 80035
 Source Host           : localhost:3306
 Source Schema         : thingslink_equipment

 Target Server Type    : MySQL
 Target Server Version : 80035
 File Encoding         : 65001

 Date: 28/12/2023 16:31:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS thingslink_equipment;
USE thingslink_equipment;
-- ----------------------------
-- Table structure for device
-- ----------------------------
DROP TABLE IF EXISTS `device`;
CREATE TABLE `device`  (
  `device_id` bigint NOT NULL COMMENT '设备id',
  `device_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备名称',
  `station_id` bigint NULL DEFAULT NULL COMMENT '电站id',
  `serial_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'sn号',
  `version` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '协议版本',
  `online_time` datetime NULL DEFAULT NULL COMMENT '在线时间',
  `device_status` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备状态',
  `road_status` json NULL COMMENT '通道状态 0-空闲 1-使用',
  `create_at` datetime NOT NULL COMMENT '创建时间',
  `create_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建者',
  `update_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`device_id`) USING BTREE,
  UNIQUE INDEX `uk_serial_no`(`serial_no` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of device
-- ----------------------------
INSERT INTO `device` VALUES (4, '123', 2, '8622363949796987', '123', NULL, 'OFFLINE', '{\"0\": 0, \"1\": 1, \"2\": 3}', '2023-06-06 00:00:00', '', '2023-12-05 13:41:34', NULL);

-- ----------------------------
-- Table structure for station
-- ----------------------------
DROP TABLE IF EXISTS `station`;
CREATE TABLE `station`  (
  `station_id` bigint NOT NULL COMMENT '电站id',
  `station_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '电站名',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门id',
  `province_id` bigint NULL DEFAULT NULL COMMENT '省id',
  `city_id` bigint NULL DEFAULT NULL COMMENT '市id',
  `district_id` bigint NULL DEFAULT NULL COMMENT '区/县id',
  `address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '详细地址',
  `longitude` decimal(9, 6) NULL DEFAULT NULL COMMENT '经度',
  `latitude` decimal(8, 6) NULL DEFAULT NULL COMMENT '纬度',
  `create_at` datetime NOT NULL COMMENT '创建时间',
  `create_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建者',
  `update_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`station_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of station
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
