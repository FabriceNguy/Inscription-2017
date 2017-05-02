package inscriptions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import src.Connect;

/**
 * Représente une Equipe. C'est-à-dire un ensemble de personnes pouvant 
 * s'inscrire à une compétition.
 * 
 */

public class Equipe extends Candidat
{
	private static final long serialVersionUID = 4147819927233466035L;
	private SortedSet<Personne> membres = new TreeSet<>();
	private static final boolean Serializable = false;
	private Connect c =new Connect();
	Equipe(Inscriptions inscriptions, String nom)
	{
		super(inscriptions, nom);
	}

	/**
	 * Retourne l'ensemble des personnes formant l'équipe.
	 */
	
	public SortedSet<Personne> getMembres()
	{
		
		ResultSet rs = c.resultatRequete("SELECT * "
				+ "FROM etre_dans, personne, candidat "
				+ "WHERE NumCandidatEquipe ="+getIdCandidat()
				+ "AND etre_dans.NumCandidatPers = personne.NumCandidat"
				+ "AND candidat.NumCandidat = personne.NumCandidatPers");
		try {
			while (rs.next()) {
				Personne personne = new Personne(getInscriptions(), 
						rs.getString("NomCandidat"),
						rs.getString("PrenomPersonne"), 
						rs.getString("MailPers"));
				membres.add(personne);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Collections.unmodifiableSortedSet(membres);
	}
	
	/**
	 * Ajoute une personne dans l'équipe.
	 * @param membre
	 * @return
	 */

	public boolean add(Personne membre)
	{
		membre.add(this);
		if(!Serializable)
			c.addMembreEquipe(getIdCandidat(), membre.getIdCandidat());
		return membres.add(membre);
	}

	/**
	 * Supprime une personne de l'équipe. 
	 * @param membre
	 * @return
	 */
	
	public boolean remove(Personne membre)
	{
		membre.remove(this);
		if(!Serializable)
			c.delMembreEquipe(getIdCandidat(), membre.getIdCandidat());
		return membres.remove(membre);
	}

	@Override
	public void delete()
	{
		super.delete();
	}
	
	@Override
	public String toString()
	{
		return "Equipe " + super.toString();
	}
}
