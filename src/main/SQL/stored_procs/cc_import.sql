DELIMITER $$
CREATE DEFINER=`macs_production_user`@`%` PROCEDURE `cc_import`()
BEGIN

	DECLARE totalRecords BIGINT DEFAULT 0;
	DECLARE ccId BIGINT DEFAULT 0;
	DECLARE field1 VARCHAR(2) DEFAULT NULL;
    DECLARE field2 VARCHAR(50) DEFAULT NULL;
    DECLARE field3 VARCHAR(50) DEFAULT NULL;
    DECLARE field4 VARCHAR(50) DEFAULT NULL;
    DECLARE field5 VARCHAR(50) DEFAULT NULL;
    DECLARE field6 VARCHAR(50) DEFAULT NULL;
    DECLARE field7 VARCHAR(50) DEFAULT NULL;
    DECLARE field8 VARCHAR(50) DEFAULT NULL;
    DECLARE field9 VARCHAR(80) DEFAULT NULL;
    DECLARE field10 VARCHAR(50) DEFAULT NULL;
    DECLARE field11 VARCHAR(50) DEFAULT NULL;
    DECLARE field12 VARCHAR(50) DEFAULT NULL;
    DECLARE field13 VARCHAR(50) DEFAULT NULL;
    DECLARE field14 VARCHAR(50) DEFAULT NULL;
    DECLARE offsetCounter BIGINT;
    DECLARE limitCounter INT DEFAULT 10000;
    DECLARE finished INT DEFAULT 0;
	DECLARE whileLoopCounter INT;
    
  
	SELECT COUNT(*) FROM macs_staging_db.cc_staging INTO totalRecords; 

	SET offsetCounter = 0;
    SET whileLoopCounter = 0;
	
	WHILE offsetCounter < totalRecords DO 
	
        SET whileLoopCounter = whileLoopCounter + 1;
		
		BLOCK1: BEGIN
		
			DECLARE stagingCursor CURSOR FOR SELECT * FROM macs_staging_db.cc_staging LIMIT offsetCounter, limitCounter;
			
			DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = 1;		
			
			DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
				BEGIN
					SHOW ERRORS; 
				END; 

			OPEN stagingCursor;
			
			BLOCK2:
				LOOP 
					FETCH stagingCursor 	INTO
							ccId, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13, field14	;

					SET offsetCounter = offsetCounter + 1;	
					
					IF finished = 1 THEN 
					   SET finished = 0;
					   LEAVE block1;
					END IF;
					
					CASE
						WHEN field1 = 'CO' THEN 
							INSERT INTO `Macs_testing_DB`.`company`(
								`record_id`,
								`company_id`,
								`company_name`,
								`street`,
								`post_box`,
								`iso_country_code`,
								`postal_code`,
								`city`,
								`deleted`,
								`oem`,
								`duns_number`
							) VALUES (
								field1,
								if(field2 IS NULL OR field2 = '', NULL, CAST(field2 AS SIGNED INTEGER)),
								field3,
								field4,
								field5,
								field6,
								field7,
								field8,
								cast(field9='1' AS unsigned),
								cast(field10='1' AS unsigned),
								field11
							)ON DUPLICATE KEY UPDATE
								street = VALUES(street),
								post_box = VALUES(post_box),
								iso_country_code = VALUES(iso_country_code),
								postal_code = VALUES(postal_code),
								city = VALUES(city),
								deleted = VALUES(deleted),
								oem = VALUES(oem),
								duns_number = VALUES(duns_number);
						WHEN field1 = 'CT' THEN 
							INSERT INTO `Macs_testing_DB`.`contact` (
								`record_id`,
								`company_id`,
								`contact_id`,
								`deleted`,
								`last_name`,
								`first_name`,
								`phone_number`,
								`fax_number`,
								`email_address`,
								`department_name`,
								`mailbox`,
								`imds_contact`,
								`reach_contact`,
								`default_contact`
							) VALUES (
								field1,
								if(field2 IS NULL OR field2 = '', NULL, CAST(field2 AS SIGNED INTEGER)),
								if(field3 IS NULL OR field3 = '', NULL, CAST(field3 AS SIGNED INTEGER)),
								CAST(field4='1' AS unsigned),
								field5,
								field6,
								field7,
								field8,
								field9,
								field10,
								field11,
								CAST(field12='1' AS unsigned),
								CAST(field13='1' AS unsigned),
								CAST(field14='1' AS unsigned)
							) ON DUPLICATE KEY UPDATE
								deleted = VALUES(deleted),
								last_name = VALUES(last_name),
								first_name = VALUES(first_name),
								phone_number = VALUES(phone_number),
								fax_number = VALUES(fax_number),
								email_address = VALUES(email_address),
								department_name = VALUES(department_name),
								mailbox = VALUES(mailbox),
								imds_contact = VALUES(imds_contact),
								reach_contact = VALUES(reach_contact),
								default_contact = VALUES(default_contact);
						WHEN field1 = 'CU' THEN 
							INSERT INTO `Macs_testing_DB`.`comp_unit` (
								`record_id` ,
								`company_id` ,
								`org_unit_id` ,
								`org_unit_name` ,
								`org_unit_street` ,
								`org_unit_post_box` ,
								`org_unit_iso_country_code` ,
								`org_unit_postal_code` ,
								`org_unit_city` ,
								`deleted` ,
								`duns_number`
							) VALUES (
								field1,
								if(field2 IS NULL OR field2 = '', NULL, CAST(field2 AS SIGNED INTEGER)),
								if(field3 IS NULL OR field3 = '', NULL, CAST(field3 AS SIGNED INTEGER)),
								field4,
								field5,
								field6,
								field7,
								field8,
								field9,
								CAST(field10='1' AS unsigned),
								field11
							) ON DUPLICATE KEY UPDATE
								`org_unit_name` = VALUES(org_unit_name),
								`org_unit_street`= VALUES(org_unit_street),
								`org_unit_post_box`= VALUES(org_unit_post_box),
								`org_unit_iso_country_code`= VALUES(org_unit_iso_country_code),
								`org_unit_postal_code`= VALUES(org_unit_postal_code),
								`org_unit_city`= VALUES(org_unit_city),
								`deleted`= VALUES(deleted),
								`duns_number`= VALUES(duns_number);							
						ELSE BEGIN END;

					END CASE;
					
			END LOOP BLOCK2;
			CLOSE stagingCursor;
			
		END BLOCK1;
			
	END WHILE;
	
	
	SELECT CONCAT ('Total Records in DB: ', totalRecords, 
	', whileLoopCounter: ', whileLoopCounter,
	', Limit: ', limitCounter, 
	', Last Offset: ', offsetCounter) AS Counters;

END$$
DELIMITER ;
