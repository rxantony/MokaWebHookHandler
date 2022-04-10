CREATE TABLE `product_mapping` (
  `id` varchar(45) NOT NULL,
  `moka_item_id` varchar(45) NOT NULL,
  `jurnal_id` varchar(45) DEFAULT NULL,
  `name` varchar(25) NOT NULL,
  `description` varchar(50) DEFAULT NULL,
  `created_at` timestamp NOT NULL,
  `updated_at` timestamp NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
