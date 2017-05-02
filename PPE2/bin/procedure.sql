procedure.sql

/*PERSONNE*/

DELIMITER |
	DROP PROCEDURE IF EXISTS ADD_PERSONNE;
	create procedure ADD_PERSONNE
	(NumCandidat  Int(25),NomCandidat  Varchar(25),MailCandidat Varchar(25), PrenomPersonne Varchar(25))
	BEGIN

		insert into CANDIDAT(NumCandidat, NomCandidat, MailCandidat) values (NumCandidat, NomCandidat, MailCandidat) ;
		insert into PERSONNE(PrenomPersonne, NumCandidat)  values (PrenomPersonne, NumCandidat) ;
	END;

|
DELIMITER |
	DROP PROCEDURE IF EXISTS GET_PERSONNE;
	create procedure GET_PERSONNE()
	BEGIN

		SELECT PERSONNE.NumCandidat,NomCandidat,PrenomPersonne,MailCandidat FROM PERSONNE,CANDIDAT WHERE PERSONNE.NumCandidat = CANDIDAT.NumCandidat  ;

	END;

|
DELIMITER |
	DROP PROCEDURE IF EXISTS GET_PRENOM_PERSONNE;
	create procedure GET_PRENOM_PERSONNE (NumCand int(25)) 
	BEGIN
		SELECT PrenomPersonne
		FROM PERSONNE
		WHERE NumCandidat = NumCand;

	END;	
|
DELIMITER |
	DROP PROCEDURE IF EXISTS GET_MAIL;
	create procedure GET_MAIL (NumCand int(25)) 
	BEGIN
		SELECT MailCandidat
		FROM CANDIDAT
		WHERE NumCandidat = NumCand;

	END;	
|
DELIMITER |
	DROP PROCEDURE IF EXISTS SET_PRENOM_PERSONNE;
	create procedure SET_PRENOM_PERSONNE (newFirstName varchar(25), NumCand int(25)) 
	BEGIN
		UPDATE PERSONNE 
		SET PrenomPersonne = newFirstName
		WHERE NumCandidat = NumCand;

	END;	
|
DELIMITER |
	DROP PROCEDURE IF EXISTS SET_MAIL_PERSONNE;
	create procedure SET_MAIL_PERSONNE (newFirstMail varchar(25), NumCand int(25)) 
	BEGIN
		UPDATE CANDIDAT 
		SET MailCandidat = newFirstMail
		WHERE NumCandidat = NumCand;

	END;	
|
/*EQUIPE*/

DELIMITER |
	DROP PROCEDURE IF EXISTS ADD_EQUIPE;
	create procedure ADD_EQUIPE
	(NumCandidat int(25), NomCandidat varchar(25), MailCandidat varchar(25))
	begin
		insert into CANDIDAT(NumCandidat,NomCandidat,MailCandidat) values (NumCandidat,NomCandidat,MailCandidat) ;
		insert into EQUIPE(NumCandidat) values (NumCandidat) ;
	end;
|

DELIMITER |
	DROP PROCEDURE IF EXISTS GET_EQUIPE;
	create procedure GET_EQUIPE()
	BEGIN

		SELECT * FROM CANDIDAT,EQUIPE WHERE CANDIDAT.NumCandidat = EQUIPE.NumCandidat  ;

	END;

|

DELIMITER |
	DROP PROCEDURE IF EXISTS GET_MEMBRE_EQUIPE;
	create procedure GET_MEMBRE_EQUIPE(NumEQUIPE int )
	BEGIN

		SELECT PERSONNE.NumCandidat, PERSONNE.PrenomPersonne, CANDIDAT.NomCandidat, CANDIDAT.MailCandidat 
		FROM PERSONNE, ETRE_DANS, CANDIDAT
		WHERE ETRE_DANS.NumCandidat_Personne = PERSONNE.NumCandidat 
		AND CANDIDAT.NumCandidat = PERSONNE.NumCandidat
		AND ETRE_DANS.NumCandidat_Equipe=NumEQUIPE  ;

	END;

