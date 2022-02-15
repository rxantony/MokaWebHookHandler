CREATE TABLE `event_source` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data_id` varchar(45) NOT NULL,
  `data_updated_at` timestamp(6) NOT NULL,
  `event_name` varchar(45) NOT NULL,
  `payload` text NOT NULL,
  `event_id` varchar(45) NOT NULL,
  `outlet_id` varchar(45) NOT NULL,
  `version` int NOT NULL,
  `timestamp` timestamp(6) NOT NULL,
  `created_at` timestamp(6) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `data_id_IDX` (`data_id`),
  KEY `data_id_updated_at_IDX` (`data_id`,`data_updated_at` DESC,`created_at`)
) ENGINE=InnoDB AUTO_INCREMENT=531 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;