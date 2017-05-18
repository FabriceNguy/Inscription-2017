package inscriptions;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import src.Connect;

/**
 * Candidat à un événement sportif, soit une personne physique, soit une équipe.
 *
 */

public abstract class Candidat implements Comparable<Candidat>
{
	private static final long serialVersionUID = -6035399822298694746L;
	private Inscriptions inscriptions;
	private String nom;
	private Set<Competition> competitions;

	private int idCandidat;

	
	Candidat(Inscriptions inscriptions, String nom)
	{
		this.setInscriptions(inscriptions);	
		this.nom = nom;
		
		competitions = new TreeSet<Competition>();
	}

	/**
	 * Retourne le nom du candidat.
	 * @return
	 */
	
	public String getNom()
	{
		return nom;
	}

	/**
	 * Modifie le nom du candidat.
	 * @param nom
	 */

	public void setNom(String nom)
	{
			
		this.nom = nom;
	}
	
	public int getIdCandidat(){
		return idCandidat;
	}
	public void setIdCandidat(int idCandidat){
		this.idCandidat=idCandidat;
	}
	/**
	 * Retourne toutes les compétitions auxquelles ce candidat est inscrit.s
	 * @return
	 */

	public Set<Competition> getCompetitions()
	{

		Connect connect = new Connect(); 
		this.competitions = connect.getCompetitionsCandidat();
		connect.close();
		return Collections.unmodifiableSet(competitions);
	}
	
	boolean add(Competition competition)
	{


		return competitions.add(competition);
	}

	boolean remove(Competition competition)
	{

		return competitions.remove(competition);
	}

	/**
	 * Supprime un candidat de l'application.
	 */
	
	public void delete()
	{
		
		
		for (Competition c : competitions)
			c.remove(this);
		getInscriptions().remove(this);
	}
	

	
	@Override
	public String toString()
	{
		return "\n" + getNom() + " -> inscrit à " + getCompetitions();
	}

	public Inscriptions getInscriptions() {
		return inscriptions;
	}

	public void setInscriptions(Inscriptions inscriptions) {
		this.inscriptions = inscriptions;
	}
	@Override
	public int compareTo(Candidat o) 
	{
		
		return getIdCandidat()- o.getIdCandidat();
	}


}
