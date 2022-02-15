CREATE TABLE `lock_tracker` (
  `connId` int NOT NULL,
  `event_id` varchar(45) NOT NULL,
  `data_id` varchar(45) NOT NULL,
  `query` varchar(100) NOT NULL,
  `created_at` timestamp(6) NOT NULL,
  PRIMARY KEY (`connId`,`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;