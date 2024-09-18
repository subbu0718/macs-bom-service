CREATE TABLE `classification_name` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` varchar(2) DEFAULT NULL,
  `material_classification_code` varchar(12) DEFAULT NULL,
  `material_classification_name` varchar(100) DEFAULT NULL,
  `iso_language` varchar(2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_classification_name` (`iso_language`,`material_classification_code`) /*!80000 INVISIBLE */,
  KEY `idx_material_classification_code` (`material_classification_code`)
) ;


CREATE TABLE `comp_unit` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` varchar(2) DEFAULT NULL,
  `company_id` bigint DEFAULT NULL,
  `org_unit_id` bigint DEFAULT NULL,
  `org_unit_name` varchar(50) DEFAULT NULL,
  `org_unit_street` varchar(50) DEFAULT NULL,
  `org_unit_post_box` varchar(20) DEFAULT NULL,
  `org_unit_iso_country_code` varchar(2) DEFAULT NULL,
  `org_unit_postal_code` varchar(10) DEFAULT NULL,
  `org_unit_city` varchar(50) DEFAULT NULL,
  `deleted` tinyint DEFAULT NULL,
  `duns_number` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_comp_unit` (`company_id`,`org_unit_id`)
) ;



CREATE TABLE `company` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` varchar(2) DEFAULT NULL,
  `company_id` bigint DEFAULT NULL,
  `company_name` varchar(50) DEFAULT NULL,
  `street` varchar(50) DEFAULT NULL,
  `post_box` varchar(20) DEFAULT NULL,
  `iso_country_code` varchar(2) DEFAULT NULL,
  `postal_code` varchar(10) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `deleted` tinyint DEFAULT NULL,
  `oem` tinyint DEFAULT NULL,
  `duns_number` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_company` (`company_id`,`company_name`)
) ;


CREATE TABLE `component` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` varchar(1) DEFAULT NULL,
  `node_id` bigint NOT NULL,
  `description` varchar(250) DEFAULT NULL,
  `tolerance` decimal(6,3) DEFAULT NULL,
  `measured_weight` decimal(18,6) DEFAULT NULL,
  `calculated_weight` decimal(18,6) DEFAULT NULL,
  `weighted_unit` varchar(2) DEFAULT NULL,
  `part_item_no` varchar(50) DEFAULT NULL,
  `polymeric_parts_marking` int DEFAULT NULL,
  `preliminary` tinyint DEFAULT '0',
  `multi_source` tinyint DEFAULT '0',
  `production_eu` int DEFAULT NULL,
  `scip_no` varchar(36) DEFAULT NULL,
  `scip_submission_no` varchar(12) DEFAULT NULL,
  `automatic_scip` tinyint DEFAULT '0',
  `version` decimal(9,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_component` (`node_id`,`version`)
) ;


CREATE TABLE `component_article_category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` varchar(3) DEFAULT NULL,
  `node_id` bigint DEFAULT NULL,
  `article_category_id` bigint DEFAULT NULL,
  `version` decimal(9,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_coponent_article_category` (`node_id`,`article_category_id`,`version`) /*!80000 INVISIBLE */,
  KEY `idx_node_id` (`node_id`),
  KEY `idx_article_category_id` (`article_category_id`)
);


CREATE TABLE `contact` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` varchar(2) DEFAULT NULL,
  `company_id` bigint NOT NULL,
  `contact_id` int NOT NULL,
  `deleted` tinyint DEFAULT '0',
  `last_name` varchar(50) DEFAULT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `phone_number` varchar(50) DEFAULT NULL,
  `fax_number` varchar(50) DEFAULT NULL,
  `email_address` varchar(80) DEFAULT NULL,
  `department_name` varchar(50) DEFAULT NULL,
  `mailbox` varchar(20) DEFAULT NULL,
  `imds_contact` tinyint DEFAULT '0',
  `reach_contact` tinyint DEFAULT '0',
  `default_contact` tinyint DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_contact` (`company_id`,`contact_id`)
) ;


CREATE TABLE `material` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` varchar(1) DEFAULT NULL,
  `node_id` bigint NOT NULL,
  `supplier` varchar(2000) DEFAULT NULL,
  `material_classification_code` varchar(12) DEFAULT NULL,
  `supplier_material_no` varchar(50) DEFAULT NULL,
  `chemical_symbol` varchar(100) DEFAULT NULL,
  `standard_material_no` varchar(50) DEFAULT NULL,
  `preliminary` tinyint DEFAULT NULL,
  `version` decimal(9,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_material` (`node_id`,`version`)
) ;


CREATE TABLE `material_category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` varchar(3) DEFAULT NULL,
  `node_id` bigint NOT NULL,
  `material_category_id` decimal(20,0) DEFAULT NULL,
  `characteristics` varchar(2000) DEFAULT NULL,
  `version` decimal(9,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_material_category` (`node_id`,`material_category_id`,`version`) /*!80000 INVISIBLE */,
  KEY `idx_node_id` (`node_id`),
  KEY `idx_material_category_id` (`material_category_id`)
);


CREATE TABLE `material_classification` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` varchar(2) DEFAULT NULL,
  `material_classification_code` varchar(12) DEFAULT NULL,
  `leaf` tinyint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_material_classification` (`material_classification_code`,`leaf`)
) ;


