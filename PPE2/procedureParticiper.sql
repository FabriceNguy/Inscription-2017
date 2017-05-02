/*participer*/
/*fait*/
DELIMITER |
	DROP PROCEDURE IF EXISTS ADD_PARTICIPATION;
	create procedure ADD_PARTICIPATION(NumCan int(25), NumCom int(25)) 
	BEGIN
		DECLARE etre_Une_Equipe Boolean;
		DECLARE en_Equipe Boolean;
		SELECT Equipe INTO etre_Une_Equipe FROM Candidat WHERE NumCandidat = NumCan; 
		SELECT EnEquipe INTO en_Equipe FROM Competition WHERE NumComp = NumCom; 
		/*SET etreUneEquipe = 'true';
		SET enEquipe = 'true';*/
		IF(en_Equipe) THEN
			IF(etre_Une_Equipe) THEN
				INSERT INTO PARTICIPER(NumCandidat, NumComp) VALUES (NumCan, NumCom);
			END IF;
		END IF;
		IF(!en_Equipe) THEN
			IF(!etre_Une_Equipe) THEN
				INSERT INTO PARTICIPER(NumCandidat, NumComp) VALUES (NumCan, NumCom);
			END IF;
		END IF;
	END;
|
/* pas fait*/
DELIMITER |
	DROP PROCEDURE IF EXISTS DEL_PARTICIPATION;
	create procedure DEL_PARTICIPATION(NumCan int, Num int(25)) 
	BEGIN
		DELETE FROM PARTICIPER  wHERE NumCandidat=NumCan and NumComp=Num;
	END;

|
/*fait*/
DELIMITER |
	DROP PROCEDURE IF EXISTS GET_PARTICIPATION;
	create procedure GET_PARTICIPATION () 
	BEGIN

		SELECT NomCandidat, NomComp
		FROM PARTICIPER, CANDIDAT, COMPETITION
		WHERE CANDIDAT.NumCandidat=PARTICIPER.NumCandidat
		AND COMPETITION.NumComp=PARTICIPER.NumComp;
	END;	
|

DELIMITER |
	
	CREATE TRIGGER before_del_comp BEFORE DELETE
	ON COMPETITION FOR EACH ROW
	BEGIN
		
 		DELETE FROM PARTICIPER
 		WHERE NumComp = Old.NumComp;
	END;	
|
