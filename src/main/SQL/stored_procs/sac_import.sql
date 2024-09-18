DELIMITER $$
CREATE DEFINER=`macs_production_user`@`%` PROCEDURE `sac_import`()
BEGIN

	DECLARE totalRecords BIGINT DEFAULT 0;
	DECLARE sacId BIGINT DEFAULT 0;
	DECLARE field1 VARCHAR(3) DEFAULT NULL;
    DECLARE field2 BIGINT DEFAULT NULL;
    DECLARE field3 VARCHAR(1000) DEFAULT NULL;
    DECLARE field4 VARCHAR(1000) DEFAULT NULL;
    DECLARE field5 VARCHAR(1000) DEFAULT NULL;
    DECLARE field6 VARCHAR(1000) DEFAULT NULL;
    DECLARE field7 VARCHAR(1000) DEFAULT NULL;
    DECLARE field8 VARCHAR(1000) DEFAULT NULL;
    DECLARE field9 VARCHAR(1000) DEFAULT NULL;
    DECLARE field10 VARCHAR(1000) DEFAULT NULL;
    DECLARE field11 VARCHAR(1000) DEFAULT NULL;
    DECLARE field12 VARCHAR(1000) DEFAULT NULL;
    DECLARE field13 VARCHAR(50) DEFAULT NULL;
    DECLARE field14 BIGINT DEFAULT NULL;
    DECLARE field15 TINYINT DEFAULT NULL;
    DECLARE field16 TINYINT DEFAULT NULL;
    DECLARE offsetCounter BIGINT;
    DECLARE limitCounter INT DEFAULT 1000;
    DECLARE finished INT DEFAULT 0;
	DECLARE whileLoopCounter INT;
    
  
	SELECT COUNT(*) FROM macs_staging_db.sac_staging INTO totalRecords; 

	SET offsetCounter = 0;
    SET whileLoopCounter = 0;
	
	WHILE offsetCounter < totalRecords DO 
	
        SET whileLoopCounter = whileLoopCounter + 1;
		
		BLOCK1: BEGIN
		
			DECLARE stagingCursor CURSOR FOR SELECT * FROM macs_staging_db.sac_staging LIMIT offsetCounter, limitCounter;
			
			DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = 1;		
			
			DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
				BEGIN
					SHOW ERRORS; 
				END; 

			OPEN stagingCursor;
			
			BLOCK2:
				LOOP 
					FETCH stagingCursor 	INTO
							sacId, field1, field2, field3, field4, field5, field6, field7, field8, field9, field10, field11, field12, field13,
							field14, field15, field16	;

					SET offsetCounter = offsetCounter + 1;	
					
					IF finished = 1 THEN 
					   SET finished = 0;
					   LEAVE block1;
					END IF;
					
					CASE
						WHEN field1 = 'SAC' THEN 
							INSERT INTO `Macs_testing_DB`.`scip_article_category`(
								`record_id`,
								`article_category_id`,
								`description_1`,
								`description_2`,
								`description_3`,
								`description_4`,
								`description_5`,
								`description_6`,
								`description_7`,
								`description_8`,
								`description_9`,
								`description_10`,
								`category_code`,
								`echa_id`,
								`obsolete`,
								`selectable`
							) VALUES (
								field1,
								if(field2 IS NULL OR field2 = '', NULL, CAST(field2 AS SIGNED INTEGER)),
								field3,
								field4,
								field5,
								field6,
								field7,
								field8,
								field9,
								field10,
								field11,
								field12,
								field13,
								if(field14 IS NULL OR field14 = '', NULL, CAST(field14 AS SIGNED INTEGER)),
								CAST(field15='1' AS unsigned),
								CAST(field16='1' AS unsigned)
							)ON DUPLICATE KEY UPDATE
								description_1 = VALUES(description_1),
								description_2 = VALUES(description_2),
								description_3 = VALUES(description_3),
								description_4 = VALUES(description_4),
								description_5 = VALUES(description_5),
								description_6 = VALUES(description_6),
								description_7 = VALUES(description_7),
								description_8 = VALUES(description_8),
								description_9 = VALUES(description_9),
								description_10 = VALUES(description_10),
								category_code = VALUES(category_code),
								echa_id = VALUES(echa_id),
								obsolete = VALUES(obsolete),
								selectable = VALUES(selectable);
						
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