|
/*CANDIDAT*/

DELIMITER |
	DROP PROCEDURE IF EXISTS DEL_CANDIDAT;
	create procedure DEL_CANDIDAT (numCanDel int(25)) 
	BEGIN
	
		DELETE FROM ETRE_DANS
 		WHERE NumCandidat_Personne = numCanDel;
 		DELETE FROM ETRE_DANS
 		WHERE NumCandidat_Equipe = numCanDel;
		DELETE FROM PERSONNE
		WHERE NumCandidat =numCanDel;
		DELETE FROM EQUIPE
		WHERE NumCandidat =numCanDel;
		DELETE FROM PARTICIPER
		WHERE NumCandidat =numCanDel;
		DELETE FROM CANDIDAT
		WHERE NumCandidat =numCanDel;
		

	END;	
|
DELIMITER |
	
	CREATE TRIGGER before_del_cand BEFORE DELETE
	ON COMPETITION FOR EACH ROW
	BEGIN
		
 		DELETE FROM ETRE_DANS
 		WHERE NumCandidat_Personne = Old.NumCandidat;
 		DELETE FROM ETRE_DANS
 		WHERE NumCandidat_Equipe = Old.NumCandidat;
	END;	
|
DELIMITER |
	DROP PROCEDURE IF EXISTS GET_CANDIDAT;
	create procedure GET_CANDIDAT()
	BEGIN

		SELECT * FROM CANDIDAT;

	END;

|

DELIMITER |
	DROP PROCEDURE IF EXISTS GET_NAME_CANDIDAT;
	create procedure GET_NAME_CANDIDAT(NumCandidat int)
	BEGIN

		SELECT * FROM CANDIDAT
		WHERE NumCandidat = NumCandidat;

	END;

|

DELIMITER |
	DROP PROCEDURE IF EXISTS SET_NAME_CANDIDAT;
	create procedure SET_NAME_CANDIDAT (newName varchar(25), NumCand int(25)) 
	BEGIN
		UPDATE CANDIDAT 
		SET NomCandidat = newName
		WHERE NumCandidat = NumCand;

	END;	
|


DELIMITER |
	DROP PROCEDURE IF EXISTS GET_COMP_CANDIDAT;
	create procedure GET_COMP_CANDIDAT
	(NumCandidat int)
	begin
		SELECT NomComp FROM PARTICIPER, COMPETITION WHERE PARTICIPER.LabelComp=COMPETITION.LabelComp AND NumCandidat =NumCandidat ;
		
	end;
|
/*competition*/

DELIMITER |
	DROP PROCEDURE IF EXISTS GET_CANDIDATS_FROM_COMP;
	create procedure GET_CANDIDATS_FROM_COMP
	(Label varchar(25))
	begin
		SELECT NomCandidat FROM PARTICIPER, CANDIDAT WHERE PARTICIPER.NumCandidat=CANDIDAT.NumCandidat AND LabelComp = Label;
		
	end;
|

DELIMITER |
	DROP PROCEDURE IF EXISTS SET_NAME_COMP;
	create procedure SET_NAME_COMP (newName varchar(25), Labe varchar(25)) 
	BEGIN
		UPDATE COMPETITION
		SET NomComp = newName
		WHERE LabelComp = Label;

	END;	
|


DELIMITER |
	DROP procedure IF EXISTS date_cloture;
	create procedure date_cloture(Label varchar(25)) 
	begin
		SELECT DateCloture FROM COMPETITION WHERE LabelComp = Label;
	end;
|

DELIMITER |
	DROP function IF EXISTS inscription_ouverte;
	create function inscription_ouverte (Label varchar(25)) returns boolean
	begin
		DECLARE DATE_COMP date;
		DECLARE RES boolean;
		SELECT `DateCloture` INTO DATE_COMP FROM `COMPETITION` WHERE `LabelComp`=Label ;
		IF DATE_COMP < NOW() 
		THEN SET RES=0;
		ELSE 
			 SET RES=1;
		END IF;
		return RES;
	end;
