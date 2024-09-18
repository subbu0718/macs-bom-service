CREATE TABLE `business_unit` (
  `id` int NOT NULL AUTO_INCREMENT,
  `business_unit_name` varchar(45) DEFAULT NULL,
  `user_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `bom_model` (
  `id` int NOT NULL AUTO_INCREMENT,
  `product_number` varchar(45) NOT NULL,
  `release_number` decimal(10,2) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `market` varchar(45) DEFAULT NULL,
  `short_description` varchar(100) DEFAULT NULL,
  `long_description` varchar(1000) DEFAULT NULL,
  `status` varchar(25) DEFAULT NULL,
  `business_unit_id` int NOT NULL,
  `taric_code` varchar(50) DEFAULT NULL,
  `taric_code_components` varchar(50) DEFAULT NULL,
  `dossier_number` varchar(50) DEFAULT NULL,
  `threer_status` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`id`,`product_number`,`release_number`),
  KEY `fk_bom_model_business_unit1_idx` (`business_unit_id`) /*!80000 INVISIBLE */,
  KEY `fk_bom_bom_model1_idx` (`product_number`,`release_number`),
  CONSTRAINT `fk_bom_model_business_unit1` FOREIGN KEY (`business_unit_id`) REFERENCES `business_unit` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `bom` (
  `id` int NOT NULL AUTO_INCREMENT,
  `part_number` varchar(50) DEFAULT NULL,
  `description` varchar(250) DEFAULT NULL,
  `quantity` decimal(10,2) DEFAULT NULL,
  `weight` decimal(10,2) DEFAULT NULL,
  `unit_of_measure` varchar(5) DEFAULT NULL,
  `morb` varchar(5) DEFAULT NULL,
  `supplier_code` varchar(10) DEFAULT NULL,
  `supplier_description` varchar(25) DEFAULT NULL,
  `bom_model_product_number` varchar(45) NOT NULL,
  `bom_model_release_number` decimal(10,2) NOT NULL,
  `mds_type` varchar(15) DEFAULT NULL,
  `mds_node_id` bigint DEFAULT NULL,
  `preliminary` varchar(5) DEFAULT 'No',
  PRIMARY KEY (`id`),
  KEY `fk_bom_bom_model1_idx` (`bom_model_product_number`,`bom_model_release_number`) /*!80000 INVISIBLE */
) ENGINE=InnoDB AUTO_INCREMENT=607 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `bom_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `status_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `business_unit_name` (
  `id` int NOT NULL AUTO_INCREMENT,
  `business_unit_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `mds_status` (
  `id` int NOT NULL AUTO_INCREMENT,
  `status_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `mds_type` (
  `id` int NOT NULL AUTO_INCREMENT,
  `mds_type_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
