/*
 Navicat Premium Data Transfer

 Source Server         : local_mysql8
 Source Server Type    : MySQL
 Source Server Version : 80035
 Source Host           : localhost:3306
 Source Schema         : thingslink_order

 Target Server Type    : MySQL
 Target Server Version : 80035
 File Encoding         : 65001

 Date: 28/12/2023 16:46:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for charging_order
-- ----------------------------
CREATE DATABASE IF NOT EXISTS thingslink_order;
USE thingslink_order;
DROP TABLE IF EXISTS `charging_order`;
CREATE TABLE `charging_order`  (
  `charging_id` bigint NOT NULL COMMENT '充电id',
  `customer_id` bigint NOT NULL COMMENT '用户id，下单用户',
  `dept_id` bigint NOT NULL COMMENT '部门id，订单归属部门',
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单号',
  `pay_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '支付方式',
  `charging_status` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '充电状态 ',
  `total_amount` bigint NOT NULL COMMENT '总金额',
  `pay_amount` bigint NOT NULL COMMENT '支付金额',
  `point_amount` bigint NOT NULL COMMENT '积分支付的金额',
  `refund_amount` bigint NOT NULL COMMENT '退款金额',
  `pay_time` datetime NULL DEFAULT NULL COMMENT '付款时间',
  `is_use_coupon` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '是否使用优惠券 0-否 1-是',
  `create_at` datetime NOT NULL COMMENT '订单创建时间',
  `create_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建者',
  `update_at` datetime NULL DEFAULT NULL COMMENT '订单更新时间',
  `update_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`charging_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of charging_order
-- ----------------------------
INSERT INTO `charging_order` VALUES (1, 4, 0, '123', NULL, '1', 0, 0, 0, 0, NULL, '0', '2023-05-04 01:25:50', '', NULL, NULL);
INSERT INTO `charging_order` VALUES (2, 4, 0, '111', '0', 'SUCCESS', 0, 0, 0, 0, NULL, '0', '2023-05-04 01:25:57', '', '2023-12-04 12:45:44', NULL);
INSERT INTO `charging_order` VALUES (3, 4, 0, '222', NULL, 'SUCCESS', 0, 0, 0, 0, NULL, '0', '2023-05-04 01:26:35', '', '2023-12-04 12:44:50', NULL);
INSERT INTO `charging_order` VALUES (4, 4, 0, '333', NULL, '1', 0, 0, 0, 0, NULL, '0', '2023-05-04 01:27:22', '', NULL, NULL);
INSERT INTO `charging_order` VALUES (5, 4, 0, '321', NULL, '1', 0, 0, 0, 0, NULL, '0', '2023-05-04 01:28:14', '', NULL, NULL);

-- ----------------------------
-- Table structure for coupon
-- ----------------------------
DROP TABLE IF EXISTS `coupon`;
CREATE TABLE `coupon`  (
  `coupon_id` bigint NOT NULL COMMENT '优惠券id',
  `tenant_id` bigint NOT NULL COMMENT '租户id',
  `coupon_title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `coupon_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '优惠券状态 \n\r\n通过审核，可以发放券-0\r\n等待审核-1\r\n停止发放，但是券依然可以被用户使用-2\r\n停用，券无法被使用-3',
  `coupon_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '优惠券类型 \r\n平台类型-1\r\n商户类型-2',
  `coupon_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '优惠券描述',
  `limit_amount` bigint NOT NULL COMMENT '门槛金额，0则为无门槛',
  `discount_amount` bigint NOT NULL COMMENT '优惠金额',
  `begin_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `sum_quantity` bigint NOT NULL COMMENT '券总数',
  `remaining_quantity` bigint NOT NULL COMMENT '券剩余数量',
  `limit_quantity` bigint NOT NULL COMMENT '用户限制持有券数量',
  PRIMARY KEY (`coupon_id`) USING BTREE,
  INDEX `idx_company_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '优惠券信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of coupon
-- ----------------------------
INSERT INTO `coupon` VALUES (1, 1, '新用户券', '2', '1', '新用户注册即领，111', 0, 100, '2023-02-05 10:49:39', '2023-03-05 10:49:39', 1000, 1000, 1);
INSERT INTO `coupon` VALUES (2, 1, '促销活动', '3', '2', '一月大促销', 200, 1000, '2023-01-12 10:49:39', '2023-01-28 10:49:39', 713, 613, 1);
INSERT INTO `coupon` VALUES (3, 1, '满100-30', '0', '1', 'desc;desc;desc;desc;desc;desc;desc;desc;desc;desc;desc;desc;desc;desc;desc;desc;desc;desc;desc;desc;desc;desc;desc;desc;desc;desc;desc;desc;desc;desc;desc;desc;desc;', 100, 3000, '2023-02-23 08:00:00', '2023-02-24 08:00:00', 1000, 1000, 2);

SET FOREIGN_KEY_CHECKS = 1;
