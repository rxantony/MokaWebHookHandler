  CREATE TABLE `lock_tracker` (
    `conn_Id` int NOT NULL,
    `trx_id` int NOT NULL,
    `event_id` varchar(45) NOT NULL,
    `event_name` varchar(100) NOT NULL,
    `data_id` varchar(45) NOT NULL,
    `query` varchar(100) NOT NULL,
    `trx_started` timestamp NOT NULL,
    `created_at` timestamp(6) NOT NULL,
    PRIMARY KEY (`conn_Id`,`created_at`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;