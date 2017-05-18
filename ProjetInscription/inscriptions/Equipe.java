package inscriptions;

import java.sql.SQLException;
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

	
	public Equipe(Inscriptions inscriptions, String nom)
	{
		super(inscriptions, nom);
	}

	/**
	 * Retourne l'ensemble des personnes formant l'équipe.
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	
	public SortedSet<Personne> getMembres()
	{
		SortedSet<Personne> membres = null;
		
		Connect connect = new Connect();
		membres = connect.getMembresEquipe(this);
		
		connect.close();
		return membres;
	}
	
	/**
	 * Ajoute une personne dans l'équipe.
	 * @param membre
	 * @return
	 */

	public boolean add(Personne membre)
	{
		membre.add(this);

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