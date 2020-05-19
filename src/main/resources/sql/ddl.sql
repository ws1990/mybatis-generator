DROP TABLE IF EXISTS `edu_clazz`;
CREATE TABLE `edu_clazz` (
  `id` bigint(36) NOT NULL,
  `name` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '班级名称',
  `code` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '班级编码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='班级表,class为关键字,所以使用clazz代替';


DROP TABLE IF EXISTS `edu_clazz_student`;
CREATE TABLE `edu_clazz_student` (
  `clazz_id` bigint(36) DEFAULT NULL COMMENT '班级ID',
  `student_id` bigint(36) DEFAULT NULL COMMENT '学生ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='班级学生关系表';


DROP TABLE IF EXISTS `edu_clazz_teacher`;
CREATE TABLE `edu_clazz_teacher` (
  `clazz_id` bigint(36) DEFAULT NULL COMMENT '班级ID',
  `teacher_id` bigint(36) DEFAULT NULL COMMENT '老师ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='班级老师关系表';


DROP TABLE IF EXISTS `edu_school`;
CREATE TABLE `edu_school` (
  `id` bigint(36) NOT NULL COMMENT 'ID',
  `name` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '学校名字',
  `code` varchar(45) CHARACTER SET utf8 DEFAULT NULL COMMENT '学校编码',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='学校表';


DROP TABLE IF EXISTS `edu_school_clazz`;
CREATE TABLE `edu_school_clazz` (
  `school_id` bigint(36) DEFAULT NULL COMMENT '学校ID',
  `clazz_id` bigint(36) DEFAULT NULL COMMENT '班级ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='学校班级关系表';


DROP TABLE IF EXISTS `edu_student`;
CREATE TABLE `edu_student` (
  `id` bigint(36) NOT NULL COMMENT 'ID',
  `username` varchar(16) CHARACTER SET utf8 NOT NULL COMMENT '姓名',
  `email` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '邮箱',
  `age` int(4) NOT NULL COMMENT '年龄',
  `gender` tinyint(1) DEFAULT NULL COMMENT '性别',
  `telephone` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '手机号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='学生表';


DROP TABLE IF EXISTS `edu_teacher`;
CREATE TABLE `edu_teacher` (
  `id` bigint(36) NOT NULL COMMENT 'ID',
  `username` varchar(16) CHARACTER SET utf8 NOT NULL COMMENT '姓名',
  `email` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '邮箱',
  `age` int(4) NOT NULL COMMENT '年龄',
  `gender` tinyint(1) DEFAULT NULL COMMENT '性别',
  `telephone` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '手机号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='老师表';
