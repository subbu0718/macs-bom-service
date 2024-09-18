DELIMITER $$
CREATE DEFINER=`macs_production_user`@`%` PROCEDURE `sa_import`()
BEGIN

	DECLARE totalRecords BIGint DEFAULT 0;
	DECLARE saId BIGINT DEFAULT 0;
	DECLARE field1 VARCHAR(2) DEFAULT NULL;
    DECLARE field2 BIGINT DEFAULT NULL;
    DECLARE field3 VARCHAR(400) DEFAULT NULL;
    DECLARE field4 INT DEFAULT NULL;
    DECLARE offsetCounter BIGINT;
    DECLARE limitCounter INT DEFAULT 1000;
    DECLARE finished INT DEFAULT 0;
	DECLARE whileLoopCounter INT;
    
  
	SELECT COUNT(*) FROM macs_staging_db.sa_staging INTO totalRecords; 

	SET offsetCounter = 0;
    SET whileLoopCounter = 0;
	
	WHILE offsetCounter < totalRecords DO 
	
        SET whileLoopCounter = whileLoopCounter + 1;
		
		BLOCK1: BEGIN
		
			DECLARE stagingCursor CURSOR FOR SELECT * FROM macs_staging_db.sa_staging LIMIT offsetCounter, limitCounter;
			
			DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = 1;		
			
			DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
				BEGIN
					SHOW ERRORS; 
				END; 

			OPEN stagingCursor;
			
			BLOCK2:
				LOOP 
					FETCH stagingCursor 	INTO
							saId, field1, field2, field3, field4	;

					SET offsetCounter = offsetCounter + 1;	
					
					IF finished = 1 THEN 
					   SET finished = 0;
					   LEAVE block1;
					END IF;
					
					CASE
						WHEN field1 = 'SA' THEN 
							INSERT INTO `Macs_testing_DB`.`substance_application`(
								`record_id`,
								`substance_application_id`,
								`substance_application_text`,
								`deleted`
							) VALUES (
								field1,
								field2,
								field3,
								if(field4 IS NULL OR field4 = '', NULL, CAST(field4 AS SIGNED INTEGER))
							)ON DUPLICATE KEY UPDATE
								substance_application_text = VALUES(substance_application_text),
								deleted = VALUES(deleted);											
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
