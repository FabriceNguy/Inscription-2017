package inscriptions;

import src.Connect;

import java.io.Serializable;
import java.util.Collections;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

/**
 * Représente une compétition, c'est-à-dire un ensemble de candidats 
 * inscrits à un événement, les inscriptions sont closes à la date dateCloture.
 *
 */

public class Competition implements Comparable<Competition>, Serializable
{
	private static final long serialVersionUID = -2882150118573759729L;
	private static final boolean Serializable = false;
	private Inscriptions inscriptions;
	private String nom;
	private Set<Candidat> candidats;
	private LocalDate dateCloture;
	private boolean enEquipe = false;
	private int id;
	private Connect connect;
	
	
	Competition(Inscriptions inscriptions, String nom, LocalDate dateCloture, boolean enEquipe)
	{
		this.enEquipe = enEquipe;
		this.inscriptions = inscriptions;
		this.nom = nom;
		this.dateCloture = dateCloture;
		candidats = new TreeSet<Candidat>();
	}
	
	/**
	 * Retourne le nom de la compétition.
	 * @return
	 */
	
	public String getNom()
	{
		return nom;
	}
	
	/**
	 * Modifie le nom de la compétition.
	 */
	
	public void setNom(String nom)
	{
		this.nom = nom ;
		if(Serializable)
			connect.setNameComp(nom,getId());
	}
	
	/**
	 * Retourne vrai si les inscriptions sont encore ouvertes, 
	 * faux si les inscriptions sont closes.
	 * @return
	 */
	
	public boolean inscriptionsOuvertes()
	{
		// TODO retourner vrai si et seulement si la date système est antérieure à la date de clôture.
		LocalDate dateAujd = LocalDate.now();
		return dateAujd.isBefore(dateCloture);
	}
	
	/**
	 * Retourne la date de cloture des inscriptions.
	 * @return
	 */
	
	public LocalDate getDateCloture()
	{
		return dateCloture;
	}
	
	/**
	 * Est vrai si et seulement si les inscriptions sont réservées aux équipes.
	 * @return
	 */
	
	public boolean estEnEquipe()
	{
		return enEquipe;
	}
	
	/**
	 * Modifie la date de cloture des inscriptions. Il est possible de la reculer 
	 * mais pas de l'avancer.
	 * @param dateCloture
	 */
	
	public void setDateCloture(LocalDate dateClotureSet)
	{
		// TODO vérifier que l'on avance pas la date.
		LocalDate dateBefore = dateCloture;
		this.dateCloture = dateClotureSet;
		if(dateCloture.isAfter(dateBefore)){
			System.out.println("Erreur ! Il est impossible d'avancer la date de cl�ture.");
			dateCloture = dateBefore;
		}
		else {
			if(!Serializable){
				connect.setDateComp(dateClotureSet, id);
			}
				
		}
	}
	
	/**
	 * Retourne l'ensemble des candidats inscrits.
	 * @return
	 */
	
	public Set<Candidat> getCandidats()
	{
		if(!Serializable){
			ResultSet rs = null;
			if(enEquipe){
				rs = connect.resultatRequete("SELECT * "
						+ "FROM participer, candidat "
						+ "WHERE participer.NumCandidat = candidat.NumCandidat "
						+ "AND participer.NumComp ="+getId()+"");
				try {
					while(rs.next()){	
						Equipe equipe = new Equipe(inscriptions, rs.getString("NomCandidat"));
						candidats.add(equipe);																								
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else{
				rs = connect.resultatRequete("SELECT * "
						+ "FROM participer, candidat, personne, "
						+ "WHERE participer.NumCandidat = candidat.NumCandidat "
						+ "AND participer.NumComp ="+getId()
						+ " AND personne.NumCandidatPers =  candidat.NumCandidat");
				try {
					while(rs.next()){
						Personne personne = new Personne(inscriptions,
								rs.getString("NomCandidat"),
								rs.getString("PrenomPersonne"),
								rs.getString("MailPers"));
						candidats.add(personne);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}	
		}
		return Collections.unmodifiableSet(candidats);
	}
	
	/**
	 * Inscrit un candidat de type Personne à la compétition. Provoque une
	 * exception si la compétition est réservée aux équipes ou que les 
	 * inscriptions sont closes.
	 * @param personne
	 * @return
	 */
	
	public boolean add(Personne personne)
	{
		// TODO vérifier que la date de clôture n'est pas passée
		if (enEquipe || this.inscriptionsOuvertes()==true)
			throw new RuntimeException();
		personne.add(this);
		if(!Serializable)
			connect.addParticipation(personne.getIdCandidat(), id);
		return candidats.add(personne);
	}

	/**
	 * Inscrit un candidat de type Equipe à la compétition. Provoque une
	 * exception si la compétition est réservée aux personnes ou que 
	 * les inscriptions sont closes.
	 * @param personne
	 * @return
	 */

	public boolean add(Equipe equipe)
	{
		// TODO vérifier que la date de clôture n'est pas passée
		if (!enEquipe || this.inscriptionsOuvertes()==true)
			throw new RuntimeException();
		equipe.add(this);
		if(!Serializable)
			connect.addParticipation(equipe.getIdCandidat(), id);
		return candidats.add(equipe);
	}
	/**
	 * Désinscrit un candidat.
	 * @param candidat
	 * @return
	 */
	
	public boolean remove(Candidat candidat)
	{
		candidat.remove(this);
		if(!Serializable)
			connect.delParticipation(candidat.getIdCandidat(), id);
		return candidats.remove(candidat);
	}
	
	/**
	 * Supprime la compétition de l'application.
	 */
	
	public void delete()
	{
		for (Candidat candidat : candidats)
			remove(candidat);
		inscriptions.remove(this);
		if(!Serializable)
			connect.delComp(id);
	}
	
	@Override
	public int compareTo(Competition o)
	{
		return getNom().compareTo(o.getNom());
	}
	
	@Override
	public String toString()
	{
		return getNom();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
