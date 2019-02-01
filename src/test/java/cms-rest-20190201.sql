/*
MySQL Backup
Source Server Version: 5.7.25
Source Database: cms-rest
Date: 2019/2/1 15:40:12
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
--  Table structure for `core_dept`
-- ----------------------------
DROP TABLE IF EXISTS `core_dept`;
CREATE TABLE `core_dept` (
  `core_dept_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_core_dept_id` bigint(20) DEFAULT NULL,
  `dept_name` varchar(100) DEFAULT NULL,
  `dept_code` varchar(100) DEFAULT NULL,
  `dept_desc` varchar(500) DEFAULT NULL,
  `order_num` int(11) DEFAULT NULL,
  `del_flag` int(11) DEFAULT NULL,
  PRIMARY KEY (`core_dept_id`),
  KEY `fk_parent_core_dept_id` (`parent_core_dept_id`),
  CONSTRAINT `fk_parent_core_dept_id` FOREIGN KEY (`parent_core_dept_id`) REFERENCES `core_dept` (`core_dept_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `core_dictionaries`
-- ----------------------------
DROP TABLE IF EXISTS `core_dictionaries`;
CREATE TABLE `core_dictionaries` (
  `core_dict_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dict_code` varchar(255) DEFAULT NULL,
  `dict_value` varchar(255) DEFAULT NULL,
  `core_dict_group_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`core_dict_id`),
  KEY `core_dictionaries_` (`core_dict_group_id`),
  CONSTRAINT `core_dictionaries_` FOREIGN KEY (`core_dict_group_id`) REFERENCES `core_dictionaries_group` (`core_dict_group_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `core_dictionaries_group`
-- ----------------------------
DROP TABLE IF EXISTS `core_dictionaries_group`;
CREATE TABLE `core_dictionaries_group` (
  `core_dict_group_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dict_group_name` varchar(255) DEFAULT NULL,
  `dict_group_code` varchar(255) DEFAULT NULL,
  `dict_group_desc` varchar(255) DEFAULT NULL,
  `parent_core_dict_group_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`core_dict_group_id`),
  UNIQUE KEY `index_core_dictionaries_group_dict_group_code` (`dict_group_code`) USING BTREE,
  KEY `fk_core_dictionaries_group_parent_core_dict_group_id` (`parent_core_dict_group_id`),
  CONSTRAINT `fk_core_dictionaries_group_parent_core_dict_group_id` FOREIGN KEY (`parent_core_dict_group_id`) REFERENCES `core_dictionaries_group` (`core_dict_group_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `core_menu`
-- ----------------------------
DROP TABLE IF EXISTS `core_menu`;
CREATE TABLE `core_menu` (
  `core_menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `menu_name` varchar(255) DEFAULT NULL,
  `menu_type` varchar(255) DEFAULT NULL,
  `menu_url` varchar(255) DEFAULT NULL,
  `permission` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `order_num` int(11) DEFAULT NULL,
  `parent_core_menu_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`core_menu_id`),
  KEY `fk_parent_core_menu_id` (`parent_core_menu_id`),
  CONSTRAINT `fk_parent_core_menu_id` FOREIGN KEY (`parent_core_menu_id`) REFERENCES `core_menu` (`core_menu_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `core_parameter`
-- ----------------------------
DROP TABLE IF EXISTS `core_parameter`;
CREATE TABLE `core_parameter` (
  `core_param_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `param_name` varchar(255) DEFAULT NULL,
  `param_type` varchar(10) DEFAULT NULL,
  `param_code` varchar(255) DEFAULT NULL,
  `param_value` varchar(255) DEFAULT NULL,
  `param_desc` varchar(255) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL,
  PRIMARY KEY (`core_param_id`),
  UNIQUE KEY `index_core_parameter_param_code` (`param_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `core_role`
-- ----------------------------
DROP TABLE IF EXISTS `core_role`;
CREATE TABLE `core_role` (
  `core_role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL,
  PRIMARY KEY (`core_role_id`),
  UNIQUE KEY `index_core_role_role` (`role`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `core_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `core_role_menu`;
CREATE TABLE `core_role_menu` (
  `core_menu_id` bigint(20) NOT NULL,
  `core_role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`core_menu_id`,`core_role_id`),
  KEY `fk_core_role_id_role` (`core_role_id`),
  CONSTRAINT `fk_core_menu_id_menu` FOREIGN KEY (`core_menu_id`) REFERENCES `core_menu` (`core_menu_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_core_role_id_role` FOREIGN KEY (`core_role_id`) REFERENCES `core_role` (`core_role_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `core_user`
-- ----------------------------
DROP TABLE IF EXISTS `core_user`;
CREATE TABLE `core_user` (
  `core_user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `real_name` varchar(255) DEFAULT NULL,
  `sex` varchar(255) DEFAULT NULL,
  `birthday` date DEFAULT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `core_dept_id` bigint(20) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `modified_time` datetime DEFAULT NULL,
  PRIMARY KEY (`core_user_id`),
  KEY `fk_user_core_dept_id` (`core_dept_id`),
  CONSTRAINT `fk_user_core_dept_id` FOREIGN KEY (`core_dept_id`) REFERENCES `core_dept` (`core_dept_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `core_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `core_user_role`;
CREATE TABLE `core_user_role` (
  `core_user_id` bigint(20) NOT NULL,
  `core_role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`core_user_id`,`core_role_id`),
  KEY `fk_core_role_id_role_2` (`core_role_id`),
  CONSTRAINT `fk_core_role_id_role_2` FOREIGN KEY (`core_role_id`) REFERENCES `core_role` (`core_role_id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_core_user_id_user_2` FOREIGN KEY (`core_user_id`) REFERENCES `core_user` (`core_user_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records 
-- ----------------------------
INSERT INTO `core_dept` VALUES ('0',NULL,'全国',NULL,'顶级节点','0','0'), ('2','0','湖南省','43','','16',NULL), ('3','2','长沙市','4301','','14',NULL), ('4','3','望城区','430122','','16',NULL), ('5','3','开福区','430121','','15',NULL), ('6','2','株洲市','4302','','2',NULL), ('7','0','湖北省','42','','2',NULL), ('8','7','武汉市','4201','','2',NULL);
INSERT INTO `core_dictionaries` VALUES ('1','sex.0','未知','10'), ('2','sex.1','男','10'), ('3','sex.2','女','10'), ('4','usr','用户参数','12'), ('5','sys','系统参数','12'), ('6','coreUserStatus.0','激活','13'), ('7','coreUserStatus.1','禁用','13');
INSERT INTO `core_dictionaries_group` VALUES ('0','根节点',NULL,'根节点',NULL), ('9','系统字典组','coreDictGroup','','0'), ('10','性别字典组','sex','','9'), ('12','系统参数类型','paramType','','9'), ('13','用户状态','coreUserStatus','','9');
INSERT INTO `core_menu` VALUES ('0','cms-rest管理系统','0',NULL,NULL,NULL,'0',NULL), ('3','系统管理','0',NULL,NULL,'fa fa-wrench','12','0'), ('4','菜单管理','1','/Sys/Menu',NULL,'fa fa-tasks','13','3'), ('11','用户管理','1','/Sys/User','','fa fa-id-card','14','3'), ('14','角色管理','1','/Sys/Role','','fa fa-male','0','3'), ('15','部门管理','1','/Sys/Dept','','fa fa-address-book','15','3'), ('16','参数管理','1','/Sys/Param','','fa fa-unlock-alt','16','3'), ('17','字典组管理','1','/Sys/DictGroup','','fa fa-glass','17','3'), ('18','字典管理','1','/Sys/Dict','','fa fa-clone','18','3'), ('19','系统监控','0','','','fa fa-bookmark','20','0'), ('20','数据监控','1','iframe:/druid/login.html','','fa fa-bug','21','19'), ('21','接口文档','1','iframe:/swagger-ui.html','','fa fa-book','24','0'), ('22','查找','2','','ROLE_CORE_MENU_EDIT_VIEW','','0','4'), ('23','删除','2','','ROLE_CORE_MENU_DEL','','0','4'), ('24','保存或修改','2','','ROLE_CORE_MENU_ADD_EDIT','','0','4'), ('25','查找','2','','ROLE_CORE_ROLE_EDIT_VIEW','','0','14'), ('26','删除','2','','ROLE_CORE_ROLE_DEL','','0','14'), ('27','角色和菜单','2','','ROLE_CORE_ROLE_MENU_SAVE','','0','14'), ('28','保存或修改','2','','ROLE_CORE_ROLE_ADD_EDIT','','0','14'), ('29','列表','2','','ROLE_CORE_ROLE_LIST','','0','14'), ('30','列表','2','','ROLE_CORE_USER_LIST','','0','11'), ('31','保存或修改','2','','ROLE_CORE_USER_ADD_EDIT','','0','11'), ('32','删除','2','','ROLE_CORE_USER_DEL','','0','11'), ('33','查找','2','','ROLE_CORE_USER_EDIT_VIEW','','0','11'), ('34','查找','2','','ROLE_CORE_DEPT_EDIT_VIEW','','0','15'), ('35','保存或修改','2','','ROLE_CORE_DEPT_ADD_EDIT','','0','15'), ('36','删除','2','','ROLE_CORE_DEPT_DEL','','0','15'), ('37','列表','2','','ROLE_CORE_PARAM_LIST','','0','16'), ('38','保存或修改','2','','ROLE_CORE_PARAM_ADD_EDIT','','0','16'), ('39','删除','2','','ROLE_CORE_PARAM_DEL','','0','16'), ('40','查找','2','','ROLE_CORE_PARAM_EDIT_VIEW','','0','16'), ('41','查找','2','','ROLE_CORE_DICTGROUP_EDIT_VIEW','','0','17'), ('42','保存或修改','2','','ROLE_CORE_DICTGROUP_ADD_EDIT','','0','17'), ('43','删除','2','','ROLE_CORE_DICTGROUP_DEL','','0','17'), ('44','列表','2','','ROLE_CORE_DICT_LIST','','0','18'), ('45','保存或修改','2','','ROLE_CORE_DICT_ADD_EDIT','','0','18'), ('46','删除','2','','ROLE_CORE_DICT_DEL','','0','18'), ('47','查找','2','','ROLE_CORE_DICT_EDIT_VIEW','','0','18'), ('48','密码重置','2','','ROLE_CORE_USER_RESET','','0','11');
INSERT INTO `core_parameter` VALUES ('3','druid的IP白名单','sys','druid_allow','127.0.0.1','','2018-12-05 19:55:58','2018-12-05 19:55:58'), ('4','druid的IP黑名单','sys','druid_deny','','','2018-12-05 19:57:26','2018-12-05 19:57:26'), ('5','druid的管理用户','sys','druid_login_username','druid','','2018-12-05 19:59:17','2018-12-05 19:59:17'), ('6','druid的密码','sys','druid_login_password','123456','','2018-12-05 19:59:48','2018-12-05 19:59:48'), ('7','druid的是否能够重置数据','sys','druid_reset_enable','false','','2018-12-05 20:00:34','2018-12-05 20:00:34'), ('8','druid的过滤规则','sys','druid_url_pattern','/*','','2018-12-05 20:02:05','2018-12-05 20:02:05'), ('9','druid的过滤格式','sys','druid_exclusions','*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*,','','2018-12-05 20:02:44','2018-12-05 20:02:44'), ('10','1231','usr','123','123','1231','2019-01-29 23:00:27','2019-01-29 23:00:27'), ('11','1231','usr','1231231','123','1231','2019-01-29 23:00:38','2019-01-29 23:00:38'), ('12','123123','usr','12311231','123','1231','2019-01-29 23:00:45','2019-01-29 23:00:45'), ('13','123123','usr','123111','1231','123123','2019-01-29 23:00:54','2019-01-29 23:00:54');
INSERT INTO `core_role` VALUES ('1','超级管理员','superadmin','2005-10-11 23:58:31','2005-10-11 23:58:31'), ('2','管理员','admin','2019-01-27 15:30:17','2019-01-27 15:34:29'), ('4','区域管理员','contryAdmin','2019-01-29 13:59:29','2019-01-29 14:45:24');
INSERT INTO `core_role_menu` VALUES ('3','1'), ('4','1'), ('11','1'), ('14','1'), ('15','1'), ('16','1'), ('17','1'), ('18','1'), ('19','1'), ('20','1'), ('21','1'), ('22','1'), ('23','1'), ('24','1'), ('25','1'), ('26','1'), ('27','1'), ('28','1'), ('29','1'), ('30','1'), ('31','1'), ('32','1'), ('33','1'), ('34','1'), ('35','1'), ('36','1'), ('37','1'), ('38','1'), ('39','1'), ('40','1'), ('41','1'), ('42','1'), ('43','1'), ('44','1'), ('45','1'), ('46','1'), ('47','1'), ('48','1'), ('3','2'), ('4','2'), ('11','2'), ('14','2'), ('15','2'), ('16','2'), ('17','2'), ('18','2'), ('22','2'), ('23','2'), ('24','2'), ('25','2'), ('29','2'), ('30','2'), ('31','2'), ('33','2'), ('34','2'), ('35','2'), ('37','2'), ('38','2'), ('40','2'), ('41','2'), ('42','2'), ('43','2'), ('44','2'), ('45','2'), ('46','2'), ('47','2'), ('48','2'), ('3','4'), ('11','4'), ('15','4');
INSERT INTO `core_user` VALUES ('11','superadmin','$2a$10$Qq5n4Z29dYJJbpycRv8bweoKEzbp6umzXP9ZBrpuRl5xCfTSjwbaW','超级管理员','sex.1','2005-10-10','13211112222','yuanshang@5563@163.com','最高权限用户','coreUserStatus.1','4','2005-10-11 23:58:31','2019-01-29 19:26:21'), ('12','kaifu','$2a$10$4M/SxKasO7VJNVWVyRBeuOYxD4xH7aymJPKbVxDhxkcJc5SF78hC2','开福区管理员','sex.1','2005-10-10','13211112222','yuanshang@5563@163.com','','coreUserStatus.1','5','2005-10-11 23:58:31','2019-01-29 15:35:17'), ('13','admin','$2a$10$jn686gYAafIrxDT9FXDoaOPsTVoBqreEEyzZ7vsQJ0hfpzJjQmBQS','管理员','sex.1','2018-12-29','13211112222','11@163.com','','coreUserStatus.0','4','2019-01-29 14:03:18','2019-01-29 16:14:16'), ('14','1231123123','123456','1231','sex.2','2018-12-29','13311112222','11@qq.com','','coreUserStatus.0','4','2019-01-29 14:03:18','2019-01-29 16:14:40'), ('15','12311231231','1','1231','sex.2','2018-12-29','13211112222','11@qq.com','','coreUserStatus.0','4','2019-01-29 14:03:18','2019-01-29 16:15:10'), ('16','123111','123456','1231','sex.0','2018-12-29','11','11','','coreUserStatus.0','4','2019-01-29 14:03:18','2019-01-29 14:46:21'), ('17','1231','123456','test','sex.0','2018-12-29','13311112222','11@qq.com','','coreUserStatus.0','4','2019-01-29 14:31:05','2019-01-29 15:35:05'), ('18','countryWcAdmin','$2a$10$yJh.qv7oWj1zEAvKeG5oM.gfhS3Pp6toAovjgr1RG.PWOt5nH28PG','望城区管理员','sex.1','2018-12-30','13211112222','11@qq.com','','coreUserStatus.0','4','2019-01-29 14:34:25','2019-01-31 23:12:55'), ('21','123123','11','1123','sex.0','2018-12-31','18211112222','123@126.com','','coreUserStatus.0','4','2019-01-29 15:50:37','2019-01-29 15:47:27'), ('22','1231231231','1231','11','sex.0','2019-01-01','14322221111','123@11.com','','coreUserStatus.0','5','2019-01-29 15:53:14','2019-01-29 15:52:31'), ('23','123222','1','23123','sex.0','2019-01-02','14411112222','11@qq.com','','coreUserStatus.0','5','2019-01-29 15:56:18','2019-01-29 15:55:43'), ('24','1','123123','11','sex.0',NULL,'13211112222','123@qq.xom','','coreUserStatus.0','4','2019-01-29 15:57:22','2019-01-29 15:56:57'), ('25','1232323','11','11','sex.0',NULL,'18811112222','11@qq.com','','coreUserStatus.0','5','2019-01-29 15:57:47','2019-01-29 15:57:25'), ('26','12312312311231231','1','111','sex.0',NULL,'13333331111','11@qq.com','','coreUserStatus.0','4','2019-01-29 15:58:16','2019-01-29 15:57:50'), ('27','123123123','1','11','sex.1',NULL,'13211112222','11@qq.com','','coreUserStatus.0','4','2019-01-29 15:58:43','2019-01-29 15:58:18');
INSERT INTO `core_user_role` VALUES ('11','1'), ('13','2'), ('17','2'), ('22','2'), ('12','4'), ('18','4');
