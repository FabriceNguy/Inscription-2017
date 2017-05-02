/*competition*/
/* pas fait*/
DELIMITER |
	DROP PROCEDURE IF EXISTS GET_CANDIDATS_FROM_COMP;
	create procedure GET_CANDIDATS_FROM_COMP(Num int)
	begin
		DECLARE une_Equipe boolean;
		SELECT Equipe INTO une_Equipe FROM CANDIDAT WHERE NumCandidat = Num;
		IF(!une_Equipe) THEN
			SELECT NumCandidat, NomCandidat 
			FROM PARTICIPER, CANDIDAT 
			WHERE PARTICIPER.NumCandidat=CANDIDAT.NumCandidat 
			AND NumComp = Num;
		END IF;
		IF(une_Equipe) THEN
			SELECT NumCandidat, NomCandidat, PrenomPersonne 
			FROM PARTICIPER, CANDIDAT, PERSONNE 
			WHERE PARTICIPER.NumCandidat=CANDIDAT.NumCandidat 
			AND PERSONNE.NumCandidatPers=CANDIDAT.NumCandidat 
			AND NumComp = Num;
		END IF;
	end;
|
/*fait*/
DELIMITER |
	DROP PROCEDURE IF EXISTS SET_NAME_COMP;
	create procedure SET_NAME_COMP (newName varchar(25), Num int) 
	BEGIN
		UPDATE COMPETITION
		SET NomComp = newName
		WHERE NumComp = Num;
	END;	
|
/*fait*/
DELIMITER |
	DROP PROCEDURE IF EXISTS SET_DATE_COMP;
	create procedure SET_DATE_COMP (newDate varchar(25), Num int) 
	BEGIN
		UPDATE COMPETITION
		SET DateCloture = newDate
		WHERE NumComp = Num;
	END;	
|
/*fait*/
DELIMITER |
	DROP PROCEDURE IF EXISTS GET_NAME_COMP;
	create procedure GET_NAME_COMP (numCompet int) 
	BEGIN
		SELECT NomComp
		FROM COMPETITION
		WHERE NumComp = NumCompet;
	END;	
|
/*fait*/
DELIMITER |
	DROP procedure IF EXISTS date_cloture;
	create procedure date_cloture(Num int(25)) 
	begin
		SELECT DateCloture FROM COMPETITION WHERE NumComp = Num;
	end;
|
/*fait*/
DELIMITER |
	DROP function IF EXISTS inscription_ouverte;
	create function inscription_ouverte (Num int(25)) returns boolean
	begin
		DECLARE DATE_COMP date;
		DECLARE RES boolean;
		SELECT `DateCloture` INTO DATE_COMP FROM `COMPETITION` WHERE `NumComp`=Num ;
		IF DATE_COMP < NOW() 
		THEN SET RES=0;
		ELSE 
			 SET RES=1;
		END IF;
		return RES;
	end;
|
/*fait*/
DELIMITER |
	DROP PROCEDURE IF EXISTS ADD_COMP;
	create procedure ADD_COMP
	(NomC varchar(25), DateClot date, Equipe boolean)
	begin
		insert into COMPETITION(NumComp,NomComp, DateCloture,EnEquipe) values (NULL,NomC, DateClot,Equipe) ;
		
	end;
|

/* fait*/
DELIMITER |
	DROP PROCEDURE IF EXISTS DEL_COMP;
	create procedure DEL_COMP (Num int(25)) 
	BEGIN
		DELETE FROM COMPETITION
		WHERE NumComp = Num;

	END;	
|
/* pas fait*/
DELIMITER |
	DROP PROCEDURE IF EXISTS GET_COMP;
	create procedure GET_COMP () 
	BEGIN
		SELECT * 
		FROM COMPETITION;

	END;	
|
/* pas fait*/
DELIMITER |
	DROP procedure IF EXISTS EN_EQUIPE_COMP;
	create procedure EN_EQUIPE_COMP(Num int(25)) 
	BEGIN
		
		SELECT EnEquipe FROM COMPETITION WHERE NumComp = Num;
		
	END;	
|
