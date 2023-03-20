CREATE TABLE `search_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `keyword` varchar(100) DEFAULT NULL,
  `search_cnt` int DEFAULT NULL,
  PRIMARY KEY (`id`)
);