CREATE TABLE `dead_letter` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `source` varchar(125) NOT NULL,
  `event_id` varchar(45) DEFAULT NULL,
  `payload` text,
  `properties` text,
  `reason` text,
  `created_at` timestamp(6) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
