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
import java.time.LocalDate;
import java.time.Month;
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
	private Connect connect;
	private SortedSet<Competition> competitions = new TreeSet<Competition>();
	private SortedSet<Candidat> candidats = new TreeSet<Candidat>();
	public static boolean SERIALIZE = false; 
	
	
	private Inscriptions()
	{
	}
	
	/**
	 * Retourne les compétitions.
	 * @return
	 */
	
	public SortedSet<Competition> getCompetitions()
	
	{
		SortedSet<Competition> competitions = new TreeSet<Competition>();
		 
		 ResultSet rs = connect.resultatRequete("SELECT * FROM Competition");
		 try {
			while(rs.next()){
				int num = rs.getInt("NumComp");
				String nom = rs.getString("NomComp");
				LocalDate date = rs.getDate("DateCloture").toLocalDate();
				Boolean enEquipe = rs.getBoolean("EnEquipe");
				System.out.println("num"+num+"nom "+nom+" date: "+ date+""+enEquipe+"");
				Competition competition = new Competition( inscriptions,nom,
						 date, 
						 enEquipe); 
				competitions.add(competition);
				 
			 }
		} catch (SQLException e) {
			System.out.println("Erreur de connection à la BDD");
			e.printStackTrace();
		}
		finally{
			connect.close();
		}
		 
		return Collections.unmodifiableSortedSet(competitions);
	}
	
	/**
	 * Retourne tous les candidats (personnes et équipes confondues).
	 * @return
	 */
	
	public SortedSet<Candidat> getCandidats()
	{
		return Collections.unmodifiableSortedSet(candidats);
	}

	/**
	 * Retourne toutes les personnes.
	 * @return
	 */
	
	public SortedSet<Personne> getPersonnes()
	{
		SortedSet<Personne> personnes = new TreeSet<Personne>();
		ResultSet rs = connect.resultatRequete("SELECT * "
				+ "FROM Candidat, Personne"
				+ "WHERE Equipe = 0 "
				+ "AND Equipe.NumCandPersonne = Candidat.NumCandidat");
		try {
			while(rs.next()){				

				Personne personne = new Personne( inscriptions
						,rs.getString("NomCandidat")
						,rs.getString("PrenomPersonne"),rs.getString("MailPersonne")); 
				personnes.add(personne);
				 
			 }
		} catch (SQLException e) {
			System.out.println("Erreur de connection à la BDD");
			e.printStackTrace();
		}
		for (Candidat c : getCandidats())
			if (c instanceof Personne)
				personnes.add((Personne)c);
		return Collections.unmodifiableSortedSet(personnes);
	}

	/**
	 * Retourne toutes les équipes.
	 * @return
	 */
	
	public SortedSet<Equipe> getEquipes()
	{
		SortedSet<Equipe> equipes = new TreeSet<>();
		ResultSet rs = connect.resultatRequete("SELECT * FROM Candidat WHERE Equipe=1");
		try {
			while(rs.next()){				

				Equipe equipe = new Equipe( inscriptions,rs.getString("NomCandidat")); 
				equipes.add(equipe);
				 
			 }
		} catch (SQLException e) {
			System.out.println("Erreur de connection à la BDD");
			e.printStackTrace();
		}
		/* 
		for (Candidat c : getCandidats())
			if (c instanceof Equipe)
				equipes.add((Equipe)c);
		*/
		return Collections.unmodifiableSortedSet(equipes);
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
		if (!SERIALIZE)
			connect.add(competition);
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
		if (!SERIALIZE)
			connect.add(personne);
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
		Equipe equipe = new Equipe(this, nom);
		if (!SERIALIZE)
			//TODO ADD EQUIPE CONNECT
			candidats.add(equipe);
		else
			connect.add(equipe);
		return equipe;
	}
	
	void remove(Competition competition)
	{
		competitions.remove(competition);
		connect.delCompetition(competition);
		//connect.delete(competition);
	}
	
	void remove(Candidat candidat)
	{
		candidats.remove(candidat);
		connect.delCandidat(candidat);
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
		SortedSet<Competition> competitions = new TreeSet<Competition>();
		/*Competition flechettes = inscriptions.createCompetition("Mondial de fléchettes", date, false);
		Personne tony = inscriptions.createPersonne("Tony", "Dent de plomb", "azerty"), 
				boris = inscriptions.createPersonne("Boris", "le Hachoir", "ytreza");
		flechettes.add(tony);
		Equipe lesManouches = inscriptions.createEquipe("Les Manouches");
		lesManouches.add(boris);
		lesManouches.add(tony);
		System.out.println(inscriptions);
		lesManouches.delete();
		System.out.println(inscriptions);
		try
		{
			inscriptions.sauvegarder();
		} 
		catch (IOException e)
		{
			System.out.println("Sauvegarde impossible." + e);
		}
		*/
		competitions = inscriptions.getCompetitions();
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
