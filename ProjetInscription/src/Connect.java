package src;

import inscriptions.Candidat;
import inscriptions.Competition;
import inscriptions.Equipe;
import inscriptions.Inscriptions;
import inscriptions.Personne;

import java.awt.List;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Month;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.mysql.jdbc.CallableStatement;
//
//CTRL + SHIFT + O pour générer les imports
public class Connect {
	
	private Connection conn;
	private static Inscriptions inscriptions;

	
   public static void main(String[]args){
       Connect c = new Connect();
       SortedSet<Competition> competitions = new TreeSet<Competition>() ;
       SortedSet<Equipe> equipes = new TreeSet<Equipe>() ;

      try {
    	  equipes = c.getCandidatEquipe();
		  int taille = c.getCandidatEquipe().size();
		  System.out.println("taille "+taille);
		  Iterator<Equipe> iterator =  equipes.iterator();
		  while (iterator.hasNext()) {
			System.out.println(iterator.next());
		  
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
       c.close();
//       for (Competition competition : competitions) {
//    	   System.out.println(competition.getNom());
//       }
    }
    
    public Connect() {
        
    	try {
			Class.forName("com.mysql.jdbc.Driver");
        System.out.println("Driver O.K.");

        String url = "jdbc:mysql://localhost/inscription2017?useSSL=false";
        String login= "root";
        String passwd = "";

        conn = DriverManager.getConnection(url, login, passwd);
		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    
    public void close()
    {
    	try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
 public  void requete(String requete) {
 
 Statement st =null;
 try {
      System.out.println("Requête executée !"); 
      st=conn.createStatement();
  
      st.executeUpdate(requete);
         
    } catch (Exception e) {
      e.printStackTrace();
    }

 }

 
 public ResultSet resultatRequete(String requete) {

	  // Information d'accès à la base de données

	  Connection cn = null;
	  Statement st = null;
	  ResultSet rs = null;
	  String url = "jdbc:mysql://localhost/inscription2017?useSSL=false";
	  String login= "root";
	  String passwd = "";
	  
	  try {

	   // Etape 1 : Chargement du driver
	   Class.forName("com.mysql.jdbc.Driver");

	   // Etape 2 : récupération de la connexion
	   cn = DriverManager.getConnection(url, login, passwd);

	   // Etape 3 : Création d'un statement
	   st = cn.createStatement();

	   String sql = requete;

	   // Etape 4 : exécution requête
	   rs = st.executeQuery(sql);


	   return rs; 
	  
	  } catch (SQLException e) {
	   e.printStackTrace();
	  } catch (ClassNotFoundException e) {
	   e.printStackTrace();
	  } 
	   
	  return null;

	 } 
 /*Candidat*/
// public List<Candidat>
 public void setNameCandidat(String prenom,int id){
   requete("call SET_NAME_CANDIDAT('"+prenom+"','"+id+"')");
 }
 public void delCandidat(Candidat candidat){
   requete("call DEL_CANDIDAT('"+candidat.getIdCandidat()+"')");
 }

 /*competition*/
 
 public SortedSet<Competition> getCompetitions() throws SQLException{
	 Inscriptions inscription;
	 inscription = Inscriptions.getInscriptions();
	 SortedSet<Competition> competitions = new TreeSet<Competition>();
	 
	 ResultSet rs = resultatRequete("SELECT * FROM Competition");
	 while(rs.next()){
		int num = rs.getInt("NumComp");
		String nom = rs.getString("NomComp");
		LocalDate date = rs.getDate("DateCloture").toLocalDate();
		Boolean enEquipe = rs.getBoolean("EnEquipe");
		System.out.println("num"+num+"nom "+nom+" date: "+ date+""+enEquipe+"");
		Competition competition = inscription.createCompetition(nom,
				 date, 
				 enEquipe); 
		competitions.add(competition);
		 
	 }
	 
	 return competitions;
 }
 public void add(Competition competition){
   requete("call ADD_COMP('"+competition.getNom()+"','"+
		   competition.getDateCloture()+"',"+competition.estEnEquipe()+")");
   // TODO récupérer l'ID
   // a faire procedure get Id
   /*	requete("call")
//   competition.setId(/* */);
 }
 
 public void setNameComp(String newName,int id){
   requete("call SET_NAME_COMP('"+newName+"','"+id+"')");
 }
 public void setDateComp(LocalDate newDate,int id){
   requete("call SET_DATE_COMP('"+newDate+"','"+id+"')");
 }

 public void delComp(int id){
	   requete("call DEL_COMP('"+id+"')");
 }
 /*Personne*/
 
 public void add(Personne personne){
   requete("call ADD_PERSONNE('"+personne.getNom()+
		   "','"+personne.getMail()+
		   "','"+personne.getPrenom()+"')");
 }
 
 public void setPrenomPersonne(String prenom,int id){
   requete("call SET_PRENOM_PERSONNE('"+prenom+"','"+id+"')");
 }
 public void setMailPersonne(String mail,int id){
   requete("call SET_MAIL_PERSONNE('"+mail+"','"+id+"')");
 }

 public SortedSet<Equipe> getCandidatEquipe() throws SQLException{
	 Inscriptions inscription;
	 inscription = Inscriptions.getInscriptions();
	 SortedSet<Equipe> equipes = new TreeSet<Equipe>();
	 
	 ResultSet rs = resultatRequete("SELECT * FROM Candidat");
	 while(rs.next()){
		
		String nom = rs.getString("NomCandidat");
		System.out.println("nom "+nom+" ");
		Equipe equipe = inscription.createEquipe(nom); 
		
		equipes.add(equipe);
		 
	 }
	 
	 return equipes;
 }
 /*Equipe*/
 public void add(Equipe equipe){
   requete("call ADD_EQUIPE('"+equipe.getNom()+"')");
 }
 
 public void addMembreEquipe(Equipe equipe,Personne personne){
   requete("call ADD_MEMBRE('"+equipe.getIdCandidat()+"','"+personne.getIdCandidat()+"')");
 }
 public void delMembreEquipe(Equipe equipe,Personne personne){
	   requete("call DEL_MEMBRE('"+equipe.getIdCandidat()+"','"+personne.getIdCandidat()+"')");
 }
 /*Participation*/
 public void addParticipation(Candidat candidat, int id){
	   requete("call ADD_PARTICIPATION('"+candidat.getIdCandidat()+"','"+id+"')");
 }
 public void delParticipation(Candidat candidat, Competition competition){
	   requete("call DEL_PARTICIPATION('"+candidat.getIdCandidat()+"','"+competition.getId()+"')");
}
//a faire
public void delCompetition(Competition competition) {
	requete("call DEL_COMP('"+competition.getId()+"'");
	// TODO Auto-generated method stub
	
} 
}