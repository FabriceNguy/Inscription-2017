
/*ETRE DANS*/
/*fait*/
DELIMITER |
	DROP PROCEDURE IF EXISTS ADD_MEMBRE;
	create procedure ADD_MEMBRE (Num_Equipe int,Num_Personne int) 
	BEGIN

		insert into ETRE_DANS(NumCandidatEquipe,NumCandidatPers) VALUES (Num_Equipe,Num_Personne); 
		

	END;	
|
/*fait*/
DELIMITER |
	DROP PROCEDURE IF EXISTS DEL_MEMBRE;
	create procedure DEL_MEMBRE (Num_Equipe int,Num_Personne int) 
	BEGIN
		DELETE FROM ETRE_DANS WHERE NumCandidatEquipe =Num_Equipe AND  NumCandidatPers= Num_Personne; 
	END;	
|


