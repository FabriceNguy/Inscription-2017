package dialogueUtilisateur;


import inscriptions.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.List;

import javafx.util.converter.LocalDateStringConverter;

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
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import src.Ajouter;
import src.Connect;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.SampleModel;

import javax.swing.JTabbedPane;

import java.awt.GridLayout;






import javax.swing.JScrollPane;

import java.awt.ScrollPane;
import java.awt.CardLayout;

import javax.swing.JLayeredPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.JCheckBoxMenuItem;

import java.awt.Choice;

import javax.swing.JList;
import javax.swing.JMenuBar;

import java.awt.Panel;

import javax.swing.JScrollBar;

import org.junit.internal.runners.model.EachTestNotifier;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.CompareGenerator;
import com.sun.rowset.internal.Row;
import com.sun.xml.internal.ws.api.message.Message;
import com.toedter.calendar.JDateChooser;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AppliFenetre extends JFrame {

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
		
		final Choice choiceEnequipe = new Choice();
		
		
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

		
		
		JButton btnAjouterComp = new JButton("Ajouter");
		btnAjouterComp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean existe = false;
				boolean vide = false;
				String choix = null;
				Object[] row = new Object[3];
				/*
				LocalDate dateChoisie = LocalDate.parse( dateChooser.getDate());
				
				for (Competition competition : competitions) {
					if(competition.estEnEquipe()){
						choix = "Oui";
					}
					else{
						choix= "Non";
					}
					if (competition.getNom().equals(textFieldNomComp) && competition.getDateCloture().equals(dateChoisie) && choix.equals(choiceEnequipe.getSelectedItem()) ) {
						existe = true;
					}
				}
				if (textFieldNomComp.getText().isEmpty()){
					vide = true;
				}
				if(!vide && !existe){
					boolean choixFormulaire;
					if(choiceEnequipe.getSelectedItem().equals("Oui")){
						choixFormulaire = true;
					}
					else{
						choixFormulaire = false;
					}
					Competition competition = inscriptions.createCompetition(textFieldNomComp.getText(), dateChoisie, choixFormulaire);
					competitions.add(competition);
					row[0] = competition.getCandidats();
					row[1] = competition.getNom();
					row[2] = competition.getDateCloture();
					if(competition.estEnEquipe())
						row[3] = "Oui";
					else
						row[3] = "Non";
					modelEquipes.addRow(row);
				}
				else{
					if(existe)
						System.out.println("Existe deja !");
						
					if(vide)
						System.out.println("Champs vide !");

				}*/
			}
		});
		btnAjouterComp.setBounds(450, 180, 250, 30);
		panel_1.add(btnAjouterComp);

		
		
		JButton btnSupprimerComp = new JButton("Supprimer");
		btnSupprimerComp.setBounds(450, 220, 250, 30);
		btnSupprimerComp.addActionListener(new ActionListener() {
			



			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
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
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Personne", null, panel, null);
		
		JButton btnAjouterPersonne = new JButton("Ajouter");
		btnAjouterPersonne.setBounds(450, 190, 250, 30);
		btnAjouterPersonne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean existe = false;
				boolean vide = false;
				for (Personne personne : personnes) {
					System.out.println(textFieldNomPers.getText()+" "+personne.getNom());
					if(personne.getNom().equals(textFieldNomPers.getText()) 
						&& personne.getPrenom().equals(textFieldPrenomPers.getText()) 
						&& personne.getMail().equals(textFieldAdresseMail.getText())){						
						existe = true;
						
					}
				}
				if(textFieldNomEquipe.getText().isEmpty() && textFieldAdresseMail.getText().isEmpty() && textFieldNomPers.getText().isEmpty()){
					vide = true;
				}
				if(!existe && !vide){
					System.out.println("Existe pas");
					
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
						System.out.println("Existe déjà!");
					}
					if(vide)
						System.out.println("Champs vide !");
						
				}
				
			}
		});
		
		textFieldNomPers = new JTextField();
		textFieldNomPers.setBounds(450, 30, 250, 30);
		textFieldNomPers.setColumns(10);
		
		textFieldPrenomPers = new JTextField();
		textFieldPrenomPers.setBounds(450, 90, 250, 30);
		textFieldPrenomPers.setColumns(10);
		
		textFieldAdresseMail = new JTextField();
		textFieldAdresseMail.setBounds(450, 150, 250, 30);
		textFieldAdresseMail.setColumns(10);
		
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
				int ligneSelectionnee = tablePersonnes.getSelectedRow();
				int colonneId = 0;
				
				if(ligneSelectionnee >= 0){
					System.out.println(tablePersonnes.getValueAt(ligneSelectionnee, colonneId));	
					for ( Personne personne : personnes) {
						if(personne.getIdCandidat() == (Integer)tablePersonnes.getValueAt(ligneSelectionnee, colonneId)){
							System.out.println(personne.getIdCandidat());
							candidats.remove(personne);
							equipes.remove(personne);
							inscriptions.remove(personne);
						}
					}
					textFieldAdresseMail.setText(null);
					textFieldNomPers.setText(null);
					textFieldPrenomPers.setText(null);
					modelPersonnes.removeRow(ligneSelectionnee);
				}
			}
		});
		btnSupprimerPersonne.setBounds(450, 230, 250, 30);
		panel.add(btnSupprimerPersonne);
		
		JButton btnModifierEquipe = new JButton("Modifier");
		btnModifierEquipe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int i = tablePersonnes.getSelectedRow();
				if( !modelPersonnes.getValueAt(i, 3).equals(textFieldAdresseMail.getText())){
					for (Personne personne : personnes) {
						
							personne.setMail(textFieldAdresseMail.getText());
							Connect Connect = new Connect();
							Connect.setMailPersonne(textFieldAdresseMail.getText(), personne);
							Connect.close();
							modelPersonnes.setValueAt(textFieldAdresseMail.getText(), i, 3);
					}
				}
				if( !modelPersonnes.getValueAt(i, 2).equals(textFieldPrenomPers.getText())){
					for (Personne personne : personnes) {
							personne.setPrenom(textFieldPrenomPers.getText());
							Connect Connect = new Connect();
							Connect.setPrenomPersonne(textFieldPrenomPers.getText(), personne);
							Connect.close();
							modelPersonnes.setValueAt(textFieldPrenomPers.getText(), i, 2);
					}
				}
				if( !modelPersonnes.getValueAt(i, 1).equals(textFieldNomPers.getText())){
					for (Personne personne : personnes) {
						
							personne.setNom(textFieldNomPers.getText());
							Connect Connect = new Connect();
							Connect.setPrenomPersonne(textFieldNomPers.getText(), personne);
							Connect.close();
							modelPersonnes.setValueAt(textFieldNomPers.getText(), i, 1);
							
					}
				}
				
				
				
				
				
			}
		});
		btnModifierEquipe.setBounds(450, 270, 250, 30);
		panel.add(btnModifierEquipe);
		
		JLabel lblApplicationParking = new JLabel("Application inscription compétition");
		lblApplicationParking.setHorizontalAlignment(SwingConstants.CENTER);
		lblApplicationParking.setBounds(331, 46, 194, 38);

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
				
				for (Equipe equipe : equipes) {
					System.out.println("Equipe"+ equipe.getNom()+ "id"+equipe.getIdCandidat());
					if (equipe.getNom().equals(textFieldNomEquipe.getText())) {
						existe = true;
					}
				}
				if (textFieldNomEquipe.getText().isEmpty()){
					vide = true;
				}
				if(!vide && !existe){
					Equipe equipe = inscriptions.createEquipe(textFieldNomEquipe.getText());
					equipes.add(equipe);
					row[0] = equipe.getIdCandidat();
					row[1] = equipe.getNom();
						
					modelEquipes.addRow(row);
				}
				else{
					if(existe)
						System.out.println("Existe deja !");
						
					if(vide)
						System.out.println("Champs vide !");

				}
				
			}
		});
		ongletEquipe.add(btnAjouterEquipe);
		
		//Bouton supprimer une équipe
		JButton buttonSupprimerEquipe = new JButton("Supprimer");
		buttonSupprimerEquipe.setBounds(450, 110, 250, 30);
		buttonSupprimerEquipe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int ligneSelectionnee = tableEquipes.getSelectedRow();
				int colonneId = 0;
				
		
				if(ligneSelectionnee >= 0){
					System.out.println(tableEquipes.getValueAt(ligneSelectionnee, colonneId));	
					for ( Equipe equipe : equipes) {
						if(equipe.getIdCandidat() == (Integer)tableEquipes.getValueAt(ligneSelectionnee, colonneId)){
							
							System.out.println(equipe.getIdCandidat());
							equipes.remove(equipe);
							inscriptions.remove(equipe);
						}
					}
					textFieldNomEquipe.setText(null);
					modelEquipes.removeRow(ligneSelectionnee);
				}
			}
		});
		ongletEquipe.add(buttonSupprimerEquipe);
	
		//Bouton modifier une équipe
		JButton buttonModifierEquipe = new JButton("Modifier");
		buttonModifierEquipe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
					int i = tableEquipes.getSelectedRow();
					if( !modelEquipes.getValueAt(i, 1).equals(textFieldNomEquipe.getText())){
						for (Equipe equipe : equipes) {
							
								equipe.setNom(textFieldNomPers.getText());
								Connect Connect = new Connect();
								Connect.setNameCandidat(textFieldNomEquipe.getText(), equipe);
								Connect.close();
								modelEquipes.setValueAt(textFieldNomEquipe.getText(), i, 1);
						}
					}
			
			}
		});
		buttonModifierEquipe.setBounds(450, 150, 250, 30);
		ongletEquipe.add(buttonModifierEquipe);
		contentPane.add(tabbedPane);
		contentPane.add(lblApplicationParking);
	}
}
