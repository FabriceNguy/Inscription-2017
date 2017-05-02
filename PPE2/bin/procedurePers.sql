/*PERSONNE*/
/*fait*/
DELIMITER |
 DROP PROCEDURE IF EXISTS ADD_PERSONNE;
 create procedure ADD_PERSONNE
 (NomCandidat  Varchar(25),MailCandidat Varchar(25), PrenomPersonne Varchar(25))

 BEGIN
  DECLARE Num int;
  insert into CANDIDAT(NumCandidat, NomCandidat,Equipe) values (null, NomCandidat,0) ;
  SELECT MAX(NumCandidat) INTO Num FROM CANDIDAT; 
  insert into PERSONNE(PrenomPersonne, NumCandidatPers, MailPers)  values (PrenomPersonne, Num, MailCandidat);
  
 END;
|
/* pas fait*/
DELIMITER |
	DROP PROCEDURE IF EXISTS GET_PERSONNE;
	create procedure GET_PERSONNE()
	BEGIN

		SELECT PERSONNE.NumCandidat,NomCandidat,PrenomPersonne,MailCandidat FROM PERSONNE,CANDIDAT WHERE PERSONNE.NumCandidat = CANDIDAT.NumCandidat  ;

	END;

|
/*fait*/
DELIMITER |
	DROP PROCEDURE IF EXISTS GET_PRENOM_PERSONNE;
	create procedure GET_PRENOM_PERSONNE (NumCand int(25)) 
	BEGIN
		SELECT PrenomPersonne
		FROM PERSONNE
		WHERE NumCandidatPers = NumCand;

	END;	
|
/* pas fait*/
DELIMITER |
	DROP PROCEDURE IF EXISTS SET_PRENOM_PERSONNE;
	create procedure SET_PRENOM_PERSONNE (newFirstName varchar(25), NumCand int(25)) 
	BEGIN
		UPDATE PERSONNE 
		SET PrenomPersonne = newFirstName
		WHERE NumCandidatPers = NumCand;

	END;	
|
/*fait*/
DELIMITER |
	DROP PROCEDURE IF EXISTS GET_MAIL;
	create procedure GET_MAIL (NumCand int(25)) 
	BEGIN
		SELECT MailPers
		FROM PERSONNE
		WHERE NumCandidatPers = NumCand;

	END;	
|
/*fait*/
DELIMITER |
	DROP PROCEDURE IF EXISTS SET_MAIL_PERSONNE;
	create procedure SET_MAIL_PERSONNE (newFirstMail varchar(25), NumCand int) 
	BEGIN
		UPDATE PERSONNE 
		SET MailPers = newFirstMail
		WHERE NumCandidatPers = NumCand;

	END;	
|