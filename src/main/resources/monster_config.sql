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

 Date: 29/09/2021 22:25:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for monster_config
-- ----------------------------
DROP TABLE IF EXISTS `monster_config`;
CREATE TABLE `monster_config`  (
  `monster_id` bigint(255) NOT NULL,
  `monster_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `monster_property` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `monster_effective_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `monster_type` bigint(255) NULL DEFAULT NULL,
  PRIMARY KEY (`monster_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of monster_config
-- ----------------------------
INSERT INTO `monster_config` VALUES (1, '史莱姆', '{\r\n    \"health\":\"100\",\r\n    \"defense\":\"0\",\r\n    \"attack\":\"5\"\r\n}', NULL, 1);

SET FOREIGN_KEY_CHECKS = 1;
