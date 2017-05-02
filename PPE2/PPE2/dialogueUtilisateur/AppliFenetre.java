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
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import src.Ajouter;
import src.Connect;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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

import com.sun.rowset.internal.Row;
import com.toedter.calendar.JDateChooser;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

public class AppliFenetre extends JFrame {

	private JPanel contentPane;
	private Inscriptions inscriptions;
	private JTextField textField_1;
	private JTextField textFieldNomPers;
	private JTextField textFieldPrenomPers;
	private JTextField textFieldAdresseMail;
	private JTextField textField;
	private Connect connect = new Connect();
	private JTable table;
	private JTable table_1;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppliFenetre frame = new AppliFenetre();
					frame.setVisible(true);
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
		final SortedSet<Competition> competitions = inscriptions.getCompetitions();
		final SortedSet<Equipe> equipes = inscriptions.getEquipes();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1006, 577);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Compétition", null, panel_1, null);
		
		JLabel lblAjoutDuneComptition = new JLabel("Ajout d'une comp\u00E9tition");
		lblAjoutDuneComptition.setBounds(270, 24, 170, 14);
		
		textField = new JTextField();
		textField.setBounds(262, 91, 238, 20);
		textField.setColumns(10);
		
		JLabel lblNomComptition = new JLabel("Nom Comp\u00E9tition :");
		lblNomComptition.setBounds(150, 94, 106, 14);
		
		JLabel lblNewLabel_5 = new JLabel("Date de cl\u00F4ture :");
		lblNewLabel_5.setBounds(150, 145, 109, 14);
		
		JLabel lblNewLabel_6 = new JLabel("En \u00E9quipe");
		lblNewLabel_6.setBounds(209, 194, 47, 14);
		panel_1.setLayout(null);
		panel_1.add(lblAjoutDuneComptition);
		panel_1.add(lblNewLabel_5);
		panel_1.add(lblNomComptition);
		panel_1.add(textField);
		panel_1.add(lblNewLabel_6);
		
		JRadioButton rdbtnOui = new JRadioButton("Oui");
		rdbtnOui.setBounds(283, 194, 74, 14);
		panel_1.add(rdbtnOui);
		rdbtnOui.setSelected(true);
		
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("Non");
		rdbtnNewRadioButton.setBounds(359, 194, 109, 14);
		panel_1.add(rdbtnNewRadioButton);
		rdbtnNewRadioButton.setSelected(false);

		
		
		JButton btnNewButton = new JButton("Ajouter");
		btnNewButton.setBounds(294, 248, 109, 23);
		panel_1.add(btnNewButton);
		
		final Choice choice = new Choice();
		choice.setBounds(164, 305, 193, 20);
		for (Competition competition : competitions){
			
			choice.add(competition.getNom());
		
			
		}
		panel_1.add(choice);
		
		JButton btnSupprimer = new JButton("Supprimer");
		btnSupprimer.addActionListener(new ActionListener() {
			

			private JOptionPane jop1;

			public void actionPerformed(ActionEvent arg0) {
				int i=0;
				for(Competition competition: competitions){
					if(choice.getSelectedIndex()== i){
						System.out.println(competition.getNom()+"id"+competition.getId()+" ");
						inscriptions.remove(competition);
						choice.remove(competition.getNom());
						jop1 = new JOptionPane();
						JOptionPane.showMessageDialog(null, "Compétition supprimée", "Information", JOptionPane.INFORMATION_MESSAGE);
					}
					i++;
				}
			}
		});
		btnSupprimer.setBounds(391, 302, 109, 23);
		panel_1.add(btnSupprimer);
		
		JDateChooser dateChooser = new JDateChooser();
		dateChooser.setBounds(262, 139, 238, 20);
		panel_1.add(dateChooser);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Personne", null, panel, null);
		
		JButton btnAjouterPersonne = new JButton("Ajouter");
		btnAjouterPersonne.setBounds(279, 237, 83, 23);
		btnAjouterPersonne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				inscriptions.createPersonne(textFieldNomPers.getText(), textFieldNomPers.getText(), textFieldAdresseMail.getText());
				
			}
		});
		
		textFieldNomPers = new JTextField();
		textFieldNomPers.setBounds(224, 85, 260, 20);
		textFieldNomPers.setColumns(10);
		
		textFieldPrenomPers = new JTextField();
		textFieldPrenomPers.setBounds(224, 135, 260, 20);
		textFieldPrenomPers.setColumns(10);
		
		textFieldAdresseMail = new JTextField();
		textFieldAdresseMail.setBounds(224, 193, 260, 20);
		textFieldAdresseMail.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Nom :");
		lblNewLabel.setBounds(118, 88, 83, 14);
		
		JLabel lblNewLabel_1 = new JLabel("Prenom :");
		lblNewLabel_1.setBounds(118, 138, 83, 14);
		
		JLabel lblNewLabel_2 = new JLabel("Adresse mail :");
		lblNewLabel_2.setBounds(118, 196, 83, 14);
		
		JLabel lblNewLabel_3 = new JLabel("Ajout d'une personne");
		lblNewLabel_3.setBounds(266, 29, 172, 14);
		panel.setLayout(null);
		panel.add(lblNewLabel_2);
		panel.add(lblNewLabel_1);
		panel.add(textFieldAdresseMail);
		panel.add(lblNewLabel);
		panel.add(textFieldNomPers);
		panel.add(textFieldPrenomPers);
		panel.add(lblNewLabel_3);
		panel.add(btnAjouterPersonne);
		
		JLabel lblApplicationParking = new JLabel("Application inscription compétition");
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Equipe", null, panel_2, null);
		
		textField_1 = new JTextField();
		textField_1.setBounds(236, 111, 259, 20);
		textField_1.setColumns(10);
		
		JLabel lblNomCandidat = new JLabel("Nom candidat : ");
		lblNomCandidat.setBounds(127, 114, 105, 14);
		
		JButton btnAjouterEquipe = new JButton("Ajouter");
		btnAjouterEquipe.setBounds(299, 163, 78, 23);
		btnAjouterEquipe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				inscriptions.createEquipe(textField_1.getText());
				
			}
		});
		
		JLabel lblNewLabel_4 = new JLabel("Ajout d'une Equipe");
		lblNewLabel_4.setBounds(287, 42, 90, 14);
		
		JButton btnNewButton_1 = new JButton("Supprimer");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		btnNewButton_1.setBounds(371, 249, 89, 23);
		panel_2.setLayout(null);
		panel_2.add(lblNewLabel_4);
		panel_2.add(btnAjouterEquipe);
		panel_2.add(btnNewButton_1);
		panel_2.add(lblNomCandidat);
		panel_2.add(textField_1);
		
		Choice choice_1 = new Choice();
		for (Equipe equipe : equipes) {
			choice_1.add(equipe.getNom());
		}
		choice_1.setBounds(127, 249, 177, 20);
		panel_2.add(choice_1);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("New tab", null, panel_3, null);
		
		JButton button = new JButton("Ajouter");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Ajouter ajouterFenetre = new Ajouter();
				ajouterFenetre.setVisible(true);
				
				
			}
		});
		
		
		JButton btnNewButton_2 = new JButton("Supprimer");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		
		JButton button_1 = new JButton("Modifier");
		
		JScrollPane scrollPane = new JScrollPane();
		
		table_1 = new JTable();
		Object [] columns = {"id","nom"};
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(columns);
		
		for(Equipe equipe : equipes) {
			
			Object[] row = new Object[2];
			
			row[0] = equipe.getIdCandidat();
			row[1] = equipe.getNom();
				
			model.addRow(row);
		}
		table_1.setModel(model);
		
		
		scrollPane.setViewportView(table_1);
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addGap(52)
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 338, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_3.createSequentialGroup()
							.addComponent(button, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)
							.addGap(30)
							.addComponent(btnNewButton_2, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
							.addGap(30)
							.addComponent(button_1, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(189, Short.MAX_VALUE))
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addGap(36)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 243, GroupLayout.PREFERRED_SIZE)
					.addGap(33)
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnNewButton_2, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
							.addComponent(button, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
						.addComponent(button_1, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)))
		);
		panel_3.setLayout(gl_panel_3);
		
		
		contentPane.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("48px"),
				ColumnSpec.decode("100px"),
				ColumnSpec.decode("149px"),
				ColumnSpec.decode("428px"),},
			new RowSpec[] {
				RowSpec.decode("52px"),
				RowSpec.decode("38px"),
				FormFactory.PARAGRAPH_GAP_ROWSPEC,
				RowSpec.decode("401px"),}));
		contentPane.add(tabbedPane, "2, 4, 3, 1, fill, fill");
		contentPane.add(lblApplicationParking, "4, 2, left, fill");
	}
}
