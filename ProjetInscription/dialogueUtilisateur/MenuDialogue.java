package dialogueUtilisateur;

import src.Connect;
import utilitaires.ligneDeCommande.Liste;
import utilitaires.ligneDeCommande.Option;
import utilitaires.ligneDeCommande.Menu;
import utilitaires.ligneDeCommande.Action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class MenuDialogue {
	public static void main(String[]args){
		final Scanner sc = new Scanner(System.in);
		
		/*Cr�ation du menu principal*/
		Menu menuPrincipal = new Menu("Menu principal");

		/*Candidats*/
		Menu menuCandidats = new Menu("Menu Candidats","a");
		menuPrincipal.ajoute(menuCandidats);
		menuCandidats.ajoute(new Option("Visualiser les candidats","a",new Action() {
			public void optionSelectionnee()
			{
				Connect.readBDD("call GET_CANDIDAT()","NumCandidat","NomCandidat");
			}
		}));
		menuCandidats.ajoute(new Option("Supprimer un candidat","b",new Action() {
			public void optionSelectionnee()
			{
				System.out.print("Saisir le num�ro du candidat � supprimer: ");
				int id = sc.nextInt();
				Connect.requete("call DEL_CANDIDAT("+id+")");
			}
		}));
		menuCandidats.ajouteRevenir("r");
		
		/*Comp�titions*/
		Menu menuCompetition = new Menu("Menu Competition","b");
		menuCompetition.ajoute(new Option("Visualiser les comp�titions","a",new Action() {
			public void optionSelectionnee()
			{
				Connect.readBDD("call GET_COMP()","labelComp","NomComp");
			}
		}));
		menuCompetition.ajoute(new Option("Cr�er une comp�tition","e",new Action() {
			public void optionSelectionnee()
			{
				/*� faire*/
			}
		}));
		menuPrincipal.ajoute(menuCompetition);
		menuCompetition.ajoute(new Option("Inscrire une �quipe � une comp�tition","b",new Action() {
			public void optionSelectionnee()
			{
				/*Fonction Pas r�ussie*/
			}
		}));
		menuCompetition.ajoute(new Option("Supprimer un candidat d'une comp�tition","c",new Action() {
			public void optionSelectionnee()
			{
				System.out.print("Saisir le label de la comp�ttion: ");
				int id = sc.nextInt();
				Connect.requete("call DEL_COMP("+id+")");
			}
		}));
		menuCompetition.ajouteRevenir("r");
		
		/*Equipes*/
		final Menu menuEquipes = new Menu("Menu Equipe","c");
		menuPrincipal.ajoute(menuEquipes);
		menuEquipes.ajoute(new Option("Visualiser les �quipes","a",new Action() {
			public void optionSelectionnee()
			{
				Connect.readBDD("call GET_EQUIPE()","NumCandidat","NomCandidat");
			}
		}));
		menuEquipes.ajoute(new Option("Creer une �quipe","f",new Action() {
			public void optionSelectionnee()
			{
			}
		}));
		menuEquipes.ajoute(new Option("Visualiser les membres d'une equipe","d",new Action() {
			public void optionSelectionnee()
			{
				System.out.print("Saisir le num�ro de l'equipe � visualiser: ");
				InputStreamReader saisie = new InputStreamReader(System.in);
				try {
					int id =  (int) saisie.read();
					Connect.readBDD("call GET_MEMBRE_EQUIPE(id)","NumCandidat","NomCandidat");;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}));
		menuEquipes.ajoute(new Option("Supprimer un membre d'une �quipe","c",new Action() {
			public void optionSelectionnee()
			{
				System.out.print("Saisir le numero de l'�quipe: ");
				int idEq = sc.nextInt();
				System.out.print("Saisir le numero de la personne: ");
				int idPers = sc.nextInt();
				Connect.requete("call DEL_MEMBRE("+idEq+","+idPers+")");
			}
		}));
	
		/*Personnes*/
		Menu menuPersonne = new Menu("Menu Personne","e");
		menuPrincipal.ajoute(menuPersonne);
		menuPersonne.ajoute(new Option("Visualiser les personnes","c",new Action() {
			public void optionSelectionnee()
			{
				Connect.readBDD("call GET_PERSONNE()","NumCandidat","PrenomPersonne");
			}
		}));
		menuPersonne.ajoute(new Option("Cr�er une personne","g",new Action() {
			public void optionSelectionnee()
			{
			}
		}));
		menuPrincipal.ajouteQuitter("q");
		
		menuPrincipal.start();

	}
	
}
