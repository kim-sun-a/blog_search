CREATE TABLE `search_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `keyword` varchar(100) DEFAULT NULL,
  `search_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
);