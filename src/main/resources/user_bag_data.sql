/*
 Navicat Premium Data Transfer

 Source Server         : 本地测试
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : localhost:3306
 Source Schema         : game_db

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 29/09/2021 16:36:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_bag_data
-- ----------------------------
DROP TABLE IF EXISTS `user_bag_data`;
CREATE TABLE `user_bag_data`  (
  `user_id` bigint(255) NULL DEFAULT NULL,
  `bag_data` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  INDEX `bag_config_user_id`(`user_id`) USING BTREE,
  CONSTRAINT `bag_config_user_id` FOREIGN KEY (`user_id`) REFERENCES `user_config` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_bag_data
-- ----------------------------
INSERT INTO `user_bag_data` VALUES (6847889141309378560, '{\"bagData\":[{\"num\":\"4\",\"userBagItemData\":{\"itemEffectiveId\":\"0\",\"itemId\":\"1\",\"itemInfo\":\"testInfo\",\"itemName\":\"Sword\",\"itemProperty\":\"ttttt\",\"itemType\":\"1\"}}],\"userId\":\"6847889141309378560\"}', NULL);

SET FOREIGN_KEY_CHECKS = 1;
