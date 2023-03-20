CREATE TABLE `search_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `keyword` varchar(100) DEFAULT NULL,
  `count` int DEFAULT NULL,
  PRIMARY KEY (`id`)
);