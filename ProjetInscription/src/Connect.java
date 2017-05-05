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
	private Inscriptions inscriptions;
    
    public Connect() {
        
    	try {
			Class.forName("com.mysql.jdbc.Driver");
        System.out.println("Driver O.K.");

        String url = "jdbc:mysql://localhost/inscription2017?useSSL=false";
        String login= "root";
        String passwd = "";

        conn = DriverManager.getConnection(url, login, passwd);
		
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    
    public void close()
    {
    	try {
			conn.close();
		} catch (SQLException e) {
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
		 finally{
			 try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
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

	 public void setNameCandidat(String prenom,Candidat candidat){
	   requete("call SET_NAME_CANDIDAT('"+prenom+"','"+candidat.getIdCandidat()+"')");
	 }
	 public void delete(Candidat candidat){
	   requete("call DEL_CANDIDAT('"+candidat.getIdCandidat()+"')");
	 }

 /*competition*/
 
 	//Ajout d'une compétition
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
	 }
 
	public void setNameComp(String newName,int id){
	   requete("call SET_NAME_COMP('"+newName+"','"+id+"')");
	}
	public void setDateComp(LocalDate newDate,int id){
	   requete("call SET_DATE_COMP('"+newDate+"','"+id+"')");
	}
	
	//Supprime une compétition de la BD
	public void delete(Competition competition) {
		requete("call DEL_COMP('"+competition.getId()+"'");
				
	} 
	
/*Personne*/
	//Ajout d'une compétition
	public void add(Personne personne){
		 ResultSet rs = null;
		 requete("call ADD_PERSONNE('"+personne.getNom()+
				"','"+personne.getMail()+
			   	"','"+personne.getPrenom()+"')");
		 rs = resultatRequete("Select NumCandidat FROM Candidat");
		 try {
			while (rs.next()) {
				 if (rs.last()) {
					 
					 personne.setIdCandidat(rs.getInt("NumCandidat"));
				 }
				
			
			 }
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
 
	 public void setPrenomPersonne(String prenom,Personne personne){
	   requete("call SET_PRENOM_PERSONNE('"+prenom+"','"+personne.getIdCandidat()+"')");
	 }
	 public void setMailPersonne(String mail,Personne personne){
	   requete("call SET_MAIL_PERSONNE('"+mail+"','"+personne.getIdCandidat()+"')");
	 }


 /*Equipe*/
 	//Ajout d'une équipe à la BD
	public void add(Equipe equipe){
		 ResultSet rs = null;
		 requete("call ADD_EQUIPE('"+equipe.getNom()+"')");
		 rs = resultatRequete("Select NumCandidat FROM Candidat");
		 try {
			while (rs.next()) {
				 if (rs.last()) {			 
					 equipe.setIdCandidat(rs.getInt("NumCandidat"));
				 }		
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}  
	 }
 
	 public void addMembreEquipe(int idEquipe,int idPersonne){
	   requete("call ADD_MEMBRE('"+idEquipe+"','"+idPersonne+"')");
	 }
	 public void delMembreEquipe(int idEquipe,int idPersonne){
		   requete("call DEL_MEMBRE('"+idEquipe+"','"+idPersonne+"')");
	 }

 /*Participation*/
 
	 //Recupere les candidats insscrits
	 public SortedSet<Candidat> getCandidatsInscrits(){
		 SortedSet<Candidat> candidats = inscriptions.getCandidats();
		 SortedSet<Candidat> candidatsInscrits = null;
		 ArrayList<Integer> ListidCand = new ArrayList<Integer>();
		 ResultSet rs =resultatRequete("SELECT * FROM participer");
		 try {
			while(rs.next()){
				ListidCand.add( rs.getInt("NumCandidat"));
			 }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 for (Candidat candidat : candidats) {
			for (int i = 0 ; i<= ListidCand.size()-1; i++) {
				if(candidat.getIdCandidat() == ListidCand.get(i))
					candidatsInscrits.add(candidat);			
			}
		}
		 return candidatsInscrits;
	 }
	//Recupere les candidats insscrits
		 public SortedSet<Competition> getCompetitionsCandidat(){
			 SortedSet<Competition> competitions = inscriptions.getCompetitions();
			 SortedSet<Competition> competitionsCandidats = null;
			 ArrayList<Integer> ListidComp = new ArrayList<Integer>();
			 ResultSet rs =resultatRequete("SELECT * FROM participer");
			 try {
				while(rs.next()){
					ListidComp.add( rs.getInt("NumComp"));
				 }
			} catch (SQLException e) {
				e.printStackTrace();
			}
			 for (Competition competition: competitions) {
				for (int i = 0 ; i<= ListidComp.size()-1; i++) {
					if(competition.getId() == ListidComp.get(i))
						competitions.add(competition);			
				}
			}
			 return competitionsCandidats;
		 }
 
	 public void addParticipation(int idCand, int idComp){
		   requete("call ADD_PARTICIPATION('"+idCand+"','"+idComp+"')");
	 }
	 public void delParticipation(int idCand, int idComp){
		   requete("call DEL_PARTICIPATION('"+idCand+"','"+idComp+"')");
	}

	
}