
/*EQUIPE*/

/*
Pas besoin, plus de table equipe
DELIMITER |
	DROP PROCEDURE IF EXISTS ADD_EQUIPE;
	create procedure ADD_EQUIPE
	(NumCandidat int(25), NomCandidat varchar(25), MailCandidat varchar(25))
	begin
		insert into CANDIDAT(NumCandidat,NomCandidat,MailCandidat) values (NumCandidat,NomCandidat,MailCandidat) ;
		insert into EQUIPE(NumCandidat) values (NumCandidat) ;
	end;
|
*/

/* pas fait*/
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
/*fait*/
DELIMITER |
	DROP PROCEDURE IF EXISTS ADD_EQUIPE;
	create procedure ADD_EQUIPE(NomCandidat  Varchar(25) )
	BEGIN

		insert into CANDIDAT(NumCandidat, NomCandidat, Equipe) values (null, NomCandidat, true) ;
	END;

|