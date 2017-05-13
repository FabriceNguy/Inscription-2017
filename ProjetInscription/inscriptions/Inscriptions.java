package inscriptions;
import src.Connect;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Point d'entrée dans l'application, un seul objet de type Inscription
 * permet de gérer les compétitions, candidats (de type equipe ou personne)
 * ainsi que d'inscrire des candidats à des compétition.
 */

public class Inscriptions implements Serializable
{
	private static final long serialVersionUID = -3095339436048473524L;
	private static final String FILE_NAME = "Inscriptions.srz";
	private static Inscriptions inscriptions;
	private Connect connect = new Connect();
	private SortedSet<Competition> competitions = new TreeSet<Competition>();
	private SortedSet<Candidat> candidats = new TreeSet<Candidat>();
	public static boolean SERIALIZE = false; 
	
	
	public Inscriptions()
	{
	}
	
	/**
	 * Retourne les compétitions.
	 * @return
	 */
	
	public SortedSet<Competition> getCompetitions()
	
	{
		SortedSet<Competition> competitions = new TreeSet<Competition>();
		 if(!SERIALIZE){
			 ResultSet rs = connect.resultatRequete("SELECT * FROM competition");
			 try {
				int i = 0;
				while(rs.next()){
					System.out.println("getcomp num "+i+"");
					i++;
					int num = rs.getInt("NumComp");
					String nom = rs.getString("NomComp");
					LocalDate date = rs.getDate("DateCloture").toLocalDate();
					Boolean enEquipe = rs.getBoolean("EnEquipe");
					System.out.println("num"+num+"nom "+nom+" date: "+ date+""+enEquipe+"");
					Competition competition = new Competition( getInscriptions(),
							nom,
							date, 
							enEquipe); 
					competition.setId(num);
					competitions.add(competition);
					this.competitions.add(competition);

					 
				 }
			} catch (SQLException e) {
				e.printStackTrace();
				
			}
			 finally{
				System.out.println("fermé");
				 try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		 }
		 
		return competitions;
	}
	
	/**
	 * Retourne tous les candidats (personnes et équipes confondues).
	 * @return
	 */
	
	public SortedSet<Candidat> getCandidats()
	{
		ResultSet rs = connect.resultatRequete("SELECT * "
				+ "FROM Candidat as c, personne as p "
				+ "WHERE c.NumCandidat = p.NumCandidatPers");
		try {
			int i=0;
			while(rs.next()){				
				System.out.println("getcandPers num "+i+"");
				i++;
				Personne personne = new Personne( getInscriptions()
						,rs.getString("NomCandidat")
						,rs.getString("PrenomPersonne")
						,rs.getString("MailPers")); 
				personne.setIdCandidat(rs.getInt("NumCandidat"));				
				candidats.add(personne);
				this.candidats.add(personne);
				 
			 }
		} catch (SQLException e) {
			System.out.println("Erreur de connection à la BDD");
			e.printStackTrace();
		}finally{
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
			ResultSet rs1 = connect.resultatRequete("SELECT * "
					+ "FROM Candidat "
					+ "WHERE NumCandidat NOT IN "
					+ "(SELECT NumCandidatPers "
					+ "FROM personne)");
			try {
				int i =0;
				while(rs1.next()){				
					System.out.println("getcompEquipe num "+i+"");
					i++;
					Equipe equipe = new Equipe( getInscriptions(),rs1.getString("NomCandidat")); 
					equipe.setIdCandidat(rs1.getInt("NumCandidat"));
					candidats.add(equipe);
					this.candidats.add(equipe);
					
				 }
			} catch (SQLException e) {
				System.out.println("Erreur de connection à la BDD");
				e.printStackTrace();
			}
			 finally{
				System.out.println("fermé");
				 try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		
		return candidats;
	}

	/**
	 * Retourne toutes les personnes.
	 * @return
	 */
	
	public SortedSet<Personne> getPersonnes()
	{
		
		SortedSet<Personne> personnes = new TreeSet<Personne>();
	
			for (Candidat c : getCandidats())
				if (c instanceof Personne)
					personnes.add((Personne)c);
		
		return personnes;
	}

	/**
	 * Retourne toutes les équipes.
	 * @return
	 */
	
	public SortedSet<Equipe> getEquipes()
	{
		SortedSet<Equipe> equipes = new TreeSet<>();
		
		
			for (Candidat c : getCandidats())
				if (c instanceof Equipe)
					equipes.add((Equipe)c);
		
		return equipes;
	}

	/**
	 * Créée une compétition. Ceci est le seul moyen, il n'y a pas
	 * de constructeur public dans {@link Competition}.
	 * @param nom
	 * @param dateCloture
	 * @param enEquipe
	 * @return
	 */
	
	public Competition createCompetition(String nom,LocalDate dateCloture, boolean enEquipe)
	{
		Competition competition = new Competition(this, nom, dateCloture, enEquipe);
		if (!SERIALIZE && !competitions.contains(competition)){
			try {
				connect.add(competition);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		}
		competitions.add(competition);
		return competition;
	}

	/**
	 * Créée une Candidat de type Personne. Ceci est le seul moyen, il n'y a pas
	 * de constructeur public dans {@link Personne}.

	 * @param nom
	 * @param prenom
	 * @param mail
	 * @return
	 */
	
	public Personne createPersonne(String nom, String prenom, String mail)
	{
		Personne personne = new Personne(this, nom, prenom, mail);
		if (!SERIALIZE && !candidats.contains(personne)){
			try {
				connect.add(personne);
			} catch (SQLIntegrityConstraintViolationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		candidats.add(personne);
		return personne;
	}
	
	/**
	 * Créée une Candidat de type équipe. Ceci est le seul moyen, il n'y a pas
	 * de constructeur public dans {@link Equipe}.
	 * @param nom
	 * @param prenom
	 * @param mail
	 * @return
	 */
	
	public Equipe createEquipe(String nom)
	{
		boolean existe = false;
		Equipe equipe = new Equipe(this, nom);
		if(candidats.contains(equipe))
			existe = true;
		if (!SERIALIZE && !existe){
			try {
				connect.add(equipe);
				candidats.add(equipe);
			} catch (SQLIntegrityConstraintViolationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	//TODO ADD EQUIPE CONNECT
		
		
			
		return equipe;
	}
	
	public void remove(Competition competition)
	{
		competitions.remove(competition);
		
		try {
			connect.delete(competition);
		} catch (SQLIntegrityConstraintViolationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void remove(Candidat candidat)
	{
		for (Candidat candidat2 : candidats) {
			System.out.println(candidat2.getIdCandidat()+" "+candidat2.getNom());
		}
		this.candidats.remove(candidat);
		for (Candidat candidat2 : candidats) {
			System.out.println(candidat2.getIdCandidat()+" "+candidat2.getNom());
		}
		candidats.remove(candidat);
		try {
			connect.delete(candidat);
		} catch (SQLIntegrityConstraintViolationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Retourne l'unique instance de cette classe.
	 * Crée cet objet s'il n'existe déjà.
	 * @return l'unique objet de type {@link Inscriptions}.
	 */
	
	public static Inscriptions getInscriptions()
	{
		
		if (inscriptions == null)
		{
			if (SERIALIZE)
				inscriptions = readObject();
			if (inscriptions == null)
				inscriptions = new Inscriptions();
			if (!SERIALIZE)
			inscriptions.connect = new Connect();
		}
		return inscriptions;
	}

	/**
	 * Retourne un object inscriptions vide. Ne modifie pas les compétitions
	 * et candidats déjà existants.
	 */
	
	public Inscriptions reinitialiser()
	{
		inscriptions = new Inscriptions();
		return getInscriptions();
	}

	/**
	 * Efface toutes les modifications sur Inscriptions depuis la dernière sauvegarde.
	 * Ne modifie pas les compétitions et candidats déjà existants.
	 */
	
	public Inscriptions recharger()
	{
		inscriptions = null;
		return getInscriptions();
	}
	
	private static Inscriptions readObject()
	{
		ObjectInputStream ois = null;
		try
		{
			FileInputStream fis = new FileInputStream(FILE_NAME);
			ois = new ObjectInputStream(fis);
			return (Inscriptions)(ois.readObject());
		}
		catch (IOException | ClassNotFoundException e)
		{
			return null;
		}
		finally
		{
				try
				{
					if (ois != null)
						ois.close();
				} 
				catch (IOException e){}
		}	
	}
	
	/**
	 * Sauvegarde le gestionnaire pour qu'il soit ouvert automatiquement 
	 * lors d'une exécution ultérieure du programme.
	 * @throws IOException 
	 */
	
	public void sauvegarder() throws IOException
	{
		ObjectOutputStream oos = null;
		try
		{
			FileOutputStream fis = new FileOutputStream(FILE_NAME);
			oos = new ObjectOutputStream(fis);
			oos.writeObject(this);
		}
		catch (IOException e)
		{
			throw e;
		}
		finally
		{
			try
			{
				if (oos != null)
					oos.close();
			} 
			catch (IOException e){}
		}
	}
	
	@Override
	public String toString()
	{
		return "Candidats : " + getCandidats().toString()
			+ "\nCompetitions  " + getCompetitions().toString();
	}
	
	public static void main(String[] args)
	{
		LocalDate date = LocalDate.of(2017,Month.APRIL,10);
		Inscriptions inscriptions = Inscriptions.getInscriptions();
		SortedSet<Candidat>candidats = inscriptions.getCandidats();
		for (Candidat candidat : candidats) {
			System.out.println(candidat.getIdCandidat()+" "+candidat.getNom());
		}
	
	}
	
	
	public void openConnection()
	{
		connect = new Connect();
	}
	
	public void closeConnection()
	{
		connect.close();
	}

}
