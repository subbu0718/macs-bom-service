INSERT INTO `Macs_testing_DB`.`business_unit` (`id`, `business_unit_name`, `user_name`) VALUES ('1', 'Commercial Cars', 'macs_test');

INSERT INTO `Macs_testing_DB`.`business_unit_name` (`id`, `business_unit_name`) VALUES ('1', 'All BU');
INSERT INTO `Macs_testing_DB`.`business_unit_name` (`id`, `business_unit_name`) VALUES ('2', 'Commercial Cars');
INSERT INTO `Macs_testing_DB`.`business_unit_name` (`id`, `business_unit_name`) VALUES ('3', 'Race Cars');

INSERT INTO `Macs_testing_DB`.`bom_status` (`id`, `status_name`) VALUES ('1', 'History');
INSERT INTO `Macs_testing_DB`.`bom_status` (`id`, `status_name`) VALUES ('2', 'Active');
INSERT INTO `Macs_testing_DB`.`bom_status` (`id`, `status_name`) VALUES ('3', 'Sent to Scip');
INSERT INTO `Macs_testing_DB`.`bom_status` (`id`, `status_name`) VALUES ('4', 'Rejected by Scip');
INSERT INTO `Macs_testing_DB`.`bom_status` (`id`, `status_name`) VALUES ('5', 'Approved by Scip');

INSERT INTO `Macs_testing_DB`.`mds_status` (`id`, `status_name`) VALUES ('1', 'Accepted');
INSERT INTO `Macs_testing_DB`.`mds_status` (`id`, `status_name`) VALUES ('2', 'Rejected');
INSERT INTO `Macs_testing_DB`.`mds_status` (`id`, `status_name`) VALUES ('3', 'In process at recipient');
INSERT INTO `Macs_testing_DB`.`mds_status` (`id`, `status_name`) VALUES ('4', 'Preliminary');
INSERT INTO `Macs_testing_DB`.`mds_status` (`id`, `status_name`) VALUES ('5', 'Warning');

INSERT INTO `Macs_testing_DB`.`mds_type` (`id`, `mds_type_name`) VALUES ('1', 'Component');
INSERT INTO `Macs_testing_DB`.`mds_type` (`id`, `mds_type_name`) VALUES ('2', 'Semicomponent');
INSERT INTO `Macs_testing_DB`.`mds_type` (`id`, `mds_type_name`) VALUES ('3', 'Material');
