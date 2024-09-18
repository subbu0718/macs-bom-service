DELIMITER $$
CREATE DEFINER=`macs_production_user`@`%` PROCEDURE `sg_import`()
BEGIN

	DECLARE totalRecords BIGINT DEFAULT 0;
	DECLARE sgId BIGINT DEFAULT 0;
	DECLARE field1 VARCHAR(2) DEFAULT NULL;
    DECLARE field2 BIGINT DEFAULT NULL;
    DECLARE field3 VARCHAR(100) DEFAULT NULL;
    DECLARE field4 TINYINT DEFAULT NULL;
    DECLARE offsetCounter BIGINT;
    DECLARE limitCounter INT DEFAULT 1000;
    DECLARE finished INT DEFAULT 0;
	DECLARE whileLoopCounter INT;
    
  
	SELECT COUNT(*) FROM macs_staging_db.sg_staging INTO totalRecords; 

	SET offsetCounter = 0;
    SET whileLoopCounter = 0;
	
	WHILE offsetCounter < totalRecords DO 
	
        SET whileLoopCounter = whileLoopCounter + 1;
		
		BLOCK1: BEGIN
		
			DECLARE stagingCursor CURSOR FOR SELECT * FROM macs_staging_db.sg_staging LIMIT offsetCounter, limitCounter;
			
			DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = 1;		
			
			DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
				BEGIN
					SHOW ERRORS; 
				END; 

			OPEN stagingCursor;
			
			BLOCK2:
				LOOP 
					FETCH stagingCursor 	INTO
							sgId, field1, field2, field3, field4	;

					SET offsetCounter = offsetCounter + 1;	
					
					IF finished = 1 THEN 
					   SET finished = 0;
					   LEAVE block1;
					END IF;
					
					CASE
						WHEN field1 = 'GR' THEN 
							INSERT INTO `Macs_testing_DB`.`substance_group`(
								`record_id`,
								`substance_group_id`,
								`name`,
								`deleted`
							) VALUES (
								field1,
								if(field2 IS NULL OR field2 = '', NULL, CAST(field2 AS SIGNED INTEGER)),
								field3,								
								cast(field4='1' AS unsigned)
							)ON DUPLICATE KEY UPDATE
								name = VALUES(name),
								deleted = VALUES(deleted);
						WHEN field1 = 'GM' THEN 
							INSERT INTO `Macs_testing_DB`.`substance_group_member` (
								`record_id`,
								`substance_group_id`,
								`substance_node_id`
							) VALUES (
								field1,
								if(field2 IS NULL OR field2 = '', NULL, CAST(field2 AS SIGNED INTEGER)),								
								if(field3 IS NULL OR field3 = '', NULL, CAST(field3 AS SIGNED INTEGER))
							) ON DUPLICATE KEY UPDATE
								substance_group_id = VALUES(substance_group_id),
								substance_node_id = VALUES(substance_node_id);
							
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
