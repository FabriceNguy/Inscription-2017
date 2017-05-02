package inscriptions;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import src.Connect;

/**
 * Candidat à un événement sportif, soit une personne physique, soit une équipe.
 *
 */

public abstract class Candidat implements Comparable<Candidat>, Serializable
{
	private static final long serialVersionUID = -6035399822298694746L;
	private Inscriptions inscriptions;
	private String nom;
	private Set<Competition> competitions;
	private Connect c;
	private int idCandidat;
	private static final boolean Serializable = false;
	
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
		if(!Serializable)
			c.setNameCandidat(nom,idCandidat);
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
		if(!Serializable){
			ResultSet rs = null;
			rs = c.resultatRequete("SELECT * "
					+ "FROM participer, competition "
					+ "WHERE NumCandidar ="+idCandidat
					+ "AND particpier.NumComp = competition.NumComp");
			try {
				while(rs.next()){
					Competition competition = new Competition(getInscriptions(), 
							rs.getString("NomComp"), 
							rs.getDate("DateCloture").toLocalDate(), 
							rs.getBoolean("EnEquipe"));
					competitions.add(competition);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		return Collections.unmodifiableSet(competitions);
	}
	
	boolean add(Competition competition)
	{
		if(!Serializable)
			try {
				c.add(competition);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return competitions.add(competition);
	}

	boolean remove(Competition competition)
	{
		if(!Serializable)
			c.delParticipation(idCandidat, competition.getId());
		return competitions.remove(competition);
	}

	/**
	 * Supprime un candidat de l'application.
	 */
	
	public void delete()
	{
		if(!Serializable)
			c.delCandidat(idCandidat);
		for (Competition c : competitions)
			c.remove(this);
		getInscriptions().remove(this);
	}
	
	@Override
	public int compareTo(Candidat o)
	{
		return getNom().compareTo(o.getNom());
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
}
