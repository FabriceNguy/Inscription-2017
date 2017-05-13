package dialogueUtilisateur;

import inscriptions.Candidat;
import inscriptions.Equipe;
import inscriptions.Inscriptions;
import inscriptions.Personne;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Frame;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;

import src.Connect;

import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import com.sun.media.jfxmedia.track.Track;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MembreEquipe extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JTable table_1;
	private JTable table_2;
	private JTable table_3;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		/*final Inscriptions inscriptions = new Inscriptions();
		
		final Equipe equipe = inscriptions.createEquipe("Test");
		Connect connect = new Connect();
		
		Personne membre = inscriptions.createPersonne("PersonneTest", "PrenomTest", "mail@test.com");
		equipe.add(membre);
		connect.addMembreEquipe(equipe.getIdCandidat(), membre.getIdCandidat());
		connect.close();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				try {
					MembreEquipe frame = new MembreEquipe(equipe, inscriptions);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		*/
	}

	/**
	 * Create the frame.
	 */
	public MembreEquipe(final Equipe equipe, Inscriptions inscription) {
		final DefaultTableModel modelMembres= new DefaultTableModel(){
			 /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		       public boolean isCellEditable(int row, int column) {
		           return false; //les cellules ne pourront pas être éditées
		       }
		};
		final DefaultTableModel modelCandidatPers= new DefaultTableModel(){
			 /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		       public boolean isCellEditable(int row, int column) {
		           return false; //les cellules ne pourront pas être éditées
		       }
		};

		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 900, 550);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 52, 350, 430);
		contentPane.add(scrollPane);
		
		table_2 = new JTable();
		Object [] columnsMembres = {"id","nom","prenom","mail"};
		modelMembres.setColumnIdentifiers(columnsMembres);
		table_2.setModel(modelMembres);
		scrollPane.setViewportView(table_2);
		
		JLabel lblMembres = new JLabel("Membres");
		lblMembres.setBounds(110, 27, 123, 14);
		lblMembres.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblMembres);
		Object [] columnsCandidatPers = {"id","nom","prenom","mail"};
		modelCandidatPers.setColumnIdentifiers(columnsCandidatPers);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(560, 52, 298, 430);
		contentPane.add(scrollPane_1);
		
		table_3 = new JTable();
		scrollPane_1.setViewportView(table_3);
		table_3.setModel(modelCandidatPers);
		
		JLabel lblNewLabel = new JLabel("Candidats");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(668, 27, 92, 14);
		contentPane.add(lblNewLabel);
		
		
		final SortedSet<Personne> personnes = inscription.getPersonnes();
		final Set<Personne> membres = equipe.getMembres();

		for (Iterator<Personne> iterator = membres.iterator(); iterator.hasNext();) {
				Personne membre = (Personne) iterator.next();
				
			
				Object[] row = new Object[columnsMembres.length];
				row[0] = membre.getIdCandidat();
				row[1] = membre.getNom();
				row[2] = membre.getPrenom();
				row[3] = membre.getMail();
				modelMembres.addRow(row);
		} 
		
		for (Personne personne : personnes) {


				Object[] row = new Object[columnsMembres.length];
				row[0] = personne.getIdCandidat();
				row[1] = personne.getNom();
				row[2] = personne.getPrenom();
				row[3] = personne.getMail();
				modelCandidatPers.addRow(row);
			
			
			
		} 
		
		JButton btnNewButton = new JButton("Ajouter");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					int i = table_3.getSelectedRow();
					
					int idPersonneSelectionnee = (int)table_3.getValueAt(i, 0);
					if(i >= 0){
						for (Iterator<Personne> iterator = personnes.iterator(); iterator
								.hasNext();) {
							
							Personne personne = (Personne) iterator.next();
							
							if(personne.getIdCandidat() == idPersonneSelectionnee){
								
								membres.add(personne);
								Connect connect = new Connect();
								try{
									connect.addMembreEquipe(equipe.getIdCandidat(), personne.getIdCandidat());
									Object[] row = new Object[4];
									row[0] = personne.getIdCandidat();
									row[1] = personne.getNom();
									row[2] = personne.getPrenom();
									row[3] = personne.getMail();
									modelMembres.addRow(row);
								}
								catch(SQLException e){
									if (e instanceof SQLIntegrityConstraintViolationException) {
										JOptionPane.showMessageDialog(null, "Déjà membre");
								    } 
									
								}
								connect.close();
								

							}	
						}
	
					}
				}
				catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Selectionner une personne");
				}
			}
		});
		btnNewButton.setBounds(413, 89, 89, 23);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Supprimer");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					int i = table_2.getSelectedRow();
					
					int idMembreSelectionnee = (int)table_2.getValueAt(i, 0);
					if(i >= 0){
						for (Iterator<Personne> iterator = membres.iterator(); iterator
								.hasNext();) {
							
							Personne membre = (Personne) iterator.next();
							
							if(membre.getIdCandidat() == idMembreSelectionnee){
								
								Connect connect = new Connect();
								connect.delMembreEquipe(equipe.getIdCandidat(), membre.getIdCandidat());
								connect.close();

								modelMembres.removeRow(i);
								iterator.remove();
								//membres.remove(membre);
								 
								
							}	
						}
	
					}
				}catch (Exception e) {
					
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Selectionner une personne");
				}
					
			}
		});
		btnNewButton_1.setBounds(413, 137, 89, 23);
		contentPane.add(btnNewButton_1);




		
		
	
	}
}
