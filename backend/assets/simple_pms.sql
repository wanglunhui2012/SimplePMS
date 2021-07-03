/*
 Navicat Premium Data Transfer

 Source Server         : 成都服务器
 Source Server Type    : MySQL
 Source Server Version : 50733
 Source Host           : 47.108.216.225:3306
 Source Schema         : simple_pms

 Target Server Type    : MySQL
 Target Server Version : 50733
 File Encoding         : 65001

 Date: 03/07/2021 16:56:42
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for system_department
-- ----------------------------
DROP TABLE IF EXISTS `system_department`;
CREATE TABLE `system_department`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父级部门id,不能为外键否则顶级部门id为0则没有对应的',
  `leader` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '领导',
  `phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建用户id',
  `create_department_id` bigint(20) NULL DEFAULT NULL COMMENT '创建部门id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_department
-- ----------------------------
INSERT INTO `system_department` VALUES (1, '总公司', 0, '王某', '12345678901', '1@qq.com', '无', 1, 1, '2021-06-27 20:34:26', '2021-06-27 20:34:29');
INSERT INTO `system_department` VALUES (2, '分公司1', 1, '张某', '12345678901', '1@qq.com', '无', 1, 1, '2021-06-27 20:35:23', '2021-06-27 20:35:25');
INSERT INTO `system_department` VALUES (3, '分公司2', 1, '赵某', '12345678901', '1@qq.com', '无', 1, 1, '2021-06-27 20:35:46', '2021-06-27 20:35:48');

-- ----------------------------
-- Table structure for system_dictionary
-- ----------------------------
DROP TABLE IF EXISTS `system_dictionary`;
CREATE TABLE `system_dictionary`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建用户id',
  `create_department_id` bigint(20) NULL DEFAULT NULL COMMENT '创建部门id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_dictionary
-- ----------------------------
INSERT INTO `system_dictionary` VALUES (1, 'systemYesNo', '是否', 1, 2, '2021-06-27 11:29:28', '2021-06-27 11:29:28');
INSERT INTO `system_dictionary` VALUES (2, 'systemTrueFalse', '真假', 1, 2, '2021-06-27 11:29:28', '2021-06-27 11:29:28');
INSERT INTO `system_dictionary` VALUES (3, 'systemEnable', '启用', 1, 2, '2021-06-27 11:29:28', '2021-06-27 11:29:28');
INSERT INTO `system_dictionary` VALUES (4, 'systemLogType', '日志类型', 1, 2, '2021-06-27 11:29:28', '2021-06-27 11:29:28');
INSERT INTO `system_dictionary` VALUES (5, 'systemDataScope', '数据权限', 1, 2, '2021-06-27 11:29:28', '2021-06-27 11:29:28');
INSERT INTO `system_dictionary` VALUES (6, 'systemUserSex', '性别', 1, 2, '2021-06-27 11:29:28', '2021-06-27 11:29:28');

-- ----------------------------
-- Table structure for system_dictionary_detail
-- ----------------------------
DROP TABLE IF EXISTS `system_dictionary_detail`;
CREATE TABLE `system_dictionary_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `dictionary_id` bigint(20) NOT NULL COMMENT '字典id',
  `key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '键',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '值',
  `sort` int(11) NULL DEFAULT 0 COMMENT '排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建用户id',
  `create_department_id` bigint(20) NULL DEFAULT NULL COMMENT '创建部门id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统字典详情表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_dictionary_detail
-- ----------------------------
INSERT INTO `system_dictionary_detail` VALUES (1, 1, '1', '是', 1, '', 1, 2, '2021-06-27 11:29:28', '2021-06-27 11:29:28');
INSERT INTO `system_dictionary_detail` VALUES (2, 1, '0', '否', 2, '', 1, 2, '2021-06-27 11:29:28', '2021-06-27 11:29:28');
INSERT INTO `system_dictionary_detail` VALUES (3, 2, '1', '正确', 1, '', 1, 2, '2021-06-27 11:29:28', '2021-06-27 11:29:28');
INSERT INTO `system_dictionary_detail` VALUES (4, 2, '0', '错误', 2, '', 1, 2, '2021-06-27 11:29:28', '2021-06-27 11:29:28');
INSERT INTO `system_dictionary_detail` VALUES (5, 3, '1', '启用', 1, '', 1, 2, '2021-06-27 11:29:28', '2021-06-27 11:29:28');
INSERT INTO `system_dictionary_detail` VALUES (6, 3, '0', '禁用', 2, '', 1, 2, '2021-06-27 11:29:28', '2021-06-27 11:29:28');
INSERT INTO `system_dictionary_detail` VALUES (7, 4, '0', '登录日志', 1, '', 1, 2, '2021-06-27 11:29:28', '2021-06-27 11:29:28');
INSERT INTO `system_dictionary_detail` VALUES (8, 4, '1', '操作日志', 2, '', 1, 2, '2021-06-27 11:29:28', '2021-06-27 11:29:28');
INSERT INTO `system_dictionary_detail` VALUES (9, 5, '0', '全部', 1, '', 1, 2, '2021-06-27 11:29:28', '2021-06-27 11:29:28');
INSERT INTO `system_dictionary_detail` VALUES (10, 5, '1', '自定义', 2, '', 1, 2, '2021-06-27 11:29:28', '2021-06-27 11:29:28');
INSERT INTO `system_dictionary_detail` VALUES (11, 5, '2', '本部门及以下部门', 3, '', 1, 2, '2021-06-27 11:29:28', '2021-06-27 11:29:28');
INSERT INTO `system_dictionary_detail` VALUES (12, 5, '3', '本部门', 4, '', 1, 2, '2021-06-27 11:29:28', '2021-06-27 11:29:28');
INSERT INTO `system_dictionary_detail` VALUES (13, 5, '4', '仅本人', 5, '', 1, 2, '2021-06-27 11:29:28', '2021-06-27 11:29:28');
INSERT INTO `system_dictionary_detail` VALUES (14, 6, '0', '男', 1, '', 1, 2, '2021-06-27 11:29:28', '2021-06-27 11:29:28');
INSERT INTO `system_dictionary_detail` VALUES (15, 6, '1', '女', 2, '', 1, 2, '2021-06-27 11:29:28', '2021-06-27 11:29:28');

-- ----------------------------
-- Table structure for system_job
-- ----------------------------
DROP TABLE IF EXISTS `system_job`;
CREATE TABLE `system_job`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '代码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建用户id',
  `create_department_id` bigint(20) NULL DEFAULT NULL COMMENT '创建部门id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统职位表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_job
-- ----------------------------
INSERT INTO `system_job` VALUES (1, '0001', '老总', '无', 1, 2, '2021-06-27 10:43:29', '2021-06-27 10:43:29');
INSERT INTO `system_job` VALUES (2, '0002', '经理', '无', 1, 2, '2021-06-27 10:43:29', '2021-06-27 10:43:29');
INSERT INTO `system_job` VALUES (3, '0003', '开发', '无', 1, 2, '2021-06-27 10:43:29', '2021-06-27 10:43:29');

-- ----------------------------
-- Table structure for system_log
-- ----------------------------
DROP TABLE IF EXISTS `system_log`;
CREATE TABLE `system_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'url',
  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '方法',
  `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '参数',
  `time` bigint(20) NULL DEFAULT NULL COMMENT '耗时,单位ms',
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ip地址',
  `log_type` enum('0','1') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型,0登录日志,1操作日志',
  `exception` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '异常',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_menu
-- ----------------------------
DROP TABLE IF EXISTS `system_menu`;
CREATE TABLE `system_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `parent_id` bigint(20) NOT NULL COMMENT '父级菜单id,不能为外键否则顶级菜单id为0则没有对应的',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问路径,为合法的URL时会自动在外链打开',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件文件位置',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标',
  `redirect` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '重定向地址,一般为上级菜单重定向到下级的某个菜单,为noredirect时在面包屑中不可被点击',
  `hidden` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '是否在侧边栏隐藏,一般像登录、404错误等不需要在侧边栏显示的需要设为true,默认为0,0否,1是',
  `always_show` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '是否总是显示,为false时当顶级菜单只有一个子菜单则子菜单会代替顶级菜单来显示顶级菜单不会显示,为false则可让顶级菜单一致显示而子菜单总在其下面,为最顶级菜单时应为1,为最低级菜单时必须为0否则不会有页面而变成一个父级菜单,默认为0,0否,1是',
  `no_cache` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '是否不缓存,一般经常变化的界面不需要缓存,默认为0,0否,1是',
  `breadcrumb` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '是否在面包屑显示,默认为1,0否,1是',
  `affix` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '是否固定在tags-view中,默认为0,0否,1是',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建用户id',
  `create_department_id` bigint(20) NULL DEFAULT NULL COMMENT '创建部门id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_menu
-- ----------------------------
INSERT INTO `system_menu` VALUES (1, 'system', 0, 1, '/system', 'Layout', 'system', '/system/user', '0', '1', '0', '1', '0', '系统管理', 1, 2, '2021-06-27 20:51:36', '2021-06-27 20:51:36');
INSERT INTO `system_menu` VALUES (2, 'monitor', 0, 2, '/monitor', 'Layout', 'monitor', '/monitor/log', '0', '1', '0', '1', '0', '系统监控', 1, 2, '2021-06-27 20:51:36', '2021-06-27 20:51:36');
INSERT INTO `system_menu` VALUES (3, 'link', 0, 3, '/link', 'Layout', 'link', '/link/official', '0', '1', '0', '1', '0', '友情链接', 1, 2, '2021-06-27 20:51:36', '2021-06-27 20:51:36');
INSERT INTO `system_menu` VALUES (4, 'menus', 0, 4, '/menus', 'Layout', 'menu', '/menus/menu1/menu1-1', '0', '1', '0', '1', '0', '菜单', 1, 2, '2021-06-27 20:51:36', '2021-06-27 20:51:36');
INSERT INTO `system_menu` VALUES (5, 'user', 1, 5, 'user', 'system/user/index', 'user', NULL, '0', '0', '0', '1', '0', '用户管理', 1, 2, '2021-06-27 20:51:36', '2021-06-27 20:51:36');
INSERT INTO `system_menu` VALUES (6, 'role', 1, 6, 'role', 'system/role/index', 'role', NULL, '0', '0', '0', '1', '0', '角色管理', 1, 2, '2021-06-27 20:51:36', '2021-06-27 20:51:36');
INSERT INTO `system_menu` VALUES (7, 'menu', 1, 7, 'menu', 'system/menu/index', 'menu', NULL, '0', '0', '0', '1', '0', '菜单管理', 1, 2, '2021-06-27 20:51:36', '2021-06-27 20:51:36');
INSERT INTO `system_menu` VALUES (8, 'permission', 1, 8, 'permission', 'system/permission/index', 'permission', NULL, '0', '0', '0', '1', '0', '权限管理', 1, 2, '2021-06-27 20:51:36', '2021-06-27 20:51:36');
INSERT INTO `system_menu` VALUES (9, 'department', 1, 9, 'department', 'system/department/index', 'department', NULL, '0', '0', '0', '1', '0', '部门管理', 1, 2, '2021-06-27 20:51:36', '2021-06-27 20:51:36');
INSERT INTO `system_menu` VALUES (10, 'job', 1, 10, 'job', 'system/job/index', 'job', NULL, '0', '0', '0', '1', '0', '职位管理', 1, 2, '2021-06-27 20:51:36', '2021-06-27 20:51:36');
INSERT INTO `system_menu` VALUES (11, 'dictionary', 1, 11, 'dictionary', 'system/dictionary/index', 'dictionary', NULL, '0', '0', '0', '1', '0', '字典管理', 1, 2, '2021-06-27 20:51:36', '2021-06-27 20:51:36');
INSERT INTO `system_menu` VALUES (12, 'log', 2, 12, 'log', 'monitor/log/index', 'log', NULL, '0', '0', '0', '1', '0', '日志监控', 1, 2, '2021-06-27 20:51:36', '2021-06-27 20:51:36');
INSERT INTO `system_menu` VALUES (13, 'officialSite', 3, 13, 'https://www.baidu.com', 'Layout', 'officialSite', NULL, '0', '0', '0', '1', '0', '官方网站', 1, 2, '2021-06-27 20:51:36', '2021-06-27 20:51:36');
INSERT INTO `system_menu` VALUES (14, 'menu1', 4, 14, 'menu1', 'menus/menu1/index', 'menu', '/menu/menu1/menu1-1', '0', '1', '0', '1', '0', '菜单1', 1, 2, '2021-06-27 20:51:36', '2021-06-27 20:51:36');
INSERT INTO `system_menu` VALUES (15, 'menu2', 4, 15, 'menu2', 'menus/menu2/index', 'menu', NULL, '0', '0', '0', '1', '0', '菜单2', 1, 2, '2021-06-27 20:51:36', '2021-06-27 20:51:36');
INSERT INTO `system_menu` VALUES (16, 'menu1-1', 14, 16, 'menu1-1', 'menus/menu1/menu1-1/index', 'menu', NULL, '0', '0', '0', '1', '0', '菜单1-1', 1, 2, '2021-06-27 20:51:36', '2021-06-27 20:51:36');
INSERT INTO `system_menu` VALUES (17, 'menu1-2', 14, 17, 'menu1-2', 'menus/menu1/menu1-2/index', 'menu', '/menu/menu1/menu1-2/menu1-2-1', '0', '1', '0', '1', '0', '菜单1-2', 1, 2, '2021-06-27 20:51:36', '2021-06-27 20:51:36');
INSERT INTO `system_menu` VALUES (18, 'menu1-3', 14, 18, 'menu1-3', 'menus/menu1/menu1-3/index', 'menu', NULL, '0', '0', '0', '1', '0', '菜单1-3', 1, 2, '2021-06-27 20:51:36', '2021-06-27 20:51:36');
INSERT INTO `system_menu` VALUES (19, 'menu1-2-1', 17, 19, 'menu1-2-1', 'menus/menu1/menu1-2/menu1-2-1/index', 'menu', NULL, '0', '0', '0', '1', '0', '菜单1-2-1', 1, 2, '2021-06-27 20:51:36', '2021-06-27 20:51:36');
INSERT INTO `system_menu` VALUES (20, 'menu1-2-2', 17, 20, 'menu1-2-2', 'menus/menu1/menu1-2/menu1-2-2/index', 'menu', NULL, '0', '0', '0', '1', '0', '菜单1-2-2', 1, 2, '2021-06-27 20:51:36', '2021-06-27 20:51:36');

-- ----------------------------
-- Table structure for system_permission
-- ----------------------------
DROP TABLE IF EXISTS `system_permission`;
CREATE TABLE `system_permission`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建用户id',
  `create_department_id` bigint(20) NULL DEFAULT NULL COMMENT '创建部门id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_permission
-- ----------------------------
INSERT INTO `system_permission` VALUES (1, 'USER_ADD', '增加用户', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (2, 'USER_DELETE', '删除用户', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (3, 'USER_UPDATE', '修改用户', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (4, 'USER_GET', '查询用户', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (5, 'ROLE_ADD', '增加角色', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (6, 'ROLE_DELETE', '删除角色', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (7, 'ROLE_UPDATE', '修改角色', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (8, 'ROLE_GET', '查询角色', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (9, 'MENU_ADD', '增加菜单', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (10, 'MENU_DELETE', '删除菜单', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (11, 'MENU_UPDATE', '修改菜单', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (12, 'MENU_GET', '查询菜单', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (13, 'PERMISSION_ADD', '增加权限', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (14, 'PERMISSION_DELETE', '删除权限', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (15, 'PERMISSION_UPDATE', '修改权限', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (16, 'PERMISSION_GET', '查询权限', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (17, 'DEPARTMENT_ADD', '增加部门', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (18, 'DEPARTMENT_DELETE', '删除部门', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (19, 'DEPARTMENT_UPDATE', '修改部门', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (20, 'DEPARTMENT_GET', '查询部门', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (21, 'JOB_ADD', '增加职位', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (22, 'JOB_DELETE', '删除职位', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (23, 'JOB_UPDATE', '修改职位', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (24, 'JOB_GET', '查询职位', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (25, 'DICTIONARY_ADD', '增加字典', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (26, 'DICTIONARY_DELETE', '删除字典', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (27, 'DICTIONARY_UPDATE', '修改字典', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (28, 'DICTIONARY_GET', '查询字典', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (29, 'DICTIONARY_DETAIL_ADD', '增加字典详情', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (30, 'DICTIONARY_DETAIL_DELETE', '删除字典详情', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (31, 'DICTIONARY_DETAIL_UPDATE', '修改字典详情', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (32, 'DICTIONARY_DETAIL_GET', '查询字典详情', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');
INSERT INTO `system_permission` VALUES (33, 'LOG_GET', '查询日志', 1, 2, '2021-06-27 20:41:09', '2021-06-27 20:41:09');

-- ----------------------------
-- Table structure for system_role
-- ----------------------------
DROP TABLE IF EXISTS `system_role`;
CREATE TABLE `system_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `data_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据权限,全部数据权限1,自定数据权限2,本部门数据权限3,本部门及以下数据权限4,仅本人数据权限5',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建用户id',
  `create_department_id` bigint(20) NULL DEFAULT NULL COMMENT '创建部门id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_role
-- ----------------------------
INSERT INTO `system_role` VALUES (1, 'adminRole', '0', '管理员角色', 1, 2, '2021-06-27 23:00:35', '2021-06-27 23:00:35');
INSERT INTO `system_role` VALUES (2, 'userRole', '0', '用户角色', 1, 2, '2021-06-27 23:00:35', '2021-06-27 23:00:35');
INSERT INTO `system_role` VALUES (3, 'roleRole', '0', '角色角色', 1, 2, '2021-06-27 23:00:35', '2021-06-27 23:00:35');
INSERT INTO `system_role` VALUES (4, 'menuRole', '0', '菜单角色', 1, 2, '2021-06-27 23:00:35', '2021-06-27 23:00:35');
INSERT INTO `system_role` VALUES (5, 'permissionRole', '0', '权限角色', 1, 2, '2021-06-27 23:00:35', '2021-06-27 23:00:35');
INSERT INTO `system_role` VALUES (6, 'departmentRole', '0', '部门角色', 1, 2, '2021-06-27 23:00:35', '2021-06-27 23:00:35');
INSERT INTO `system_role` VALUES (7, 'jobRole', '0', '职位角色', 1, 2, '2021-06-27 23:00:35', '2021-06-27 23:00:35');
INSERT INTO `system_role` VALUES (8, 'dictionaryRole', '0', '字典角色', 1, 2, '2021-06-27 23:00:35', '2021-06-27 23:00:35');
INSERT INTO `system_role` VALUES (9, 'dictionaryDetailRole', '0', '字典详情角色', 1, 2, '2021-06-27 23:00:35', '2021-06-27 23:00:35');
INSERT INTO `system_role` VALUES (10, 'logRole', '0', '日志角色', 1, 2, '2021-06-27 23:00:35', '2021-06-27 23:00:35');

-- ----------------------------
-- Table structure for system_role_department
-- ----------------------------
DROP TABLE IF EXISTS `system_role_department`;
CREATE TABLE `system_role_department`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色id',
  `department_id` bigint(20) NULL DEFAULT NULL COMMENT '部门id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统角色部门关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `system_role_menu`;
CREATE TABLE `system_role_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色id',
  `menu_id` bigint(20) NULL DEFAULT NULL COMMENT '菜单id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统角色菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_role_menu
-- ----------------------------
INSERT INTO `system_role_menu` VALUES (1, 1, 1);
INSERT INTO `system_role_menu` VALUES (2, 1, 2);
INSERT INTO `system_role_menu` VALUES (3, 1, 3);
INSERT INTO `system_role_menu` VALUES (4, 1, 4);
INSERT INTO `system_role_menu` VALUES (5, 1, 5);
INSERT INTO `system_role_menu` VALUES (6, 1, 6);
INSERT INTO `system_role_menu` VALUES (7, 1, 7);
INSERT INTO `system_role_menu` VALUES (8, 1, 8);
INSERT INTO `system_role_menu` VALUES (9, 1, 9);
INSERT INTO `system_role_menu` VALUES (10, 1, 10);
INSERT INTO `system_role_menu` VALUES (11, 1, 11);
INSERT INTO `system_role_menu` VALUES (12, 1, 12);
INSERT INTO `system_role_menu` VALUES (13, 1, 13);
INSERT INTO `system_role_menu` VALUES (14, 1, 14);
INSERT INTO `system_role_menu` VALUES (15, 1, 15);
INSERT INTO `system_role_menu` VALUES (16, 1, 16);
INSERT INTO `system_role_menu` VALUES (17, 1, 17);
INSERT INTO `system_role_menu` VALUES (18, 1, 18);
INSERT INTO `system_role_menu` VALUES (19, 1, 19);
INSERT INTO `system_role_menu` VALUES (20, 1, 20);

-- ----------------------------
-- Table structure for system_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `system_role_permission`;
CREATE TABLE `system_role_permission`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色id',
  `permission_id` bigint(20) NULL DEFAULT NULL COMMENT '权限id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统角色权限关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_role_permission
-- ----------------------------
INSERT INTO `system_role_permission` VALUES (1, 1, 1);
INSERT INTO `system_role_permission` VALUES (2, 1, 2);
INSERT INTO `system_role_permission` VALUES (3, 1, 3);
INSERT INTO `system_role_permission` VALUES (4, 1, 4);
INSERT INTO `system_role_permission` VALUES (5, 1, 5);
INSERT INTO `system_role_permission` VALUES (6, 1, 6);
INSERT INTO `system_role_permission` VALUES (7, 1, 7);
INSERT INTO `system_role_permission` VALUES (8, 1, 8);
INSERT INTO `system_role_permission` VALUES (9, 1, 9);
INSERT INTO `system_role_permission` VALUES (10, 1, 10);
INSERT INTO `system_role_permission` VALUES (11, 1, 11);
INSERT INTO `system_role_permission` VALUES (12, 1, 12);
INSERT INTO `system_role_permission` VALUES (13, 1, 13);
INSERT INTO `system_role_permission` VALUES (14, 1, 14);
INSERT INTO `system_role_permission` VALUES (15, 1, 15);
INSERT INTO `system_role_permission` VALUES (16, 1, 16);
INSERT INTO `system_role_permission` VALUES (17, 1, 17);
INSERT INTO `system_role_permission` VALUES (18, 1, 18);
INSERT INTO `system_role_permission` VALUES (19, 1, 19);
INSERT INTO `system_role_permission` VALUES (20, 1, 20);
INSERT INTO `system_role_permission` VALUES (21, 1, 21);
INSERT INTO `system_role_permission` VALUES (22, 1, 22);
INSERT INTO `system_role_permission` VALUES (23, 1, 23);
INSERT INTO `system_role_permission` VALUES (24, 1, 24);
INSERT INTO `system_role_permission` VALUES (25, 1, 25);
INSERT INTO `system_role_permission` VALUES (26, 1, 26);
INSERT INTO `system_role_permission` VALUES (27, 1, 27);
INSERT INTO `system_role_permission` VALUES (28, 1, 28);
INSERT INTO `system_role_permission` VALUES (29, 1, 29);
INSERT INTO `system_role_permission` VALUES (30, 1, 30);
INSERT INTO `system_role_permission` VALUES (31, 1, 31);
INSERT INTO `system_role_permission` VALUES (32, 1, 32);
INSERT INTO `system_role_permission` VALUES (33, 1, 33);

-- ----------------------------
-- Table structure for system_user
-- ----------------------------
DROP TABLE IF EXISTS `system_user`;
CREATE TABLE `system_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '加密盐',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像url',
  `enable` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '是否启用,0否,1是',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '性别,0男,1女,3未知',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `department_id` bigint(20) NOT NULL COMMENT '部门id',
  `create_user_id` bigint(20) NULL DEFAULT NULL COMMENT '创建用户id',
  `create_department_id` bigint(20) NULL DEFAULT NULL COMMENT '创建部门id',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_user
-- ----------------------------
INSERT INTO `system_user` VALUES (1, 'admin', '管理员', '{bcrypt}$2a$10$t/FryohjwP.1exfviFFq8uQzIlvlaK9U9pIt/gHXll.VuQMUomyf2', 'YzcmCZNvbXocrsz9dm8e', 'http://localhost:8080/fileUpload/defaultAvatar.png', '1', '123@qq.com', '1234567890', '1', '管理员用户', 2, 1, 2, '2021-06-27 15:38:19', '2021-06-27 23:51:37');

-- ----------------------------
-- Table structure for system_user_job
-- ----------------------------
DROP TABLE IF EXISTS `system_user_job`;
CREATE TABLE `system_user_job`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `job_id` bigint(20) NULL DEFAULT NULL COMMENT '职位id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户职位关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_user_job
-- ----------------------------
INSERT INTO `system_user_job` VALUES (1, 1, 1);
INSERT INTO `system_user_job` VALUES (2, 1, 2);
INSERT INTO `system_user_job` VALUES (3, 1, 3);

-- ----------------------------
-- Table structure for system_user_role
-- ----------------------------
DROP TABLE IF EXISTS `system_user_role`;
CREATE TABLE `system_user_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of system_user_role
-- ----------------------------
INSERT INTO `system_user_role` VALUES (1, 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
