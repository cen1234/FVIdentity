-----------------------------------------------------------------------------------
-- TABLE USER --
-----------------------------------------------------------------------------------
DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `role` VARCHAR(50) NULL COMMENT '角色',
  `password` VARCHAR(50) NOT NULL COMMENT '密码',
  `realname` VARCHAR(50) NULL COMMENT '真实姓名',
  `sex` CHAR(2) NULL,
  `age` INT(3) NULL COMMENT '年龄',
  `email` VARCHAR(50)  NULL COMMENT '邮箱',
  `phone` VARCHAR(50)  NULL COMMENT '电话',
  `address` VARCHAR(255)  NULL COMMENT '地址',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  CONSTRAINT `sex` CHECK (`sex` IN('男','女'))
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';


--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'哈哈哈',NULL,'123456','张三','男',14,'123@163.com','18709261628','西安市',NULL);
INSERT INTO `user` VALUES(2,'想遛狗',NULL,'123456','李四','男',34,'123@163.com','18709261628','西安市',NULL);
INSERT INTO `user` VALUES(3,'精神状态',NULL,'123456','王五','男',23,'123@163.com','18709261628','西安市',NULL);
INSERT INTO `user` VALUES(4,'想回家',NULL,'123456','六六','女',21,'123@163.com','18709261628','西安市',NULL);
UNLOCK TABLES;



----------------------------------------------------------------------------------------------------
-- TABLE ROLE ---
----------------------------------------------------------------------------------------------------
DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` VARCHAR(50) NULL COMMENT '名称',
  `description` VARCHAR(255) NULL COMMENT '描述',
  `role_key` VARCHAR(50)  NULL COMMENT '唯一标识',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='角色表';


--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES(1,'管理员','所有权限','admin',NULL);
INSERT INTO `role` VALUES(2,'普通用户','部分权限','user',NULL);
UNLOCK TABLES;




--------------------------------------------------------------
--- TABLE MENU ----
--------------------------------------------------------------
DROP TABLE IF EXISTS `menu`;

CREATE TABLE `menu` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `pid` INT(11) NULL COMMENT '父级id',
  `name` VARCHAR(255) NULL COMMENT '名称',
  `description` VARCHAR(255) NULL COMMENT '描述',
  `path` VARCHAR(255) NULL COMMENT '路径',
  `icon` VARCHAR(255) NULL COMMENT '图标',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';


--
-- Dumping data for table `menu`
--

LOCK TABLES `menu` WRITE;
/*!40000 ALTER TABLE `menu` DISABLE KEYS */;
INSERT INTO `menu` VALUES(1,NULL,'用户管理','管理注册的所有用户','/user',NULL,NULL);
INSERT INTO `menu` VALUES(3,2,'单个识别','识别一张图片中的一种果蔬','/single',NULL,NULL);
UNLOCK TABLES;



----------------------------------------------------------------------------------
---TABLE DIST ----
----------------------------------------------------------------------------------
DROP TABLE IF EXISTS `dist`;

CREATE TABLE `dist` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` VARCHAR(255) NULL COMMENT '名称',
  `value` VARCHAR(255) NULL COMMENT '值',
  `type` VARCHAR(255) NULL COMMENT '类型',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='图标表';


--
-- Dumping data for table `dist`
--

LOCK TABLES `dist` WRITE;
/*!40000 ALTER TABLE `dist` DISABLE KEYS */;
INSERT INTO `dist` VALUES(1,'role','el-icon-plus','icon',NULL);
INSERT INTO `dist` VALUES(2,'user','el-icon-user','icon',NULL);
INSERT INTO `dist` VALUES(3,'log','el-icon-edit-outline','icon',NULL);
INSERT INTO `dist` VALUES(4,'single','el-icon-apple','icon',NULL);
INSERT INTO `dist` VALUES(5,'similar','el-icon-apple','icon',NULL);
INSERT INTO `dist` VALUES(6,'multiple','el-icon-grape','icon',NULL);
INSERT INTO `dist` VALUES(7,'fv','el-icon-grape','icon',NULL);
INSERT INTO `dist` VALUES(8,'statistical','el-icon-pie-chart','icon',NULL);
UNLOCK TABLES;



-----------------------------------------------------------------------------
-----TABLE ROLE-MENU ------
-----------------------------------------------------------------------------
DROP TABLE IF EXISTS `role_menu`;

CREATE TABLE `role_menu` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_id` INT NOT NULL COMMENT '角色id',
  `menu_id` INT  NULL COMMENT '菜单id',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='角色-菜单对应表';


--
-- Dumping data for table `role_menu`
--

LOCK TABLES `role_menu` WRITE;
/*!40000 ALTER TABLE `role_menu` DISABLE KEYS */;
INSERT INTO `role_menu` VALUES(1,1,1);
INSERT INTO `role_menu` VALUES(2,1,2);
UNLOCK TABLES;`menu``role_menu``menu`




-----------------------------------------------------------------
----TABLE PICTURE-----
-----------------------------------------------------------------
CREATE TABLE `picture` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `name` VARCHAR(255) NULL COMMENT '图片名称',
  `type` VARCHAR(255) NULL COMMENT '文件类型',
  `size` BIGINT(20) NULL COMMENT '文件大小',
  `url`  VARCHAR(255) NULL COMMENT '下载链接',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='图片表';


--------------------------------------------------------------------
--TABLE SINGLE --
--------------------------------------------------------------------
DROP TABLE IF EXISTS `single`;

CREATE TABLE `single` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `log_id` VARCHAR(50) NULL COMMENT '日志id',
  `username` VARCHAR(255) NULL COMMENT '用户',
  `name` VARCHAR(50)  NULL COMMENT '名字',
  `score` DOUBLE(10,10) NULL COMMENT '分数',
  `description` VARCHAR(1000) NULL COMMENT '描述',
  `benefit` VARCHAR(1000) NULL COMMENT '益处',
  `price` DOUBLE(10,2) NULL COMMENT '价格',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='单个果蔬识别表';




--------------------------------------------------------------------------
-----TABLE LOG -----
--------------------------------------------------------------------------
DROP TABLE IF EXISTS `log`;

CREATE TABLE `log` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `record_id` VARCHAR(30) NULL COMMENT '日志id',
  `username` VARCHAR(255) NULL COMMENT '用户名',
   `path` VARCHAR(255) NULL COMMENT '访问路径',
  `photo` VARCHAR(255) NULL COMMENT '识别图片',
  `resule_num` INT(11) NULL COMMENT '识别结果数',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='日志表';



----------------------------------------------------------------------------
----TABLE  MULTIPLE-----
----------------------------------------------------------------------------
DROP TABLE IF EXISTS `multiple`;

CREATE TABLE `multiple` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `log_id` VARCHAR(50) NULL COMMENT '日志id',
  `username` VARCHAR(255) NULL COMMENT '用户',
  `name` VARCHAR(50)  NULL COMMENT '名字',
  `score` DOUBLE(10,10) NULL COMMENT '分数',
  `type` CHAR(10) NULL COMMENT '类型',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  CONSTRAINT `type` CHECK (`type` IN('水果','蔬菜','非果蔬'))
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='多个果蔬识别表';