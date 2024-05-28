/*
 Navicat Premium Data Transfer

 Source Server         : local_mysql8
 Source Server Type    : MySQL
 Source Server Version : 80035
 Source Host           : localhost:3306
 Source Schema         : thingslink_auth

 Target Server Type    : MySQL
 Target Server Version : 80035
 File Encoding         : 65001

 Date: 29/05/2024 14:12:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS thingslink_auth;
USE thingslink_auth;
-- ----------------------------
-- Table structure for oauth2_client
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_client`;
CREATE TABLE `oauth2_client`  (
  `id` bigint NOT NULL,
  `client_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `client_secret` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `scopes` json NOT NULL COMMENT '权限域',
  `authorization_grant_types` json NOT NULL COMMENT '授权类型',
  `redirect_uris` json NOT NULL COMMENT '回调地址',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端状态',
  `auto_approve` tinyint(1) NOT NULL COMMENT '是否自动放行',
  `create_at` bigint NOT NULL,
  `create_id` bigint NOT NULL,
  `update_at` bigint NULL DEFAULT NULL,
  `update_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_client_id`(`client_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'OAUTH2客户端' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of oauth2_client
-- ----------------------------
INSERT INTO `oauth2_client` VALUES (1, 'inner-client', '{bcrypt}$2a$10$jKXqoIKK.w57jS5MKKasH.NQxxpfyUOlvP9yIXYsXelR7dbUGgBn.', '[\"scope.inner\"]', '[\"client_credentials\"]', '[]', '0', 0, 1713334134616, 1, NULL, NULL);
INSERT INTO `oauth2_client` VALUES (2, 'server', '{bcrypt}$2a$10$jKXqoIKK.w57jS5MKKasH.NQxxpfyUOlvP9yIXYsXelR7dbUGgBn.', '[]', '[\"password\", \"sms\", \"refresh_token\", \"authorization_code\"]', '[\"http://localhost:8080/auth/oauth2/authorization_code_callback\"]', '0', 0, 1713334134616, 1, NULL, 1);

SET FOREIGN_KEY_CHECKS = 1;
