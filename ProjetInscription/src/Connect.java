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
	private ArrayList<Integer> idMembreEquipe;

	
   public static void main(String[]args){
       Connect c = new Connect();
       SortedSet<Competition> competitions = new TreeSet<Competition>() ;
       SortedSet<Equipe> equipes = new TreeSet<Equipe>() ;
/*
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
	*/
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

 public void setNameCandidat(String prenom,int id){
   requete("call SET_NAME_CANDIDAT('"+prenom+"','"+id+"')");
 }
 public void delCandidat(int idCandidat){
   requete("call DEL_CANDIDAT('"+idCandidat+"')");
 }

 /*competition*/
 

 public void add(Competition competition) throws SQLException{
	ResultSet rs =null;
   requete("call ADD_COMP('"+competition.getNom()+"','"+
		   competition.getDateCloture()+"',"+competition.estEnEquipe()+")");
	rs = resultatRequete("Select NumComp FROM Competition");
	while (rs.next()) {
		if (rs.last()) {
			competition.setId(rs.getInt("NumComp"));
		}
		
	
	}
   // TODO récupérer l'ID
   

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
 
 public void add(Personne personne) throws SQLException{
	 ResultSet rs = null;
	 requete("call ADD_PERSONNE('"+personne.getNom()+
			"','"+personne.getMail()+
		   	"','"+personne.getPrenom()+"')");
	 rs = resultatRequete("Select NumCandidat FROM Candidat");
	 while (rs.next()) {
		 if (rs.last()) {
			 
			 personne.setIdCandidat(rs.getInt("NumCandidat"));
		 }
		
	
	}
 }
 
 public void setPrenomPersonne(String prenom,int id){
   requete("call SET_PRENOM_PERSONNE('"+prenom+"','"+id+"')");
 }
 public void setMailPersonne(String mail,int id){
   requete("call SET_MAIL_PERSONNE('"+mail+"','"+id+"')");
 }


 /*Equipe*/
 public void add(Equipe equipe) throws SQLException{
	 ResultSet rs = null;
	 requete("call ADD_EQUIPE('"+equipe.getNom()+"')");
	 rs = resultatRequete("Select NumCandidat FROM Candidat");
	 while (rs.next()) {
		 if (rs.last()) {
			 
			 equipe.setIdCandidat(rs.getInt("NumCandidat"));
		 }
		
	
	}
   
 }
 
 public void addMembreEquipe(int idEquipe,int idPersonne){
   requete("call ADD_MEMBRE('"+idEquipe+"','"+idPersonne+"')");
 }
 public void delMembreEquipe(int idEquipe,int idPersonne){
	   requete("call DEL_MEMBRE('"+idEquipe+"','"+idPersonne+"')");
 }

 /*Participation*/
 public void addParticipation(int idCand, int idComp){
	   requete("call ADD_PARTICIPATION('"+idCand+"','"+idComp+"')");
 }
 public void delParticipation(int idCand, int idComp){
	   requete("call DEL_PARTICIPATION('"+idCand+"','"+idComp+"')");
}

public void delCompetition(Competition competition) {
	requete("call DEL_COMP('"+competition.getId()+"'");
	
	
} 
}