DELIMITER $$
CREATE DEFINER=`macs_production_user`@`%` PROCEDURE `mc_import`()
BEGIN

	DECLARE totalRecords BIGINT DEFAULT 0;
	DECLARE mcId BIGINT DEFAULT 0;
	DECLARE field1 VARCHAR(2) DEFAULT NULL;
    DECLARE field2 VARCHAR(12) DEFAULT NULL;
    DECLARE field3 VARCHAR(100) DEFAULT NULL;
    DECLARE field4 VARCHAR(2) DEFAULT NULL;
    DECLARE offsetCounter BIGINT;
    DECLARE limitCounter INT DEFAULT 1000;
    DECLARE finished INT DEFAULT 0;
	DECLARE whileLoopCounter INT;
    
  
	SELECT COUNT(*) FROM macs_staging_db.mc_staging INTO totalRecords; 

	SET offsetCounter = 0;
    SET whileLoopCounter = 0;
	
	WHILE offsetCounter < totalRecords DO 
	
        SET whileLoopCounter = whileLoopCounter + 1;
		
		BLOCK1: BEGIN
		
			DECLARE stagingCursor CURSOR FOR SELECT * FROM macs_staging_db.mc_staging LIMIT offsetCounter, limitCounter;
			
			DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = 1;		
			
			DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
				BEGIN
					SHOW ERRORS; 
				END; 

			OPEN stagingCursor;
			
			BLOCK2:
				LOOP 
					FETCH stagingCursor 	INTO
							mcId, field1, field2, field3, field4	;

					SET offsetCounter = offsetCounter + 1;	
					
					IF finished = 1 THEN 
					   SET finished = 0;
					   LEAVE block1;
					END IF;
					
					CASE
						WHEN field1 = 'CL' THEN 
							INSERT INTO `Macs_testing_DB`.`material_classification`(
								`record_id`,
								`material_classification_code`,
								`leaf`
							) VALUES (
								field1,
								field2,
								cast(field3='1' AS unsigned)
							)ON DUPLICATE KEY UPDATE
								material_classification_code = VALUES(material_classification_code),
								leaf = VALUES(leaf);
						WHEN field1 = 'CN' THEN 
							INSERT INTO `Macs_testing_DB`.`classification_name` (
								`record_id`,
								`material_classification_code`,
								`material_classification_name`,
								`iso_language`
							) VALUES (
								field1,
								field2,
								field3,
								field4
							) ON DUPLICATE KEY UPDATE
								material_classification_name = VALUES(material_classification_name);
												
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
