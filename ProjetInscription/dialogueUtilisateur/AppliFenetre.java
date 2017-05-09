package dialogueUtilisateur;


import inscriptions.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.List;



import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JButton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import java.util.SortedSet;

import src.Connect;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


import javax.swing.JTabbedPane;


import javax.swing.JScrollPane;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;


import java.awt.Choice;




import com.toedter.calendar.JDateChooser;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;

public class AppliFenetre extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldNomPers;
	private JTextField textFieldPrenomPers;
	private JTextField textFieldAdresseMail;
	private JTextField textFieldNomComp;
	private JTable tableEquipes;
	private JTextField textFieldNomEquipe;
	private JTable tablePersonnes;
	private JTable tableCompetitions;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppliFenetre frame = new AppliFenetre();
					frame.setVisible(true)
					;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AppliFenetre() {
		
		final Inscriptions inscriptions = new Inscriptions();
		final SortedSet<Candidat> candidats = inscriptions.getCandidats();
		final SortedSet<Competition> competitions = inscriptions.getCompetitions();
		final SortedSet<Equipe> equipes = inscriptions.getEquipes();
		final SortedSet<Personne> personnes = inscriptions.getPersonnes();
		final JDateChooser dateChooser = new JDateChooser();
		dateChooser.setDateFormatString("yyyy-MM-dd");
		final Choice choiceEnequipe = new Choice();
		final String oui = "Oui";
		final String non = "Non";
		final String messageChampsVides ="Champs Vides";
		final String messageExiste = "Exite déjà";
		
		//modele Equipe
		final DefaultTableModel modelEquipes = new DefaultTableModel(){
			 /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		       public boolean isCellEditable(int row, int column) {
		           return false; //les cellules ne pourront pas être éditées
		       }
		};
		final DefaultTableModel modelPersonnes = new DefaultTableModel(){
			 /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		       public boolean isCellEditable(int row, int column) {
		           return false; //les cellules ne pourront pas être éditées
		       }
		};
		final DefaultTableModel modelCompetitions = new DefaultTableModel(){
			 	/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
			    public boolean isCellEditable(int row, int column){
			          return false; //les cellules ne pourront pas être éditées
			     }
			
		};
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 879, 577);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setTitle("Application Inscription");
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(53, 109, 760, 401);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Compétition", null, panel_1, null);
		
		textFieldNomComp = new JTextField();
		textFieldNomComp.setBounds(450, 30, 250, 30);
		textFieldNomComp.setColumns(10);
		
		JLabel lblNomComptition = new JLabel("Nom Comp\u00E9tition :");
		lblNomComptition.setHorizontalAlignment(SwingConstants.CENTER);
		lblNomComptition.setBounds(450, 0, 250, 30);
		
		JLabel lblNewLabel_5 = new JLabel("Date de cl\u00F4ture :");
		lblNewLabel_5.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5.setBounds(450, 60, 250, 30);
		
		JLabel lblNewLabel_6 = new JLabel("En \u00E9quipe");
		lblNewLabel_6.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_6.setBounds(450, 120, 250, 30);
		

		dateChooser.setBounds(450, 90, 250, 30);
		panel_1.add(dateChooser);
		
		
		choiceEnequipe.setBounds(450, 150, 250, 30);
		choiceEnequipe.add("Oui");
		choiceEnequipe.add("Non");
		panel_1.add(choiceEnequipe);
		
		panel_1.setLayout(null);
		panel_1.add(lblNewLabel_5);
		panel_1.add(lblNomComptition);
		panel_1.add(textFieldNomComp);
		panel_1.add(lblNewLabel_6);
		

		//Ajouter une compétition
		
		JButton btnAjouterComp = new JButton("Ajouter");
		btnAjouterComp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean existe = false;
				boolean vide = false;
				boolean dateDepassee = false;

				try{
					Object[] row = new Object[4];
					LocalDate aujourdhui = LocalDate.now();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String date = sdf.format(dateChooser.getDate());
					DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					LocalDate dateChoisie = LocalDate.parse(date, DATE_FORMAT);
					if(dateChoisie.isBefore(aujourdhui))
						dateDepassee =true; 
					for (Competition competition : competitions) {
						if (competition.getNom().equals(textFieldNomComp.getText()) ) {
							existe = true;
						}
					}
					if (textFieldNomComp.getText().isEmpty()){
						vide = true;
					}
					if(!vide && !existe && !dateDepassee){
						boolean choixFormulaire;
						if(choiceEnequipe.getSelectedItem().equals("Oui")){
							choixFormulaire = true;
						}
						else{
							choixFormulaire = false;
						}
						Competition competition = inscriptions.createCompetition(textFieldNomComp.getText(), dateChoisie, choixFormulaire);
						competitions.add(competition);
						
						row[0] = competition.getId();
						row[1] = competition.getNom();
						row[2] = competition.getDateCloture();
						if(competition.estEnEquipe())
							row[3] = oui;
						else
							row[3] = non;
						modelCompetitions.addRow(row);
					}
					else{
						if(existe)
							System.out.println(messageExiste);
						JOptionPane.showMessageDialog(null, messageExiste);
						if(vide)
							System.out.println(messageChampsVides);
						if(dateDepassee)
							System.out.println("Date incorrecte");
	
					}
				}
				catch (Exception e){
					JOptionPane.showMessageDialog(null, messageChampsVides);
				}
			}
		});
		btnAjouterComp.setBounds(450, 180, 250, 30);
		panel_1.add(btnAjouterComp);

		
		//Supprimer une compétition
		JButton btnSupprimerComp = new JButton("Supprimer");
		btnSupprimerComp.setBounds(450, 220, 250, 30);
		btnSupprimerComp.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				try{
					int ligneSelectionnee = tableCompetitions.getSelectedRow();
					int colonneId = 0;
						
					if(ligneSelectionnee >= 0){
						System.out.println(tableCompetitions.getValueAt(ligneSelectionnee, colonneId));	
						for (Iterator<Competition> iterator = competitions.iterator(); iterator
								.hasNext();) {
							Competition  competition = (Competition) iterator.next();
							if(competition.getId() == (Integer)tableCompetitions.getValueAt(ligneSelectionnee, colonneId)){
								System.out.println(competition.getId());							
								iterator.remove();
								Connect connect = new Connect();
								connect.delete(competition);
								inscriptions.remove(competition);
							}
						}
						textFieldNomComp.setText(null);
		
						modelCompetitions.removeRow(ligneSelectionnee);
					}
				}
				
				catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Selectionner une compétition");
				}
			}
				
		}
			
			
		);
		panel_1.add(btnSupprimerComp);
		
		
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(0, 0, 400, 370);
		panel_1.add(scrollPane_2);
		
		

		//table des compétitions
		tableCompetitions = new JTable();
		scrollPane_2.setViewportView(tableCompetitions);
		tableCompetitions.setAutoCreateRowSorter(true);
		Object [] columnsCompetitions = {"id","nom","DateCloture","En Equipe"};
		
		modelCompetitions.setColumnIdentifiers(columnsCompetitions);
		
		for (Competition competition : competitions) {
			Object[] row = new Object[columnsCompetitions.length];
			row[0] = competition.getId();
			row[1] = competition.getNom();
			row[2] = competition.getDateCloture();
			if(competition.estEnEquipe()){
				row[3] = "Oui"; 
			}
			else{
				row[3] = "Non"; 
			}
			
			modelCompetitions.addRow(row);
		} 
		tableCompetitions.setModel(modelCompetitions);	
		
		//Modifier les données d'une compétition
		JButton btnModifierComp = new JButton("Modifier");
		btnModifierComp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					int i = tableCompetitions.getSelectedRow();
					
					if( !modelCompetitions .getValueAt(i, 1).equals(textFieldNomComp.getText()) && !textFieldNomComp.getText().trim().isEmpty()){
						
						for (Iterator<Competition> iterator = competitions.iterator(); iterator
								.hasNext();) {
							Competition competition = (Competition) iterator.next();
							if(competition.getId() == (int)modelCompetitions.getValueAt(i, 0)){
								competition.setNom(textFieldNomComp.getText());
								Connect connect = new Connect();
								connect.setNameComp(textFieldNomComp.getText(), competition.getId()); 
								connect.close();
								modelCompetitions.setValueAt(textFieldNomComp.getText(), i, 1);
							}
						}
					}
					
					if( !modelCompetitions .getValueAt(i, 2).equals(dateChooser.getDateFormatString())){
						for (Iterator<Competition> iterator = competitions.iterator(); iterator
								.hasNext();) {
							Competition competition = (Competition) iterator.next();
							if(competition.getId() == (int)modelCompetitions.getValueAt(i, 0)){
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
								String date = sdf.format(dateChooser.getDate());
								DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
								LocalDate dateChoisie = LocalDate.parse(date, DATE_FORMAT);
								competition.setDateCloture(dateChoisie);
								Connect connect = new Connect();
								connect.setDateComp(dateChoisie, competition.getId()); 
								
								connect.close();
								modelCompetitions.setValueAt(competition.getDateCloture(), i, 2);
							}
						}
					}
					if( choiceEnequipe.getSelectedItem() != modelCompetitions.getValueAt(i, 3)){
						for (Iterator<Competition> iterator = competitions.iterator(); iterator
								.hasNext();) {
							Competition competition = (Competition) iterator.next();
							if(competition.getId() == (int)modelCompetitions.getValueAt(i, 0)){
								if(choiceEnequipe.getSelectedItem() == oui){	
									competition.setEnEquipe(true);
									Connect connect = new Connect();
									connect.setEnEquipe(true, competition.getId());
									connect.delAllParticipation(competition.getId());
									connect.close();
									modelCompetitions.setValueAt(oui, i, 3);
								}
								else{
									competition.setEnEquipe(false);
									Connect connect = new Connect();
									connect.setEnEquipe(false, competition.getId());
									connect.delAllParticipation(competition.getId());
									modelCompetitions.setValueAt(non, i, 3);
								}
	
							}	
							
						}
					}
				}
				catch (Exception e) {
					System.out.println(e);
					JOptionPane.showMessageDialog(null, "Selectionner une compétition");
				}
				
			}
		});
		btnModifierComp.setBounds(450, 260, 250, 30);
		panel_1.add(btnModifierComp);
		
		JButton btnCompetitionCandidat = new JButton("Inscrire un candidat");
		btnCompetitionCandidat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					int i = tableCompetitions.getSelectedRow();
					
					Competition competitionSelectionnee = null;
					
					int idCompSelectionnee = (int)tableCompetitions.getValueAt(i, 0);
					if(i >= 0){
						for (Iterator<Competition> iterator = competitions.iterator(); iterator
								.hasNext();) {
							
							Competition competition = (Competition) iterator.next();
							
							if(competition.getId() == idCompSelectionnee)
								competitionSelectionnee = competition;
								
						}
						System.out.println(competitionSelectionnee.getId());
						CandidatCompetition candidatCompetition = new CandidatCompetition(competitionSelectionnee, inscriptions);
						candidatCompetition.setVisible(true);
					}
				}
				catch (Exception e) {
					//e.printStackTrace();

				    JOptionPane.showMessageDialog(null, "Selectionner une competition pour voir les candidats inscrits ");
				
				    
				}
				
			}
		});
		btnCompetitionCandidat.setBounds(450, 300, 250, 30);
		panel_1.add(btnCompetitionCandidat);
		
		
		//Recupere les données de la table et les met dans le formulaire de l'onglet Personne
		tableCompetitions.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int i = tableCompetitions.getSelectedRow();
				textFieldNomComp.setText(modelCompetitions.getValueAt(i, 1).toString());
				String date = modelCompetitions.getValueAt(i, 2).toString();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				
				try {
					dateChooser.setDate(formatter.parse(date));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(modelCompetitions.getValueAt(i, 3).toString() == oui)
					choiceEnequipe.select(oui);
				else {
					choiceEnequipe.select(non);
				}
			}
		});
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Personne", null, panel, null);
		
		JButton btnAjouterPersonne = new JButton("Ajouter");
		btnAjouterPersonne.setBounds(450, 190, 250, 30);
		btnAjouterPersonne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean existe = false;
				boolean vide = false;
				for (Iterator<Personne> iterator = personnes.iterator(); iterator
						.hasNext();) {
					Personne personne = (Personne) iterator.next();
					System.out.println(textFieldNomPers.getText()+" "+personne.getNom());
					if(personne.getNom().equals(textFieldNomPers.getText()) 
						&& personne.getPrenom().equals(textFieldPrenomPers.getText()) 
						&& personne.getMail().equals(textFieldAdresseMail.getText())){						
						existe = true;
						
					}
				}

				if(textFieldPrenomPers.getText().trim().isEmpty()){
					vide = true;
				}
				if(textFieldAdresseMail.getText().trim().isEmpty()){
					vide = true;
				}
				if(textFieldNomPers.getText().trim().isEmpty()){
					vide = true;
				}
		
				if(!existe && !vide){										
					Personne personne = inscriptions.createPersonne(textFieldNomPers.getText(), textFieldPrenomPers.getText(), textFieldAdresseMail.getText());
					personnes.add(personne);
					
					Object[] row = new Object[4];
					row[0] = personne.getIdCandidat();
					row[1] = personne.getNom();
					row[2] = personne.getPrenom();
					row[3] = personne.getMail();
					modelPersonnes.addRow(row);
					textFieldNomPers.setText(null);
					textFieldPrenomPers.setText(null);
					textFieldAdresseMail.setText(null);
					
				}
				else {
					if(existe){
						System.out.println(messageExiste);
					}
					if(vide){
						System.out.println(textFieldNomEquipe.getText().isEmpty()+
						"" +textFieldPrenomPers.getText() 
						+""+textFieldAdresseMail.getText());
						System.out.println(messageChampsVides);
					}
				}
				
			}
		});
		
		textFieldNomPers = new JTextField();
		textFieldNomPers.setBounds(450, 30, 250, 30);
		textFieldNomPers.setColumns(10);
		textFieldNomPers.setText(null);
		textFieldPrenomPers = new JTextField();
		textFieldPrenomPers.setBounds(450, 90, 250, 30);
		textFieldPrenomPers.setColumns(10);
		textFieldPrenomPers.setText(null);
		textFieldAdresseMail = new JTextField();
		textFieldAdresseMail.setBounds(450, 150, 250, 30);
		textFieldAdresseMail.setColumns(10);
		textFieldAdresseMail.setText(null);
		JLabel lblNewLabel = new JLabel("Nom :");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(450, 0, 250, 30);
		
		JLabel lblNewLabel_1 = new JLabel("Prenom :");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(450, 60, 250, 30);
		
		JLabel lblNewLabel_2 = new JLabel("Adresse mail :");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setBounds(450, 120, 250, 30);
		panel.setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		scrollPane_1.setBounds(0, 2, 400, 370);
				
		
		//tablePersonnes contient toutes les personnes crées
		
		tablePersonnes = new JTable();
		tablePersonnes.setAutoCreateRowSorter(true);
		Object [] columnsPersonnes = {"id","nom","prenom","mail"};
		modelPersonnes.setColumnIdentifiers(columnsPersonnes);
		
		for (Personne personne : personnes) {
			Object[] row = new Object[columnsPersonnes.length];
			row[0] = personne.getIdCandidat();
			row[1] = personne.getNom();
			row[2] = personne.getPrenom();
			row[3] = personne.getMail();
			modelPersonnes.addRow(row);
		} 
		tablePersonnes.setModel(modelPersonnes);	
		
		
		//Recupere les données de la table et les met dans le formulaire de l'onglet Personne
		tablePersonnes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int i = tablePersonnes.getSelectedRow();
				textFieldNomPers.setText(modelPersonnes.getValueAt(i, 1).toString());
				textFieldPrenomPers.setText(modelPersonnes.getValueAt(i, 2).toString());
				textFieldAdresseMail.setText(modelPersonnes.getValueAt(i, 3).toString());
			}
		});
		scrollPane_1.setViewportView(tablePersonnes);
		
		panel.add(scrollPane_1);
		panel.add(lblNewLabel);
		panel.add(textFieldNomPers);
		panel.add(lblNewLabel_1);
		panel.add(textFieldPrenomPers);
		panel.add(lblNewLabel_2);
		panel.add(textFieldAdresseMail);
		panel.add(btnAjouterPersonne);
		
		//Bouton supprimer une personne
		JButton btnSupprimerPersonne = new JButton("Supprimer");
		btnSupprimerPersonne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					int ligneSelectionnee = tablePersonnes.getSelectedRow();
					int colonneId = 0;
					
					if(ligneSelectionnee >= 0){
						System.out.println(tablePersonnes.getValueAt(ligneSelectionnee, colonneId));	
						for (Iterator<Personne> iterator = personnes.iterator(); iterator
								.hasNext();) {
							Personne personne = (Personne) iterator.next();
							if(personne.getIdCandidat() == (Integer)tablePersonnes.getValueAt(ligneSelectionnee, colonneId)){
								System.out.println(personne.getIdCandidat());
								candidats.remove(personne);
								iterator.remove();
								inscriptions.remove(personne);
							}
						}
						textFieldAdresseMail.setText(null);
						textFieldNomPers.setText(null);
						textFieldPrenomPers.setText(null);
						modelPersonnes.removeRow(ligneSelectionnee);
					}
				}
				catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Selectionner une equipe pour voir  ses membres");
				}
			}
		});
		btnSupprimerPersonne.setBounds(450, 230, 250, 30);
		panel.add(btnSupprimerPersonne);
		
		//Modifier les données d'une personne
		
		JButton btnModifierPersonne = new JButton("Modifier");
		btnModifierPersonne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					int i = tablePersonnes.getSelectedRow();
					if( !modelPersonnes.getValueAt(i, 3).equals(textFieldAdresseMail.getText()) && !textFieldAdresseMail.getText().trim().isEmpty()){
						for (Iterator<Personne> iterator = personnes.iterator(); iterator
								.hasNext();) {
							Personne personne = (Personne) iterator.next();
							if(personne.getIdCandidat() == (int)modelPersonnes.getValueAt(i, 0)){
								personne.setMail(textFieldAdresseMail.getText());
								Connect Connect = new Connect();
								Connect.setMailPersonne(textFieldAdresseMail.getText(), personne);
								Connect.close();
								modelPersonnes.setValueAt(textFieldAdresseMail.getText(), i, 3);
							}
						}
					}
					if( !modelPersonnes.getValueAt(i, 2).equals(textFieldPrenomPers.getText()) && !textFieldPrenomPers.getText().trim().isEmpty()){
						for (Iterator<Personne> iterator = personnes.iterator(); iterator
								.hasNext();) {
							Personne personne = (Personne) iterator.next();
							if(personne.getIdCandidat() == (int) modelPersonnes.getValueAt(i, 0)){
								personne.setPrenom(textFieldPrenomPers.getText());
								Connect Connect = new Connect();
								Connect.setPrenomPersonne(textFieldPrenomPers.getText(), personne);
								Connect.close();
								modelPersonnes.setValueAt(textFieldPrenomPers.getText(), i, 2);
							}
						}
					}
					if( !modelPersonnes.getValueAt(i, 1).equals(textFieldNomPers.getText()) && !textFieldNomPers.getText().trim().isEmpty()){
						for (Iterator<Personne> iterator = personnes.iterator(); iterator
								.hasNext();) {
							Personne personne = (Personne) iterator.next();
							if(personne.getIdCandidat() == (int)modelPersonnes.getValueAt(i, 0)){
								personne.setNom(textFieldNomPers.getText());
								
								Connect Connect = new Connect();
								Connect.setPrenomPersonne(textFieldNomPers.getText(), personne);
								Connect.close();
								modelPersonnes.setValueAt(textFieldNomPers.getText(), i, 1);
							}	
						}
					}
				}
				catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Selectionner une personne");
				}

				
				
				
				
			}
		});
		btnModifierPersonne.setBounds(450, 270, 250, 30);
		panel.add(btnModifierPersonne);
		
		JLabel lblApplicationParking = new JLabel("Application Inscription");
		lblApplicationParking.setFont(new Font("Tahoma", Font.PLAIN, 19));
		lblApplicationParking.setHorizontalAlignment(SwingConstants.CENTER);
		lblApplicationParking.setBounds(305, 50, 250, 40);

		//Tableau Equipes
		JPanel ongletEquipe = new JPanel();
		tabbedPane.addTab("Equipe", null, ongletEquipe, null);
		Object [] columnsEquipes = {"id","nom"};
		
		modelEquipes.setColumnIdentifiers(columnsEquipes);
		
		for(Equipe equipe : equipes) {
			
			Object[] row = new Object[columnsEquipes.length];
			
			row[0] = equipe.getIdCandidat();
			row[1] = equipe.getNom();
				
			modelEquipes.addRow(row);
		}
		contentPane.setLayout(null);
		ongletEquipe.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(1, 0, 360, 370);
		
		tableEquipes = new JTable();
		tableEquipes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int i = tableEquipes.getSelectedRow();
				textFieldNomEquipe.setText(modelEquipes.getValueAt(i, 1).toString());
			}
		});
		tableEquipes.setModel(modelEquipes);
		
		scrollPane.setViewportView(tableEquipes);
		ongletEquipe.add(scrollPane);
		
		//Form Equipe
		JLabel lblNomEquipe = new JLabel("Nom Equipe :");
		lblNomEquipe.setHorizontalAlignment(SwingConstants.CENTER);
		lblNomEquipe.setBounds(450, 0, 250, 30);
		ongletEquipe.add(lblNomEquipe);
		
		textFieldNomEquipe = new JTextField();
		textFieldNomEquipe.setBounds(450, 30, 250, 30);
		ongletEquipe.add(textFieldNomEquipe);
		textFieldNomEquipe.setColumns(10);
		
		JLabel label_8 = new JLabel("");
		label_8.setBounds(1, 93, 251, 93);
		ongletEquipe.add(label_8);
		
		JLabel label_9 = new JLabel("");
		label_9.setBounds(252, 93, 251, 93);
		ongletEquipe.add(label_9);
		
		//Bouton ajouter une équipe
		JButton btnAjouterEquipe = new JButton("Ajouter");
		btnAjouterEquipe.setBounds(450, 70, 250, 30);
		btnAjouterEquipe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				boolean existe = false;
				boolean vide = false;
				
				Object[] row = new Object[2];
				
				for (Iterator<Equipe> iterator = equipes.iterator(); iterator
						.hasNext();) {
					Equipe equipe = (Equipe) iterator.next();
					System.out.println("Equipe"+ equipe.getNom()+ "id"+equipe.getIdCandidat());
					if (equipe.getNom().equals(textFieldNomEquipe.getText())) {
						existe = true;
					}
				}
				if (textFieldNomEquipe.getText().trim().isEmpty()){
					vide = true;
				}
				if(!vide && !existe){
					Equipe equipe = inscriptions.createEquipe(textFieldNomEquipe.getText());
					equipes.add(equipe);
					row[0] = equipe.getIdCandidat();
					row[1] = equipe.getNom();
					textFieldNomEquipe.setText(null);	
					modelEquipes.addRow(row);
				}
				else{
					if(existe){
						System.out.println("Existe deja !");
						JOptionPane.showMessageDialog(null, messageExiste);
					}	
					else{
						System.out.println("Champs vide !");
						JOptionPane.showMessageDialog(null, messageChampsVides);
					}
				}
				
			}
		});
		ongletEquipe.add(btnAjouterEquipe);
		
		//Bouton supprimer une équipe
		JButton buttonSupprimerEquipe = new JButton("Supprimer");
		buttonSupprimerEquipe.setBounds(450, 110, 250, 30);
		buttonSupprimerEquipe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					int ligneSelectionnee = tableEquipes.getSelectedRow();
					int colonneId = 0;
					
			
					if(ligneSelectionnee >= 0 && tableEquipes.getSelectedRow() != -1){
						System.out.println(tableEquipes.getValueAt(ligneSelectionnee, colonneId));	
						for (Iterator<Equipe> iterator = equipes.iterator(); iterator
								.hasNext();) {
							Equipe equipe = (Equipe) iterator.next();
							if(equipe.getIdCandidat() == (Integer)tableEquipes.getValueAt(ligneSelectionnee, colonneId)){
								
								System.out.println(equipe.getIdCandidat());
								iterator.remove();
								inscriptions.remove(equipe);
							}
						}
						textFieldNomEquipe.setText(null);
						modelEquipes.removeRow(ligneSelectionnee);
					}
				}
				catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Selectionner une equipe");
				}
			}
		});
		ongletEquipe.add(buttonSupprimerEquipe);
	
		//Bouton modifier une équipe
		JButton buttonModifierEquipe = new JButton("Modifier");
		buttonModifierEquipe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					try{
						int i = tableEquipes.getSelectedRow();
						if( !modelEquipes.getValueAt(i, 1).equals(textFieldNomEquipe.getText()) && !textFieldNomEquipe.getText().trim().isEmpty()){
							for (Iterator<Equipe> iterator = equipes.iterator(); iterator
									.hasNext();) {
								
								Equipe equipe = (Equipe) iterator.next();																				
								if(equipe.getIdCandidat() == (int)modelEquipes.getValueAt(i, 0)){
									equipe.setNom(textFieldNomPers.getText());
									Connect Connect = new Connect();
									Connect.setNameCandidat(textFieldNomEquipe.getText(), equipe);
									Connect.close();
									modelEquipes.setValueAt(textFieldNomEquipe.getText(), i, 1);
								
								}
							}
						}
					}
					catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Selectionner une equipe pour la modifier");
					}
			
			}
		});
		buttonModifierEquipe.setBounds(450, 150, 250, 30);
		ongletEquipe.add(buttonModifierEquipe);
		//Membre Equipe
		JButton btnMembreEquipe = new JButton("Membre");
		btnMembreEquipe.addActionListener(new ActionListener() {
			private Equipe equipeSelectionnee;

			public void actionPerformed(ActionEvent arg0) {
				try{
					int i = tableEquipes.getSelectedRow();
					
					int idEquipeSelectionnee = (int)tableEquipes.getValueAt(i, 0);
					if(i >= 0){
						for (Iterator<Equipe> iterator = equipes.iterator(); iterator
								.hasNext();) {
							
							Equipe equipe = (Equipe) iterator.next();
							
							if(equipe.getIdCandidat() == idEquipeSelectionnee)
								equipeSelectionnee = equipe;
						}
						MembreEquipe membreEquipe = new MembreEquipe(equipeSelectionnee, inscriptions);
						membreEquipe.setVisible(true);
					}
				}
				catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Selectionner une equipe pour voir  ses membres");
				}
				
			}
		});
		btnMembreEquipe.setBounds(450, 190, 250, 30);
		ongletEquipe.add(btnMembreEquipe);
		contentPane.add(tabbedPane);
		contentPane.add(lblApplicationParking);
	}
}