|

DELIMITER |
	DROP PROCEDURE IF EXISTS ADD_COMP;
	create procedure ADD_COMP
	(Label varchar(25), NomC varchar(25), DateClot date, Equipe boolean)
	begin
		insert into COMPETITION(LabelComp, NomComp, DateCloture,EnEquipe) values (Label, NomC, DateClot,Equipe) ;
		
	end;
|


DELIMITER |
	DROP PROCEDURE IF EXISTS DEL_COMP;
	create procedure DEL_COMP (Label varchar(25)) 
	BEGIN
		DELETE FROM COMPETITION
		WHERE LabelComp = Label;

	END;	
|
DELIMITER |
	DROP PROCEDURE IF EXISTS GET_COMP;
	create procedure GET_COMP () 
	BEGIN
		SELECT * 
		FROM COMPETITION;


	END;	
|
DELIMITER |
	DROP function IF EXISTS EN_EQUIPE_COMP;
	create function EN_EQUIPE_COMP (Label varchar(25)) returns boolean 
	BEGIN
		DECLARE bool boolean;
		SELECT EnEquipe into bool FROM COMPETITION WHERE LabelComp = Label ;
		return bool;
	END;	
|

DELIMITER |
	DROP PROCEDURE IF EXISTS ADD_CANDIDAT_COMP;
	create procedure ADD_CANDIDAT_COMP (NumCan int, Label varchar(25)) 
	BEGIN	
		insert into PARTICIPER(NumCandidat,LabelComp) values (NumCan,Label);
	END;	
|
/*participer*/

DELIMITER |
	DROP PROCEDURE IF EXISTS ADD_PARTICIPATION;
	create procedure ADD_PARTICIPATION(NumCan int, Label varchar(25)) 
	BEGIN
		INSERT INTO PARTICIPATION(NumCandidat, LabelComp) VALUES (NumCan, Label);
	END;
|

DELIMITER |
	DROP PROCEDURE IF EXISTS DEL_PARTICIPATION;
	create procedure DEL_PARTICIPATION(NumCan int, Label varchar(25)) 
	BEGIN
		DELETE FROM PARTICIPER  wHERE NumCandidat=NumCan and LabelComp=Label;
	END;

|
DELIMITER |
	DROP PROCEDURE IF EXISTS GET_PARTICIPATION;
	create procedure GET_PARTICIPATION () 
	BEGIN
		SELECT NomCandidat, NomComp
		FROM PARTICIPER, CANDIDAT, COMPETITION
		WHERE CANDIDAT.NumCandidat=PARTICIPER.NumCandidat
		AND COMPETITION.LabelComp=PARTICIPER.LabelComp;
	END;	
|

DELIMITER |
	
	CREATE TRIGGER before_del_comp BEFORE DELETE
	ON COMPETITION FOR EACH ROW
	BEGIN
		
 		DELETE FROM PARTICIPER
 		WHERE LabelComp = Old.LabelComp;
	END;	
|


/*ETRE DANS*/

	DROP PROCEDURE IF EXISTS ADD_MEMBRE;
	create procedure ADD_MEMBRE (Num_Equipe int,Num_Personne int) 
	BEGIN
		insert into ETRE_DANS(NumCandidat_Equipe,NumCandidat_Personne) VALUES (Num_Equipe,Num_Personne); 
	END;	
|

DELIMITER |
	DROP PROCEDURE IF EXISTS DEL_MEMBRE;
	create procedure DEL_MEMBRE (Num_Equipe int,Num_Personne int) 
	BEGIN
		DELETE FROM ETRE_DANS wHERE NumCandidat_Equipe =Num_Equipe AND  NumCandidat_Personne= Num_Personne; 
	END;	
|