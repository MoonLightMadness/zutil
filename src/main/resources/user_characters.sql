/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : localhost:3306
 Source Schema         : game_db

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 29/09/2021 17:28:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_characters
-- ----------------------------
DROP TABLE IF EXISTS `user_characters`;
CREATE TABLE `user_characters`  (
  `user_id` bigint(255) NULL DEFAULT NULL,
  `character_id` bigint(255) NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `delete_flag` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  INDEX `user_id_config`(`user_id`) USING BTREE,
  CONSTRAINT `user_id_config` FOREIGN KEY (`user_id`) REFERENCES `user_config` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
