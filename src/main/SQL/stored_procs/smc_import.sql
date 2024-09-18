DELIMITER $$
CREATE DEFINER=`macs_production_user`@`%` PROCEDURE `smc_import`()
BEGIN

	DECLARE totalRecords BIGINT DEFAULT 0;
	DECLARE smcId BIGINT DEFAULT 0;
	DECLARE field1 VARCHAR(3) DEFAULT NULL;
    DECLARE field2 BIGINT DEFAULT NULL;
    DECLARE field3 INT DEFAULT NULL;
    DECLARE field4 VARCHAR(200) DEFAULT NULL;
    DECLARE field5 VARCHAR(200) DEFAULT NULL;
    DECLARE field6 VARCHAR(200) DEFAULT NULL;
    DECLARE offsetCounter BIGINT;
    DECLARE limitCounter INT DEFAULT 1000;
    DECLARE finished INT DEFAULT 0;
	DECLARE whileLoopCounter INT;
    
  
	SELECT COUNT(*) FROM macs_staging_db.smc_staging INTO totalRecords; 

	SET offsetCounter = 0;
    SET whileLoopCounter = 0;
	
	WHILE offsetCounter < totalRecords DO 
	
        SET whileLoopCounter = whileLoopCounter + 1;
		
		BLOCK1: BEGIN
		
			DECLARE stagingCursor CURSOR FOR SELECT * FROM macs_staging_db.smc_staging LIMIT offsetCounter, limitCounter;
			
			DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = 1;		
			
			DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
				BEGIN
					SHOW ERRORS; 
				END; 

			OPEN stagingCursor;
			
			BLOCK2:
				LOOP 
					FETCH stagingCursor 	INTO
							smcId, field1, field2, field3, field4, field5, field6	;

					SET offsetCounter = offsetCounter + 1;	
					
					IF finished = 1 THEN 
					   SET finished = 0;
					   LEAVE block1;
					END IF;
					
					CASE
						WHEN field1 = 'SMC' THEN 
							INSERT INTO `Macs_testing_DB`.`scip_material_category`(
								`record_id` ,
								`identifier`,
								`scip_material_category_type`,
								`level_1`,
								`level_2`,
								`level_3`
							) VALUES (
								field1,
								if(field2 IS NULL OR field2 = '', NULL, CAST(field2 AS SIGNED INTEGER)),
								if(field3 IS NULL OR field3 = '', NULL, CAST(field3 AS SIGNED INTEGER)),
								field4,
								field5,
								field6
							)ON DUPLICATE KEY UPDATE
								scip_material_category_type = VALUES(scip_material_category_type),
								level_1 = VALUES(level_1),
								level_2 = VALUES(level_2),
								level_3 = VALUES(level_3);
							
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
