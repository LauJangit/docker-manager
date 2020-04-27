/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50721
 Source Host           : localhost:3306
 Source Schema         : docker

 Target Server Type    : MySQL
 Target Server Version : 50721
 File Encoding         : 65001

 Date: 27/04/2020 13:45:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for containerlist
-- ----------------------------
DROP TABLE IF EXISTS `containerlist`;
CREATE TABLE `containerlist`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `containnames` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `image` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ports` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of containerlist
-- ----------------------------
INSERT INTO `containerlist` VALUES ('241ac0ab858313cfa6ea78b3682690cb0c032823aad0314b11ea1ad1084680ca', '/brave_cannon', 'Exited (0) 21 hours ago', 'ubuntu', '', '127.0.0.1');
INSERT INTO `containerlist` VALUES ('cd04550d68510c8d99d7ca0cd6178e10809d3d8ac6c6205482c7bf86ef69870a', '/myubuntu', 'Exited (0) 21 hours ago', 'ubuntu', '', '127.0.0.1');

SET FOREIGN_KEY_CHECKS = 1;
