DELIMITER $$
CREATE DEFINER=`macs_production_user`@`%` PROCEDURE `sb_import`()
BEGIN

	DECLARE totalRecords BIGINT DEFAULT 0;
	DECLARE sbId BIGINT DEFAULT 0;
	DECLARE field1 VARCHAR(2) DEFAULT NULL;
    DECLARE field2 BIGINT DEFAULT NULL;
    DECLARE field3 VARCHAR(20) DEFAULT NULL;
    DECLARE field4 VARCHAR(20) DEFAULT NULL;
    DECLARE field5 VARCHAR(250) DEFAULT NULL;
    DECLARE field6 TINYINT DEFAULT NULL;
    DECLARE field7 TINYINT DEFAULT NULL;
    DECLARE field8 TINYINT DEFAULT NULL;
    DECLARE field9 TINYINT DEFAULT NULL;
    DECLARE field10 TINYINT DEFAULT NULL;
    DECLARE field11 TINYINT DEFAULT NULL;
    DECLARE field12 VARCHAR(20) DEFAULT NULL;
    DECLARE field13 VARCHAR(20) DEFAULT NULL;
    DECLARE offsetCounter BIGINT;
    DECLARE limitCounter INT DEFAULT 1000;
    DECLARE finished INT DEFAULT 0;
	DECLARE whileLoopCounter INT;
    
  
	SELECT COUNT(*) FROM macs_staging_db.sb_staging INTO totalRecords; 

	SET offsetCounter = 0;
    SET whileLoopCounter = 0;
	
	WHILE offsetCounter < totalRecords DO 
	
        SET whileLoopCounter = whileLoopCounter + 1;
		
		BLOCK1: BEGIN
		
			DECLARE stagingCursor CURSOR FOR SELECT * FROM macs_staging_db.sb_staging LIMIT offsetCounter, limitCounter;
			
			DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = 1;		
			
			DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
				BEGIN
					SHOW ERRORS; 
				END; 

			OPEN stagingCursor;
			
			BLOCK2:
				LOOP 
					FETCH stagingCursor 	INTO
							sbId, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13;

					SET offsetCounter = offsetCounter + 1;	
					
					IF finished = 1 THEN 
					   SET finished = 0;
					   LEAVE block1;
					END IF;
					
					CASE
						WHEN field1 = 'S' THEN 
							INSERT INTO `Macs_testing_DB`.`substance`(
								`record_id`,
								`node_id`,
								`cas_code`,
								`eu_index`,
								`einecs`,
								`gadsl_duty_to_declare`,
								`gadsl_is_unwanted`,
								`gadsl_is_prohibited`,
								`reach`,
								`deleted`,
								`hidden`,
								`sunset_date`,
								`last_application_date`
							) VALUES (
								field1,
								if(field2 IS NULL OR field2 = '', NULL, CAST(field2 AS SIGNED INTEGER)),
								field3,
								field4,
								field5,
								cast(field6='1' AS unsigned),
								cast(field7='1' AS unsigned),
								cast(field8='1' AS unsigned),
								cast(field9='1' AS unsigned),
								cast(field10='1' AS unsigned),
								cast(field11='1' AS unsigned),
								if(field12 IS NULL OR field12 = '', NULL, CAST(field12 AS DATE)),
								if(field13 IS NULL OR field13 = '', NULL, CAST(field13 AS DATE))
							)ON DUPLICATE KEY UPDATE
								cas_code = VALUES(cas_code),
								eu_index = VALUES(eu_index),
								einecs = VALUES(einecs),
								gadsl_duty_to_declare = VALUES(gadsl_duty_to_declare),
								gadsl_is_unwanted = VALUES(gadsl_is_unwanted),
								gadsl_is_prohibited = VALUES(gadsl_is_prohibited),
								reach = VALUES(reach),
								deleted = VALUES(deleted),
								hidden = VALUES(hidden),
								sunset_date = VALUES(sunset_date),
								last_application_date = VALUES(last_application_date);
						WHEN field1 = 'SS' THEN 
							INSERT INTO `Macs_testing_DB`.`synonym` (
								`record_id`,								
								`substance_node_id`,
								`synonym_id`,
								`iso_language`,
								`synonym_name`
							) VALUES (
								field1,
								if(field2 IS NULL OR field2 = '', NULL, CAST(field2 AS SIGNED INTEGER)),
								if(field3 IS NULL OR field3 = '', NULL, CAST(field3 AS SIGNED INTEGER)),
								field4,
								field5
							) ON DUPLICATE KEY UPDATE
								synonym_name = VALUES(synonym_name);
						
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
