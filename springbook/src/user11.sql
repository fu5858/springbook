DROP TABLE IF EXISTS `springbookwithtest`.`users11`;
CREATE TABLE  `springbookwithtest`.`users11` (
  `id` varchar(10) NOT NULL DEFAULT '' COMMENT 'User ID',
  `name` varchar(20) NOT NULL COMMENT 'User Name',
  `password` varchar(20) NOT NULL COMMENT 'User Password',
  `level` tinyint(4) NOT NULL COMMENT 'User Level',
  `login` int(11) NOT NULL COMMENT 'Number of Login',
  `recommend` int(11) NOT NULL COMMENT 'Number of recommedation',
  `email` varchar(50) NOT NULL COMMENT 'Email Address',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;