CREATE TABLE `material_content_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` varchar(3) DEFAULT NULL,
  `immediate_parent_node_id` bigint DEFAULT NULL,
  `node_id` bigint DEFAULT NULL,
  `inorganic_min_value` decimal(7,4) DEFAULT NULL,
  `inorganic_max_value` decimal(7,4) DEFAULT NULL,
  `bio_based_min_value` decimal(7,4) DEFAULT NULL,
  `bio_based_max_value` decimal(7,4) DEFAULT NULL,
  `contains_recyclate` tinyint DEFAULT NULL,
  `primary_inorganic_min_value` decimal(7,4) DEFAULT NULL,
  `primary_inorganic_max_value` decimal(7,4) DEFAULT NULL,
  `recyclate_min_value` decimal(7,4) DEFAULT NULL,
  `recyclate_max_value` decimal(7,4) DEFAULT NULL,
  `mechanical_recyclate_min_value` decimal(7,4) DEFAULT NULL,
  `mechanical_recyclate_max_value` decimal(7,4) DEFAULT NULL,
  `mechanical_pre_consumer_recyclate_min_value` decimal(7,4) DEFAULT NULL,
  `mechanical_pre_consumer_recyclate_max_value` decimal(7,4) DEFAULT NULL,
  `mechanical_post_consumer_recyclate_min_value` decimal(7,4) DEFAULT NULL,
  `mechanical_post_consumer_recyclate_max_value` decimal(7,4) DEFAULT NULL,
  `chemical_recyclate_min_value` decimal(7,4) DEFAULT NULL,
  `chemical_recyclate_max_value` decimal(7,4) DEFAULT NULL,
  `chemical_pre_consumer_recyclate_min_value` decimal(7,4) DEFAULT NULL,
  `chemical_pre_consumer_recyclate_max_value` decimal(7,4) DEFAULT NULL,
  `chemical_post_consumer_recyclate_min_value` decimal(7,4) DEFAULT NULL,
  `chemical_post_consumer_recyclate_max_value` decimal(7,4) DEFAULT NULL,
  `chemical_pre_consumer_recyclate_mass_balanced` tinyint DEFAULT NULL,
  `chemical_post_consumer_recyclate_mass_balanced` tinyint DEFAULT NULL,
  `chemical_recycling_certification_id` int DEFAULT NULL,
  `primary_bio_based_min_value` decimal(7,4) DEFAULT NULL,
  `primary_bio_based_max_value` decimal(7,4) DEFAULT NULL,
  `secondary_bio_based_min_value` decimal(7,4) DEFAULT NULL,
  `secondary_bio_based_max_value` decimal(7,4) DEFAULT NULL,
  `secondary_bio_based_mass_balanced` tinyint DEFAULT NULL,
  `bio_based_certification_id` int DEFAULT NULL,
  `version` decimal(9,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_material_content_detail` (`immediate_parent_node_id`,`node_id`,`version`) /*!80000 INVISIBLE */,
  KEY `idx_node_id` (`node_id`)
);


CREATE TABLE `material_content_summarized` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` varchar(3) DEFAULT NULL,
  `immediate_parent_node_id` bigint DEFAULT NULL,
  `node_id` bigint DEFAULT NULL,
  `contains_recyclate` tinyint DEFAULT NULL,
  `mechanical_pre_consumer_recyclate_min_value` decimal(7,4) DEFAULT NULL,
  `mechanical_pre_consumer_recyclate_max_value` decimal(7,4) DEFAULT NULL,
  `mechanical_post_consumer_recyclate_min_value` decimal(7,4) DEFAULT NULL,
  `mechanical_post_consumer_recyclate_max_value` decimal(7,4) DEFAULT NULL,
  `chemical_pre_consumer_recyclate_min_value` decimal(7,4) DEFAULT NULL,
  `chemical_pre_consumer_recyclate_max_value` decimal(7,4) DEFAULT NULL,
  `chemical_post_consumer_recyclate_min_value` decimal(7,4) DEFAULT NULL,
  `chemical_post_consumer_recyclate_max_value` decimal(7,4) DEFAULT NULL,
  `secondary_bio_based_min_value` decimal(7,4) DEFAULT NULL,
  `secondary_bio_based_max_value` decimal(7,4) DEFAULT NULL,
  `version` decimal(9,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_material_content_summarized` (`immediate_parent_node_id`,`node_id`,`version`) /*!80000 INVISIBLE */,
  KEY `idx_node_id` (`node_id`)
) ;


CREATE TABLE `mdb_common` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` varchar(2) DEFAULT NULL,
  `module_id` bigint DEFAULT NULL,
  `version` decimal(9,2) DEFAULT NULL,
  `company_id` bigint DEFAULT NULL,
  `org_unit_id` bigint DEFAULT NULL,
  `contact_person_id` bigint DEFAULT NULL,
  `create_date` date DEFAULT NULL,
  `last_change_date` date DEFAULT NULL,
  `hidden` tinyint DEFAULT NULL,
  `release_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_mdb_common` (`module_id`,`version`) /*!80000 INVISIBLE */,
  KEY `idx_company_id` (`company_id`)
) ;


CREATE TABLE `module_information` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` varchar(2) DEFAULT NULL,
  `module_id` bigint DEFAULT NULL,
  `version` decimal(9,2) DEFAULT NULL,
  `top_level_node_id` bigint DEFAULT NULL,
  `preliminary` tinyint DEFAULT NULL,
  `deleted` tinyint DEFAULT NULL,
  `create_date` date DEFAULT NULL,
  `check_date` date DEFAULT NULL,
  `origin_node_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_module_information` (`module_id`,`version`),
  KEY `idx_node_id` (`module_id`)
) ;


CREATE TABLE `multilingual` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` varchar(2) DEFAULT NULL,
  `node_id` bigint NOT NULL,
  `iso_language` varchar(2) DEFAULT NULL,
  `name` varchar(250) DEFAULT NULL,
  `trade_name` varchar(100) DEFAULT NULL,
  `remark` varchar(4000) DEFAULT NULL,
  `version` decimal(9,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_multilingual` (`node_id`,`name`,`iso_language`,`version`) /*!80000 INVISIBLE */,
  KEY `idx_node_id` (`node_id`)
) ;


CREATE TABLE `norm_entry` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` varchar(2) DEFAULT NULL,
  `node_id` bigint DEFAULT NULL,
  `institute_id` bigint DEFAULT NULL,
  `norm_code` varchar(100) DEFAULT NULL,
  `norm_description` varchar(250) DEFAULT NULL,
  `version` decimal(9,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_norm_entry` (`node_id`,`norm_code`,`institute_id`,`version`) /*!80000 INVISIBLE */,
  KEY `idx_node_id` (`node_id`)
) ;


CREATE TABLE `recipient_spec` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` varchar(1) DEFAULT NULL,
  `top_level_node_id` bigint NOT NULL,
  `org_unit_id` bigint NOT NULL,
  `product_name` varchar(250) DEFAULT NULL,
  `report_no` varchar(20) DEFAULT NULL,
  `part_item_no` varchar(50) DEFAULT NULL,
  `purchase_order_no` varchar(20) DEFAULT NULL,
  `bill_of_delivery_no` varchar(30) DEFAULT NULL,
  `information_for_mds_supplier` varchar(10000) DEFAULT NULL,
  `last_change_date` date DEFAULT NULL,
  `supplier_code` varchar(20) DEFAULT NULL,
  `drawing_no` varchar(20) DEFAULT NULL,
  `drawing_dated` varchar(20) DEFAULT NULL,
  `drawing_change_level` varchar(30) DEFAULT NULL,
  `report_date` date DEFAULT NULL,
  `recipient_status_change_date` date DEFAULT NULL,
  `recipient_status_change_time` time DEFAULT NULL,
  `recipient_status` varchar(1) DEFAULT NULL,
  `transmission_date` date DEFAULT NULL,
  `forwarding_allowed` int DEFAULT NULL,
  `spare_part` int DEFAULT NULL,
  `version` decimal(9,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_recipient_spec` (`top_level_node_id`,`org_unit_id`,`version`) /*!80000 INVISIBLE */
)  ;


CREATE TABLE `rel_amount` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` varchar(2) DEFAULT NULL,
  `immediate_parent_node_id` bigint NOT NULL,
  `node_id` bigint DEFAULT NULL,
  `no_of_components_attached` decimal(12,6) DEFAULT NULL,
  `ra_sequence` int DEFAULT NULL,
  `version` decimal(9,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_rel_amount` (`node_id`,`immediate_parent_node_id`,`version`) /*!80000 INVISIBLE */,
  KEY `idx_node_id` (`node_id`)
)  ;


CREATE TABLE `rel_percentage` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` varchar(2) DEFAULT NULL,
  `immediate_parent_node_id` bigint NOT NULL,
  `node_id` bigint NOT NULL,
  `ingredient_value` decimal(12,9) DEFAULT NULL,
  `ingredient_min_percentage` decimal(12,9) DEFAULT NULL,
  `ingredient_max_percentage` decimal(12,9) DEFAULT NULL,
  `ingredient_type` int DEFAULT NULL,
  `confidential` tinyint DEFAULT NULL,
  `rp_sequence` int DEFAULT NULL,
  `application_id` bigint DEFAULT NULL,
  `node_id_of_parent_component` bigint DEFAULT NULL,
  `process_chemical` int DEFAULT NULL,
  `substance_of_high_concern` int DEFAULT NULL,
  `filler` int DEFAULT NULL,
  `version` decimal(9,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_rel_percentage` (`node_id`,`immediate_parent_node_id`,`version`),
  KEY `idx_node_id` (`node_id`)
)  ;


CREATE TABLE `rel_weight` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` varchar(2) DEFAULT NULL,
  `immediate_parent_node_id` bigint NOT NULL,
  `node_id` bigint NOT NULL,
  `weight` decimal(18,9) DEFAULT NULL,
  `weight_unit` varchar(2) DEFAULT NULL,
  `rw_sequence` int DEFAULT NULL,
  `applied_to_article_as` int DEFAULT NULL,
  `version` decimal(9,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_rel_weight` (`node_id`,`immediate_parent_node_id`,`version`) /*!80000 INVISIBLE */,
  KEY `idx_node_id` (`node_id`)
)  ;


CREATE TABLE `safe_use_instruction` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` varchar(3) DEFAULT NULL,
  `node_id` bigint DEFAULT NULL,
  `safe_use_instruction_required` tinyint DEFAULT NULL,
  `instruction_text` varchar(10000) DEFAULT NULL,
  `version` decimal(9,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_safe_use_instruction` (`node_id`,`version`)
) ;


CREATE TABLE `scip_article_category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` varchar(3) DEFAULT NULL,
  `article_category_id` bigint DEFAULT NULL,
  `description_1` varchar(1000) DEFAULT NULL,
  `description_2` varchar(1000) DEFAULT NULL,
  `description_3` varchar(1000) DEFAULT NULL,
  `description_4` varchar(1000) DEFAULT NULL,
  `description_5` varchar(1000) DEFAULT NULL,
  `description_6` varchar(1000) DEFAULT NULL,
  `description_7` varchar(1000) DEFAULT NULL,
  `description_8` varchar(1000) DEFAULT NULL,
  `description_9` varchar(1000) DEFAULT NULL,
  `description_10` varchar(1000) DEFAULT NULL,
  `category_code` varchar(50) DEFAULT NULL,
  `echa_id` bigint DEFAULT NULL,
  `obsolete` tinyint DEFAULT '0',
  `selectable` tinyint DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_scip_article_category` (`article_category_id`)
)  ;


CREATE TABLE `scip_material_category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` varchar(3) DEFAULT NULL,
  `identifier` bigint DEFAULT NULL,
  `scip_material_category_type` int DEFAULT NULL,
  `level_1` varchar(200) DEFAULT NULL,
  `level_2` varchar(200) DEFAULT NULL,
  `level_3` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_scip_material_category` (`identifier`)
)  ;


CREATE TABLE `semi_component` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` varchar(1) DEFAULT NULL,
  `node_id` bigint DEFAULT NULL,
  `article_name` varchar(250) DEFAULT NULL,
  `item_no` varchar(50) DEFAULT NULL,
  `specific_weight` decimal(12,6) DEFAULT NULL,
  `specific_weight_type` int DEFAULT NULL,
  `preliminary` tinyint DEFAULT NULL,
  `version` decimal(9,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_semi_component` (`node_id`,`version`)
) ;


CREATE TABLE `substance` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` varchar(1) DEFAULT NULL,
  `node_id` bigint NOT NULL,
  `cas_code` varchar(20) DEFAULT NULL,
  `eu_index` varchar(20) DEFAULT NULL,
  `einecs` varchar(20) DEFAULT NULL,
  `gadsl_duty_to_declare` tinyint DEFAULT NULL,
  `gadsl_is_unwanted` tinyint DEFAULT NULL,
  `gadsl_is_prohibited` tinyint DEFAULT NULL,
  `reach` tinyint DEFAULT NULL,
  `deleted` tinyint DEFAULT NULL,
  `hidden` tinyint DEFAULT NULL,
  `sunset_date` date DEFAULT NULL,
  `last_application_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_substance` (`node_id`)
)  ;


CREATE TABLE `substance_application` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` varchar(2) DEFAULT NULL,
  `substance_application_id` bigint DEFAULT NULL,
  `substance_application_text` varchar(400) DEFAULT NULL,
  `deleted` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_substance_application` (`substance_application_id`)
);


CREATE TABLE `substance_group` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` varchar(2) DEFAULT NULL,
  `substance_group_id` bigint DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `deleted` tinyint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_substance_group` (`substance_group_id`)
) ;


CREATE TABLE `substance_group_member` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` varchar(2) DEFAULT NULL,
  `substance_group_id` bigint DEFAULT NULL,
  `substance_node_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_substance_group_member` (`substance_node_id`,`substance_group_id`),
  KEY `idx_substance_node_id` (`substance_node_id`),
  KEY `idx_substance_group_id` (`substance_group_id`) /*!80000 INVISIBLE */
)  ;


CREATE TABLE `synonym` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `record_id` varchar(2) DEFAULT NULL,
  `substance_node_id` bigint NOT NULL,
  `synonym_id` int DEFAULT NULL,
  `iso_language` varchar(2) DEFAULT NULL,
  `synonym_name` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_synonym` (`synonym_id`,`iso_language`,`substance_node_id`) /*!80000 INVISIBLE */,
  KEY `idx_substance_node_id` (`substance_node_id`),
  KEY `idx_synonym_id` (`synonym_id`)
) ;

CREATE INDEX idx_part_item_no ON `Macs_testing_DB`.`component` (`part_item_no`);
CREATE INDEX idx_part_number ON `Macs_testing_DB`.`bom` (`part_number`);
CREATE INDEX idx_mds_node_id ON `Macs_testing_DB`.`bom` (`mds_node_id`);
CREATE INDEX idx_bom_model_product_number ON `Macs_testing_DB`.`bom` (`bom_model_product_number`);
CREATE INDEX idx_product_number ON `Macs_testing_DB`.`bom_model` (`product_number`);
CREATE INDEX idx_top_level_node_id ON `Macs_testing_DB`.`recipient_spec` (`top_level_node_id`);
CREATE INDEX idx_node_id ON `Macs_testing_DB`.`component` (`node_id`);
CREATE INDEX idx_immediate_parent_node_id ON `Macs_testing_DB`.`rel_weight` (`immediate_parent_node_id`);
CREATE INDEX idx_node_id ON `Macs_testing_DB`.`material` (`node_id`);
CREATE INDEX idx_node_id ON `Macs_testing_DB`.`semi_component` (`node_id`);
CREATE INDEX idx_immediate_parent_node_id ON `Macs_testing_DB`.`rel_percentage` (`immediate_parent_node_id`);
