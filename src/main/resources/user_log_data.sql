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

 Date: 29/09/2021 00:03:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_log_data
-- ----------------------------
DROP TABLE IF EXISTS `user_log_data`;
CREATE TABLE `user_log_data`  (
  `user_id` bigint(255) NOT NULL,
  `log_in_time` datetime(0) NOT NULL,
  `log_off_time` datetime(0) NULL DEFAULT NULL,
  `last_time` bigint(100) NULL DEFAULT NULL,
  INDEX `user_id_logdata`(`user_id`) USING BTREE,
  CONSTRAINT `user_id_logdata` FOREIGN KEY (`user_id`) REFERENCES `user_config` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_log_data
-- ----------------------------
INSERT INTO `user_log_data` VALUES (6847889141309378560, '2021-09-27 00:08:10', '2021-09-27 00:11:15', 185206);

SET FOREIGN_KEY_CHECKS = 1;
